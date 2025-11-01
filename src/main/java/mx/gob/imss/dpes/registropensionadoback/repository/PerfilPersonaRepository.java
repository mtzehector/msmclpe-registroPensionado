package mx.gob.imss.dpes.registropensionadoback.repository;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.registropensionadoback.entity.PerfilPersona;
import mx.gob.imss.dpes.registropensionadoback.exception.TokenLoginException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class PerfilPersonaRepository {

    protected final transient Logger log = Logger.getLogger(getClass().getName());
    @PersistenceContext
    private EntityManager entityManager;

    public PerfilPersona buscarPerfilPersonaPorEmail(String email) throws BusinessException {
        try {
            List<PerfilPersona> personaList = entityManager.createNativeQuery(
                this.obtenerConsultaPerfilPersonaPorEmail(email), PerfilPersona.class).getResultList();
            if(personaList != null && !personaList.isEmpty())
                return personaList.get(0);
        }
        catch(Exception e) {
            log.log(Level.SEVERE, "PerfilPersonaRepository.buscarPerfilPersonaPorEmail - email = [" +
                    email + "]", e);
        }

        throw new TokenLoginException(TokenLoginException.ERROR_OBTENER_PERFIL_USUARIO);
    }

    private String obtenerConsultaPerfilPersonaPorEmail(String email) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
            .append("    u.nom_usuario nom_usuario, ")
            .append("    0 num_sesion, ")
            .append("    p.cve_persona cve_persona, ")
            .append("    perfil.cve_perfil cve_perfil, ")
            .append("    ef.convenio convenio ")
            .append("FROM ")
            .append("    mgpmclpe04.mclt_usuario u ")
            .append("    INNER JOIN mgpmclpe04.mclt_perfil_usuario pu ")
            .append("        ON (u.cve_usuario = pu.cve_usuario) ")
            .append("    INNER JOIN mgpmclpe04.mclc_perfil perfil ")
            .append("        ON (pu.cve_perfil = perfil.cve_perfil) ")
            .append("    INNER JOIN mgpmclpe04.mclt_persona p ")
            .append("        ON (u.cve_id_persona = p.cve_persona) ")
            .append("    LEFT OUTER JOIN mgpmclpe04.mclc_entidad_financiera ef ")
            .append("        ON (u.nom_usuario = ef.correo_admin")
            .append("            AND ef.fec_registro_baja IS NULL) ")
            .append("WHERE ")
            .append("    u.nom_usuario = '").append(email).append("' ")
            .append("    AND u.ind_activo = 1 ")
            .append("    AND u.fec_registro_baja IS NULL ")
            .append("    AND pu.fec_registro_baja IS NULL ")
            .append("    AND p.fec_registro_baja IS NULL");
        return sb.toString();
    }

    private Date agregarHoras(Date fecha, int horas) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.HOUR, horas);
        return calendar.getTime();
    }

    private String obtenerConsultaPerfilPersonaPorToken() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append("    t.nom_usuario nom_usuario, ")
                .append("    t.num_sesion num_sesion, ")
                .append("    p.cve_persona cve_persona, ")
                .append("    pu.cve_perfil cve_perfil, ")
                .append("    ef.convenio convenio ")
                .append("FROM ")
                .append("    mgpmclpe04.mclt_token t ")
                .append("    INNER JOIN mgpmclpe04.mclt_persona p ")
                .append("        ON (t.cve_persona = p.cve_persona) ")
                .append("    INNER JOIN mgpmclpe04.mclt_usuario u ")
                .append("        ON (u.cve_usuario = p.cve_usuario) ")
                .append("    INNER JOIN mgpmclpe04.mclt_perfil_usuario pu ")
                .append("        ON (u.cve_usuario = pu.cve_usuario) ")
                .append("    LEFT OUTER JOIN mgpmclpe04.mclc_entidad_financiera ef ")
                .append("        ON (t.nom_usuario = ef.correo_admin")
                .append("            AND ef.fec_registro_baja IS NULL) ")
                .append("WHERE ")
                .append("    t.token = :tokenSeguridad ")
                .append("    AND t.fec_registro_baja IS NULL ")
                .append("    AND t.fec_registro_alta >= :fechaInicio ")
                .append("    AND t.fec_registro_alta <= :fechaFin ")
                .append("    AND p.fec_registro_baja IS NULL ")
                .append("    AND u.fec_registro_baja IS NULL ")
                .append("    AND pu.fec_registro_baja IS NULL ")
        ;
        return sb.toString();
    }

    public PerfilPersona buscarPerfilPersonaPorToken(String tokenSeguridad) throws BusinessException {
        try {
            Date fechaActual = new Date();

            List<PerfilPersona> personaList = entityManager.createNativeQuery(
                    this.obtenerConsultaPerfilPersonaPorToken(), PerfilPersona.class)
                    .setParameter("tokenSeguridad", tokenSeguridad)
                    .setParameter("fechaInicio", agregarHoras(fechaActual, -4))
                    .setParameter("fechaFin", agregarHoras(fechaActual, 4))
                    .getResultList();

            if(personaList != null && !personaList.isEmpty())
                return personaList.get(0);
        }
        catch(Exception e) {
            log.log(Level.SEVERE, "PerfilPersonaRepository.buscarPerfilPersonaPorToken - tokenSeguridad = [" +
                    tokenSeguridad + "]", e);
        }

        throw new TokenLoginException();
    }
}

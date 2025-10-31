/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.repository;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.interfaces.userdata.model.UserData;

import mx.gob.imss.dpes.registropensionadoback.exception.UsuarioException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

/**
 * @author gabriel.rios
 */
@Component
public class UserOperations {

    protected final transient Logger log = Logger.getLogger(getClass().getName());
    @PersistenceContext
    private EntityManager entityManager;

    public UserOperations() {
    }

    public UserData findUserData(String userName) throws BusinessException {
        try {
            //log.log(Level.INFO, "	++++++++++   UserOperations.findUserData entityManager.isOpen()={}", entityManager.isOpen());
            UserData userDataDTO = (UserData) entityManager.createNativeQuery(
            " SELECT "
                + " perfil.cve_perfil, "
                + " perfil.des_perfil, "
                + " peusuario.firma_carta_recibo, "
                + " persona.nombre, "
                + " persona.num_nss, "
                + " persona.primer_apellido, "
                + " persona.segundo_apellido, "
                + " persona.cve_curp, "
                + " persona.tel_celular, "
                + " persona.matricula_trabajador_imss, "
                + " persona.delegacion_trabajador_imss, "
                + " persona.fec_nacimiento, "
                + " persona.rfc, "
                + " usuario.nom_usuario "
                + " FROM MCLC_PERFIL perfil, "
                + " MCLT_PERFIL_USUARIO peusuario, "
                + " MCLT_PERSONA persona, "
                + " MCLT_USUARIO usuario "
                + " WHERE usuario.nom_usuario = ?1 "
                + " AND usuario.ind_activo = 1 "
                + " AND usuario.cve_id_persona = persona.cve_persona "
                + " AND peusuario.cve_usuario = usuario.cve_usuario "
                + " AND peusuario.cve_perfil = perfil.cve_perfil ")
            .setParameter(1, userName)
            .unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(UserData.class)).uniqueResult();
            return userDataDTO;
        }
        catch(Exception e) {
            log.log(Level.SEVERE, "UserOperations.findUserData - userName = [" + userName + "]", e);
        }

        throw new UsuarioException(UsuarioException.ERROR_DE_LECTURA_DE_BD);
    }

    public UserData findUserDataByCurpAndNss(String curp, String nss) {
        //log.log(Level.INFO, "	++++++++++   UserOperations.findUserData entityManager.isOpen()={}", entityManager.isOpen());
        UserData userDataDTO = (UserData) entityManager.createNativeQuery(
                        "select *" +
                                "  from (" +
                                "SELECT " +
                                " perfil.cve_perfil, " +
                                " perfil.des_perfil, " +
                                " persona.nombre, " +
                                " persona.num_nss, " +
                                " persona.primer_apellido, " +
                                " persona.segundo_apellido, " +
                                " persona.cve_curp, " +
                                " persona.tel_celular, " +
                                " persona.tel_local," +
                                " persona.matricula_trabajador_imss, " +
                                " persona.delegacion_trabajador_imss, " +
                                " persona.correo_electronico nom_usuario" +
                                " FROM " +
                                " MCLT_PERFIL_USUARIO peusuario, " +
                                " MCLT_PERSONA persona," +
                                " MCLC_PERFIL perfil" +
                                " WHERE "
                                + " persona.num_nss =?1 and persona.cve_curp=?2 "
                                + "AND peusuario.cve_perfil = perfil.cve_perfil order by cve_persona desc )" +
                                "where rownum <= 1")
                .setParameter(1, nss)
                .setParameter(2, curp)
                .unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(UserData.class)).uniqueResult();
        return userDataDTO;
    }

}

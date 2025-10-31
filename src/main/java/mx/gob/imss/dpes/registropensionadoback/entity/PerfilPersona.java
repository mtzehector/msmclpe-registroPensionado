package mx.gob.imss.dpes.registropensionadoback.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class PerfilPersona {
    @Id
    @Column(name = "CVE_PERSONA")
    @Getter
    @Setter
    private Long cvePersona;

    @Column(name = "CVE_PERFIL")
    @Getter
    @Setter
    private Integer cvePerfil;

    @Column(name = "CONVENIO")
    @Getter
    @Setter
    private Integer convenio;

    @Column(name = "NOM_USUARIO")
    @Getter
    @Setter
    private String nomUsuario;

    @Column(name = "NUM_SESION")
    @Getter
    @Setter
    private Long sesion;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cvePersona != null ? cvePersona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PerfilPersona)) {
            return false;
        }
        PerfilPersona other = (PerfilPersona) object;
        if (
            (this.cvePersona == null && other.cvePersona != null) ||
            (this.cvePersona != null && !this.cvePersona.equals(other.cvePersona))
        ) {
            return false;
        }
        return true;
    }
}

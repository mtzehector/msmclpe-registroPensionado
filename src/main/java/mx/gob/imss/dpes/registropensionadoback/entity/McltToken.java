package mx.gob.imss.dpes.registropensionadoback.entity;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.entity.LogicDeletedEntity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
@Entity
@Table(name= "MCLT_TOKEN")
public class McltToken extends LogicDeletedEntity<Long> {
    private static final long serialVersionUID = 1689765465L;

    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "MCLS_TOKEN")
    @GenericGenerator(
            name = "MCLS_TOKEN",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "MCLS_TOKEN")
                ,
        @Parameter(name = "initial_value", value = "1")
                ,
        @Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "CVE_TOKEN")
    @Getter
    @Setter
    private Long id;

    @Column(name = "NUM_SESION")
    @Getter
    @Setter
    private Long sesion;

    @Column(name = "NOM_USUARIO")
    @Getter
    @Setter
    private String nomUsuario;
      
    @Column(name = "TOKEN")
    @Getter
    @Setter
    private String token;

    @Column(name = "CVE_PERSONA")
    @Getter
    @Setter
    private Long cvePersona;

    @Column(name = "CVE_PERFIL")
    @Getter
    @Setter
    private Integer cvePerfil;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof McltToken)) {
            return false;
        }
        McltToken other = (McltToken) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.entity.LogicDeletedEntity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author edgar.arenas
 */
@Entity
@Table(name= "MCLT_PERFIL_USUARIO")
public class McltPerfilUsuario extends LogicDeletedEntity<Long> {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "MCLS_PERFIL_USUARIO")
    @GenericGenerator(
            name = "MCLS_PERFIL_USUARIO",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "MCLS_PERFIL_USUARIO")
                ,
        @Parameter(name = "initial_value", value = "1")
                ,
        @Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "CVE_PERFIL_USUARIO")
    @Getter
    @Setter
    private Long id;
    
    @Column(name = "CVE_USUARIO")
    @Getter
    @Setter
    private Long idUsuario;
    
    @Column(name = "CVE_PERFIL")
    @Getter
    @Setter
    private Long idPerfil;
     
    @Column(name = "FIRMA_CARTA_RECIBO")
    @Getter
    @Setter
    private Long firmaCartaRecibo;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof McltPerfilUsuario)) {
            return false;
        }
        McltPerfilUsuario other = (McltPerfilUsuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}

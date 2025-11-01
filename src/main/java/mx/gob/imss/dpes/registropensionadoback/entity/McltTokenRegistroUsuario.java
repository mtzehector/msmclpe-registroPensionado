/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.entity.LogicDeletedEntity;
import mx.gob.imss.dpes.registropensionadoback.model.TokenRegistroUsusario;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;
import mx.gob.imss.dpes.support.config.CustomDateSerializer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author edgar.arenas
 */
@Entity
@Table(name = "MCLT_TOKEN_REGISTRO_USUARIO")
public class McltTokenRegistroUsuario extends LogicDeletedEntity<Long> {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "MCLS_TOKEN_REGISTRO_USUARIO")
    @GenericGenerator(
            name = "MCLS_TOKEN_REGISTRO_USUARIO",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "MCLS_TOKEN_REGISTRO_USUARIO"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "CVE_TOKEN")
    @Getter
    @Setter
    private Long id;

    @Column(name = "NOM_USUARIO")
    @Size(min = 1, max = 50)
    @Getter
    @Setter
    private String nomUsuario;

    @Column(name = "REF_TOKEN")
    @Size(min = 1, max = 50)
    @Getter
    @Setter
    private String token;

    @Column(name = "IND_ACTIVO")
    @Getter
    @Setter
    private Integer indActivo;

    @Column(name = "REF_NUM_TELEFONO")
    @Getter
    @Setter
    private Long numTelefono;

    @Column(name = "REF_CORREO_ELECTRONICO")
    @Size(min = 1, max = 50)
    @Getter
    @Setter
    private String correo;

    @Column(name = "NUM_NSS")
    @Size(min = 1, max = 11)
    @Getter
    @Setter
    private String nss;

    @Column(name = "REF_CURP")
    @Size(min = 1, max = 18)
    @Getter
    @Setter
    private String curp;

    @Column(name = "FEC_EXPIRACION")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date vigenciaToken;

    @Column(name = "IND_TOKEN_CONFIRMADO")
    @Getter
    @Setter
    private Integer indTokenConfirmado;

    @Column(name = "CVE_PERFIL")
    @Getter
    @Setter
    private Long cvePerfil;

    @Column(name = "MATRICULA_TRABAJADOR_IMSS")
    @Getter
    @Setter
    private String matriculaTrabajadorImss;

    @Column(name = "DELEGACION_TRABAJADOR_IMSS")
    @Getter
    @Setter
    private String delegacionTrabajadorImss;

    @Column(name = "RFC")
    @Getter
    @Setter
    private String rfc;
    @Column(name = "REGISTRO_PATRONAL")
    @Getter
    @Setter
    private String registroPatronal;
    @Column(name = "NUM_EMPLEADO")
    @Getter
    @Setter
    private String numEmpleado;
    @Column(name = "CVE_ENTIDAD_FINANCIERA")
    @Getter
    @Setter
    private String cveEntidadFinanciera;
    @Column(name = "OTROS_DATOS_JSON")
    @Getter
    @Setter
    private String otrosDatosJson;
    @Column(name = "FIRMA_CARTA_RECIBO")
    @Getter
    @Setter
    private Long firmaCartaRecibo;
        
    @Getter
    @Setter
    @Transient
    private TokenRegistroUsusario tokenRegistroUsuario;
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof McltTokenRegistroUsuario)) {
            return false;
        }
        McltTokenRegistroUsuario other = (McltTokenRegistroUsuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}

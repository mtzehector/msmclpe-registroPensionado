/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.entity.LogicDeletedEntity;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;
import mx.gob.imss.dpes.support.config.CustomDateSerializer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author edgar.arenas
 */
@Entity
@Table(name = "MCLT_PERSONA")
public class McltPersona extends LogicDeletedEntity<Long> {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "MCLS_PERSONA")
    @GenericGenerator(
            name = "MCLS_PERSONA",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "MCLS_PERSONA"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "CVE_PERSONA")
    @Getter
    @Setter
    private Long id;

    @Column(name = "CVE_SEXO")
    @Getter
    @Setter
    private short cveSexo;

    @Column(name = "CVE_ENTIDAD_FINANCIERA")
    @Getter
    @Setter
    private Long cveEntidadFinanciera;

    @Column(name = "CVE_PERSONAL_EF")
    @Getter
    @Setter
    private Long cvePersonalEf;

    @Column(name = "CVE_ESTADO_VITAL")
    @Getter
    @Setter
    private Long cveEstadoVital;

    @Column(name = "CVE_TIPO_EMPLEADO")
    @Getter
    @Setter
    private Long cveTipoEmpleado;

    @Size(max = 50)
    @Column(name = "CVE_REF_DOMICILIO")
    @Getter
    @Setter
    private String cveRefDomicilio;

    @Column(name = "CORREO_ELECTRONICO")
    @Getter
    @Setter
    private String correoElectronico;
    
    @Size(max = 50)
    @Column(name = "NOMBRE")
    @Getter
    @Setter
    private String nombre;

    @Size(max = 50)
    @Column(name = "PRIMER_APELLIDO")
    @Getter
    @Setter
    private String primerApellido;

    @Size(max = 50)
    @Column(name = "SEGUNDO_APELLIDO")
    @Getter
    @Setter
    private String segundoApellido;

    @Column(name = "CVE_CURP")
    @Getter
    @Setter
    private String cveCurp;

    @Size(max = 50)
    @Column(name = "NUM_EMPLEADO")
    @Getter
    @Setter
    private String numEmpleado;

    @Size(max = 11)
    @Column(name = "NUM_NSS")
    @Getter
    @Setter
    private String numNss;

    @Column(name = "FEC_NACIMIENTO")
    @Temporal(TemporalType.DATE)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    @Getter
    @Setter
    private Date fecNacimiento;

    @Size(max = 30)
    @Column(name = "REGISTRO_PATRONAL")
    @Getter
    @Setter
    private String registroPatronal;

    @Column(name = "TEL_LOCAL")
    @Getter
    @Setter
    private BigInteger telLocal;

    @Column(name = "TEL_CELULAR")
    @Getter
    @Setter
    private Long telCelular;

    @Column(name = "CVE_MOTIVO_BAJA")
    @Getter
    @Setter
    private Long baja;

    @Column(name = "CVE_USUARIO")
    @Getter
    @Setter
    private Long cveUsuario;

    @Column(name = "IND_REGISTRADO")
    @Getter
    @Setter
    private Integer indRegistrado;

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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof McltPersona)) {
            return false;
        }
        McltPersona other = (McltPersona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}

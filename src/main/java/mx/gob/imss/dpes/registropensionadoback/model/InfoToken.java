/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;
import mx.gob.imss.dpes.support.config.CustomDateSerializer;

/**
 *
 * @author edgar.arenas
 */
@Data
public class InfoToken extends BaseModel {

    private Long id;
    private String curp;
    private String nss;
    private Long numTelefono;
    private String correo;
    private String token;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date vigenciaToken;
    private String matriculaTrabajadorImss;
    private String delegacionTrabajadorImss;
    private Long cvePerfil;
    private Integer cveEntidadFinanciera;
    private String rfc;
    private String registroPatronal;
    private String numEmpleado;
    private Long firmaCartaRecibo;
}

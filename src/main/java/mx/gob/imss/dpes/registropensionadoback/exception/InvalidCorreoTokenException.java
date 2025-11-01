/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.exception;

import java.util.List;


/**
 *
 * @author eduardo.montesh
 */
public class InvalidCorreoTokenException extends LocalBusinessException {
    public final static String KEY = "msg355";
    public final static String ERROR_DESCONOCIDO_EN_EL_SERVICIO = "err003";
    public final static String ERROR_DE_LECTURA_DE_BD = "err006";
    
    public InvalidCorreoTokenException() {
        super(KEY);
    }
    
    public InvalidCorreoTokenException(List parameters) {
       super(KEY);
       super.addParameters(parameters);
               
    }
    public InvalidCorreoTokenException(String causa) {
        super(causa);
    }
}

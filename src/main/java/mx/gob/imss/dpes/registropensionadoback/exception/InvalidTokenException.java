/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.exception;

import java.util.List;
import mx.gob.imss.dpes.common.exception.BusinessException;


/**
 *
 * @author eduardo.montesh
 */
public class InvalidTokenException extends LocalBusinessException {
    public final static String KEY = "msg354";
    public final static String ERROR_DE_LECTURA_DE_BD = "err006";
    public final static String ERROR_DE_ESCRITURA_DE_BD = "err007";
    
    public InvalidTokenException() {
        super(KEY);
    }
    
    public InvalidTokenException(List parameters) {
       super(KEY);
       super.addParameters(parameters);
               
    }
    public InvalidTokenException(String causa) {
        super(causa);
    }
}

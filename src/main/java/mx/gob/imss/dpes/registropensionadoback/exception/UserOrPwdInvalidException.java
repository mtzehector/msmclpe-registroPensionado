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
public class UserOrPwdInvalidException extends LocalBusinessException {
    public final static String KEY = "msg352";
    
    public UserOrPwdInvalidException() {
        super(KEY);
    }
    
    public UserOrPwdInvalidException(List parameters) {
       super(KEY);
       super.addParameters(parameters);
               
    }
    public UserOrPwdInvalidException(String causa) {
        super(causa);
    }
}

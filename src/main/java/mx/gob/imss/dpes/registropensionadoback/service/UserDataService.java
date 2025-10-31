/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.interceptor.Interceptors;
import javax.ws.rs.ext.Provider;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.interfaces.userdata.model.UserData;
import mx.gob.imss.dpes.registropensionadoback.exception.UsuarioException;
import mx.gob.imss.dpes.registropensionadoback.repository.UserOperations;
import mx.gob.imss.dpes.support.config.CustomSpringBeanAutowiringInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author gabriel.rios
 */
@Provider
@Interceptors(CustomSpringBeanAutowiringInterceptor.class)
public class UserDataService {

    Logger log = Logger.getLogger(getClass().getName());

    @Autowired
    private UserOperations userDAO;

    public UserData findUserData(String userName) throws BusinessException {
        try {
            return userDAO.findUserData(userName);
        }
        catch(BusinessException be) {
            throw be;
        }
        catch (Exception e) {
            log.log(Level.SEVERE, "UserDataService.findUserData - userName = [" + userName + "]", e);
            throw new UsuarioException(UsuarioException.ERROR_DE_LECTURA_DE_BD);
        }
    }

}

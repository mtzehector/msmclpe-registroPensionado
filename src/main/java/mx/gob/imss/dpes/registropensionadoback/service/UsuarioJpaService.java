/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.service;

import javax.interceptor.Interceptors;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.registropensionadoback.repository.UsuarioRepository;
import mx.gob.imss.dpes.support.config.CustomSpringBeanAutowiringInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author edgar.arenas
 */
@Provider
@Interceptors(CustomSpringBeanAutowiringInterceptor.class)
public class UsuarioJpaService {

    @Autowired
    private UsuarioRepository repository;

    public void updateIndActivo(String nomUsuario, Integer activo) {

        repository.updateIndActivoUsuario(activo, nomUsuario);

    }

    public void updateNomUsuario(String nomUsuarioNew, String nomUsuarioOld) {

        repository.updateNomUsuario(nomUsuarioNew, nomUsuarioOld);

    }
}

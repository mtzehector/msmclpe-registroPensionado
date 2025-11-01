/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import javax.interceptor.Interceptors;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.baseback.persistence.BaseSpecification;
import mx.gob.imss.dpes.baseback.service.BaseCRUDService;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.registropensionadoback.entity.McltUsuario;
import mx.gob.imss.dpes.registropensionadoback.exception.UsuarioException;
import mx.gob.imss.dpes.registropensionadoback.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadoback.repository.UsuarioByCorreoSpecification;
import mx.gob.imss.dpes.registropensionadoback.repository.UsuarioRepository;
import mx.gob.imss.dpes.support.config.CustomSpringBeanAutowiringInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 *
 * @author edgar.arenas
 */
@Provider
@Interceptors(CustomSpringBeanAutowiringInterceptor.class)
public class UsuarioService extends BaseCRUDService<McltUsuario, McltUsuario, Long, Long> {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public JpaSpecificationExecutor<McltUsuario> getRepository() {
        return repository;
    }

    @Override
    public JpaRepository<McltUsuario, Long> getJpaRepository() {
        return repository;
    }

    public McltUsuario validarUsuario(String correo) throws BusinessException {
        try {
            Collection<BaseSpecification> constraints = new ArrayList<>();
            constraints.add(new UsuarioByCorreoSpecification(correo));
            McltUsuario mcltUsuario = findOne(constraints);
            return mcltUsuario;
        } catch(Exception e) {
            log.log(Level.SEVERE,
                    "UsuarioService.validarUsuario - correo = [" + correo + "]", e);
            throw new UsuarioException(UsuarioException.ERROR_DE_LECTURA_DE_BD);
        }
    }

    public McltUsuario registrarUsuario(Long idPersona, RegistroRequest request) throws BusinessException {

        try {
            McltUsuario usuario = new McltUsuario();
            usuario.setIdPersona(idPersona);
            usuario.setNomUsuario(request.getInfoToken().getCorreo());
            usuario.setPassword(request.getPassword());
            usuario.setIndActivo(1);
            //log.log(Level.INFO, ">>>>BACK Registrar Usuario: {0}", usuario);

            McltUsuario saved = save(usuario);
            return saved;
        } catch (Exception e) {
            log.log(Level.SEVERE,
                    "UsuarioService.registrarUsuario - idPersona = [" + idPersona + "], request = [" + request +
                            "]", e);
            throw new UsuarioException(UsuarioException.ERROR_DE_ESCRITURA_EN_BD);
        }
    }

    public McltUsuario actualizarPassword(RegistroRequest request) throws BusinessException {

        try {
            McltUsuario usuario = validarUsuario(request.getInfoToken().getCorreo());
            usuario.setPassword(request.getPassword());
            //log.log(Level.INFO, ">>>>BACK Actualizar Password: {0}", usuario);
            save(usuario);
            return usuario;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            log.log(Level.SEVERE,
                "UsuarioService.actualizarPassword - request = [" + request + "]", e);
            throw new UsuarioException(UsuarioException.ERROR_DE_ESCRITURA_EN_BD);
        }
    }
    
    public McltUsuario validaCreaUsuario(RegistroRequest request, Long idPersona) throws BusinessException {
        try {
            //log.log(Level.INFO, ">>>>BACK validaCreaUsusario: {0}", request.getInfoToken().getCorreo());
            Collection<BaseSpecification> constraints = new ArrayList<>();
            constraints.add(new UsuarioByCorreoSpecification(request.getInfoToken().getCorreo()));
            McltUsuario us = findOne(constraints);
            McltUsuario use;
            if (us == null) {
                McltUsuario user = new McltUsuario();
                user.setIdPersona(idPersona);
                user.setNomUsuario(request.getInfoToken().getCorreo());
                user.setPassword(request.getPassword());
                user.setIndActivo(1);
                use = save(user);
                //log.log(Level.INFO, ">>>>BACK USUARIO CREADO: {0}", use);
            } else {
                us.setPassword(request.getPassword());
                us.setNomUsuario(request.getInfoToken().getCorreo());
                us.setIdPersona(idPersona);
                us.setIndActivo(1);
                use = save(us);
                //log.log(Level.INFO, ">>>>BACK USUARIO ACTUALIZADO: {0}", use);
            }
            return use;
        }catch (Exception e) {
            log.log(Level.SEVERE,
                    "UsuarioService.validaCreaUsuario - request = [" + request + "], idPersona = [" +
                            idPersona + "]", e);
            throw new UsuarioException(UsuarioException.ERROR_DE_ESCRITURA_EN_BD);
        }
    }

    public McltUsuario obtenerUsuarioByCvePersona(Long cvePersona) throws BusinessException{
        try {
            return repository.findByIdPersona(cvePersona);
        }catch (Exception e){
            log.log(Level.SEVERE,
                    "UsuarioService.obtenerUsuarioByCvePersona - cvePersona = [" + cvePersona + "]", e);
            throw new UsuarioException(UsuarioException.ERROR_DE_LECTURA_DE_BD);
        }
    }

    public McltUsuario registrarUsuarioPensionado(String correo, Long idPersona) throws BusinessException {
        try {
            McltUsuario usuario = new McltUsuario();
            usuario.setNomUsuario(correo);
            usuario.setPassword("cafeT3mporal123456");
            usuario.setIndActivo(1);
            usuario.setIdPersona(idPersona);
            return save(usuario);
        } catch(Exception e) {
            log.log(Level.SEVERE,
                    "UsuarioService.registrarUsuarioPensionado - correo = [" + correo + "], idPersona = [" +
                            idPersona + "]", e);
            throw new UsuarioException(UsuarioException.ERROR_DE_ESCRITURA_EN_BD);
        }
    }

}

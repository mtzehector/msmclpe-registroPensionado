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
import mx.gob.imss.dpes.registropensionadoback.entity.McltPerfilUsuario;
import mx.gob.imss.dpes.registropensionadoback.exception.PerfilUsuarioException;
import mx.gob.imss.dpes.registropensionadoback.repository.PerfilSpecification;
import mx.gob.imss.dpes.registropensionadoback.repository.PerfilUsuarioRepository;
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
public class PerfilUsuarioService extends BaseCRUDService<McltPerfilUsuario, McltPerfilUsuario, Long, Long>{

    @Autowired
    private PerfilUsuarioRepository repository;

    public McltPerfilUsuario getPerfilByCveUsuario(Long cveUsuario) throws BusinessException {
        try {
            return repository.findByIdUsuario(cveUsuario);
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR PerfilUsuarioService.getPerfilByCveUsuario - cveUsuario=[" +
                    cveUsuario + "]", e);
            throw new PerfilUsuarioException(PerfilUsuarioException.ERROR_DE_LECTURA_DE_BD);
        }
    }
    
    public void registrarPerfil(Long idUsuario, Long idPerfil, Long idFirmaCarta) throws BusinessException {
        try {
            McltPerfilUsuario perfil = new McltPerfilUsuario();
            perfil.setIdUsuario(idUsuario);
            perfil.setIdPerfil(idPerfil);
            perfil.setFirmaCartaRecibo(idFirmaCarta);
            save(perfil);
        } catch(Exception e) {
            log.log(Level.SEVERE,
                    "PerfilUsuarioService.registrarPerfil - idUsuario = [" + idUsuario + "], idPerfil=[" +
                            idPerfil + "], idFirmaCarta = [" + idFirmaCarta + "]", e);
            throw new PerfilUsuarioException(PerfilUsuarioException.ERROR_DE_ESCRITURA_EN_BD);
        }
    }
    
     public McltPerfilUsuario getPerfil(McltPerfilUsuario usuario)throws BusinessException{
        try {
             Collection<BaseSpecification> constraints = new ArrayList<>();
             constraints.add(new PerfilSpecification(usuario.getIdUsuario()));
             McltPerfilUsuario mcltPerfil = findOne(constraints);
             return mcltPerfil;
        } catch(Exception e) {
            log.log(Level.SEVERE,
                    "PerfilUsuarioService.getPerfil - usuario = [" + usuario + "]", e);
            throw new PerfilUsuarioException(PerfilUsuarioException.ERROR_DE_LECTURA_DE_BD);
        }
     }
     
    public McltPerfilUsuario getPerfilUsuario(Long idUsuario)throws BusinessException{
        try {
            Collection<BaseSpecification> constraints = new ArrayList<>();
            constraints.add(new PerfilSpecification(idUsuario));
            McltPerfilUsuario mcltPerfil = findOne(constraints);
            //log.log(Level.INFO,">>>>BACK Resultado GET PERFIL: {0}", mcltPerfil);
            return mcltPerfil;
        } catch(Exception e) {
            log.log(Level.SEVERE,
                    "PerfilUsuarioService.getPerfilUsuario - idUsuario = [" + idUsuario + "]", e);
            throw new PerfilUsuarioException(PerfilUsuarioException.ERROR_DE_LECTURA_DE_BD);
        }
    }
    
    public McltPerfilUsuario actualizarPerfil(Long idUsuario, Long firmaCartaRecibo) throws BusinessException {
         try {
        	 
        	 Collection<BaseSpecification> constraints = new ArrayList<>();
             constraints.add(new PerfilSpecification(idUsuario));
             McltPerfilUsuario mcltPerfil = findOne(constraints);
             mcltPerfil.setFirmaCartaRecibo(firmaCartaRecibo);
             save(mcltPerfil);
             return mcltPerfil;
             
         } catch(Exception e) {
             log.log(Level.SEVERE,
                     "PerfilUsuarioService.actualizaPerfil - idUsuario = [" + idUsuario + "], firmaCartaRecibo = [" +
                             firmaCartaRecibo + "]", e);
             throw new PerfilUsuarioException(PerfilUsuarioException.ERROR_DE_ESCRITURA_EN_BD);
         }
     }
    
    @Override
    public JpaSpecificationExecutor<McltPerfilUsuario> getRepository() {
         return repository;
    }

    @Override
    public JpaRepository<McltPerfilUsuario, Long> getJpaRepository() {
         return repository;
    }
    
}

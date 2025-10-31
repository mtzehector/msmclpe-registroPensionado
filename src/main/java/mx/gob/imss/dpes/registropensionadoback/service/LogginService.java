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
import mx.gob.imss.dpes.registropensionadoback.entity.McltUsuario1;
import mx.gob.imss.dpes.registropensionadoback.repository.LogginRepository;
import mx.gob.imss.dpes.registropensionadoback.repository.LogginSpecification;
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
public class LogginService extends BaseCRUDService<McltUsuario1, McltUsuario1, Long, Long>{
    
     
    @Autowired
    private LogginRepository repository;

    @Override
    public JpaSpecificationExecutor<McltUsuario1> getRepository() {
        return repository;
    }

    @Override
    public JpaRepository<McltUsuario1, Long> getJpaRepository() {
        return repository;
    }
    
    public McltUsuario1 loggeo(McltUsuario1 usuario)throws BusinessException{
        
        Collection<BaseSpecification> constraints = new ArrayList<>();
        constraints.add(new LogginSpecification(usuario.getNomUsuario(), usuario.getPassword()));
        McltUsuario1 mcltUsuario = findOne(constraints);
        log.log(Level.INFO,">>>>BACK Resultado Validar Usuario Rec: {0}", mcltUsuario);
        return mcltUsuario;
    }
    

}

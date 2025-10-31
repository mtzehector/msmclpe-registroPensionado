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
import mx.gob.imss.dpes.common.exception.BadRequestException;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.registropensionadoback.entity.McltTokenRegistroUsuario;
import mx.gob.imss.dpes.registropensionadoback.exception.InvalidTokenException;
import mx.gob.imss.dpes.registropensionadoback.model.PregistroRequest;
import mx.gob.imss.dpes.registropensionadoback.repository.ObtenerInfoTokenSpecification;
import mx.gob.imss.dpes.registropensionadoback.repository.TokenRegistroUsuarioRepository;
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
public class ValidarTokenService extends BaseCRUDService<McltTokenRegistroUsuario, McltTokenRegistroUsuario, Long, Long> {

    @Autowired
    private TokenRegistroUsuarioRepository repository;

    @Override
    public JpaSpecificationExecutor<McltTokenRegistroUsuario> getRepository() {
        return repository;
    }

    @Override
    public JpaRepository<McltTokenRegistroUsuario, Long> getJpaRepository() {
        return repository;
    }

    public Message<McltTokenRegistroUsuario> execute(Message<PregistroRequest> request)
            throws BusinessException {
        log.log(Level.INFO, ">>>>ValidarTokenService execute Entro obtenInfoTokenService: {0}", request.getPayload().getToken());
        Collection<BaseSpecification> constraints = new ArrayList<>();
        constraints.add(new ObtenerInfoTokenSpecification(request.getPayload().getToken()));
        McltTokenRegistroUsuario mcltToken = findOne(constraints);
        log.log(Level.INFO, ">>>>ValidarTokenService execute Resultado de la consulta: {0}", mcltToken);
        if (mcltToken == null) {
            Message<McltTokenRegistroUsuario> ret = new Message<>();
            ret.getHeader().setStatus(ServiceStatusEnum.EXCEPCION);
            ret.getHeader().setException(new InvalidTokenException());
            return ret;
        }
        return new Message<>(mcltToken);
    }
}

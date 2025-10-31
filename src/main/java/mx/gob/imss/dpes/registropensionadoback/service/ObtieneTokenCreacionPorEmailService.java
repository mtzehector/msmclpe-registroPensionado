/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import javax.interceptor.Interceptors;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.baseback.persistence.BaseSpecification;
import mx.gob.imss.dpes.baseback.service.BaseCRUDService;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.registropensionadoback.entity.McltTokenRegistroUsuario;
import mx.gob.imss.dpes.registropensionadoback.exception.InvalidCorreoTokenException;
import mx.gob.imss.dpes.registropensionadoback.model.PregistroRequest;
import mx.gob.imss.dpes.registropensionadoback.repository.ObtenerInfoCorreoTokenSpecification;
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
public class ObtieneTokenCreacionPorEmailService extends BaseCRUDService<McltTokenRegistroUsuario, McltTokenRegistroUsuario, Long, Long> {

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
        try {
            Collection<BaseSpecification> constraints = new ArrayList<>();
            constraints.add(new ObtenerInfoCorreoTokenSpecification(request.getPayload().getCorreo()));
            List<McltTokenRegistroUsuario> tokenList = load(constraints);

            if (tokenList == null || tokenList.size() == 0)
                return null;

            for (McltTokenRegistroUsuario token : tokenList) {
                if (token.getToken().length() == 36)
                    return new Message<>(token);
            }

            return null;

        } catch (Exception e) {
            log.log(Level.SEVERE,"ERROR ValidarCorreoTokenService.execute - request = [" + request + "]", e);
        }

        throw new InvalidCorreoTokenException(InvalidCorreoTokenException.ERROR_DE_LECTURA_DE_BD);
    }
}

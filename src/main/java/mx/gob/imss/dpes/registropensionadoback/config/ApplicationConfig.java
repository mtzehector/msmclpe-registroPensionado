/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author edgar.arenas
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(mx.gob.imss.dpes.common.exception.AlternateFlowMapper.class);
        resources.add(mx.gob.imss.dpes.common.exception.BusinessMapper.class);
        resources.add(mx.gob.imss.dpes.common.rule.MontoTotalRule.class);
        resources.add(mx.gob.imss.dpes.common.rule.PagoMensualRule.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.endpoint.PerfilEndPoint.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.endpoint.RegistroPensionadoEndPoint.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.endpoint.TokenEndPoint.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.endpoint.UserDataEndPoint.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.service.BitacoraInterfazService.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.service.LogginService.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.service.ObtieneTokenCreacionPorEmailService.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.service.PensionadoService.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.service.PerfilPersonaService.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.service.PerfilUsuarioService.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.service.SesionService.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.service.TokenRegistroUsuarioPersistenceService.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.service.TokenSeguridadService.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.service.TokenService.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.service.UserDataByCurpAndNssService.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.service.UserDataService.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.service.UsuarioJpaService.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.service.UsuarioService.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.service.ValidarTokenService.class);
        resources.add(mx.gob.imss.dpes.registropensionadoback.assembler.UsuarioAssembler.class);
    }

}

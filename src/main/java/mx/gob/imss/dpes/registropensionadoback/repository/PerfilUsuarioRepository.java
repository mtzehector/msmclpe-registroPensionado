/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.repository;

import mx.gob.imss.dpes.registropensionadoback.entity.McltPerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author edgar.arenas
 */
public interface PerfilUsuarioRepository extends JpaRepository<McltPerfilUsuario, Long>, JpaSpecificationExecutor<McltPerfilUsuario>{

    McltPerfilUsuario findByIdUsuario(@Param("cveUsuario") Long cveUsuario);
    
}

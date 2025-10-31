/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.repository;

import mx.gob.imss.dpes.registropensionadoback.entity.McltUsuario1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 *
 * @author edgar.arenas
 */
public interface LogginRepository extends JpaRepository<McltUsuario1, Long>, 
        JpaSpecificationExecutor<McltUsuario1>{
    
}

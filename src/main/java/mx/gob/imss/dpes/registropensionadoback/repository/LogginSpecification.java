/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadoback.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import mx.gob.imss.dpes.baseback.persistence.BaseSpecification;
import mx.gob.imss.dpes.registropensionadoback.entity.McltTokenRegistroUsuario_;
import mx.gob.imss.dpes.registropensionadoback.entity.McltUsuario;
import mx.gob.imss.dpes.registropensionadoback.entity.McltUsuario_;

/**
 *
 * @author edgar.arenas
 */
public class LogginSpecification extends BaseSpecification<McltUsuario>{
    
   private final String nomUsuario;
    private final String password;
  
  public LogginSpecification(String nomUsuario, String password) {
    
    this.nomUsuario = nomUsuario;
    this.password = password;
  }
  
  @Override
  public Predicate toPredicate(Root<McltUsuario> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        return cb.and(cb.equal(root.get(McltUsuario_.nomUsuario), this.nomUsuario), cb.equal(root.get(McltUsuario_.password), this.password));
                
    }
}

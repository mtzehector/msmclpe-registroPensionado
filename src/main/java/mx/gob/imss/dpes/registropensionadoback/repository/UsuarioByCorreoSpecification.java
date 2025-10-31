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
import mx.gob.imss.dpes.registropensionadoback.entity.McltUsuario;
import mx.gob.imss.dpes.registropensionadoback.entity.McltUsuario_;

/**
 *
 * @author edgar.arenas
 */
public class UsuarioByCorreoSpecification extends BaseSpecification<McltUsuario>{
    
  //private final String curp;
  private final String correo;
  
  public UsuarioByCorreoSpecification(String correo) {
    //this.curp = curp;
    this.correo = correo;
  }
  
  @Override
    public Predicate toPredicate(Root<McltUsuario> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        return cb.equal(root.get(McltUsuario_.nomUsuario), this.correo);
         
    }
    
}

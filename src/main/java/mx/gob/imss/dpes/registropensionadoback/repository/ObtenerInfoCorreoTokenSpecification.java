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
import mx.gob.imss.dpes.registropensionadoback.entity.McltTokenRegistroUsuario;
import mx.gob.imss.dpes.registropensionadoback.entity.McltTokenRegistroUsuario_;

/**
 *
 * @author edgar.arenas
 */
public class ObtenerInfoCorreoTokenSpecification extends BaseSpecification<McltTokenRegistroUsuario>{
    
  private final String correo;
  
  public ObtenerInfoCorreoTokenSpecification(String correo) {
    
    this.correo = correo;
  }
  
  @Override
  public Predicate toPredicate(Root<McltTokenRegistroUsuario> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        cq.orderBy(cb.asc(root.get(McltTokenRegistroUsuario_.id)));
        return cb.and(
          cb.equal(root.get(McltTokenRegistroUsuario_.nomUsuario), this.correo),
          cb.equal(root.get(McltTokenRegistroUsuario_.indActivo), 1)
        );
    }
}

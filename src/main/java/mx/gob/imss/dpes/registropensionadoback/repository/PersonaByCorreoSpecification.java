package mx.gob.imss.dpes.registropensionadoback.repository;

import mx.gob.imss.dpes.baseback.persistence.BaseSpecification;
import mx.gob.imss.dpes.registropensionadoback.entity.McltPersona;
import mx.gob.imss.dpes.registropensionadoback.entity.McltPersona_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class PersonaByCorreoSpecification extends BaseSpecification<McltPersona>{

  private final String correo;

  public PersonaByCorreoSpecification(String correo) {
    this.correo = correo;
  }
  
  @Override
    public Predicate toPredicate(Root<McltPersona> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        return cb.equal(root.get(McltPersona_.correoElectronico), this.correo);
    }
    
}

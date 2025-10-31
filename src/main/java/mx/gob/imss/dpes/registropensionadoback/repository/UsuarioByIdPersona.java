package mx.gob.imss.dpes.registropensionadoback.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import mx.gob.imss.dpes.baseback.persistence.BaseSpecification;
import mx.gob.imss.dpes.registropensionadoback.entity.McltUsuario;
import mx.gob.imss.dpes.registropensionadoback.entity.McltUsuario_;

/**
 *
 * @author luisr.rodriguez
 */
@AllArgsConstructor
public class UsuarioByIdPersona extends BaseSpecification<McltUsuario> {
    private Long idPersona;

    @Override
    public Predicate toPredicate(Root<McltUsuario> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        return cb.equal(root.get(McltUsuario_.ID_PERSONA), this.idPersona);
    }
    
}

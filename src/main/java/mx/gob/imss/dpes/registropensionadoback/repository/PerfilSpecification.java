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
import mx.gob.imss.dpes.registropensionadoback.entity.McltPerfilUsuario;
import mx.gob.imss.dpes.registropensionadoback.entity.McltPerfilUsuario_;

/**
 *
 * @author edgar.arenas
 */
public class PerfilSpecification extends BaseSpecification<McltPerfilUsuario> {

    private final Long idUsuario;

    public PerfilSpecification(Long idUsuario) {

        this.idUsuario = idUsuario;
    }

    @Override
    public Predicate toPredicate(Root<McltPerfilUsuario> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        return cb.equal(root.get(McltPerfilUsuario_.idUsuario), this.idUsuario);

    }


}

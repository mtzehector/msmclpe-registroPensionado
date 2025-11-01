package mx.gob.imss.dpes.registropensionadoback.repository;

import mx.gob.imss.dpes.registropensionadoback.entity.McltToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<McltToken, Long>, JpaSpecificationExecutor<McltToken> {
    @Query(value="SElECT MCLS_SESION.NEXTVAL FROM DUAL",nativeQuery = true)
    Long obtenerSesion();
}

package mx.gob.imss.dpes.registropensionadoback.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class PerfilUsuarioException extends BusinessException {

    public final static String ERROR_DESCONOCIDO_EN_EL_SERVICIO = "err003";
    public final static String ERROR_DE_LECTURA_DE_BD = "err006";
    public final static String ERROR_DE_ESCRITURA_EN_BD = "err007";

    public PerfilUsuarioException(String messageKey) {
        super(messageKey);
    }
}

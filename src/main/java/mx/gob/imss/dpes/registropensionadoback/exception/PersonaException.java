package mx.gob.imss.dpes.registropensionadoback.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class PersonaException extends BusinessException {

    public final static String ERROR_DESCONOCIDO_EN_EL_SERVICIO = "err003";
    public final static String ERROR_DE_LECTURA_DE_BD = "err006";
    public final static String ERROR_DE_ESCRITURA_EN_BD = "err007";
    public final static String ERROR_EMAIL_NO_EXISTE_O_CURP_ES_NULO = "err009";
    public final static String ERROR_EXISTE_RELACION_CON_USUARIO = "msg358";

    public PersonaException(String messageKey) {
        super(messageKey);
    }
}

package mx.gob.imss.dpes.registropensionadoback.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class UsuarioException extends BusinessException {

    public final static String ERROR_DESCONOCIDO_EN_EL_SERVICIO = "err003";
    public final static String ERROR_DE_LECTURA_DE_BD = "err006";
    public final static String ERROR_DE_ESCRITURA_EN_BD = "err007";
    public final static String ERROR_EMAIL_EXISTE = "err008";
    public final static String ERROR_AL_RECUPERAR_ID_DE_USUARIO = "err010";
    public final static String MENSAJE_DE_SOLICITUD_INCORRECTO = "msg357";

    public UsuarioException(String messageKey) {
        super(messageKey);
    }
}

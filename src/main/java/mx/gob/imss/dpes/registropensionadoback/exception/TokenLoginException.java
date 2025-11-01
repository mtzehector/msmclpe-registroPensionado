package mx.gob.imss.dpes.registropensionadoback.exception;

import java.util.List;

public class TokenLoginException extends LocalBusinessException {
    public final static String KEY = "msg354";
    public final static String ERROR_DE_LECTURA_DE_BD = "err006";
    public final static String ERROR_DE_ESCRITURA_DE_BD = "err007";
    public final static String ERROR_DE_CREDENCIALES = "msg359";
    public final static String ERROR_DE_SESION = "msg360";
    public final static String ERROR_DESCONOCIDO_EN_EL_SERVICIO = "err003";
    public final static String ERROR_OBTENER_PERFIL_USUARIO = "msg0361";
    public final static String ERROR_OBTENER_TOKEN = "msg0362";

    public TokenLoginException() {
        super(KEY);
    }

    public TokenLoginException(List parameters) {
       super(KEY);
       super.addParameters(parameters);

    }
    public TokenLoginException(String causa) {
        super(causa);
    }
}

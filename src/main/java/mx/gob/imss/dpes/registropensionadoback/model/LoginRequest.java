package mx.gob.imss.dpes.registropensionadoback.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraInterfaz;
import mx.gob.imss.dpes.registropensionadoback.entity.PerfilPersona;

@Data
public class LoginRequest extends BaseModel {
    private Long sesion;
    private String username;
    private String password;
    private LoginResponse response;
    private BitacoraInterfaz bitacoraInterfaz;
    private PerfilPersona perfilPersona;

    public String toStringLog() {
        StringBuilder builder = new StringBuilder();
        builder.append("LoginRequest [sesion=");
        builder.append(sesion);
        builder.append(", username=");
        builder.append(username);
        builder.append("]");
        return builder.toString();
    }
}

package mx.gob.imss.dpes.registropensionadoback.assembler;

import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.interfaces.userdata.model.UserData;
import mx.gob.imss.dpes.registropensionadoback.entity.McltUsuario;

import javax.ws.rs.ext.Provider;

@Provider
public class UsuarioAssembler extends BaseAssembler<McltUsuario, UserData> {
    @Override
    public UserData assemble(McltUsuario source) {
        if(source == null)
            return null;

        UserData userData = new UserData();
        userData.setNOM_USUARIO(source.getNomUsuario());
        return userData;
    }
}

package mx.gob.imss.dpes.registropensionadoback.exception;

import java.util.List;

public class InvalidHorario extends LocalBusinessException {

    public final static String KEY = "msg356";

    public InvalidHorario() {
        super(KEY);
    }
    public InvalidHorario(List parameters){
        super(KEY);
        super.addParameters(parameters);
    }
    public InvalidHorario(String causa) {
        super(causa);
    }
}

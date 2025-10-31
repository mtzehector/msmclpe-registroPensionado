package mx.gob.imss.dpes.registropensionadoback.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;

@Data
public class LoginResponse extends BaseModel {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private Long expiresIn;
    @JsonProperty("acr")
    private String acr;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("session")
    private Long session;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\"access_token\":\"");
        builder.append(accessToken);
        builder.append("\",\"token_type\":\"");
        builder.append(tokenType);
        builder.append("\",\"expires_in\":");
        builder.append(expiresIn);
        builder.append(",\"acr\":\"");
        builder.append(acr);
        builder.append("\",\"scope\":\"");
        builder.append(scope);
        builder.append("\"}");
        return builder.toString();
    }
}

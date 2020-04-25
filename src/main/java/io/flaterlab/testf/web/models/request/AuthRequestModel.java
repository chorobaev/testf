package io.flaterlab.testf.web.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestModel {

    private static final long serialVersionUID = -6986746375915710855L;
    private String username;
    private String password;
}

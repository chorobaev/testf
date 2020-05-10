package io.flaterlab.testf.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {
    private static final long serialVersionUID = -6986746375915710855L;

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private String mobile;
    private String info;
    private String profile;
}

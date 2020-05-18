package io.flaterlab.testf.web.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfoRequestDto {

    private String firstName;
    private String lastName;
    private String middleName;
    private String mobile;
    private String info;
    private String profile;
}

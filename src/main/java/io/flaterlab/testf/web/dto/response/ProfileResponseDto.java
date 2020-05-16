package io.flaterlab.testf.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {

    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String mobile;
    private String username;
    private String info;
    private String profile;
}

package io.flaterlab.testf.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestDto {

    @NotNull
    @NotEmpty
    private String type; // MULTIPLE_CHOSE || SINGLE_CHOSE

    @NotNull
    @NotEmpty
    private Integer level; // From 1 to 10

    @NotNull
    @NotEmpty
    private Integer score; // From 0 to 10

    @NotNull
    @NotEmpty
    private String content;

    private Boolean active;
}

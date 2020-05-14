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
public class AnswerRequestDto {

    @NotNull
    @NotEmpty
    private Boolean correct;

    @NotNull
    @NotEmpty
    private String content;

    private Boolean active;
}

package io.flaterlab.testf.web.dto.request;

import io.flaterlab.testf.persistence.model.Test;
import io.flaterlab.testf.validation.ValidTestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestRequestDto {

    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    @NotEmpty
    @ValidTestType
    private Test.Type type;

    private String slug;
    private String summary;
    private Boolean published;
    private Date startsAt;
    private Date endsAt;
    private String content;
}

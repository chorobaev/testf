package io.flaterlab.testf.web.dto.response;

import io.flaterlab.testf.persistence.model.Test;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResponseDto {

    private Long id;
    private Long hostId;
    private String title;
    private String slug;
    private String summary;
    private Test.Type type;
    private Integer score;
    private Date startsAt;
    private Date endsAt;
    private String content;
}

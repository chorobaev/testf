package io.flaterlab.testf.web.dto.response;

import io.flaterlab.testf.persistence.model.Test;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttemptResponseDto {

    private Long id;
    private Long attemptId;
    private Long hostId;
    private String title;
    private String slug;
    private String summary;
    private Test.Type type;
    private Integer score;
    private Date startsAt;
    private Date endsAt;
    private String content;
    private List<AttemptQuestionResponseDto> questions;
}

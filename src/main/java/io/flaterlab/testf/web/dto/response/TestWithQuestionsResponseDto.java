package io.flaterlab.testf.web.dto.response;

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
public class TestWithQuestionsResponseDto {

    private long id;
    private long hostId;
    private String title;
    private String slug;
    private String summary;
    private String type;
    private int score;
    private Date startsAt;
    private Date endsAt;
    private String content;

    private List<QuestionResponseDto> questions;
}

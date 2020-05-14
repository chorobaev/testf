package io.flaterlab.testf.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDto {

    private long id;
    private long testId;
    private String type; // MULTIPLE_CHOSE || SINGLE_CHOSE
    private int level;
    private int score;
    private String content;
}

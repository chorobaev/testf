package io.flaterlab.testf.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttemptResultResponseDto {

    private Long attemptId;
    private Long testId;
    private String testTitle;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Integer totalScore;
    private Integer earnedScore;
    private String summary;
}

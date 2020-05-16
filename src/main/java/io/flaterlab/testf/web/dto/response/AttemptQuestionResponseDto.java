package io.flaterlab.testf.web.dto.response;

import io.flaterlab.testf.persistence.model.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttemptQuestionResponseDto {

    private Long id;
    private Question.Type type;
    private Integer level;
    private String content;
    private List<AttemptAnswerResponseDto> answers;
}

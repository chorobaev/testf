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
public class QuestionResponseDto {

    private Long id;
    private Long testId;
    private String type; // MULTIPLE_CHOSE || SINGLE_CHOSE
    private Boolean active;
    private Integer level;
    private Integer score;
    private Date createdAt;
    private Date updatedAt;
    private String content;
    private List<AnswerResponseDto> answers;
}

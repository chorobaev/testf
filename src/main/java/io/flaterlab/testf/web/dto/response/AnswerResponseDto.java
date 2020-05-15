package io.flaterlab.testf.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponseDto {

    private Long id;
    private Long questionId;
    private Boolean active;
    private Boolean correct;
    private Date createdAt;
    private Date updatedAt;
    private String content;
}

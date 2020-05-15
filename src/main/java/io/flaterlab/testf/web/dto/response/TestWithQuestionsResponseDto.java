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

    private Long id;
    private Long hostId;
    private String title;
    private String slug;
    private String summary;
    private String type;
    private Integer score;
    private Boolean published;
    private Date createdAt;
    private Date updatedAt;
    private Date publishedAt;
    private Date startsAt;
    private Date endsAt;
    private String content;
    private List<QuestionResponseDto> questions;
}

package io.flaterlab.testf.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id", nullable = false)
    private Test test;

    @Enumerated
    @Column(nullable = false, columnDefinition = "smallint")
    private Status status;

    private Integer totalQuestions;

    private Integer correctAnswers;

    private Integer totalScore;

    private Integer earnedScore;

    @Column(nullable = false)
    private Boolean published;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Temporal(TemporalType.DATE)
    private Date startedAt;

    @Temporal(TemporalType.DATE)
    private Date finishedAt;

    private String content;

    public enum Status {
        STARTED, FINISHED, PAUSED
    }
}

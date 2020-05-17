package io.flaterlab.testf.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "questions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id", nullable = false)
    private Test test;

    @Enumerated
    @Column(nullable = false, columnDefinition = "smallint")
    private Type type;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false)
    private Integer score;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Column(nullable = false)
    private String content;

    public enum Type {
        MULTIPLE_CHOSE, SINGLE_CHOSE
    }
}

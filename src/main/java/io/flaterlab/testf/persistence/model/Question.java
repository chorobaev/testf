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
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id", nullable = false)
    private Test test;

    @Column(length = 50, nullable = false)
    private String type; // MULTIPLE_CHOSE || SINGLE_CHOSE

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private int score;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Column(nullable = false)
    private String content;
}

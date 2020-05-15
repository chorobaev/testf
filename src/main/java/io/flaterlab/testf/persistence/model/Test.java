package io.flaterlab.testf.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"title", "user_id"}))
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Test {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(length = 75, nullable = false)
    private String title;

    @Column(length = 100)
    private String slug;

    private String summary;

    @Column(length = 10)
    private String type; // TEST || QUIZ

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private Boolean published;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Temporal(TemporalType.DATE)
    private Date publishedAt;

    @Temporal(TemporalType.DATE)
    private Date startsAt;

    @Temporal(TemporalType.DATE)
    private Date endsAt;

    private String content;
}

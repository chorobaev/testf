package io.flaterlab.testf.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "privileges")
@Data
@NoArgsConstructor
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    public Privilege(String name) {
        this.name = name;
    }
}

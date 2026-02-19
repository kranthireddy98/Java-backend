package org.user.entity;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "department")
    private List<User> users;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}
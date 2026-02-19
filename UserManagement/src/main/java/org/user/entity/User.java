package org.user.entity;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;


    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private Integer age;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(mappedBy = "user")
    private List<UserRole> roles;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}

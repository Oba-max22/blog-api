package com.decagon.obamax.BlogRest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "users")
public class User extends AuditModel {
    @Id
    @GeneratedValue(generator = "user_generator")
    @SequenceGenerator(
            name = "user_generator",
            sequenceName = "user_sequence",
            initialValue = 1000
    )
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    private String email;

    @NotBlank
    @Size(min=8, max = 16)
    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @JsonIgnore
    @Column(columnDefinition = "int default 0")
    private int isDeactivated;

    @JsonIgnore
    @Column(columnDefinition = "int default 0")
    private int isDeleted;

    @JsonIgnore
    private String deleteDate;

    @OneToMany(mappedBy = "post")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Favorites> FavoritesList;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<User> followers;
}

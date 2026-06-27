package com.oxaira.airq.iam.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oxaira.airq.subscription.domain.model.Subscription;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private String phone;

    private String zone;

    private Boolean enabled;

    private LocalDateTime createdAt;

    private String companyName;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Subscription subscription;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_campuses", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "campus_name")
    private java.util.List<String> campuses = new java.util.ArrayList<>();
}

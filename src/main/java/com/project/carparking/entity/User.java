package com.project.carparking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



@Entity
@Table(name = "user", indexes = {
        @Index(name = "name_index", columnList = "name"),
        @Index(name = "phoneNo_index", columnList = "phoneNo")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "Name can not be blank")
    @Length(min = 3, max = 25, message = "Name should be between 3 to 25 characters")
    @NotEmpty(message = "Name can not be blank")
    private String name;

    private String address;

    @Column(name = "phoneNo", unique = true)
    @Length(min = 10, message = "Phone number must be 10 digits and valid")
    @NotEmpty(message = "Phone number can not be blank")
    private String phoneNo;

    @NotBlank( message = "Password can not be blank")
    @Column(name = "password", nullable = false)
    @Length(min = 6, message = "Password must be grater than 6 character")
    @NotEmpty(message = "Password can not be blank")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private EnumRole role;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> vehicles = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDate createdAt;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phoneNo;
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



    private boolean hasRole(String role) {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authorities.stream().anyMatch(authority -> authority.getAuthority().equals(role));
    }
}
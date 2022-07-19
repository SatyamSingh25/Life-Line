package com.hu22.bloodBankBackendPrivate.entities;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@ApiModel(description = "User Entity")
public class User {

    @Id
    @NotNull
    private String email;

    @NotNull
    private String firstName;

    private String secondName;

    @NotNull
    private String password;

    @NotNull
    private String dob;

    private String bloodGroup;

    @NotNull
    private String city;

    @NotNull
    private String phone;

    //joining table user and userRole
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID")
            }
    )
    private Set<Role> roles;

    public void addRole(Role role){
        this.roles.add(role);
    }

    public User(String email, String firstName, String secondName, String password, String dob, String bloodGroup, String city, String phone) {
        this.email = email;
        this.firstName = firstName;
        this.secondName = secondName;
        this.password = password;
        this.dob = dob;
        this.bloodGroup = bloodGroup;
        this.city = city;
        this.phone = phone;
    }
}

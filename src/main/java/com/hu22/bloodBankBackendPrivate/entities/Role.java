package com.hu22.bloodBankBackendPrivate.entities;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ApiModel(description = "Role Entity")
public class Role {
    @Id
    private String roleName;
    private String roleDescription;
}

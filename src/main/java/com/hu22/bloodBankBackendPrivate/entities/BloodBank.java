package com.hu22.bloodBankBackendPrivate.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class BloodBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String bloodBankName;

    private Long apositive;
    private Long abpositive;
    private Long opositive;
    private Long bpositive;
    private Long anegative;
    private Long abnegative;
    private Long onegative;
    private Long bnegative;

    @NotNull
    private String city;
    @NotNull
    private String contact;
    @NotNull
    private String email;

    public BloodBank(String bloodBankName, Long apositive, Long abpositive, Long opositive, Long bpositive, Long anegative, Long abnegative, Long onegative, Long bnegative, String city, String contact, String email) {
        this.bloodBankName = bloodBankName;
        this.apositive = apositive;
        this.abpositive = abpositive;
        this.opositive = opositive;
        this.bpositive = bpositive;
        this.anegative = anegative;
        this.abnegative = abnegative;
        this.onegative = onegative;
        this.bnegative = bnegative;
        this.city = city;
        this.contact = contact;
        this.email = email;
    }
}

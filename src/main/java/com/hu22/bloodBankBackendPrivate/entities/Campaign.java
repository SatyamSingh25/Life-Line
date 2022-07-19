package com.hu22.bloodBankBackendPrivate.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String campaignName; //fontEnd
    @NotNull
    private Long byBloodBankId; //frontEnd
    private String bloodBankName;
    private String city; //frontEnd
    private String geoLocation;
    @NotNull
    private String longitude; //frontEnd
    @NotNull
    private String latitude; //frontEnd
    private String address; //frontEnd
    @NotNull
    private String contact; //frontEnd

    private Long apositive = 0L;
    private Long abpositive = 0L;
    private Long opositive = 0L;
    private Long bpositive = 0L;
    private Long anegative = 0L;
    private Long abnegative = 0L;
    private Long onegative = 0L;
    private Long bnegative = 0L;

    private Date startDate; //frontEnd
    private Date endDate; //frontEnd
    private int target; //frontEnd

    //isDeleted = true  -> Campaign Closed
    //isDeleted = false -> Campaign Open
    private boolean isDeleted;
}
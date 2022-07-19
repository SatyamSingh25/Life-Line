package com.hu22.bloodBankBackendPrivate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CampaignUpdate {

    private Long id; //campign ID
    private Long apositive;
    private Long abpositive;
    private Long opositive;
    private Long bpositive;
    private Long anegative;
    private Long abnegative;
    private Long onegative;
    private Long bnegative;
}

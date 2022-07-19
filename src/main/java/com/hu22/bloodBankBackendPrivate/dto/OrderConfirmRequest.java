package com.hu22.bloodBankBackendPrivate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OrderConfirmRequest {
    @NotNull
    private String bloodGroup;
    @NotNull
    private Long unit;
    @NotNull
    private String bloodBankName;
    @NotNull
    private String phone;
}

package com.highfi.gpagn.model;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Balance {
    @Valid
    @NotNull
    private BankAccount bankAccount;

    @NotNull
    private double initialBalance;

    @NotNull
    private Date dateBalance;

}

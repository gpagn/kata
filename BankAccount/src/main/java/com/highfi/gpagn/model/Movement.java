package com.highfi.gpagn.model;


import com.highfi.gpagn.model.enums.MovementType;
import com.highfi.gpagn.model.enums.Sens;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Movement {


    @NotEmpty
    private Sens sens;

    private String description;

    @Positive
    private double amount;

    @NotNull
    @Valid
    private BankAccount receiverBankAccount;

    @NotNull
    @Valid
    private Currency movementCurrency;

    @NotNull
    private Date tradeDate;

    @NotNull
    private MovementType movementType;
}

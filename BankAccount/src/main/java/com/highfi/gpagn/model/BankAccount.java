package com.highfi.gpagn.model;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BankAccount {
    @NotEmpty
    private String accountNumber;

    @Valid
    @NotNull
    private Currency currency;

}

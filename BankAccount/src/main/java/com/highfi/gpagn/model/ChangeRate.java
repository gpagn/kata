package com.highfi.gpagn.model;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ChangeRate {
    @Valid
    @NotNull
    private Currency initialCurrency;

    @Valid
    @NotNull
    private Currency convertedCurrency;

    @NotNull
    @Positive
    private Double rate;

    @NotNull
    private Date rateDate;
}

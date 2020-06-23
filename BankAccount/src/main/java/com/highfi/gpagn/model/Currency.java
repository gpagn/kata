package com.highfi.gpagn.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Currency {

    @NotNull
    private String code;

    @NotNull
    private String libelle;

}

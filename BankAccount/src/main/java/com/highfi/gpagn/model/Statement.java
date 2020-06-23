package com.highfi.gpagn.model;

import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Statement {
    public final String header = "OPERATION | DATE | AMOUNT | BALANCE";

    private BankAccount bankAccount;
    private List<StatementLine> statementLines = new ArrayList<>();
    private Date startDate;
    private Date endDate;

    public List<StatementLine> getSortedStatementLines() {
        statementLines.sort(Comparator.comparing(StatementLine::getTradeDate));
        return statementLines;
    }

    public void addStatementLine(StatementLine statementLine) {
        System.out.println(statementLine);
        this.statementLines.add(statementLine);
    }


}

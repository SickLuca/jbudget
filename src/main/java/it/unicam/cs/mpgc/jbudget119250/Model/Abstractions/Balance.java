package it.unicam.cs.mpgc.jbudget119250.Model.Abstractions;

import java.math.BigDecimal;

public interface Balance {

    Long getId();

    BigDecimal getBalance();

    void setBalance(BigDecimal balance);


}

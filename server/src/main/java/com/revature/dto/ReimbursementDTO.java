package com.revature.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class ReimbursementDTO {

    private BigDecimal amount;
    private String description;
    private String type;
    private String receipt;

    public ReimbursementDTO (){}

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReimbursementDTO that = (ReimbursementDTO) o;
        return Objects.equals(amount, that.amount) && Objects.equals(description, that.description) && Objects.equals(type, that.type) && Objects.equals(receipt, that.receipt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, description, type, receipt);
    }

    @Override
    public String toString() {
        return "ReimbursementDTO{" +
                "amount=" + amount +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", receipt='" + receipt + '\'' +
                '}';
    }
}

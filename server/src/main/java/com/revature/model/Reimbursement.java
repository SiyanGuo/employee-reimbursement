package com.revature.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Reimbursement {
    private int id;
    private BigDecimal amount;
    private String description;
    private String type;
    private String submittedAt;
    private String status;
    private String resolvedAt;
    private String receipt;

    private User author;
    private User resolver;

    public Reimbursement(int id, BigDecimal amount, String description, String type, String submittedAt, String status, String resolvedAt, String receipt, User author, User resolver) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.submittedAt = submittedAt;
        this.status = status;
        this.resolvedAt = resolvedAt;
        this.receipt = receipt;
        this.author = author;
        this.resolver = resolver;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(String submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(String resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getResolver() {
        return resolver;
    }

    public void setResolver(User resolver) {
        this.resolver = resolver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reimbursement that = (Reimbursement) o;
        return id == that.id && Objects.equals(amount, that.amount) && Objects.equals(description, that.description) && Objects.equals(type, that.type) && Objects.equals(submittedAt, that.submittedAt) && Objects.equals(status, that.status) && Objects.equals(resolvedAt, that.resolvedAt) && Objects.equals(receipt, that.receipt) && Objects.equals(author, that.author) && Objects.equals(resolver, that.resolver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, description, type, submittedAt, status, resolvedAt, receipt, author, resolver);
    }

    @Override
    public String toString() {
        return "Reimbursement{" +
                "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", submittedAt='" + submittedAt + '\'' +
                ", status='" + status + '\'' +
                ", resolvedAt='" + resolvedAt + '\'' +
                ", receipt='" + receipt + '\'' +
                ", author=" + author +
                ", resolver=" + resolver +
                '}';
    }
}

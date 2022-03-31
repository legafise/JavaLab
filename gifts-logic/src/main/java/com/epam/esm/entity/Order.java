package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.epam.esm.entity.EntityConstant.DATE_FORMAT_PATTERN;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    private BigDecimal price;
    private LocalDateTime purchaseTime;

    @ManyToOne()
    @JoinColumn(name = "certificate_id")
    private Certificate certificate;

    public Order() {
    }

    public Order(long id, Certificate certificate, BigDecimal price, LocalDateTime purchaseTime) {
        super(id);
        this.certificate = certificate;
        this.price = price;
        this.purchaseTime = purchaseTime;
    }

    public Order(Certificate certificate, BigDecimal price, LocalDateTime purchaseTime) {
        this.certificate = certificate;
        this.price = price;
        this.purchaseTime = purchaseTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @JsonFormat(pattern = DATE_FORMAT_PATTERN)
    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(LocalDateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Order order = (Order) o;
        return Objects.equals(certificate, order.certificate) && Objects.equals(price, order.price) && Objects.equals(purchaseTime, order.purchaseTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), certificate, price, purchaseTime);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + super.getId() +
                ", certificates=" + certificate +
                ", price=" + price +
                ", purchaseTime=" + purchaseTime +
                '}';
    }
}

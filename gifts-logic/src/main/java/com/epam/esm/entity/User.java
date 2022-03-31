package com.epam.esm.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    private String login;
    private BigDecimal balance;

    @OneToMany
    @JoinTable(name = "user_orders", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id"))
    private List<Order> orders;

    public User() {
        orders = new ArrayList<>();
    }

    public User(long id, String login, BigDecimal balance, List<Order> orders) {
        super(id);
        this.login = login;
        this.balance = balance;
        this.orders = orders;
    }

    public User(long id, String login, BigDecimal balance) {
        super(id);
        this.login = login;
        this.balance = balance;
        orders = new ArrayList<>();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) && Objects.equals(balance, user.balance) && Objects.equals(orders, user.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), login, balance, orders);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + super.getId() +
                ", login='" + login + '\'' +
                ", balance=" + balance +
                ", orders=" + orders +
                '}';
    }
}

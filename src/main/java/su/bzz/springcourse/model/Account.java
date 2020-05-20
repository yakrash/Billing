package su.bzz.springcourse.model;

import java.util.Objects;

public class Account {
    private double amount;
    private int id;

    public Account(double amount, int id) {
        this.amount = amount;
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Double.compare(account.amount, amount) == 0 &&
                id == account.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, id);
    }

    @Override
    public String toString() {
        return "Account{" +
                "amount=" + amount +
                ", id=" + id +
                '}';
    }
}

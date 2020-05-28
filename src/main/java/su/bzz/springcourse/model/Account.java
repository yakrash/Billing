package su.bzz.springcourse.model;

import java.util.Objects;

public class Account {
    private long id;
    private double debit;
    private double credit;

    public Account(long id, double debit, double credit) {
        this.id = id;
        this.debit = debit;
        this.credit = credit;
    }

    public Account() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id &&
                Double.compare(account.debit, debit) == 0 &&
                Double.compare(account.credit, credit) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, debit, credit);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", debit=" + debit +
                ", credit=" + credit +
                '}';
    }
}

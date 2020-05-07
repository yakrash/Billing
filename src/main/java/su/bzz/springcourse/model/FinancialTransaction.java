package su.bzz.springcourse.model;

import java.util.Objects;

public class FinancialTransaction {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int src;
    private int dst;
    private double amount;

    public FinancialTransaction(int src, int dst, double amount) {
        this.src = src;
        this.dst = dst;
        this.amount = amount;
    }

    public FinancialTransaction(){

    }

    public long getId() {
        return id;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getDst() {
        return dst;
    }

    public void setDst(int dst) {
        this.dst = dst;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialTransaction that = (FinancialTransaction) o;
        return id == that.id &&
                src == that.src &&
                dst == that.dst &&
                Double.compare(that.amount, amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, src, dst, amount);
    }

    @Override
    public String toString() {
        return "FinancialTransaction{" +
                "id=" + id +
                ", src=" + src +
                ", dst=" + dst +
                ", amount=" + amount +
                '}';
    }
}

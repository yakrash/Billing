package su.bzz.springcourse.model;

import java.util.Objects;

public class FinancialTransaction {

    private long src;
    private long dst;
    private double amount;

    public FinancialTransaction(long src, long dst, double amount) {
        this.src = src;
        this.dst = dst;
        this.amount = amount;
    }

    public FinancialTransaction() {

    }


    public long getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public long getDst() {
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
        return src == that.src &&
                dst == that.dst &&
                Double.compare(that.amount, amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dst, amount);
    }

    @Override
    public String toString() {
        return "FinancialTransaction{" +
                "src=" + src +
                ", dst=" + dst +
                ", amount=" + amount +
                '}';
    }
}

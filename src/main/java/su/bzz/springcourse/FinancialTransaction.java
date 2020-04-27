package su.bzz.springcourse;

import java.util.Objects;

public class FinancialTransaction {
    private int src;
    private int dst;
    private double amount;

    public FinancialTransaction(int src, int dst, double amount) {
        this.src = src;
        this.dst = dst;
        this.amount = amount;
    }

    public FinancialTransaction() {

    }

    @Override
    public String toString() {
        return "FinancialTransaction{" +
                "src=" + src +
                ", dst=" + dst +
                ", amount=" + amount +
                '}';
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
        return src == that.src &&
                dst == that.dst &&
                Double.compare(that.amount, amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dst, amount);
    }
}

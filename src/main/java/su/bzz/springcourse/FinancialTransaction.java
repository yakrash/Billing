package su.bzz.springcourse;

public class FinancialTransaction {
    private int src;
    private int dst;
    private double amount;

    @Override
    public String toString() {
        return "FinancialTransaction{" +
                "src=" + src +
                ", dst=" + dst +
                ", amount=" + amount +
                '}';
    }

    public FinancialTransaction(int src, int dst, double amount) {
        this.src = src;
        this.dst = dst;
        this.amount = amount;
    }

    public FinancialTransaction() {

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
}

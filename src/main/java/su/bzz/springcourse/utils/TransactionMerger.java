package su.bzz.springcourse.utils;

import su.bzz.springcourse.FinancialTransaction;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class TransactionMerger {

    @PostConstruct
    public static void merge(BlockingQueue<FinancialTransaction> tempFinancialTransaction) {
        final List<FinancialTransaction> transactionsMerger = new ArrayList<>();
        Iterator<FinancialTransaction> financialTransactionIterator =
                transactionsMerger.iterator();

        tempFinancialTransaction.drainTo(transactionsMerger);

        for (int i = 0; i < transactionsMerger.size() - 1; i++) {
            for (int j = 1; j < transactionsMerger.size(); j++) {
                if (transactionsMerger.get(i).getSrc() == transactionsMerger.get(j).getSrc() &&
                        transactionsMerger.get(i).getDst() == transactionsMerger.get(j).getDst()) {

                    transactionsMerger.get(i).setAmount(transactionsMerger.get(i).getAmount() +
                            transactionsMerger.get(j).getAmount());
                    transactionsMerger.get(j).setAmount(0);
                }
            }
        }

        while (financialTransactionIterator.hasNext()) {
            FinancialTransaction next = financialTransactionIterator.next();
            if (next.getAmount() == 0) {
                financialTransactionIterator.remove();
            }
        }
        System.out.println("объединяем: " + transactionsMerger);
        // return transactionsMerger;
    }
}

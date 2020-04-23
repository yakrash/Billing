package su.bzz.springcourse.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import su.bzz.springcourse.FinancialTransaction;
import su.bzz.springcourse.TransactionLogger;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionMerger {
    private TransactionLogger transactionLogger;
    private final List<FinancialTransaction> transactionsMerger = new ArrayList<>();
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    public TransactionMerger(TransactionLogger transactionLogger) {
        this.transactionLogger = transactionLogger;
    }

    @PostConstruct
    public void merge() {
        executorService.scheduleAtFixedRate(new Thread(() -> {

            Iterator<FinancialTransaction> financialTransactionIterator =
                    transactionsMerger.iterator();

            transactionLogger.getTempFinancialTransaction().drainTo(transactionsMerger);

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
        }), 0, 6, TimeUnit.SECONDS);
    }
}

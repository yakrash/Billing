package su.bzz.springcourse.utils;

import su.bzz.springcourse.FinancialTransaction;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionMerger {

    @PostConstruct
    public static Map<FinancialTransaction, Double> merge(List<FinancialTransaction> transactionsMerger) {

        final Map<FinancialTransaction, Double> transactionsMergerMap = new HashMap<>();

        for (FinancialTransaction e : transactionsMerger) {
            if (transactionsMergerMap.containsKey(e)) {
                transactionsMergerMap.put(e, transactionsMergerMap.get(e) + e.getAmount());
            } else {
                transactionsMergerMap.put(e, e.getAmount());
            }
        }
        transactionsMerger.clear();

        return transactionsMergerMap;
    }
}

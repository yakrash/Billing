package su.bzz.springcourse.utils;

import su.bzz.springcourse.model.FinancialTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionMerger {

    public static List<FinancialTransaction> merge(List<FinancialTransaction> transactionsMerger) {
        final Map<String, FinancialTransaction> transactionsMergerMap = new HashMap<>();

        for (FinancialTransaction e : transactionsMerger) {
            String key = e.getSrc() + ":" + e.getDst();

            if (transactionsMergerMap.containsKey(key)) {
                e.setAmount(e.getAmount() + transactionsMergerMap.get(key).getAmount());
            }
            transactionsMergerMap.put(key, e);
        }
        return new ArrayList<>(transactionsMergerMap.values());
    }
}

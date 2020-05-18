package su.bzz.springcourse.utils;

import su.bzz.springcourse.model.FinancialTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergeFinancialTransactions {

    public static List<FinancialTransaction> merge(List<FinancialTransaction> mergedFinancialTransactions) {
        final Map<String, FinancialTransaction> transactionsMergerMap = new HashMap<>();

        for (FinancialTransaction e : mergedFinancialTransactions) {
            String key = e.getSrc() + ":" + e.getDst();

            if (transactionsMergerMap.containsKey(key)) {
                e.setAmount(e.getAmount() + transactionsMergerMap.get(key).getAmount());
            }
            transactionsMergerMap.put(key, e);
        }
        return new ArrayList<>(transactionsMergerMap.values());
    }
}

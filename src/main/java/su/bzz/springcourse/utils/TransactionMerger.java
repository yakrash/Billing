package su.bzz.springcourse.utils;

import su.bzz.springcourse.FinancialTransaction;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionMerger {

    @PostConstruct
    public static ArrayList<FinancialTransaction> merge(List<FinancialTransaction> transactionsMerger) {
        final Map<String, FinancialTransaction> transactionsMergerMap = new HashMap<>();

        for (FinancialTransaction e : transactionsMerger) {
            String key = e.getSrc() + ":" + e.getDst();

            if (transactionsMergerMap.containsKey(key)) {
                e.setAmount(e.getAmount() + transactionsMergerMap.get(key).getAmount());
            }
            transactionsMergerMap.put(key, e);
        }
        ArrayList<FinancialTransaction> result = new ArrayList<>(transactionsMergerMap.values());
        return result;
    }
}

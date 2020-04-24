package su.bzz.springcourse.utils;

import su.bzz.springcourse.FinancialTransaction;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class TransactionMerger {
    private final static List<FinancialTransaction> transactionsMerger = new ArrayList<>();
    private final static Map<FinancialTransaction, Double> transactionsMergerMap = new HashMap<>();

    @PostConstruct
    public static Map<FinancialTransaction, Double> merge(BlockingQueue<FinancialTransaction> tempFinancialTransaction) {
        tempFinancialTransaction.drainTo(transactionsMerger);

        for(FinancialTransaction e : transactionsMerger){
            if(transactionsMergerMap.containsKey(e)){
                transactionsMergerMap.put(e, transactionsMergerMap.get(e) + e.getAmount());
            }else{
                transactionsMergerMap.put(e, e.getAmount());
            }
        }

        transactionsMerger.clear();

//        System.out.println("объединяем: " + transactionsMergerMap);
         return transactionsMergerMap;
    }
}

package su.bzz.springcourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.bzz.springcourse.model.FinancialTransaction;
import su.bzz.springcourse.utils.TransactionMerger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionLogger {

    private final BlockingQueue<FinancialTransaction> loggerFinancialTransaction = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private List<FinancialTransaction> transactionsMerger = new ArrayList<>();
    JdbcTemplate jdbcTemplate;
    public BlockingQueue<FinancialTransaction> getLoggerFinancialTransaction() {
        return loggerFinancialTransaction;
    }

    public void push(FinancialTransaction financialTransaction) {
        loggerFinancialTransaction.add(financialTransaction);
    }

    @Autowired
    @Transactional
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println("insert dataSource");

    }

    @PostConstruct
    public void parserLoggerFT() {
        executorService.scheduleAtFixedRate(new Thread(() -> {
            BlockingQueue<FinancialTransaction> tempFinancialTransaction = new LinkedBlockingQueue<>();

            loggerFinancialTransaction.drainTo(tempFinancialTransaction);
            System.out.println("В нашей заглушке: " + tempFinancialTransaction);
            tempFinancialTransaction.drainTo(transactionsMerger);
            transactionsMerger = TransactionMerger.merge(transactionsMerger);
            System.out.println("transactionsMerger: " + transactionsMerger);
            for (FinancialTransaction e : transactionsMerger) {
                jdbcTemplate.update("INSERT INTO financial_transaction(src, dst, amount) " +
                        "values(?, ?, ?)", e.getSrc(), e.getSrc(), e.getAmount());
            }
        }), 0, 5, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void shutDownExecutorService() {
        executorService.shutdown();
    }

}

package su.bzz.springcourse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import su.bzz.springcourse.model.FinancialTransaction;
import su.bzz.springcourse.utils.TransactionMerger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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

    public BlockingQueue<FinancialTransaction> getLoggerFinancialTransaction() {
        return loggerFinancialTransaction;
    }

    public void push(FinancialTransaction financialTransaction) {
        loggerFinancialTransaction.add(financialTransaction);
    }

    public DriverManagerDataSource dataSource () {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUsername("postgres");
        dataSource.setPassword("");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/billing_db");
        return dataSource;
    }

    @PostConstruct
    public void test() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        System.out.println("insert raw");

        jdbcTemplate.update("INSERT INTO financial_transaction(src, dst, amount) " +
                "values(?, ?, ?)", 100, 200, 10.5);

    }

    @PostConstruct
    public void parserLoggerFT() {
        executorService.scheduleAtFixedRate(new Thread(() -> {
            List<FinancialTransaction> transactionsMerger = new ArrayList<>();
            BlockingQueue<FinancialTransaction> tempFinancialTransaction = new LinkedBlockingQueue<>();

            loggerFinancialTransaction.drainTo(tempFinancialTransaction);
            System.out.println("В нашей заглушке: " + tempFinancialTransaction);

            tempFinancialTransaction.drainTo(transactionsMerger);
            TransactionMerger.merge(transactionsMerger);

        }), 0, 5, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void shutDownExecutorService() {
        executorService.shutdown();
    }

}

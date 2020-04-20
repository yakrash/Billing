package su.bzz.springcourse;

import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class TransactionLogger {
    private BlockingQueue<FinancialTransaction> loggerFT = new LinkedBlockingQueue<>();
    private BlockingQueue<FinancialTransaction> tempFT = new LinkedBlockingQueue<>();

    public void push(FinancialTransaction financialTransaction) {
        loggerFT.add(financialTransaction);
    }

    public BlockingQueue<FinancialTransaction> getLoggerFT() {
        return loggerFT;
    }

    public BlockingQueue<FinancialTransaction> getTempFT() {
        return tempFT;
    }

    Thread threadLoggerInTemp = new Thread(() -> {
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loggerFT.drainTo(tempFT);
            System.out.println("В нашей зашлушке: " + getTempFT());
        }
    });

    public void parserLoggerFT() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(threadLoggerInTemp);
    }
}

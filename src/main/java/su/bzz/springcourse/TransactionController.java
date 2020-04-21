package su.bzz.springcourse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.BlockingQueue;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private BillingAPIImpl billingAPI;
    private TransactionLogger transactionLogger;

    public TransactionController(BillingAPIImpl billingAPI, TransactionLogger transactionLogger) {
        this.billingAPI = billingAPI;
        this.transactionLogger = transactionLogger;
        transactionLogger.parserLoggerFT();
    }

    @PostMapping("/transfer")
    ResponseEntity<?> create(@RequestBody FinancialTransaction financialTransaction) {
        billingAPI.transfer(financialTransaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/transfer")
    public ResponseEntity<BlockingQueue<FinancialTransaction>> read() {

        return new ResponseEntity<>(transactionLogger.getLoggerFinancialTransaction(), HttpStatus.OK);
    }


}

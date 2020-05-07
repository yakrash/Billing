package su.bzz.springcourse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su.bzz.springcourse.service.BillingAPIImpl;
import su.bzz.springcourse.service.TransactionLogger;
import su.bzz.springcourse.model.FinancialTransaction;

import java.util.concurrent.BlockingQueue;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final BillingAPIImpl billingAPI;
    private final TransactionLogger transactionLogger;

    @Autowired
    public TransactionController(BillingAPIImpl billingAPI, TransactionLogger transactionLogger) {
        this.billingAPI = billingAPI;
        this.transactionLogger = transactionLogger;
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

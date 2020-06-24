package su.bzz.springcourse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su.bzz.springcourse.model.FinancialTransaction;
import su.bzz.springcourse.service.BillingAPIImpl;
import su.bzz.springcourse.service.TransactionLogger;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final BillingAPIImpl billingAPI;

    @Autowired
    public TransactionController(BillingAPIImpl billingAPI) {
        this.billingAPI = billingAPI;
    }

    @PostMapping("/transfer")
    ResponseEntity<?> create(@RequestBody FinancialTransaction financialTransaction) {
        billingAPI.transfer(financialTransaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

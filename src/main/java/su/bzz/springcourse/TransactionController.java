package su.bzz.springcourse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {
    private List<FinancialTransaction> listFinancialTransaction;

    @Autowired
    BillingAPI billingAPI;

    @Autowired
    public TransactionController(BillingAPI billingAPI) {
        this.listFinancialTransaction = billingAPI.listFinancialTransaction;
    }

    @PostMapping("/transfer")
    ResponseEntity<?> create(@RequestBody FinancialTransaction financialTransaction) {
        billingAPI.transfer(financialTransaction);
        listFinancialTransaction.add(financialTransaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/transfer")
    public ResponseEntity<List<FinancialTransaction>> read() {

        return new ResponseEntity<>(listFinancialTransaction, HttpStatus.OK);
    }
}

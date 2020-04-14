package su.bzz.springcourse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TransactionController {

    List <FinancialTransaction> listFinancialTransaction = new ArrayList<>();
    FinancialTransaction firstTest1 = new FinancialTransaction(125, 350, 1000.5);
    listFinancialTransaction.add(firstTest1);

    @PostMapping("/transfer")
    ResponseEntity<?> create(@RequestBody FinancialTransaction financialTransaction) {
        listFinancialTransaction.add(financialTransaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/transfer")
    public ResponseEntity<List<FinancialTransaction>> read() {

        return listFinancialTransaction != null &&  !listFinancialTransaction.isEmpty()
                ? new ResponseEntity<>(listFinancialTransaction, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

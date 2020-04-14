package su.bzz.springcourse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TransactionController {

    List <FinancialTransaction> listFinancialTransaction = new ArrayList<>();

    @PostMapping("/transfer")
    ResponseEntity<?> create(@RequestBody FinancialTransaction financialTransaction) {
        listFinancialTransaction.add(financialTransaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

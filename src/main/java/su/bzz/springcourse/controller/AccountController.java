package su.bzz.springcourse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import su.bzz.springcourse.model.Account;
import su.bzz.springcourse.service.BillingAPIImpl;

@RestController
@RequestMapping("/api")
public class AccountController {
    BillingAPIImpl billingAPIImpl;

    @Autowired
    public AccountController(BillingAPIImpl billingAPIImpl) {
        this.billingAPIImpl = billingAPIImpl;
    }

    @GetMapping("/createAccount")
    ResponseEntity<?> createAccount() {
        billingAPIImpl.createAccount();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/accounts/{id}")
    ResponseEntity<Account> getAccount(@PathVariable(name = "id") long id) {
        final Account account = billingAPIImpl.getAccount(id);
        return account != null
                ? new ResponseEntity<>(account, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

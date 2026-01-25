package com.springprojects.banking_application.controller;

import com.itextpdf.text.DocumentException;
import com.springprojects.banking_application.entity.Transactions;
import com.springprojects.banking_application.service.BankStatementImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor
public class TransactionController {

    private BankStatementImpl bankStatement;

    @GetMapping("/transactions")
    public List<Transactions> generateBankStatement(@RequestParam String accountNumber,
                                                    @RequestParam String startDate,
                                                    @RequestParam String endDate) throws DocumentException, FileNotFoundException {

        return bankStatement.generateStatement(accountNumber,startDate,endDate);
    }


}

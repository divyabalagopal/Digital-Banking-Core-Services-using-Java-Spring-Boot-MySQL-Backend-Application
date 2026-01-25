package com.springprojects.banking_application.service;

//import com.itextpdf.awt.geom.Rectangle;
////import com.itextpdf.commons.*;
//import com.itextpdf.kernel.geom.PageSize;
////import com.itextpdf.kernel.geom.Rectangle;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfWriter;
import com.springprojects.banking_application.entity.Transactions;
import com.springprojects.banking_application.entity.User;
import com.springprojects.banking_application.repository.TransactionalRepository;
import com.springprojects.banking_application.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
@AllArgsConstructor
public class BankStatementImpl {

    /**
     * Retreieve the list of trasactions within a date range,
     * given an account number*/

    private TransactionalRepository transactionalRepository;

    private UserRepo userRepo;

    private static final Logger logger = LoggerFactory.getLogger(BankStatementImpl.class);

    private static final String FILE="C:\\Users\\divya\\OneDrive\\Desktop\\Banking Statements- Backend Application\\BankStatement.pdf";

    public List<Transactions> generateStatement(String accountNumber, String startDate, String endDate) throws DocumentException, FileNotFoundException {
        //filter all records of this account number
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate,DateTimeFormatter.ISO_DATE);

        //Convert the string request into local date format
        List<Transactions> transactionsList = transactionalRepository.findAll().stream().filter(transactions -> transactions.getAccountNumber().equals(accountNumber))
                .filter(transactions -> transactions.getDateTime().isAfter(start.atStartOfDay())).filter(transactions -> transactions.getDateTime().isBefore(end.atStartOfDay())).toList();

        User user = userRepo.findByAccountNumber(accountNumber);
        String customerName =
                user.getFirstName() + " " +
                        user.getLastName() + " " +
                        user.getOtherName();


        Document document = new Document(PageSize.A4);

        logger.info("Setting Size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document,outputStream);

        //open the document
        document.open();

        //add contents

        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName =  new PdfPCell(new Phrase("ABC Demo Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.CYAN);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("No. 35, Moorthy Street, Chennai"));
        bankAddress.setBorder(0);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: "+ startDate));
        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell((new Phrase("STATEMENT OF ACCOUNT")));
        statement.setBorder(0);

        PdfPCell lastDate = new PdfPCell(new Phrase("End Date: "+endDate));
        lastDate.setBorder(0);

        PdfPCell name = new PdfPCell(new Phrase("Account Holder Name: "+customerName));
        name.setBorder(0);

        PdfPCell space = new PdfPCell();
        PdfPCell address = new PdfPCell(new Phrase("Address: "+ user.getAddress()));
        address.setBorder(0);

        PdfPTable transactionsTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBorder(0);
        PdfPCell transactionType = new PdfPCell(new Phrase("TYPE "));
        transactionType.setBorder(0);
        PdfPCell transactionAmt = new PdfPCell(new Phrase("Amount"));
        transactionAmt.setBorder(0);
        PdfPCell status = new PdfPCell(new Phrase("Status"));
        status.setBorder(0);

        transactionsList.forEach(transactions -> {
            transactionsTable.addCell(new Phrase(transactions.getDateTime().toString()));
            transactionsTable.addCell(new Phrase(transactions.getTransactionalType()));
            transactionsTable.addCell(new Phrase(transactions.getAmount().toString()));
            transactionsTable.addCell(new Phrase(transactions.getStatus()));

        });

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(endDate);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        transactionsTable.addCell(date);
        transactionsTable.addCell(transactionType);
        transactionsTable.addCell(transactionAmt);
        transactionsTable.addCell(status);


        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionsTable);

        document.close();

        return transactionsList;

    }
    /**
     * Generate a pdf file of transactions
     * send the file via email
     */

//
//    private void designStatement(List<Transactions> transactions) throws FileNotFoundException, DocumentException {
//
//
//
//    }
}

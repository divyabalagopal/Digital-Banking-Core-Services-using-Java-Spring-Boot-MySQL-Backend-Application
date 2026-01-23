package com.springprojects.banking_application.service;

import com.springprojects.banking_application.dto.*;
import com.springprojects.banking_application.entity.User;
import com.springprojects.banking_application.repository.userRepo;
import com.springprojects.banking_application.utils.AccountUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    userRepo userRepo;

    @Autowired
    TransactionService transactionService;

    @Autowired
    EmailService emailService;
    @Override
    public BankResponseDTO createAccount(UserRequestDTO userRequestDTO) {

        /* Create an account -- saving a new user into the DB*/

        //Check if user already exists & has an account

        if(userRepo.existsByEmail(userRequestDTO.getEmail()))
        {
            return BankResponseDTO.builder().responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                            .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User newUser = User.builder().firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .otherName(userRequestDTO.getOtherName())
                .gender(userRequestDTO.getGender())
                .address(userRequestDTO.getAddress())
                .stateOfOrigin(userRequestDTO.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequestDTO.getEmail())
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .alternativePhoneNumber(userRequestDTO.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();

        User savedUser = userRepo.save(newUser);

        //Send email Alerts
        EmailDetailsDTO emailDetailsDTO = EmailDetailsDTO.builder().recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION SUCCESSFUL")
                .messageBody("Congratulations, your account has been created successfully created.\n Find your account details:\n Account name: "+savedUser.getFirstName() +" "+savedUser.getLastName()+" "+savedUser.getOtherName() +"\nAccount Number: " + savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetailsDTO);

        return BankResponseDTO.builder()
                        .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName()+" " + savedUser.getOtherName())
                        .build()).build();
    }

    @Override
    public BankResponseDTO balanceEnquiry(EnquiryRequestDTO enquiryRequest) {
        //check if the provided account number exists in DB
        //we have a repo method that checks, existsByAccountNumber
        boolean isAccountExists = userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if(!isAccountExists)
        {
            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        //Retrieve an object of user and return a response with balance, account number, account name and success messages
        User foundUser = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponseDTO.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .accountName(String.format(
                                "%s %s %s",
                                foundUser.getFirstName(),
                                foundUser.getLastName(),
                                foundUser.getOtherName()
                        )
)
                        .build()).build();
    }

    //find the name associated with account number
    @Override
    public String nameEnquiry(EnquiryRequestDTO enquiryRequest) {
        boolean isAccountExists= userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if(!isAccountExists)
        {
            return AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE;
        }

        User foundUser = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName() + " " +
                foundUser.getLastName() + " " +
                foundUser.getOtherName();


    }

    @Override
    public BankResponseDTO creditRequest(CreditDebitRequestDTO creditDebitRequestDTO) {
        //check if the account exists
        boolean isAccountExists = userRepo.existsByAccountNumber(creditDebitRequestDTO.getAccountNumber());
        if(!isAccountExists)
        {
            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User creditToUserAccount = userRepo.findByAccountNumber(creditDebitRequestDTO.getAccountNumber());
        //update the account_balance field in db
        creditToUserAccount.setAccountBalance(creditToUserAccount.getAccountBalance().add(creditDebitRequestDTO.getAmount()));
        userRepo.save(creditToUserAccount);

        //Save Credit transaction
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(creditToUserAccount.getAccountNumber())
                .transactionalType("CREDIT")
                .amount(creditDebitRequestDTO.getAmount())
                .status("SUCCESS")
                .build();

        transactionService.saveTransaction(transactionDTO);

        return BankResponseDTO.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDIT_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDIT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(
                                String.format(
                                        "%s %s %s",
                                        creditToUserAccount.getFirstName(),
                                        creditToUserAccount.getLastName(),
                                        creditToUserAccount.getOtherName()
                                )
                        )
                        .accountNumber(creditToUserAccount.getAccountNumber())
                        .accountBalance(creditToUserAccount.getAccountBalance()).build())
                .build();
    }

    public BankResponseDTO debitRequest(CreditDebitRequestDTO debitRequestDTO)
    {
        //check if the account exists
        boolean isAccountExists = userRepo.existsByAccountNumber(debitRequestDTO.getAccountNumber());
        if(!isAccountExists)
        {
            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }


        BigDecimal amount = debitRequestDTO.getAmount();
        User debitFromUserAccount = userRepo.findByAccountNumber(debitRequestDTO.getAccountNumber());
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {

            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBIT_FAILED_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBIT_FAILED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(
                                    debitFromUserAccount.getFirstName() + " " +
                                            debitFromUserAccount.getLastName() + " " +
                                            debitFromUserAccount.getOtherName()
                            )
                            .accountBalance(debitFromUserAccount.getAccountBalance())
                            .accountNumber(debitRequestDTO.getAccountNumber())
                            .build())
                    .build();
        }
        BigDecimal currentBalance = debitFromUserAccount.getAccountBalance();
        if (currentBalance == null) {
            // defensive default
            currentBalance = BigDecimal.ZERO;
        }

        if(currentBalance.compareTo(amount)<0)
        {
            return BankResponseDTO.builder()
                    .responseMessage("Insufficent Balance")
                    .responseCode("500")
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(debitRequestDTO.getAccountNumber())
                            .accountName(
                                    String.format(
                                            "%s %s %s",
                                            debitFromUserAccount.getFirstName(),
                                            debitFromUserAccount.getLastName(),
                                            debitFromUserAccount.getOtherName()
                                    )
                            )
                            .accountBalance(debitFromUserAccount.getAccountBalance())
                            .build())
                    .build();
        }

        debitFromUserAccount.setAccountBalance(debitFromUserAccount.getAccountBalance().subtract(debitRequestDTO.getAmount()));
        userRepo.save(debitFromUserAccount);

        //Save Debit transaction
        TransactionDTO transactionDTODebit = TransactionDTO.builder()
                .accountNumber(debitFromUserAccount.getAccountNumber())
                .transactionalType("DEBIT")
                .amount(debitRequestDTO.getAmount())
                .status("SUCCESS")
                .build();

        transactionService.saveTransaction(transactionDTODebit);


        return BankResponseDTO.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBIT_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBIT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(
                                debitFromUserAccount.getFirstName() + " " +
                                        debitFromUserAccount.getLastName() + " " +
                                        debitFromUserAccount.getOtherName()
                        )
                        .accountBalance(debitFromUserAccount.getAccountBalance())
                        .accountNumber(debitRequestDTO.getAccountNumber())
                        .build())
                .build();
    }

    @Transactional
    @Override
    public BankResponseDTO transferRequest(TransferDTO transfer) {
       //get the account to debit, check if it exists
        boolean isSourceAccExists = userRepo.existsByAccountNumber(transfer.getAccountNumberTransferredFrom());
        boolean isDestinationAccExists = userRepo.existsByAccountNumber(transfer.getAccountNumberTransferredTo());

        if(!isDestinationAccExists)
        {
            return BankResponseDTO.builder()
                    .responseCode("500")
                    .responseMessage("Destination account does not exist")
                    .accountInfo(null)
                    .build();
        }

        if(!isSourceAccExists)
        {
            return BankResponseDTO.builder()
                    .responseCode("500")
                    .responseMessage("Source account ds not exist")
                    .accountInfo(null)
                    .build();
        }

        User sourceAccountUser = userRepo.findByAccountNumber(transfer.getAccountNumberTransferredFrom());

        //check if the amount debitting is not more than current balance of source acc

        if(transfer.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0)
        {
            return BankResponseDTO.builder()
                    .responseMessage("Insufficent Balance")
                    .responseCode("500")
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(transfer.getAccountNumberTransferredFrom())
                            .accountName(
                                    sourceAccountUser.getFirstName() + " " +
                                            sourceAccountUser.getLastName() + " " +
                                            sourceAccountUser.getOtherName()
                            )
                            .accountBalance(sourceAccountUser.getAccountBalance())
                            .build())
                    .build();
        }

        //debit the current account
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(transfer.getAmount()));
        userRepo.save(sourceAccountUser);




        //Email to be sent

        EmailDetailsDTO alertEmailforDebit = EmailDetailsDTO.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAccountUser.getEmail())
                .messageBody(
                        "Amount: " + transfer.getAmount() + " has been deducted from your account."
                ).build();

        //call the email service and send an email to the user
        emailService.sendEmailAlert(alertEmailforDebit);

        //Save Debit Transfer transaction
        TransactionDTO transactionDTODebitTransfer = TransactionDTO.builder()
                .accountNumber(sourceAccountUser.getAccountNumber())
                .transactionalType("DEBIT TRANSFER")
                .amount(transfer.getAmount())
                .status("SUCCESS")
                .build();

        transactionService.saveTransaction(transactionDTODebitTransfer);


        //get the account to credit

        User destinationAccountNumber = userRepo.findByAccountNumber(transfer.getAccountNumberTransferredTo());

        //credit the account
        destinationAccountNumber.setAccountBalance(destinationAccountNumber.getAccountBalance().add(transfer.getAmount()));
        userRepo.save(destinationAccountNumber);

        //Save Credit Transfer transaction
        TransactionDTO transactionDTOCreditTransfer = TransactionDTO.builder()
                .accountNumber(destinationAccountNumber.getAccountNumber())
                .transactionalType("CREDIT TRANSFER")
                .amount(transfer.getAmount())
                .status("SUCCESS")
                .build();

        transactionService.saveTransaction(transactionDTOCreditTransfer);

        //Email to be sent to credited user

        EmailDetailsDTO alertEmailForCredit = EmailDetailsDTO.builder()
                .subject("CREDIT ALERT")
                .recipient(destinationAccountNumber.getEmail())
                .messageBody(
                        "Amount: " + transfer.getAmount() + " has been credited to your account."
                ).build();

        //call the email service and send an email to the user
        emailService.sendEmailAlert(alertEmailForCredit);

        return BankResponseDTO.builder()
                .responseCode("100")
                .responseMessage("Transfer has been successful")
                .accountInfo(AccountInfo.builder()
                        .accountNumber(sourceAccountUser.getAccountNumber())
                        .accountBalance(sourceAccountUser.getAccountBalance())
                        .build()).build();
    }
}

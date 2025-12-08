package com.microservice.demo.service.impl;

import com.microservice.demo.constants.AccountConstants;
import com.microservice.demo.dto.AccountsDto;
import com.microservice.demo.dto.CustomerDto;
import com.microservice.demo.entity.Accounts;
import com.microservice.demo.entity.Customer;
import com.microservice.demo.exception.CustomerAlreadyExistsException;
import com.microservice.demo.exception.ResourceNotFoundException;
import com.microservice.demo.mapper.AccountsMapper;
import com.microservice.demo.mapper.CustomerMapper;
import com.microservice.demo.repository.AccountsRespository;
import com.microservice.demo.repository.CustomerRepository;
import com.microservice.demo.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl  implements IAccountsService {


    private AccountsRespository accountsRespository;
    private CustomerRepository customerRepository;


    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto,new Customer());
        Optional<Customer> optionCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already exists" + customerDto.getMobileNumber());

        }

        Customer savedCustomer = customerRepository.save(customer);
        accountsRespository.save(createNewAccount(savedCustomer));
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()->new ResourceNotFoundException("Customer","mobilenumber",mobileNumber)
        );
        Accounts account = accountsRespository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                ()->new ResourceNotFoundException("Account","customerId",customer.getCustomerId().toString())
        );
        CustomerDto customerDto=CustomerMapper.mapToCustomerDto(customer,new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(account,new AccountsDto()));
        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto != null) {
            Accounts accounts = accountsRespository.findById(accountsDto.getAccountNumber())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Account","AccountNumber",accountsDto.getAccountNumber().toString())
                    );
            AccountsMapper.mapToAccount(accountsDto,accounts);
            accounts = accountsRespository.save(accounts);

            Long id = accounts.getCustomerId();

            Customer customer = customerRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("Customer","id",id.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()->new ResourceNotFoundException("Customer","mobileNumber",mobileNumber)
        );
        Accounts accounts = accountsRespository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                ()->new ResourceNotFoundException("Account","customerId",customer.getCustomerId().toString())
        );

        accountsRespository.delete(accounts);
        customerRepository.delete(customer);
        return true;
    }

    private Accounts createNewAccount(Customer customer){

        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber  = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);

        newAccount.setAccountType(AccountConstants.SAVINGS);
        newAccount.setBranchAddress(AccountConstants.ADDRESS);

        return newAccount;
    }
}

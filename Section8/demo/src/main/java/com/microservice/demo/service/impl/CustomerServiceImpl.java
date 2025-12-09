package com.microservice.demo.service.impl;

import com.microservice.demo.dto.AccountsDto;
import com.microservice.demo.dto.CardsDto;
import com.microservice.demo.dto.CustomerDetailsDto;
import com.microservice.demo.dto.LoansDto;
import com.microservice.demo.entity.Accounts;
import com.microservice.demo.entity.Customer;
import com.microservice.demo.exception.ResourceNotFoundException;
import com.microservice.demo.mapper.AccountsMapper;
import com.microservice.demo.mapper.CustomerMapper;
import com.microservice.demo.repository.AccountsRespository;
import com.microservice.demo.repository.CustomerRepository;
import com.microservice.demo.service.ICustomersService;
import com.microservice.demo.service.client.CardsFeignClient;
import com.microservice.demo.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomersService {

    private AccountsRespository accountsRespository;
    private CustomerRepository customerRepository;

    private CardsFeignClient cardsFeignClient;

    private LoansFeignClient loansFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()->new ResourceNotFoundException("Customer","mobilenumber",mobileNumber)
        );
        Accounts account = accountsRespository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                ()->new ResourceNotFoundException("Account","customerId",customer.getCustomerId().toString())
        );
        CustomerDetailsDto  customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer,new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(account,new AccountsDto()));
        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;

    }
}

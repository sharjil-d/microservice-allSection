package com.microservice.demo.service;

import com.microservice.demo.dto.CustomerDetailsDto;

public interface ICustomersService {

    CustomerDetailsDto fetchCustomerDetails(String mobileNumber);
}

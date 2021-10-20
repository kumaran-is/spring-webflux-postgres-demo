package com.demo.utils;

import org.springframework.beans.BeanUtils;

import com.demo.dto.CustomerDTO;
import com.demo.model.Customer;

public class AppUtils {


    public static CustomerDTO entityToDTO(Customer customer) {
    	CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    public static Customer dtoToEntity(CustomerDTO customerDTO) {
    	Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }
}
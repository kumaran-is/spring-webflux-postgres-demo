package com.demo.util;

import com.demo.model.Customer;

public class CustomerCreator {

	public static Customer createCustomerToBeSaved() {
        return Customer.builder()
            .name("Sachin")
            .email("sachin1@gmail.com")
            .build();
    }

    public static Customer createValidCustomer() {
        return Customer.builder()
            .id(1)
            .name("Sachin")
            .email("sachin1@gmail.com")
            .build();
    }

    public static Customer createdValidUpdatedCustomer() {
        return Customer.builder()
            .id(1)
            .name("Sachin")
            .email("sachin1@gmail.com")
            .build();
    }
}

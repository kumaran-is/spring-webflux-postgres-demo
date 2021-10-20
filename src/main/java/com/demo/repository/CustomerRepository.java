package com.demo.repository;


import com.demo.model.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
public interface CustomerRepository extends ReactiveCrudRepository<Customer,Integer> {

}

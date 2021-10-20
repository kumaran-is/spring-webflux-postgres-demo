package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.demo.dto.CustomerDTO;
import com.demo.service.CustomerService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
	
	@Autowired
	CustomerService customerService;

    @GetMapping
    public Flux<CustomerDTO> getCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Mono<CustomerDTO> getCustomer(@PathVariable Integer id){
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public Mono<CustomerDTO> createCustomer(@RequestBody Mono<CustomerDTO> customerDTO){
       return  customerService.saveCustomer(customerDTO);
    }

    @PutMapping("/{id}")
    public Mono<CustomerDTO> updateCustomer(@RequestBody Mono<CustomerDTO> customerDTO, @PathVariable Integer id){
        return customerService.updateCustomer(customerDTO, id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomer(@PathVariable Integer id){
        return customerService.deleteCustomer(id);
    }

}

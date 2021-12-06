package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.demo.dto.CustomerDTO;
import com.demo.service.CustomerService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
// @RequestMapping("/api/v1/customers")
@RequestMapping(value = "/api/v1/customers", produces = MediaType.APPLICATION_NDJSON_VALUE)
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	//@GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	//@GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    @GetMapping
    public Flux<CustomerDTO> getCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerDTO>> getCustomer(@PathVariable Integer id){
        return customerService.getCustomerById(id)
    	   .map((item) -> new ResponseEntity<>(item, HttpStatus.OK))
           .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/fetch/{customerIds}")
    public Flux<CustomerDTO> getCustomerByIds(@PathVariable List<Integer> customerIds){
        return customerService.getCustomerByIds(customerIds);
    }

    @PostMapping
    public Mono<ResponseEntity<CustomerDTO>> createCustomer(@RequestBody Mono<CustomerDTO> customerDTO){
    	 return customerService.saveCustomer(customerDTO)
		 .map((item) -> new ResponseEntity<>(item, HttpStatus.CREATED));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomerDTO>> updateCustomer(@RequestBody Mono<CustomerDTO> customerDTO, @PathVariable Integer id){
        return customerService.updateCustomer(customerDTO, id)
		.map((item) -> new ResponseEntity<>(item, HttpStatus.OK))
		.defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable Integer id){
        return customerService.deleteCustomer(id)
		.map((item) -> ResponseEntity.noContent().build());
    }

}

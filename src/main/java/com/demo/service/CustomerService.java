package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.utils.AppUtils;
import com.demo.dto.CustomerDTO;
import com.demo.model.Customer;
import com.demo.repository.CustomerRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {
	
	@Autowired
    private CustomerRepository customerRepository;


    public Flux<CustomerDTO> getAllCustomers(){
        return customerRepository.findAll().map(AppUtils::entityToDTO);
    }

    public Mono<CustomerDTO> getCustomerById(Integer id){
        return customerRepository.findById(id).map(AppUtils::entityToDTO);
    }


    public Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customerDTOMono){

      return  customerDTOMono.map(AppUtils::dtoToEntity)
                .flatMap(customerRepository::save)
                .map(AppUtils::entityToDTO);
    }

    public Mono<CustomerDTO> updateCustomer(Mono<CustomerDTO> customerDTOMono,Integer id){
       return customerRepository.findById(id)
                .flatMap(p->customerDTOMono.map(AppUtils::dtoToEntity)
                .doOnNext(e->e.setId(id)))
                .flatMap(customerRepository::save)
                .map(AppUtils::entityToDTO);

    }

    public Mono<Void> deleteCustomer(Integer id){
        return customerRepository.deleteById(id);
    }


}

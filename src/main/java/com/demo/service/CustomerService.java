package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;
import com.demo.utils.AppUtils;
import com.demo.dto.CustomerDTO;
import com.demo.repository.CustomerRepository;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;

@Service
public class CustomerService {
	
	@Autowired
    private CustomerRepository customerRepository;


    public Flux<CustomerDTO> getAllCustomers(){
        return customerRepository.findAll()
    	.switchIfEmpty(monoResponseStatusNotFoundException())
    	.map(AppUtils::entityToDTO)
    	.log();
    }

    public Mono<CustomerDTO> getCustomerById(Integer id){
        return customerRepository.findById(id)
		.switchIfEmpty(monoResponseStatusNotFoundException())
		.map(AppUtils::entityToDTO)
		.log();
    }
    
    
    /*
     *  we are trying to fetch the details of multiple users simultaneously and return the result as a list of users. 
     *  After creating a Flux from the list of userIds, it calls the parallel method, which internally creates ParallelFlux — 
     *  this indicates parallel execution. Here, we have decided to use the elastic scheduler to run the call on, 
     *  but we could have chosen any other configuration. Next, we invoke flatMap to run the findById method, 
     *  which returns ParallelFlux. Finally, we need to specify how to convert ParallelFlux to simple Flux. 
     *  Hence, we have used an ordered method with a custom comparator.
     */
    public Flux<CustomerDTO> getCustomerByIds(List<Integer> customerIds) {
        return Flux.fromIterable(customerIds)
        .parallel()
        .runOn(Schedulers.boundedElastic())
        .flatMap(id -> customerRepository.findById(id))
        .ordered((c1, c2) -> c2.getId() - c1.getId())
        .map(AppUtils::entityToDTO)
        .log();
  
    }


    public Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customerDTOMono){

		return  customerDTOMono.map(AppUtils::dtoToEntity)
		.flatMap(customerRepository::save)
		.map(AppUtils::entityToDTO)
		.log();
    }

   public Mono<CustomerDTO> updateCustomer(Mono<CustomerDTO> customerDTOMono,Integer id){
		return customerRepository.findById(id)
		.switchIfEmpty(monoResponseStatusNotFoundException())
		.flatMap(p->customerDTOMono.map(AppUtils::dtoToEntity)
		.doOnNext(e->e.setId(id)))
		.flatMap(customerRepository::save)
		.map(AppUtils::entityToDTO)
		.log();

    }
  
    /* option 2
    public Mono<CustomerDTO> updateCustomer(CustomerDTO customerDTO,Integer id){
        return customerRepository.findById(id)
    		.flatMap(currentCustomer -> {
    			Customer newCustomer =  AppUtils.dtoToEntity(customerDTO);
    			currentCustomer.setName(newCustomer.getName());
    			currentCustomer.setEmail(newCustomer.getEmail());
    			currentCustomer.setDob(newCustomer.getDob());
                return customerRepository.save(currentCustomer);
            })
    		.map(AppUtils::entityToDTO);
                 
     }  */

    public Mono<Void> deleteCustomer(Integer id){
        return customerRepository.deleteById(id)
        .log();
    }
    
    private <T> Mono<T> monoResponseStatusNotFoundException(){
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer not found"));
    }


}

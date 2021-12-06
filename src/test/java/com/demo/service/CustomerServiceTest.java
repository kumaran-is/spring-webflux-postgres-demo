package com.demo.service;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.demo.model.Customer;
import com.demo.repository.CustomerRepository;
import com.demo.util.CustomerCreator;
import com.demo.utils.AppUtils;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.scheduler.Schedulers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.server.ResponseStatusException;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
public class CustomerServiceTest {
	
	@InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepositoryMock;

    private final Customer customer = CustomerCreator.createValidCustomer();

    @BeforeAll
    public static void blockHoundSetup(){
        BlockHound.install();
    }

    @BeforeEach
    public void setUp() {
        BDDMockito.when(customerRepositoryMock.findAll())
            .thenReturn(Flux.just(customer));

        BDDMockito.when(customerRepositoryMock.findById(ArgumentMatchers.anyInt()))
            .thenReturn(Mono.just(customer));
        
        BDDMockito.when(customerRepositoryMock.save(CustomerCreator.createCustomerToBeSaved()))
        .thenReturn(Mono.just(customer));

        BDDMockito.when(customerRepositoryMock.delete(ArgumentMatchers.any(Customer.class)))
        .thenReturn(Mono.empty());
        
        BDDMockito.when(customerRepositoryMock.save(CustomerCreator.createValidCustomer()))
        .thenReturn(Mono.empty());
    }
    
    @Test
    public void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
                return "";
            });
            Schedulers.parallel().schedule(task);

            task.get(10, TimeUnit.SECONDS);
            Assertions.fail("should fail");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }
    
    @Test
    @DisplayName("getAllCustomers returns a flux of CustomerDTO")
    public void getAllCustomers_ReturnFluxOfCountry_WhenSuccessful() {
        StepVerifier.create(customerService.getAllCustomers())
            .expectSubscription()
            .expectNext(AppUtils.entityToDTO(customer))
            .verifyComplete();
    }

    @Test
    @DisplayName("getCustomerById returns a Mono with CustomerDTO when it exists")
    public void getCustomerById_ReturnMonoCountry_WhenSuccessful() {
        StepVerifier.create(customerService.getCustomerById(1))
            .expectSubscription()
            .expectNext(AppUtils.entityToDTO(customer))
            .verifyComplete();
    }

    @Test
    @DisplayName("getCustomerById returns Mono error when customer does not exist")
    public void getCustomerById_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(customerRepositoryMock.findById(ArgumentMatchers.anyInt()))
            .thenReturn(Mono.empty());

        StepVerifier.create(customerService.getCustomerById(1))
            .expectSubscription()
            .expectError(ResponseStatusException.class)
            .verify();
    }
    
    @Test
    @DisplayName("save creates an customer when successful")
    public void save_CreatesCustomer_WhenSuccessful() {
        Customer customerToBeSaved = CustomerCreator.createCustomerToBeSaved();

        StepVerifier.create(customerService.saveCustomer(Mono.just(AppUtils.entityToDTO(customerToBeSaved))))
            .expectSubscription()
            .expectNext(AppUtils.entityToDTO(customer))
            .verifyComplete();
    }
    
    @Test
    @DisplayName("delete removes the customer when successful")
    public void delete_RemovesCustomer_WhenSuccessful() {
        StepVerifier.create(customerService.deleteCustomer(1))
            .expectSubscription()
            .verifyComplete();
    }
    
    @Test
    @DisplayName("delete returns Mono error when anome does not exist")
    public void delete_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(customerRepositoryMock.findById(ArgumentMatchers.anyInt()))
            .thenReturn(Mono.empty());

        StepVerifier.create(customerService.deleteCustomer(1))
            .expectSubscription()
            .expectError(ResponseStatusException.class)
            .verify();
    }
    
    @Test
    @DisplayName("update save updated customer and returns empty mono when successful")
    public void update_SaveUpdatedCustomer_WhenSuccessful() {
        StepVerifier.create(customerService.updateCustomer(Mono.just(AppUtils.entityToDTO(CustomerCreator.createValidCustomer())), 1))
            .expectSubscription()
            .verifyComplete();
    }
    
    @Test
    @DisplayName("update returns Mono error when anime does not exist")
    public void update_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(customerRepositoryMock.findById(ArgumentMatchers.anyInt()))
            .thenReturn(Mono.empty());

        StepVerifier.create(customerService.updateCustomer(Mono.just(AppUtils.entityToDTO(CustomerCreator.createValidCustomer())), 1))
            .expectSubscription()
            .expectError(ResponseStatusException.class)
            .verify();
    }


}

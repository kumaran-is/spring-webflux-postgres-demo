package com.demo.config;

import com.demo.dto.CustomerDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class SinkConfig {

	// publish or push 
    @Bean
    public Sinks.Many<CustomerDTO> sink(){
        return Sinks.many().replay().limit(1);
    }

    // observe or subscribe
    @Bean
    public Flux<CustomerDTO> productBroadcast(Sinks.Many<CustomerDTO> sink){
        return sink.asFlux();
    }

}
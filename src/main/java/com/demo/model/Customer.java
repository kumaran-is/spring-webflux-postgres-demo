package com.demo.model;

import java.time.LocalDate;
import java.time.Period;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;


@Table("customer")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	@Id
	private Integer id;
	private String name;
	private String email;
	private LocalDate dob;
	@Transient
	private Integer age;
	
	public Integer getAge() {
		return Period.between(dob, LocalDate.now()).getYears();
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}

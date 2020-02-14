package com.example.demo.domain;

import java.sql.Date;

import lombok.Data;

@Data
public class Employee {
	
	Integer employeeId;
	
	String name;
	
	Date joinDate;
	
	Integer jobCategoryId;
	
	AuthInfo authInfo = new AuthInfo();
	
	Answer answer;
}

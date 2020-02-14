package com.example.demo.domain;

import lombok.Data;

@Data
public class AuthInfo {

	Integer employeeId;
	
	String mailAddress;
	
	String password;
	
	Integer authorityId;
	
	String authorityName;
	
	String jobCategoryName;
	
}

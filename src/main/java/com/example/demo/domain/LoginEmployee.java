package com.example.demo.domain;

import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LoginEmployee extends User{

	private static final long serialVersionUID = 1L;

	private final Employee employee;

	public LoginEmployee(Employee employee, Collection<GrantedAuthority> authorityList) {
		super(employee.authInfo.getMailAddress(),employee.getAuthInfo().getPassword(),authorityList);
		this.employee = employee;
	}
	
	public Employee getEmployee() {
		return employee;
	}
	
}

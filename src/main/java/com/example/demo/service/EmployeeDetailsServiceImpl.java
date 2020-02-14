package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Employee;
import com.example.demo.domain.LoginEmployee;
import com.example.demo.mapper.EmployeeMapper;

@Service
public class EmployeeDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Override
	public UserDetails loadUserByUsername(String mailAddress) throws UsernameNotFoundException{
		Employee employee  = employeeMapper.findEmployee(mailAddress);
		if(employee == null) {
			throw new UsernameNotFoundException("そのメールアドレスは登録されていません。");
		}
		Collection<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
		if(employee.getAuthInfo().getAuthorityId() != null && employee.getAuthInfo().getAuthorityId() == 1) {
			authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		return new LoginEmployee(employee,authorityList);
	}
	
}

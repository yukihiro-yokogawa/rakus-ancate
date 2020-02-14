package com.example.demo.common;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;

import com.example.demo.domain.LoginEmployee;

@Component
public class GetUserInfo {

	static final String ADMIN = "ROLE_ADMIN";
	static final String USER = "ROLE_USER";
	
	public String getAuthority(@AuthenticationPrincipal LoginEmployee employee) {
		String authority = USER;
		List<GrantedAuthority> authorityList = employee.getAuthorities().stream()
				.collect(Collectors.toList());
		for(int i = 0;i<= authorityList.size()-1;i++) {
			authority = String.valueOf(authorityList.get(i)).equals(ADMIN)?ADMIN:USER;
			if(String.valueOf(authorityList.get(i)).equals(ADMIN)) {
				break;
			}
		}
		return authority;
	}
}

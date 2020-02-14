package com.example.demo.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Employee;
import com.example.demo.domain.JobCategory;
import com.example.demo.form.EmployeeForm;
import com.example.demo.mapper.EmployeeMapper;

@Service
@Transactional
public class EmployeeService {

	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Employee findEmployee(String mailAddress) {
		return employeeMapper.findEmployee(mailAddress);
	}
	
	/**
	 * 職業リストの全件検索をするメソッドです(アンケート作成画面用).
	 * 
	 * @return 職業リスト
	 */
	public List<List<JobCategory>> searchJobCategoryByQuestionnaire() {
		List<List<JobCategory>> jobCategoryList = new ArrayList<>();
		List<JobCategory> jobCategories = new ArrayList<>();
		for(int i = 0; i <= employeeMapper.findJobCategory().size() - 1; i++) {
			jobCategories.add(employeeMapper.findJobCategory().get(i)); 
			if((i+1)%3 == 0){
				jobCategoryList.add(jobCategories);
				jobCategories = new ArrayList<>();
			}
		}
		return jobCategoryList;
	}
	
	/**
	 * 職業リストの全件検索をするメソッドです.
	 * 
	 * @return 職業リスト
	 */
	public List<JobCategory> searchJobCategory() {
		return employeeMapper.findJobCategory();
	}
	
	/**
	 * ユーザー登録
	 * 
	 * @param form 入力されたユーザー情報
	 */
	public void insertEmployee(EmployeeForm form) {
		Employee employee = new Employee();
		BeanUtils.copyProperties(form, employee);
		BeanUtils.copyProperties(form.getAuthInfoForm(), employee.getAuthInfo());
		employee.setJobCategoryId(Integer.parseInt(form.getJobCategoryId()));
		employee.getAuthInfo().setAuthorityId(Integer.parseInt(form.getAuthInfoForm().getAuthorityId()));
		String encodePassword = passwordEncoder.encode(form.getAuthInfoForm().getPassword());
		employee.getAuthInfo().setPassword(encodePassword);
		Date joinDate = Date.valueOf(form.getJoinYear() + "-" + form.getJoinMonth() + "-01");
		employee.setJoinDate(joinDate);
		employeeMapper.insertEmployee(employee);
	}
	
	public List<Employee> findAll(String date,String type,String engineer){
		return employeeMapper.findAll(date, type, engineer);
	}
	
}

package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domain.LoginEmployee;
import com.example.demo.exception.LoggedInUserIdNotMatchException;
import com.example.demo.form.EmployeeForm;
import com.example.demo.form.PasswordChangeForm;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.QuestionnaireService;

/**
 * ユーザー情報に関するViewを扱うコントローラークラスです.
 * 
 * @author yukihiro.yokogawa
 *
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private QuestionnaireService questionnaireService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@ModelAttribute
	public EmployeeForm setEmployeeForm() {
		return new EmployeeForm();
	}
	
	@ModelAttribute
	public PasswordChangeForm setPasswordChangeForm() {
		return new PasswordChangeForm();
	}
	
	/**
	 * ログインフォームを表示させるViewメソッドです.
	 * 
	 * @return
	 */
	@RequestMapping("/toLogin")
	public String toLogin() {
		return "employee/login.html";
	}
	
	/**
	 * ユーザー登録画面を表示させるViewメソッドです.
	 * 
	 * @return
	 */
	@RequestMapping("/toInsert")
	public String toInsert(Model model) {
		model.addAttribute("jobCategoryList",employeeService.searchJobCategory());
		
		return "employee/insert.html";
	}
	
	/**
	 * ユーザー登録処理
	 * 
	 * @param form
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping("/insertProcess")
	public String insertProcess(@Validated EmployeeForm form, BindingResult result, Model model) {
		if(!form.getAuthInfoForm().getPassword().equals(form.getAuthInfoForm().getConfirmPassword())) {
			result.rejectValue("authInfoForm.password", "","パスワード、又は確認用パスワードが間違っています。");
		}
		if(result.hasErrors()){
			return toInsert(model);
		}
		employeeService.insertEmployee(form);
		return "redirect:/employee/toLogin";
	}
	
	/**
	 * ユーザー情報を表示させるメソッドです.
	 * 
	 * @param employee ログインユーザーの情報
	 * @param id リクエストパラメータ
	 * @param model
	 * @return ユーザー情報
	 */
	@RequestMapping("/mypage")
	public String findMypage(@AuthenticationPrincipal LoginEmployee employee, String id, Model model) {
		try{
			if(!String.valueOf(employee.getEmployee().getEmployeeId()).equals(id)) {
				throw new LoggedInUserIdNotMatchException("不正なユーザーIDでアクセスされた可能性があります。");
			}
			model.addAttribute("detail",questionnaireService.findMyAnswer(Integer.parseInt(id)));
		}catch (LoggedInUserIdNotMatchException e) {
			System.out.println(e.getMessage());//独自の例外です
			System.out.println(e);
		}
		return "employee/detail";
	}
	
	@RequestMapping("/password_change")
	public String passwordChange() {
		return "employee/password_change";
	}
	
	@RequestMapping("/update_password")
	public String updatePassword(@Validated PasswordChangeForm passwordChangeForm,BindingResult result,@AuthenticationPrincipal LoginEmployee employee) {
		
		if(!passwordChangeForm.getMailAddress().equals(employee.getEmployee().getAuthInfo().getMailAddress())) {
			result.rejectValue("mailAddress", "","ログインしているユーザー情報と一致しません");
		}
		
		if(!encoder.matches(passwordChangeForm.getPassword(), employee.getEmployee().getAuthInfo().getPassword())) {
			result.rejectValue("password", "","ログインしているユーザー情報と一致しません");
		}
		
		if(!passwordChangeForm.getChangePassword().equals(passwordChangeForm.getChangeConfirmPassword())) {
			result.rejectValue("changePassword", "", "確認用のパスワードと変更後のパスワードと一致していません");
		}
		
		if(result.hasErrors()) {
			return passwordChange();
		}
		employeeService.findEmployee(employee.getEmployee().getAuthInfo().getMailAddress());
		return "redirect:/mypage";
	}
}

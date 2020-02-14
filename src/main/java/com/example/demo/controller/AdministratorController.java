package com.example.demo.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.common.GetUserInfo;
import com.example.demo.domain.Employee;
import com.example.demo.domain.LoginEmployee;
import com.example.demo.domain.Questionnaire;
import com.example.demo.domain.SortType;
import com.example.demo.form.QuestionnaireForm;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.QuestionnaireService;

/**
 * 管理者ページに関するView
 * 
 * @author yukihiro.yokogawa
 *
 */
@Controller
@RequestMapping("/admin")
public class AdministratorController {
	
	@Autowired
	private QuestionnaireService questionnaireService;

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private GetUserInfo getUserInfo;

	@ModelAttribute
	public QuestionnaireForm setQuestionnaireForm() {
		return new QuestionnaireForm();
	};
	
	/**
	 * 管理者ページのインフォメーションを表示するメソッドです.
	 * 
	 * @return infomation
	 */
	@RequestMapping("/info")
	public String adminInfo() {
		return "administrator/administrator_info";
	}
	
	/**
	 * 管理者用のアンケート一覧を表示するメソッドです.
	 * 
	 * @param employee ユーザー情報
	 * @param model modelAttribute
	 * @param type 検索条件（MyBatisのifで条件分岐するためのデータ）
	 * @return 管理者ページのView
	 */
	@RequestMapping("/questionnaire")
	public String adminQuestionnaire(@AuthenticationPrincipal LoginEmployee employee, Model model, String type) {
		String authority = getUserInfo.getAuthority(employee);
		SortType types = new SortType();
		type = type == null?"NORMAL": type;
		List<Questionnaire> questionnaireList = questionnaireService.findQuestionnaire(type, employee.getEmployee(),null,authority);
		model.addAttribute("questionnaireList",questionnaireList);
		model.addAttribute("sortMap",types.getSortQuestionaireMap());
		model.addAttribute("sort",type);
		return "administrator/administrator_questionnaire";
	}
	
	/**
	 * 従業員一覧を表示するメソッドです.
	 * 
	 * @param model
	 * @param joinDate
	 * @param type
	 * @return
	 */
	@RequestMapping("/employee")
	public String adminEmployeeList(Model model,String joinDate, String type, String engineer) {
		SortType types = new SortType();
		List<Employee> employeeList = employeeService.findAll(joinDate, type, engineer);
		LocalDate localDate = LocalDate.now();
		Map<String,String> joinDateMap = new LinkedHashMap<>();
		joinDateMap.put("ALL", "全社員");
		for(int i = localDate.getYear();i >= 2017;i--) {
			if(i == localDate.getYear()) {
				for(int j = localDate.getMonthValue();j>=1;j--) {
					if(j%3 == 1) {
						joinDateMap.put(String.valueOf(i) + "-" + String.format("%02d", j) + "-01", String.valueOf(i) + "年" + String.valueOf(j) + "月生");
					}
				}
			}else {
				for(int k = 12; k>=1;k--) {
					if(k%3==1)joinDateMap.put(String.valueOf(i) + "-" + String.format("%02d", k) + "-01", String.valueOf(i) + "年" + String.valueOf(k) + "月生");
				}
			}
		}
		
		model.addAttribute("employeeList",employeeList);
		model.addAttribute("sortMap",types.getSortEmployeeMap());
		model.addAttribute("engineerMap",types.getSortEngineerMap());
		model.addAttribute("sort",type);
		model.addAttribute("joinDateMap",joinDateMap);
		model.addAttribute("engineerType",engineer);
		model.addAttribute("joinEmployee",joinDate);
		return "administrator/administrator_employee";
	}
	
	/**
	 * 管理者用の従業員詳細を表示するメソッドです.
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/employee_detail")
	public String adminEmployeeDetail(String id, Model model) {
		Employee employeeDetail = questionnaireService.findMyAnswer(Integer.parseInt(id));
		model.addAttribute("detail",employeeDetail);
		return "administrator/administrator_employee_detail";
	}
}

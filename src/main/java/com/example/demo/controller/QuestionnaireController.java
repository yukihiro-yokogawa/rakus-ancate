package com.example.demo.controller;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.common.GetUserInfo;
import com.example.demo.domain.LoginEmployee;
import com.example.demo.domain.Questionnaire;
import com.example.demo.domain.SortType;
import com.example.demo.form.AnswerForm;
import com.example.demo.form.QuestionnaireForm;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.QuestionnaireService;

/**
 * アンケートに関するViewから送られてきたデータを受け取るクラスです.
 * 
 * @author yukihiro.yokogawa
 *
 */
@Controller
@RequestMapping("/questionnaire")
public class QuestionnaireController {

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
	
	@ModelAttribute
	public AnswerForm setAnswerForm() {
		return new AnswerForm();
	};
	
	/**
	 * アンケート一覧を表示させるViewメソッドです.
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	public String finbAll(@AuthenticationPrincipal LoginEmployee employee, Model model, HttpSession session, String type) {
		String authority = getUserInfo.getAuthority(employee);
		SortType types = new SortType();
		type = type == null?"NORMAL": type;
		List<Questionnaire> questionnaireList = questionnaireService.findQuestionnaire(type, employee.getEmployee(),null,authority);
		model.addAttribute("questionnaireList",questionnaireList);
		model.addAttribute("sortMap",types.getSortQuestionaireMap());
		model.addAttribute("sort",type);
		session.setAttribute("id",employee.getEmployee().getEmployeeId());
		return "employee/list";
	}

	/**
	 * 回答フォームを表示するViewメソッドです.
	 * 
	 * @return
	 */
	@RequestMapping("/reply")
	public String employeeReply(@AuthenticationPrincipal LoginEmployee employee,Model model,Integer id,String type) {
		String authority = getUserInfo.getAuthority(employee);
		List<Questionnaire> questionnaireList = questionnaireService.findQuestionnaire(type, employee.getEmployee(),id,authority);
		model.addAttribute("insertAns","newAns");
		if(questionnaireList.get(0).getAnswerForm().getAnswerCategoryFormList() != null) {
			AnswerForm answerForm = questionnaireList.get(0).getAnswerForm();
			model.addAttribute("insertAns","update");
			model.addAttribute("answerForm",answerForm);
		}
		System.out.println(questionnaireList);
		model.addAttribute("questionnaireList",questionnaireList);
		model.addAttribute("employee",employee.getEmployee());
		return "employee/questionnaire";
	}

	/**
	 * 回答のリクエストパラメータをサービスクラスに送るメソッドです.
	 * 
	 * @return
	 */
	@RequestMapping("/sendAnswer")
	public String sendAnswer(@Validated AnswerForm form,BindingResult result, Model model,@AuthenticationPrincipal LoginEmployee employee,Integer questionId,String type) {
		List<GrantedAuthority> admin = employee.getAuthorities().stream()
				.filter(a -> String.valueOf(a).equals("ROLE_ADMIN"))
				.collect(Collectors.toList());
		
		if(result.hasErrors()) {
			model.addAttribute("error","未回答の項目がある、又は不正な操作が行われた可能性があります。");
			return employeeReply(employee,model,questionId,"reply");
		}
		
		//管理者の場合アンケートに回答することは基本的にできない
		if(!type.equals("update") && admin.size() == 0) {
			questionnaireService.insertAnswered(form);
		}else if(type != null && type.equals("update") && admin.size() == 0) {
			questionnaireService.updateAnswered(form);
		}
		return "redirect:/questionnaire/list";
	}

	/**
	 * アンケート作成フォームを表示するViewメソッドです.
	 * 
	 * @return
	 */
	@RequestMapping("/create")
	public String createQuestionnaire(Model model) {
		model.addAttribute("jobCategoryList", employeeService.searchJobCategoryByQuestionnaire());
		return "administrator/create_questionnaire";
	}

	/**
	 * アンケート登録処理を行うメソッドです.
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping("/create_questionnaire_process")
	public String createQuestionnaireProcess(@Validated QuestionnaireForm form, BindingResult result, Model model) {
		questionnaireService.insertQuestionnaire(form);
		return "redirect:/questionnaire/list";
	}
	
	/**
	 * アンケートの解答結果のViewを表示させるメソッドです.
	 * 
	 * @param employee ログイン情報
	 * @param type 検索条件
	 * @param id アンケート番号
	 * @return 解答結果のView
	 */
	@RequestMapping("/viewAnswer")
	public String viewAnswer(@AuthenticationPrincipal LoginEmployee employee,String type,Integer id,Model model) {
		String authority = getUserInfo.getAuthority(employee);
		System.out.println(authority);
		List<Questionnaire> questionnaireList = questionnaireService.findQuestionnaire(type, employee.getEmployee(),null,authority);
		Collections.reverse(questionnaireList);
		LinkedHashMap<Integer, String> questionnaireTitleMap = 	questionnaireList.stream()
		.collect(Collectors.toMap(a -> a.getQuestionnaireId(), b -> b.getTitle(),(c,d) -> c,LinkedHashMap::new));
		model.addAttribute("titleMap",questionnaireTitleMap);
		model.addAttribute("id",id);
		return "employee/view_answer";
	}

}

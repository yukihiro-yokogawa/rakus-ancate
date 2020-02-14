package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.domain.AnswerCategory;
import com.example.demo.domain.AnswerPoint;
import com.example.demo.domain.Employee;
import com.example.demo.domain.LoginEmployee;
import com.example.demo.domain.Questionnaire;
import com.example.demo.domain.QuestionnaireCategory;
import com.example.demo.service.QuestionnaireService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * JavaScriptから送られてきたデータによって返すデータを操作するcontrollerクラスです.
 * 
 * @author yukihiro.yokogawa
 *
 */
@Controller
@RequestMapping("/agreegate")
public class AgreegateAjax {

	@Autowired
	private QuestionnaireService questionnaireService;

	/**
	 * Ajax通信でアンケート結果を返すメソッドです.
	 * 
	 * @param employee
	 * @param type
	 * @param id
	 * @param date
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/all_answer")
	public String agreegateAllAnswer(@AuthenticationPrincipal LoginEmployee employee, String type, Integer id,String date,HttpSession session) {
		ObjectMapper mapper = new ObjectMapper();// コレクションフレームワークをString型のJson形式に変換するオブジェクト
		String agreegateJson = null;// Jsonで返すデータ
		Map<String, Object> chartMap = new HashMap<>();// Jsonに変換したいデータセット
		
		@SuppressWarnings("unchecked")
		List<String> categoryNameList 
			= date.equals("all")?new ArrayList<>()
				:session.getAttribute("categoryNameList") != null
				?(List<String>)session.getAttribute("categoryNameList")
				:new ArrayList<>();
		
		if (date.equals("all")) {
			Questionnaire answer = questionnaireService.findAnswer(employee.getEmployee(), type, id);
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			// 回答が0件の時の処理
			if (answer == null) {
				chartMap.put("noAnswer", "現在アンケートを集計中です。");
				try {
					agreegateJson = mapper.writeValueAsString(chartMap);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				return agreegateJson;
			}

			answer.getQuestionnaireCategoryList().stream().forEach(s -> refillCategoryName(s, categoryNameList));
			chartMap.put("categoryName", categoryNameList);
			chartMap.put("title", answer.getTitle());
			session.setAttribute("categoryNameList", categoryNameList);

			if (type.equals("point") && answer != null) {
				// 点数ごとの人数
				Map<Integer, List<Integer>> numOfPeopleMap = new HashMap<>();
				for (int i = 1; i <= 5; i++) {
					numOfPeopleMap.put(i, new ArrayList<>());
				}
				answer.getAnswer().getAnswerCategoryList().stream().forEach(s -> refillNumOfPeople(s, numOfPeopleMap));
				// データをセット
				chartMap.put("noAnswer", null);
				chartMap.put("numOfPeople", numOfPeopleMap);
			} else if (type.equals("joinYear") && answer != null) {
				Map<String, List<String>> joinYearMap = new LinkedHashMap<>();
				answer.getAnswer().getAnswerCategoryList().stream().forEach(s -> refillJoinYear(s, joinYearMap));
				chartMap.put("joinYear", joinYearMap);
			}
		}
		
		List<GrantedAuthority> admin = employee.getAuthorities().stream()
				.filter(a -> String.valueOf(a).equals("ROLE_ADMIN")).collect(Collectors.toList());
		
		//ログインしているユーザーが管理者であるとき個々人の回答結果を得ることができる.
		if (admin.size() != 0) {
			List<Employee> employeeAnswerList = questionnaireService.findAllEmployeeAnswer(id, date);
			Map<String, List<String>> employeeAnswersMap = new LinkedHashMap<>();
			employeeAnswerList.stream().forEach(i -> refillEmployee(i, employeeAnswersMap,categoryNameList));
			chartMap.put("employeeAnswers", employeeAnswersMap);
		}

		try {
			agreegateJson = mapper.writeValueAsString(chartMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return agreegateJson;
	}

	/**
	 * アンケート結果で返ってきた回答の中のカテゴリネームをchart.js用に加工するメソッドです.
	 * 
	 * @param questionnaireCategory
	 * @param categoryNameList
	 */
	public void refillCategoryName(QuestionnaireCategory questionnaireCategory, List<String> categoryNameList) {
		questionnaireCategory.getChildNameList().stream()
				.map(s -> s.getParentName() + "(" + questionnaireCategory.getParentName() + ")")
				.forEach(l -> categoryNameList.add(l));
	}

	/**
	 * アンケート結果で返ってきた回答の中の得点をchart.js用に加工するメソッドです.
	 * 
	 * @param answerCategory
	 * @param numOfPeopleMap
	 */
	public void refillNumOfPeople(AnswerCategory answerCategory, Map<Integer, List<Integer>> numOfPeopleMap) {
		Integer categoryId = null;
		Integer count = numOfPeopleMap.get(1).size();
		for (AnswerPoint answerPoint : answerCategory.getAnswerPointList()) {
			if (categoryId != null && categoryId != answerPoint.getChildCategoryId()) {
				count++;
				for (List<Integer> numOfPeopleList : numOfPeopleMap.values()) {
					if (numOfPeopleList.size() != count && numOfPeopleList.size() < count) {
						numOfPeopleList.add(0);
					}
				}
			}
			numOfPeopleMap.get(answerPoint.getPoint()).add(answerPoint.getNumOfPeople());
			categoryId = answerPoint.getChildCategoryId();
			if (answerPoint == answerCategory.getAnswerPointList()
					.get(answerCategory.getAnswerPointList().size() - 1)) {
				count++;
				for (List<Integer> numOfPeopleList : numOfPeopleMap.values()) {
					if (numOfPeopleList.size() != count && numOfPeopleList.size() < count) {
						numOfPeopleList.add(0);
					}
				}
			}
		}
	}

	/**
	 * アンケート結果で返ってきた回答の中のユーザーの入社日をchart.js用に加工するメソッドです.
	 * 
	 * @param answerCategory
	 * @param joinYearMap
	 */
	public void refillJoinYear(AnswerCategory answerCategory, Map<String, List<String>> joinYearMap) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String joinDate = dateFormat.format(answerCategory.getRespondentJoinDate());
		for (AnswerPoint answerPoint : answerCategory.getAnswerPointList()) {
			String avgPoint = String.format("%.3f", answerPoint.getAvgPoint());
			if (joinYearMap.containsKey(joinDate)) {
				joinYearMap.get(joinDate).add(avgPoint);
			} else {
				joinYearMap.put(joinDate, new ArrayList<>(Arrays.asList(avgPoint)));
			}
		}
	}

	/**
	 * アンケートを回答した個人名を詰め治すメソッドです.
	 * 
	 * @param employee
	 * @param employeeAnswersMap
	 * @param categoryNameList
	 */
	public void refillEmployee(Employee employee, Map<String, List<String>> employeeAnswersMap,List<String> categoryNameList) {
		if (!employeeAnswersMap.containsKey("name")) {
			employeeAnswersMap.put("name", new ArrayList<>(Arrays.asList(employee.getName())));
		}else {
			employeeAnswersMap.get("name").add(employee.getName());
		}
		int count = 0;
		for(int i = 0;i<=employee.getAnswer().getAnswerCategoryList().size()-1;i++){
			for(int j = 0;j<=employee.getAnswer().getAnswerCategoryList().get(i).getAnswerPointList().size()-1;j++) {
				refillEmployeePoint(employeeAnswersMap,employee.getAnswer().getAnswerCategoryList().get(i).getAnswerPointList().get(j), categoryNameList.get(count));
				count++;
			}
		}
	}
	
	/**
	 * 個人の回答結果を詰め治すメソッドです.
	 * 
	 * @param employeeAnswersMap
	 * @param answerPoint
	 * @param categoryName
	 */
	private void refillEmployeePoint(Map<String, List<String>>employeeAnswersMap ,AnswerPoint answerPoint, String categoryName) {
		if (!employeeAnswersMap.containsKey(categoryName)) {
			employeeAnswersMap.put(categoryName, new ArrayList<>());
		}
		employeeAnswersMap.get(categoryName).add(String.valueOf(answerPoint.getPoint()));
	}
}

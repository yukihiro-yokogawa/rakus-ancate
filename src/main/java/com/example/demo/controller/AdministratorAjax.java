package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.common.GetUserInfo;
import com.example.demo.domain.LoginEmployee;
import com.example.demo.service.QuestionnaireService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 管理者関連ページとのAjax通信を行うためのコントローラークラスです.
 * 
 * @author yukihiro.yokogawa
 *
 */
@Controller
public class AdministratorAjax {
	
	@Autowired
	private GetUserInfo userInfo;
	
	@Autowired
	private QuestionnaireService questionnaireService;
	
	static final String ADMIN = "ROLE_ADMIN";
	
	/**	アンケート削除スイッチ（default : false trueになると削除する） */
	static boolean delete = false; 
	/**	アンケート非表示スイッチ（default : true falseになると非表示に する） */
	static boolean view = true;
	
	@ResponseBody
	@RequestMapping("/questionnaire_edit")
	public String questionnaireReview(@AuthenticationPrincipal LoginEmployee employee, Integer id, String type) {
		if(!ADMIN.equals(userInfo.getAuthority(employee)))return "accessBan";
		Boolean isSwitch = null;
		String mapper = null;
		if("delete".equals(type)) {
			//関連するすべての項目を削除する（回答、項目、質問内容すべて）
			
		}else if("view".equals(type)) {
			isSwitch = questionnaireService.viewQuestionnaire(id);
		}
		try {
			mapper = new ObjectMapper().writeValueAsString(String.valueOf(isSwitch));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return mapper;
	}
	
}

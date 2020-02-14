package com.example.demo.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class QuestionnaireForm {
	
	@NotBlank(message="アンケートのタイトルを入力してください。")
	String title;
	
	@NotBlank(message="アンケートの説明文を入力してください。")
	String description;
	
	@Valid
	List<String> questionnaireTargetList;
	
	QuestionnaireStatusForm questionaireStatusForm;
	
	List<@Valid QuestionnaireCategoryForm> questionnaireCategoryFormList;
	
	
}

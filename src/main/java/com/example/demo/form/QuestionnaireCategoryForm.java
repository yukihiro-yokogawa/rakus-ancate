package com.example.demo.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.example.demo.domain.QuestionnaireCategory;

import lombok.Data;

@Data
public class QuestionnaireCategoryForm {
	
	@NotBlank(message="未入力です。")
	String parentName;
	
	List<@Valid QuestionnaireCategory> childNameList;
	
}

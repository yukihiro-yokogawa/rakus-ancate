package com.example.demo.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AnswerForm {
	
	@NotBlank
	String questionId;
	
	@NotBlank
	String employeeId;
	
	@Valid
	List<AnswerCategoryForm> answerCategoryFormList;
	
}

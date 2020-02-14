package com.example.demo.domain;

import java.util.List;

import lombok.Data;

@Data
public class Answer {
	
	Integer answerId;

	Integer questionId;
	
	Integer employeeId;
	
	List<AnswerCategory> answerCategoryList;
	
	Questionnaire questionnaire;

}

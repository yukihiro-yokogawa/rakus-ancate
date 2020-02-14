package com.example.demo.domain;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.form.AnswerCategoryForm;
import com.example.demo.form.AnswerForm;
import com.example.demo.form.AnswerPointForm;

import lombok.Data;

@Data
public class Questionnaire {
	
	Integer questionnaireId;
	
	String title;
	
	String description;
	
	QuestionnaireStatus questionnaireStatus;
	
	List<Integer> questionnaireTargetList;
	
	List<QuestionnaireCategory> questionnaireCategoryList;
	
	Answer answer;
	
	/**
	 * 検索してきたユーザーと紐づく回答をフォームに詰め治す。
	 * 
	 * @return 検索された回答（ない場合は空）
	 */
	public AnswerForm getAnswerForm() {
		AnswerForm answerForm = new AnswerForm();
		if(this.answer != null) {
			answerForm.setAnswerCategoryFormList(this.answer.getAnswerCategoryList().stream()
			.map(c -> new AnswerCategoryForm(c,c.getAnswerPointList().stream()
					.map(p -> new AnswerPointForm(p))
					.collect(Collectors.toList())))
			.collect(Collectors.toList()));
			answerForm.setEmployeeId(String.valueOf(this.answer.getEmployeeId()));
			answerForm.setQuestionId(String.valueOf(this.answer.getQuestionId()));
		}
		return answerForm;
	}
	
}

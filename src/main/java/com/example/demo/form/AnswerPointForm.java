package com.example.demo.form;

import javax.validation.constraints.NotBlank;

import com.example.demo.domain.AnswerPoint;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnswerPointForm {
	
	@NotBlank
	String childCategoryId;
	
	@NotBlank(message="未入力の項目があります。")
	String point;
	
	public AnswerPointForm(AnswerPoint answerPoint) {
		this.childCategoryId = String.valueOf(answerPoint.getChildCategoryId());
		this.point = String.valueOf(answerPoint.getPoint());
	}

}

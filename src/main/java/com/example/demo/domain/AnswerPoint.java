package com.example.demo.domain;

import com.example.demo.form.AnswerPointForm;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnswerPoint {

	Integer childCategoryId;
	
	Integer point;
	
	Integer numOfPeople;
	
	Double avgPoint;
	
	public AnswerPoint(AnswerPointForm form) {
		this.childCategoryId = Integer.parseInt(form.getChildCategoryId());
		this.point = Integer.parseInt(form.getPoint());
	}
}

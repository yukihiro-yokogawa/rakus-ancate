package com.example.demo.domain;

import java.sql.Date;
import java.util.List;

import com.example.demo.form.AnswerCategoryForm;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnswerCategory {

	Integer parentCategoryId;
	
	Date respondentJoinDate;
	
	List<AnswerPoint> answerPointList;
	
	public AnswerCategory(AnswerCategoryForm form,List<AnswerPoint> answerPointList) {
		this.parentCategoryId = Integer.parseInt(form.getParentCategoryId());
		this.answerPointList = answerPointList;
	}
	
}

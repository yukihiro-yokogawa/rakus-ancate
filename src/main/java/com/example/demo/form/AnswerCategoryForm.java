package com.example.demo.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.example.demo.domain.AnswerCategory;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnswerCategoryForm {
	

	@NotBlank
	String parentCategoryId;
	
	@Valid
	List<@Valid AnswerPointForm> answerPointFormList;

	public AnswerCategoryForm(AnswerCategory answerCategory, List<AnswerPointForm> answerPointFormList) {
		this.parentCategoryId = String.valueOf(answerCategory.getParentCategoryId());
		this.answerPointFormList = answerPointFormList;
	}
}

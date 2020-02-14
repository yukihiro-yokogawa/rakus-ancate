package com.example.demo.domain;

import java.util.List;

import com.example.demo.form.QuestionnaireCategoryForm;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * アンケートの問のリクエストパラメータを受け取るFormClassです.
 * 
 * @author yukihiro.yokogawa
 *
 */
@Data
@NoArgsConstructor//デフォルトコンストラクタを自動生成する（おぶじぇ）
public class QuestionnaireCategory {
	
	
	/**	問ID */
	Integer questionnaireCategoryId;
	
	/**	評価項目（大）*/
	String parentName;
	
	/**	評価項目（中） */
	List<QuestionnaireCategory> childNameList;
	
	/**
	 * streamApi用
	 * formに送られたリクエストパラメータをdomeinに詰め替える.
	 * 
	 * @param form
	 */
	public QuestionnaireCategory(QuestionnaireCategoryForm form){
		this.parentName = form.getParentName();
		this.childNameList = form.getChildNameList();
	}

	
}

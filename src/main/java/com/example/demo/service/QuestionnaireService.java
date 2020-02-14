package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Answer;
import com.example.demo.domain.AnswerCategory;
import com.example.demo.domain.AnswerPoint;
import com.example.demo.domain.Employee;
import com.example.demo.domain.Questionnaire;
import com.example.demo.domain.QuestionnaireCategory;
import com.example.demo.form.AnswerForm;
import com.example.demo.form.QuestionnaireForm;
import com.example.demo.mapper.QuestionnaireMapper;

/**
 * Contorollerぁら送られてきたリクエストパラメータをDomeinに詰め替えて送信するサービスクラスです.
 * 
 * @author yukihiro.yokogawa
 *
 */
@Service
@Transactional
public class QuestionnaireService {

	@Autowired
	private QuestionnaireMapper questionnaireMapper;
	
	/**
	 * アンケートを検索するメソッドです.
	 * 
	 * @param sortType
	 * @param employee
	 * @param questionnaireId
	 * @return
	 */
	public List<Questionnaire> findQuestionnaire(String sortType, Employee employee,Integer questionnaireId,String authority){
		return questionnaireMapper.findQuestionnaire(sortType,employee,questionnaireId,authority);
	}
	
	/**
	 * 初めて回答したアンケートの解答を登録するメソッドです.
	 * 
	 * @param form
	 */
	public void insertAnswered(AnswerForm form) {
		Answer answer = new Answer();
		refillAnswer(form,answer);
		questionnaireMapper.insertAnswered(answer);
	}
	
	/**
	 * 一度回答したアンケートを再度解答した際にアップデートするメソッドです.
	 * 
	 * @param form
	 */
	public void updateAnswered(AnswerForm form) {
		Answer answer = new Answer();
		refillAnswer(form,answer);
		questionnaireMapper.updateAnswered(answer);
	}
	
	/**
	 * アンケートの解答を検索するメソッドです.
	 * 
	 * @param employee ユーザー情報
	 * @param type 検索条件
	 * @return 解答
	 */
	public Questionnaire findAnswer(Employee employee,String type,Integer id) {
		return questionnaireMapper.findAnswer(employee,type,id);
	}
	
	public List<Employee> findAllEmployeeAnswer(Integer id, String date) {
		return questionnaireMapper.findAllEmployeeAnswer(id,date);
	}
	
	
	/**
	 * リクエストパラメータをDomeinに詰め替えるメソッドです.
	 * 
	 * @param form 回答（リクエストパラメータ）
	 * @param answer 回答（ドメイン）
	 */
	public void refillAnswer(AnswerForm form, Answer answer) {
		answer.setEmployeeId(Integer.parseInt(form.getEmployeeId()));
		answer.setQuestionId(Integer.parseInt(form.getQuestionId()));
		List<AnswerCategory> answerCategoryList = form.getAnswerCategoryFormList().stream()
													.map(c -> new AnswerCategory(c,c.getAnswerPointFormList().stream()
															.map(p -> new AnswerPoint(p))
															.collect(Collectors.toList())))
													.collect(Collectors.toList());
		answer.setAnswerCategoryList(answerCategoryList);
	}
	
	/**
	 * アンケート作成用のリクエストパラメータをDomeinに詰め替えてMapperClassに送るメソッドです.
	 * 
	 * @param form
	 */
	public void insertQuestionnaire(QuestionnaireForm form) {
		Questionnaire questionnaire = new Questionnaire();
		BeanUtils.copyProperties(form, questionnaire);
		refillQuestionnaireCategoryForm(questionnaire,form);
		questionnaireMapper.insertQuestionnaire(questionnaire);
	}
	
	/**
	 * リクエストパラメータをDomeinに詰め替えるメソッドです.
	 * 
	 * @param questionnaire(ドメイン) 
	 * @param form(リクエストパラメータ)
	 * @return カテゴリーリストを含んだドメイン.
	 */
	public void refillQuestionnaireCategoryForm(Questionnaire questionnaire,QuestionnaireForm form) {
		//stringのリストをintegerのリストに詰め替え
		List<Integer> questionnaireTargetList = form.getQuestionnaireTargetList().stream()
				.map(f -> {
					return Integer.parseInt(f);
				})
				.collect(Collectors.toList());
		//object型を持つListのオブジェクトに詰め替え
		List<QuestionnaireCategory> questionnaireCategoryList = form.getQuestionnaireCategoryFormList().stream()
			.map(f -> new QuestionnaireCategory(f))
			.collect(Collectors.toList());
		questionnaire.setQuestionnaireTargetList(questionnaireTargetList);
		questionnaire.setQuestionnaireCategoryList(questionnaireCategoryList);
	}
	
	/**
	 * アンケートを表示を管理するメソッドです.
	 * 
	 * @param id
	 */
	public boolean viewQuestionnaire(Integer id) {
		return questionnaireMapper.viewQuestionnaire(id);
	}
	
	/**
	 * アンケートとその関連情報を完全削除するメソッドです.
	 * 
	 * @param id アンケートID
	 */
	public void deleteQuestionnaire(Integer id) {
		questionnaireMapper.deleteQuestionnaire(id);
	}
	
	/**
	 * 各ユーザー毎の回答履歴を検索するメソッドです.
	 * 
	 * @param id ユーザーID
	 * @return 
	 */
	public Employee findMyAnswer(Integer id) {
		return 	questionnaireMapper.findMyAnswer(id);
	}
	
}

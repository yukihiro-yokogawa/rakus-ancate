package com.example.demo.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SortType {
	
	public Map<SORT_TYPE_QUESTIONNAIRE,String> getSortQuestionaireMap() {
		LinkedHashMap<SORT_TYPE_QUESTIONNAIRE,String> sortList = new LinkedHashMap<>();
		Stream.of(SORT_TYPE_QUESTIONNAIRE.values()).forEach(e -> sortList.put(e, e.getValue()));
		return sortList;
	}
	
	public Map<SORT_TYPE_EMPLOYEE,String> getSortEmployeeMap(){
		LinkedHashMap<SORT_TYPE_EMPLOYEE,String> sortList = Stream.of(SORT_TYPE_EMPLOYEE.values())
		.collect(Collectors.toMap(e -> e
				, e -> e.getValue()
				,(e1,e2) -> e1
				,LinkedHashMap::new
				));
		return sortList;
	}
	
	public Map<SORT_TYPE_ENGINEER,String> getSortEngineerMap(){
		LinkedHashMap<SORT_TYPE_ENGINEER,String> sortList = Stream.of(SORT_TYPE_ENGINEER.values())
		.collect(Collectors.toMap(e -> e
				, e -> e.getValue()
				,(e1,e2) -> e1
				,LinkedHashMap::new
				));
		return sortList;
	}
	
	/**
	 * ソート形式（アンケート一覧）
	 * 
	 * @author yukihiro.yokogawa
	 *
	 */
	protected enum SORT_TYPE_QUESTIONNAIRE{
		ALL("全件"),
		NEW("登録が新しい順"),
		OLD("登録が古い順"),
		ANSWERED_NEW("回答済み（新）"),
		ANSWERED_OLD("回答済み（古）"),
		NOT_ANSWERED_NEW("未回答（新）"),
		NOT_ANSWERED_OLD("未回答（古）");
		
		private String sortType;
		
		private SORT_TYPE_QUESTIONNAIRE(String sortType) {
			this.sortType = sortType;
		}
		
		public String getValue() {
			return this.sortType;
		}
	}
	
	/**
	 * ソート形式（従業員一覧 管理者用）
	 * 
	 * @author yukihiro.yokogawa
	 *
	 */
	protected enum SORT_TYPE_EMPLOYEE{
		
		EMPLOYEE_ALL("全件"),
		JOINDATE_NEW("入社年順（新）"),
		JOINDATE_OLD("入社年順（古）");
		
		private String sortType;
		
		private SORT_TYPE_EMPLOYEE(String sortType) {
			this.sortType=sortType;
		}
		
		public String getValue() {
			return this.sortType;
		}
	}
	
	/**
	 * ソート形式（エンジニア種別 管理者用）
	 * 
	 * @author yukihiro.yokogawa
	 *
	 */
	protected enum SORT_TYPE_ENGINEER{
		
		ENGINEER_ALL("全エンジニア"),
		APPLICATION("アプリ"),
		INFRASTRUCTURE("インフラ"),
		MACHINE_LEARNING("機械学習");
		
		private String sortType;
		
		private SORT_TYPE_ENGINEER(String sortType) {
			this.sortType=sortType;
		}
		
		public String getValue() {
			return this.sortType;
		}
	}
}

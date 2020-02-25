package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.Answer;
import com.example.demo.domain.Employee;
import com.example.demo.domain.Questionnaire;

/**
 * mybatis3用のxmlマッパーにSQLに埋め込むデータを渡すMapperClassです.
 * 
 * @author yukihiro.yokogawa
 *
 */
@Mapper
public interface QuestionnaireMapper {
	
	/**
	 * アンケート登録処理.
	 * 
	 * @param questionnaire アンケート
	 */
	void insertQuestionnaire(@Param("questionnaire") Questionnaire questionnaire);
	
	/**
	 * アンケート全件検索()
	 * sortTypeはSQLのORDER BY句を動的に組み替えるための値を持つ.
	 * 
	 * @param id アンケート全体のid
	 * @return アンケート内容
	 */
	List<Questionnaire> findQuestionnaire(@Param("type") String type,@Param("employee") Employee employee,@Param("id") Integer questionnaireId,@Param("authority") String authority);

	/**
	 * アンケートの回答を新規に登録するメソッドです.
	 * 
	 * @param answer 回答
	 */
	void insertAnswered(@Param("answered") Answer answer);
	
	/**
	 * アンケートを回答済みの場合回答をアップデートするメソッドです.
	 * 
	 * @param answer 回答
	 */
	void updateAnswered(@Param("answered") Answer answer);
	
	/**
	 * アンケートの解答を検索するメソッドです.
	 * 
	 * @param employee
	 * @param type
	 * @return answer 回答
	 */
	Questionnaire findAnswer(@Param("employee") Employee employee,@Param("type") String type,@Param("id") Integer id); 
	
	/**
	 * 個々人のアンケートの回答を検索するメソッドです（管理者用）
	 * 
	 * @param id アンケートID
	 * @return 個々人のアンケート結果
	 */
	List<Employee> findAllEmployeeAnswer(@Param("id") Integer id,@Param("date") String date);
	
	/**
	 * 各ユーザー毎の回答履歴を検索するメソッドです.
	 * 
	 * @param id
	 * @return ユーザー自身の回答と履歴
	 */
	Employee findMyAnswer(@Param("id") Integer id);
	
	/**
	 * アンケートの表示を切り替えるメソッドです（管理者用）
	 * 
	 * @param id アンケートID
	 */
	boolean viewQuestionnaire(@Param("id") Integer id);
	
	/**
	 * アンケートの表示・非表示を切り替えるメソッドです.
	 * 
	 * @param id
	 * @return true or false
	 */
	boolean deleteQuestionnaire(@Param("id") Integer id);

}

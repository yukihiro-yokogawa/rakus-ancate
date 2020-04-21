package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.Employee;
import com.example.demo.domain.JobCategory;

@Mapper
public interface EmployeeMapper {

	/**
	 * ログインユーザーの情報.
	 * 
	 * @param mailAddress ログイン画面で入力されたメールアドレス.
	 * @return ログインするユーザーの情報
	 */
	Employee findEmployee(@Param("mailAddress") String mailAddress);
	
	/**
	 * 職種検索
	 * 
	 * @return 職情報
	 */
	List<JobCategory> findJobCategory();
	
	/**
	 * ユーザー登録.
	 * 
	 * @param employee ユーザー情報
	 */
	void insertEmployee(@Param("employee") Employee employee);
	
	/**
	 * 全ユーザー情報の検索（管理者用）.
	 * 
	 * @param date 検索したい従業員の入社年度
	 * @param type 入社年度順
	 * @return 検索された全従業員情報
	 */
	List<Employee> findAll(@Param("date") String date,@Param("type")String type,@Param("engineer")String engineer);

	String findMailAddress(@Param("mailAddress") String mailAddress);

	/**
	 * パスワードの更新（全ユーザー用）
	 *
	 * @param id ログインしているユーザーのID
	 * @param password 変更したいパスワード
	 */
	void updatePassword(@Param("id") Integer id, @Param("password") String password);}

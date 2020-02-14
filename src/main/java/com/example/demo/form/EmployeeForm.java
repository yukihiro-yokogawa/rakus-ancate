package com.example.demo.form;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class EmployeeForm {

	/**	ユーザーネーム */
	@NotBlank(message="ユーザー名を入力してください。")
	String name;
	
	/**	入社年 */
	String joinYear;
	
	/**	入社月 */
	String joinMonth;
	
	/**	職種 */
	String jobCategoryId;
	
	/**	ユーザー認証情報 */
	@Valid
	AuthInfoForm authInfoForm;
}

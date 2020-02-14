package com.example.demo.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AuthInfoForm {

	/**	ユーザーメールアドレス */
	@NotBlank(message="メールアドレスを入力してください。")
	@Email(message="メールアドレスの形式が不正です。")
	@Pattern(regexp=".*@rakus-partners.co.jp$",message="ドメイン名が不正です。")
	String mailAddress;
	
	/**	ユーザーパスワード */
	@NotBlank(message="パスワードを入力してください。")
	@Size(max = 48,min = 8,message = "パスワードは8文字以上48文字以下で入力してください。")
	String password;
	
	@NotBlank(message="確認用パスワードを入力してください。")
	String confirmPassword;
	
	/**	ユーザー確認ID */
	String authorityId;
	
}

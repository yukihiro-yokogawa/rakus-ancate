package com.example.demo.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class PasswordChangeForm {

	@NotBlank(message="メールアドレスを入力してください")
	String mailAddress;
	@NotBlank(message="パスワードを入力してください")
	String password;
	@NotBlank(message="変更したいパスワードを入力してください")
	String changePassword;
	@NotBlank(message="変更したいパスワードを確認用のため再度入力してください")
	String changeConfirmPassword;
	
}

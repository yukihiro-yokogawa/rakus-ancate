package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService memberDetailService;
	
	@Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers("/css/**","/js/**","/img/**");
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
        .antMatchers("/questionnaire/**").hasAnyRole("USER","ADMIN")
        .antMatchers("/employee/**","/agreegate/**").permitAll()
        .anyRequest().authenticated();
        
		//ログインに関する設定
		http.formLogin()
			.loginPage("/employee/toLogin")
			.loginProcessingUrl("/login")
			.failureUrl("/employee/toLogin?error=true") 
			.defaultSuccessUrl("/questionnaire/list",true)
			.usernameParameter("mailAddress")
			.passwordParameter("password");
		
		//ログアウトに関する設定
		http.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout**"))
			.logoutSuccessUrl("/questionnaire/list")// ログアウト後に遷移させるパス(ここでは商品一覧画面を設定)
			.deleteCookies("JSESSIONID")
			.invalidateHttpSession(true);
	}
	
	/**
	 * 認証に関する設定.
	 * 認証ユーザを設定する「UserDetailsService」の設定およびパスワード照合時に使う「PasswordEncoder」の設定
	 * 
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberDetailService)
			.passwordEncoder(new BCryptPasswordEncoder());
	}
	
	/**
	 * bcryptアルゴリズムでハッシュ化する実装を返す
	 * パスワードハッシュ化やマッチ確認する際にPasswordEncoderクラスのDpomIを可能にする
	 * 
	 * @return bcryptアルゴリズムでハッシュ化する実装オブジェクト
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}

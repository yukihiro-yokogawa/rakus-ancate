package com.example.demo.controller;

import com.example.demo.domain.AuthInfo;
import com.example.demo.domain.Employee;
import com.example.demo.domain.JobCategory;
import com.example.demo.domain.LoginEmployee;
import com.example.demo.form.AuthInfoForm;
import com.example.demo.form.EmployeeForm;
import com.example.demo.form.PasswordChangeForm;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.*;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@DisplayName("employeeControllerの")
class EmployeeControllerTest {

    @Autowired
    private EmployeeController employeeController;

    private MockMvc mockMvc;

    Model model = new BindingAwareModelMap();

    @Nested
    @DisplayName("各リクエストが正しく送れているかのテスト：")
    class request {
        @BeforeEach
        public void setup() {
            mockMvc = MockMvcBuilders.standaloneSetup(employeeController).alwaysDo(log()).build();
        }

        @Test
        @DisplayName("認証されているユーザーでログインできるかどうか")
        public void loginAdd(){
            MvcResult mvcResult = mockMvc.perform(post("/employee/login")
                    .param("mailAddress","test@rakus-partners.co.jp")
                    .param("password","mai6f9irunoa"))
                    .andExpect(status().is(200))
                    .andExpect(redirectedUrl("/questionnaire/list"))
                    .andReturn();
        }


        @Test
        @DisplayName("パスワード変更完了処理")
        public void changeFinishPasswordTest() throws Exception {
            ResultActions results = mockMvc.perform(post("/employee/change_finish"));
            results.andExpect(status().is(200));
        }


        @Test
        @DisplayName("ログイン画面")
        public void toLoginTest() throws Exception {
            ResultActions results = mockMvc.perform(post("/employee/toLogin"));
            results.andExpect(status().is(200));//チェックできる（200は成功　200でなかったら失敗するようになっている）
        }

        @Test
        @DisplayName("ユーザー登録画面")
        public void toInsertTest() throws Exception {
            MvcResult mvcResult = mockMvc.perform(post("/employee/toInsert"))
                    .andExpect(status().is(200))
                    .andReturn();
            ModelAndView mav = mvcResult.getModelAndView();
            List<JobCategory> jobCategoryList = (List<JobCategory>) mav.getModel().get("jobCategoryList");
            assertAll(
                    () -> assertNotNull(jobCategoryList, jobCategoryList.toString()),
                    () -> assertEquals(3, jobCategoryList.size()),
                    () -> jobCategoryList.forEach(l -> {
                            switch (l.getJobCategoryId()) {
                                case 1:
                                    assertEquals("アプリ", l.getName(),"アプリエンジニア取得");
                                    break;
                                case 2:
                                    assertEquals("インフラ", l.getName(),"インフラエンジニア取得");
                                    break;
                                case 3:
                                    assertEquals("機械学習", l.getName(),"機械学習エンジニア取得");
                                    break;
                                default:
                                    assertEquals("error",l.getName(),"新しくjobが追加されたか何かしらのエラーが出ています");
                                    break;
                            }
                        }
                    )
            );
        }

        @Test
        @DisplayName("ユーザー登録処理が成功する時")
        public void insertProcessTest() throws Exception{
            MultiValueMap <String,String> map = new LinkedMultiValueMap<String,String>(){
                {
                    put("authInfoForm.mailAddress",Collections.singletonList("test@rakus-partners.co.jp"));
                    put("authInfoForm.password", Collections.singletonList("password"));
                    put("authInfoForm.confirmPassword",Collections.singletonList("password"));
                    put("authInfoForm.authorityId",Collections.singletonList("2"));
                    put("joinYear",Collections.singletonList(String.valueOf(LocalDate.now().getYear())));
                    put("joinMonth",Collections.singletonList(String.valueOf(LocalDate.now().getMonthValue())));
                    put("name",Collections.singletonList("taro"));
                    put("jobCategoryId",Collections.singletonList("1"));
                }
            };
            mockMvc.perform(post("/employee/insertProcess").params(map))
                    .andExpect(redirectedUrl("/employee/toLogin"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(view().name("redirect:/employee/toLogin"));
        }

        @Test
        @DisplayName("ユーザー登録処理が失敗する時")
        public void insertProcessTestError() throws Exception{
            MultiValueMap <String,String> map = new LinkedMultiValueMap<String,String>(){
                {
                    put("authInfoForm.mailAddress",Collections.singletonList("test@rakus-partners.co.jp"));
                    put("authInfoForm.password", Collections.singletonList("password"));
                    put("authInfoForm.confirmPassword",Collections.singletonList("not_confirm_password"));
                    put("authInfoForm.authorityId",Collections.singletonList("2"));
                    put("joinYear",Collections.singletonList("2021"));
                    put("joinMonth",Collections.singletonList("1"));
                    put("name",Collections.singletonList("taro"));
                    put("jobCategoryId",Collections.singletonList("1"));
                }
            };
            MvcResult mvcResult = mockMvc.perform(post("/employee/insertProcess").params(map))
                    .andExpect(status().isOk())
                    .andExpect(model().hasErrors())
                    .andExpect(view().name("employee/insert.html"))
                    .andReturn();
            System.out.println(model().hasErrors().toString());
        }

        @Test
        public void passwordChangeTest() throws Exception {
            ResultActions results = mockMvc.perform(post("/employee/password_change"));
            results.andExpect(status().is(200));
        }
    }

    @Nested
    @DisplayName("ユーザー登録処理のバリデーションチェック")
    class insertEmployee {
        private EmployeeForm employeeForm = new EmployeeForm();
        private AuthInfoForm authInfoForm = new AuthInfoForm();
        private BindingResult result = new BindException(employeeForm, "EmployeeForm");

        @BeforeEach
        void setup() {
            employeeForm.setJobCategoryId("1");
            employeeForm.setName("test");
            employeeForm.setAuthInfoForm(authInfoForm);
            authInfoForm.setAuthorityId("1");
        }


        @Nested
        @DisplayName("joinYearのバリデーションチェックが")
        class beforeCreatedRPJoinYear {
            @BeforeEach
            void setup() {
                authInfoForm.setMailAddress("test");
                authInfoForm.setPassword("test");
                authInfoForm.setConfirmPassword("test");
                employeeForm.setJoinMonth("1");
            }

            @Test
            @DisplayName("エラーになるとき（2016）")
            void joinYearErrorTest2016() {
                employeeForm.setJoinYear("2016");
                employeeController.insertProcess(employeeForm, result, model);
                assertNotNull(result.getFieldError("joinYear"));
            }

            @Test
            @DisplayName("エラーになるとき（2021～）")
            void joinYearErrorTest2021() {
                for (int i = LocalDate.now().getYear() + 1; i <= LocalDate.now().getYear() + 5; i++) {
                    employeeForm.setJoinYear(String.valueOf(i));
                    employeeController.insertProcess(employeeForm, result, model);
                    assertNotNull(result.getFieldError("joinYear"));
                }
            }

            @Test
            @DisplayName("成功する時")
            void joinYearSuccess() {
                for (int i = 2017; i <= LocalDate.now().getYear(); i++) {
                    employeeForm.setJoinYear(String.valueOf(i));
                    employeeController.insertProcess(employeeForm, result, model);
                    assertNull(result.getFieldError("joinYear"));
                }
            }
        }

        @Nested
        @DisplayName("joinMonthのバリデーションチェックが")
        class beforeCreatedRPJoinMonth {
            @BeforeEach
            void setup() {
                authInfoForm.setMailAddress("test");
                authInfoForm.setPassword("test");
                authInfoForm.setConfirmPassword("test");
                employeeForm.setJoinYear("2020");
            }

            @Test
            @DisplayName("成功する時")
            void joinMonthSuccessTest() {
                for (int i = 1; i <= LocalDate.now().getMonthValue(); i++) {
                    employeeForm.setJoinMonth(String.valueOf(i));
                    employeeController.insertProcess(employeeForm, result, model);
                    assertNull(result.getFieldError("joinMonth"));
                }
            }

            @Test
            @DisplayName("エラーになるとき")
            void joinMonthErrorTest() {
                for (int i = LocalDate.now().getMonthValue() + 1; i <= LocalDate.now().getMonthValue(); i++) {
                    employeeForm.setJoinMonth(String.valueOf(i));
                    employeeController.insertProcess(employeeForm, result, model);
                    assertNotNull(result.getFieldError("joinMonth"));
                }
            }
        }

        @Nested
        @DisplayName("パスワードと確認用パスワードが")
        class passwordChecker {
            @BeforeEach
            void setup() {
                authInfoForm.setMailAddress("test");
                employeeForm.setJoinYear("2020");
                employeeForm.setJoinMonth("1");
            }

            @Test
            @DisplayName("一致する時")
            void passwordSuccess() {
                authInfoForm.setPassword("success");
                authInfoForm.setConfirmPassword("success");
                employeeController.insertProcess(employeeForm, result, model);
                assertNull(result.getFieldError("authInfoForm.password"));
            }

            @Test
            @DisplayName("一致していない時")
            void passwordError() {
                authInfoForm.setPassword("success");
                authInfoForm.setConfirmPassword("error");
                employeeController.insertProcess(employeeForm, result, model);
                assertNotNull(result.getFieldError("authInfoForm.password"));
            }
        }

        @Nested
        @DisplayName("全てにエラーが")
        class all {
            @BeforeEach
            void setup() {
                authInfoForm.setMailAddress("test");
            }

            @Test
            @DisplayName("あるとき")
            void error() {
                authInfoForm.setPassword("success");
                authInfoForm.setConfirmPassword("error");
                employeeForm.setJoinYear("2016");
                employeeForm.setJoinMonth("1");
                employeeController.insertProcess(employeeForm, result, model);
                assertAll(
                        () -> assertNotNull(result.getFieldError("authInfoForm.password")),
                        () -> assertNotNull(result.getFieldError("joinYear")),
                        () -> assertNotNull(result.getFieldError("joinMonth"))
                );
            }

            //エラーがない時のテストなので実行するときにデータベース登録処理が入るため使用するときは注意すること
//            @Test
//            @DisplayName("ないとき")
//            void success(){
//                String response = null;
//                authInfoForm.setPassword("success");
//                authInfoForm.setConfirmPassword("success");
//                for(int i = 2017; i <= LocalDate.now().getYear();i++) {
//                    employeeForm.setJoinYear(String.valueOf(i));
//                    for(int j = 1; j <= 12; j++) {
//                        if(j == LocalDate.now().getMonthValue() + 1){
//                            break;
//                        }
//                        employeeForm.setJoinMonth(String.valueOf(j));
//                        response = employeeController.insertProcess(employeeForm,result,model);
//                        assertNull(result.getFieldError());
//                    }
//                }
//                assertEquals(response,"redirect:/employee/toLogin");
//            }
        }
    }

    @Test
    @DisplayName("ユーザー登録画面の表示が成功する時")
    void responseTest() {
        assertEquals("employee/insert.html", employeeController.toInsert(model));
        assertNotNull(model.getAttribute("jobCategoryList"));
    }

    @Test
    @DisplayName("ログイン画面の表示が成功する時")
    void responseTopTest() {
        assertEquals("employee/login.html", employeeController.toLogin());
    }

    @Test
    @DisplayName("パスワード変更画面の表示が成功する時")
    void responsePasswordChange() {
        assertEquals("employee/login.html", employeeController.toLogin());
    }

//    @Nested
//    @DisplayName("パスワード変更処理が")
//    class springSecurityTestChecker{
//
//        PasswordChangeForm passwordChangeForm = new PasswordChangeForm();
//        Employee employee = new Employee();
//        AuthInfo authInfo = new AuthInfo();
//        private BindingResult result = new BindException(passwordChangeForm, "PasswordChangeForm");
//        @BeforeEach
//        void setup(){
//            authInfo.setMailAddress("test");
//            authInfo.setPassword("test");
//            passwordChangeForm.setPassword("test");
//            passwordChangeForm.setChangePassword("test");
//            passwordChangeForm.setMailAddress("test");
//            passwordChangeForm.setChangeConfirmPassword("test");
//        }
//
//        @Test
//        @WithMockUser
//        @DisplayName("errorになる")
//        void error(){
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            LoginEmployee loginEmployee = new LoginEmployee(employee, (Collection<GrantedAuthority>) auth.getAuthorities());
//            employeeController.updatePassword(passwordChangeForm,result,loginEmployee);
//        }
//    }
}
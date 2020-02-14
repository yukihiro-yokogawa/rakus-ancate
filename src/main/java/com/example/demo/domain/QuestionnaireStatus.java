package com.example.demo.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import lombok.Data;

@Data
public class QuestionnaireStatus {
	
	Integer questionnaireStatusId;
	
	Timestamp createdAt;
	
	Timestamp updatedAt;
	
	Boolean deleted;
	
	/**
	 * 質問作成日時を取得するGetterメソッド
	 * 
	 * @return 加工した日付データ（形式 2019/07/01）
	 */
	public String getCreatedAtStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String createdAtStr = sdf.format(this.createdAt);
        return createdAtStr;
	}
	
	/**
	 * 質問更新日時を取得するSetterメソッド
	 * 
	 * @return 加工した日付データ（形式 2019/07/01）
	 */
	public String getUpdatedAtStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String updatedAtStr = sdf.format(this.updatedAt);
		return updatedAtStr;
	}
	
}

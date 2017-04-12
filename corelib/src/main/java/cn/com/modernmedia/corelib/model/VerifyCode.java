package cn.com.modernmedia.corelib.model;

import org.json.JSONObject;

/**
 * 手机验证码
 * 
 * @author lusiyuan
 *
 */
public class VerifyCode extends Entry {
	private String phone;

	public VerifyCode(JSONObject json) {
		if (!json.isNull("phone")) {
			setPhone(json.optString("phone"));
		}
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String code) {
		this.phone = code;
	}

}

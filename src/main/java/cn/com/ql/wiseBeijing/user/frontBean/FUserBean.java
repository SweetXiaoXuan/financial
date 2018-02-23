package cn.com.ql.wiseBeijing.user.frontBean;

import javax.ws.rs.FormParam;

public class FUserBean {
	@FormParam("nickname")
	private String nickname;
	@FormParam("id")
	private String id;
	@FormParam("phone")
	private String phone;
	@FormParam("password")
	private String password;
	@FormParam("avatar")
	private String avatar;
	private Boolean verified;
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}
}

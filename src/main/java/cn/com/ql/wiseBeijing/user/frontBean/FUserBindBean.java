package cn.com.ql.wiseBeijing.user.frontBean;

import javax.ws.rs.FormParam;

public class FUserBindBean {
	
	@FormParam("id")
	private String id;
	
	@FormParam("platform")
	private String platform;
	
	@FormParam("platformUid")
	private String platform_uid;

	@FormParam("avatar")
	private String avatar;
	
	@FormParam("nickname")
	private String nickname;

	private Boolean verified;
	private String phone;
	private Integer firstTime;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getPlatform_uid() {
		return platform_uid;
	}
	public void setPlatform_uid(String platform_uid) {
		this.platform_uid = platform_uid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(Integer firstTime) {
		this.firstTime = firstTime;
	}
}

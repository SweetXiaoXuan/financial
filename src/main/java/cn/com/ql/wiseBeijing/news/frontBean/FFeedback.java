package cn.com.ql.wiseBeijing.news.frontBean;

import javax.ws.rs.FormParam;


public class FFeedback {
	private int id;
	@FormParam("comment")
	private String comment;
	@FormParam("uid")
	private String userid;//前缀绑定了类型，本身平台，微信，微博……
	private String createtime;
	@FormParam("wid")
	private String workID;
	@FormParam("email")
	private String email;
	private String status;
	@FormParam("type")
	private String type;//0文本，1图片
	@FormParam("phone")
	private String phone;
	@FormParam("pversion")
	private String phone_version;
	@FormParam("oversion")
	private String os_version;
	@FormParam("cversion")
	private String client_version;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getWorkID() {
		return workID;
	}
	public void setWorkID(String workID) {
		this.workID = workID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhone_version() {
		return phone_version;
	}
	public void setPhone_version(String phone_version) {
		this.phone_version = phone_version;
	}
	public String getOs_version() {
		return os_version;
	}
	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}
	public String getClient_version() {
		return client_version;
	}
	public void setClient_version(String client_version) {
		this.client_version = client_version;
	}
}

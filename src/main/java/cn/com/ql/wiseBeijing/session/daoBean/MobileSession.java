package cn.com.ql.wiseBeijing.session.daoBean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.com.ql.wiseBeijing.session.util.PrimaryKeyGenerator;


@Table(name="mobilesession")
@Entity
public class MobileSession 
{
	@Id
	@GenericGenerator(name="session_key",strategy=PrimaryKeyGenerator.SESSION_PRIMARY_KEY)
	@GeneratedValue(generator="session_key")
	private String sessionid;
	private String uid;
	private String uname;
	private String createTime;
	private String logoutTime;
	private String status;
	private String other;
	
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(String logoutTime) {
		this.logoutTime = logoutTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	
}

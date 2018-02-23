package cn.com.ql.wiseBeijing.news.daoBean;

import cn.lv.jewelry.activity.daoBean.Activity;
import cn.lv.jewelry.activity.daoBean.ActivityComment;
import cn.lv.jewelry.activity.daoBean.ActivityPrivilege;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name="likes")
@Entity
public class Likes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	// 用户权限id
	@ManyToOne
	@JoinColumn(name = "privilege_id")
	private ActivityPrivilege privilegeId;
	private Long cid;
	@ManyToOne
	@JoinColumn(name="acid")
	private ActivityComment acid;
	@ManyToOne
	@JoinColumn(name="aid")
	private Activity aid;

	private String platform;
	private String createtime;
	private String status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public ActivityComment getAcid() {
		return acid;
	}

	public void setAcid(ActivityComment acid) {
		this.acid = acid;
	}

	public Activity getAid() {
		return aid;
	}

	public void setAid(Activity aid) {
		this.aid = aid;
	}

	public ActivityPrivilege getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(ActivityPrivilege privilegeId) {
		this.privilegeId = privilegeId;
	}
}

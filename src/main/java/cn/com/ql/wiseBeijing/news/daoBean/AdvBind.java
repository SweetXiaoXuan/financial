package cn.com.ql.wiseBeijing.news.daoBean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Table(name = "advsbind")
@Entity
public class AdvBind {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String pnid;
	private String createtime;
	private String pubtime;
	private int status;
	private String platform;//news,dec,policy
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="nid_list")
	private Advs adv;
	
	
	
	
	public Advs getAdv() {
		return adv;
	}
	public void setAdv(Advs adv) {
		this.adv = adv;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPnid() {
		return pnid;
	}
	public void setPnid(String pnid) {
		this.pnid = pnid;
	}
	/**
	 * 以前设计返回是一个列表，现在只是为一个news id
	 * @return
	 */
	/*public String getNid_list() {
		return nid_list;
	}
	public void setNid_list(String nid_list) {
		this.nid_list = nid_list;
	}*/
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getPubtime() {
		return pubtime;
	}
	public void setPubtime(String pubtime) {
		this.pubtime = pubtime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
}

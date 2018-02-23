package cn.com.ql.wiseBeijing.dec.daoBean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Table(name = "dectopics")
@Entity
public class DecTopics {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String pnid;
	//private String nid_list;//设计发生变化，这字段只存一个新闻id，而不是列表
	private String createtime;
	private String pubtime;
	private int status;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="nid_list")
	private DecMainNews mainNews;
	
	public DecMainNews getMainNews() {
		return mainNews;
	}
	public void setMainNews(DecMainNews mainNews) {
		this.mainNews = mainNews;
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
	
}

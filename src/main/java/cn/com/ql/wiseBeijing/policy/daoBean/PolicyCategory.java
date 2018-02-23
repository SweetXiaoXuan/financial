package cn.com.ql.wiseBeijing.policy.daoBean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "policy_category")
@Entity
public class PolicyCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title;
	@Column(name = "abstract")
	private String description;
	private String keywords;
	private String createtime;
	private String pubdate;
	private String pubstatus;
	private String listimage;
	private String smallimage;
	private String largeimage;
	private String status;
	private String sources;
	private String urls;
	private String sort_flag;
	private int next;//这个字段标识是否还有子列表0表示没有，那就取他的子内容，1表示有，在这个表里找到他的子列表
	private int parent_id;
	private int clevel;//顶级clevel为0，其它可以设置目前也可以不设置
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getPubstatus() {
		return pubstatus;
	}

	public void setPubstatus(String pubstatus) {
		this.pubstatus = pubstatus;
	}

	public String getListimage() {
		return listimage;
	}

	public void setListimage(String listimage) {
		this.listimage = listimage;
	}

	public String getSmallimage() {
		return smallimage;
	}

	public void setSmallimage(String smallimage) {
		this.smallimage = smallimage;
	}

	public String getLargeimage() {
		return largeimage;
	}

	public void setLargeimage(String largeimage) {
		this.largeimage = largeimage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSources() {
		return sources;
	}

	public void setSources(String sources) {
		this.sources = sources;
	}

	public String getUrls() {
		return urls;
	}

	public void setUrls(String urls) {
		this.urls = urls;
	}

	public String getSort_flag() {
		return sort_flag;
	}

	public void setSort_flag(String sort_flag) {
		this.sort_flag = sort_flag;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public int getClevel() {
		return clevel;
	}

	public void setClevel(int clevel) {
		this.clevel = clevel;
	}

	

}

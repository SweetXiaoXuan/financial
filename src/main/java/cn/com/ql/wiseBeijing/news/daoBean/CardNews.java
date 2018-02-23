package cn.com.ql.wiseBeijing.news.daoBean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="cardnews")
@Entity
public class CardNews {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String template;
	private String newsarray;
	private String pubdate;
	private String createdate;
	private String status;
	private String sort;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getNewsarray() {
		return newsarray;
	}
	public void setNewsarray(String newsarray) {
		this.newsarray = newsarray;
	}
	public String getPubdate() {
		return pubdate;
	}
	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}
	
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
}

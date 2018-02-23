package cn.lv.jewelry.product.daoBean;

import javax.persistence.*;

/**
 * 商品->图片
 */
@Table(name = "PRODUCT_IMAGE")
@Entity
public class ProductImageItemBean {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JoinColumn(name = "pid")
	private Product pid;
	private int type;
	private int status;
	private String content;
	private int sort;
	private String extend;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Product getPid() {
		return pid;
	}
	public void setPid(Product pid) {
		this.pid = pid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getExtend() {
		return extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}
}

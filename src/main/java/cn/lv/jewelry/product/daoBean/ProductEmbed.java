package cn.lv.jewelry.product.daoBean;

import javax.persistence.*;

/**
 * 商品 -> 镶嵌
 * @author hy
 *
 */
@Table(name = "PRODUCT_EMBED")
@Entity
public class ProductEmbed {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private int status;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}

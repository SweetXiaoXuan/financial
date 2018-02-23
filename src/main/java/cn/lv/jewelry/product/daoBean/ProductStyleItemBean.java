package cn.lv.jewelry.product.daoBean;

import javax.persistence.*;

/**
 * 商品 -> 风格
 * @author hy
 *
 */
@Table(name = "PRODUCT_STYLE")
@Entity
public class ProductStyleItemBean {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private int status;

    public ProductStyleItemBean(ProductStyle productStyle) {
        this.id = productStyle.getId();
        this.name = productStyle.getName();
        this.status = productStyle.getStatus();
    }

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

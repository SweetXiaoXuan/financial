package cn.lv.jewelry.product.daoBean;

import javax.persistence.*;

/**
 * 商品 -> 材质
 * @author hy
 *
 */
@Table(name = "PRODUCT_MATERIAL")
@Entity
public class ProductMaterialItemBean {



	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private int status;
    public ProductMaterialItemBean(){}
    public ProductMaterialItemBean(ProductMaterial productMaterial) {
        this.id = productMaterial.getId();
        this.name  = productMaterial.getName();
        this.status = productMaterial.getStatus();
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

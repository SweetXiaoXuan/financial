package cn.lv.jewelry.product.daoBean;

import javax.persistence.*;

/**
 * 商品 -> 品牌
 * @author hy
 *
 */
@Table(name = "PRODUCT_BRAND")
@Entity
public class ProductBrand {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long pid;
	private long did;
	private int status;
	private String extend;

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public long getDid() {
		return did;
	}

	public void setDid(long did) {
		this.did = did;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}

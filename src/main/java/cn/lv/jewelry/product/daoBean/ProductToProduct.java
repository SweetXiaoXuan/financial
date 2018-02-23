package cn.lv.jewelry.product.daoBean;

import javax.persistence.*;

/**
 * 商品 -> 相关商品
 * @author hy
 *
 */
@Table(name = "PRODUCT_PRODUCT")
@Entity
public class ProductToProduct {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long mid;
	private long rid;
	private int status;
	private int extend;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	public long getRid() {
		return rid;
	}

	public void setRid(long rid) {
		this.rid = rid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getExtend() {
		return extend;
	}

	public void setExtend(int extend) {
		this.extend = extend;
	}
}

package cn.lv.jewelry.product.daoBean;

/**
 * Created by lixing on 16/5/5.
 */

import javax.persistence.*;

/**
 * 商品 -> 类型
 * @author hy
 *
 */
@Table(name = "PRODUCT_TYPE")
@Entity
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int status;
    private String extend;

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

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}

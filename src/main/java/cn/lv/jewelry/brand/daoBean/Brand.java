package cn.lv.jewelry.brand.daoBean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商品 -> 品牌
 * @author hy
 *
 */
@Table(name = "brand")
@Entity
public class Brand {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	// 品牌名称
    @Column(name = "brand_name")
	private String brandName;
    // 类型 1企业 2个人
    private Integer type;
    // 介绍
    private String description;
    // 品牌图片，(类似企业logo)
    @Column(name = "brand_pic")
    private String brandPic;
    private String email;
    private String mobile;
    private String address;
    @Column(name = "create_time")
    private String createTime;
    // 状态 0正常 1异常
	private Integer status;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrandPic() {
        return brandPic;
    }

    public void setBrandPic(String brandPic) {
        this.brandPic = brandPic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

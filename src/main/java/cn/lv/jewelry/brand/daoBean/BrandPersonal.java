package cn.lv.jewelry.brand.daoBean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by 24593 on 2018/2/10.
 */
@Table(name = "brand_personal")
@Entity
public class BrandPersonal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    // 品牌id
    @OneToOne
    @JoinColumn(name = "brand_id")
    private Brand brandId;
    // 真实姓名
    private String givename;
    // 身份证正面
    @Column(name = "id_card_front")
    private String idCardFront;
    // 身份证反面
    @Column(name = "id_card_back")
    private String idCardBack;
    // 手持身份证
    @Column(name = "id_card_in_hand")
    private String idCardInHand;
    // 身份证号
    private String idNumber;
    @Column(name = "create_time")
    private String createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Brand getBrandId() {
        return brandId;
    }

    public void setBrandId(Brand brandId) {
        this.brandId = brandId;
    }

    public String getGivename() {
        return givename;
    }

    public void setGivename(String givename) {
        this.givename = givename;
    }

    public String getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
    }

    public String getIdCardBack() {
        return idCardBack;
    }

    public void setIdCardBack(String idCardBack) {
        this.idCardBack = idCardBack;
    }

    public String getIdCardInHand() {
        return idCardInHand;
    }

    public void setIdCardInHand(String idCardInHand) {
        this.idCardInHand = idCardInHand;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

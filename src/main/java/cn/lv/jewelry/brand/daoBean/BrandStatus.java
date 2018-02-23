package cn.lv.jewelry.brand.daoBean;

/**
 * Created by 24593 on 2018/2/10.
 */
public enum BrandStatus {
    // 0-正常，1-异常
    NORMAL(0), ABNORMAL(1);

    BrandStatus(Integer status) {
        this.status = status;
    }

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

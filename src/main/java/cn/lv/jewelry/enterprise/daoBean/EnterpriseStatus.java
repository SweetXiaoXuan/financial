package cn.lv.jewelry.enterprise.daoBean;

/**
 * Created by Administrator on 2017/8/27 0027.
 */
public enum EnterpriseStatus {
    // 账号状态
    NORMAL(0), WRITEOFF(1), ABNORMAL(2);

    EnterpriseStatus(Integer status) {
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

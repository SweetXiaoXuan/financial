package cn.lv.jewelry.activity.daoBean;

/**
 * Created by Administrator on 2017/8/20 0020.
 */
public enum  ActivityPrivilegeStatus {
    // 账号状态
    NORMAL(0), WRITEOFF(1), ABNORMAL(2),
    // 发活动权限
    CANRELEASED(0), CANNOTPUBLISHED(1),
    // 承办活动权限
    CANCONTRACTED(0), CANNOTCONTRACTED(1),
    // 用户类型
    PERSONAL(0), ENTERPRISE(1), RHIRD_PARTY(2)
    ;


    ActivityPrivilegeStatus(Integer status)
    {
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

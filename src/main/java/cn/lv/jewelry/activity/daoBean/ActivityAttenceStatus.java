package cn.lv.jewelry.activity.daoBean;

/**
 * Created by Administrator on 2017/8/21 0021.
 */
public enum  ActivityAttenceStatus {
    // 0报名 1参与 2异常 3预通过 4退出 5已删除
    SIGNUP(0), PARTICIPATE(1), ABNORMAL(2), PRE_PASS(3), DROP_OUT(4), DELETE(5);

    ActivityAttenceStatus(Integer status) {
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

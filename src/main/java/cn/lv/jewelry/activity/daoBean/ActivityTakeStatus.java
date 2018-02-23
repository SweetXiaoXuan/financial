package cn.lv.jewelry.activity.daoBean;

/**
 * Created by Administrator on 2017/8/19 0019.
 */
public enum ActivityTakeStatus {
    NORMAL(0), ABNORMAL(1);

    ActivityTakeStatus(Integer status)
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

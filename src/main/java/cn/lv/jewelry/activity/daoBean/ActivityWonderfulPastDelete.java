package cn.lv.jewelry.activity.daoBean;

/**
 * Created by 24593 on 2017/11/26.
 */
public enum  ActivityWonderfulPastDelete {
    NOT_DELETE(0), DELETE(1);

    ActivityWonderfulPastDelete(Integer status)
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

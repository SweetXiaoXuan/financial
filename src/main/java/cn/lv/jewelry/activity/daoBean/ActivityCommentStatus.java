package cn.lv.jewelry.activity.daoBean;

/**
 * Created by Administrator on 2017/8/23 0023.
 */
public enum ActivityCommentStatus {
    // 0-正常，1-异常
    NORMAL(0), ABNORMAL(1);

    ActivityCommentStatus(Integer status) {
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
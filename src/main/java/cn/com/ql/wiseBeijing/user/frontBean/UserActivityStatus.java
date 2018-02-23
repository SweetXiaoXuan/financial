package cn.com.ql.wiseBeijing.user.frontBean;

/**
 * Created by Administrator on 2017/9/24 0024.
 */
public enum UserActivityStatus {
    // 0关注 1报名 2入围 3参与 4发布
    FOCUS(0), JOIN(1), SHORTLISTED(2), ATTENCE(3), RELEASE(4);

    private int status;
    UserActivityStatus(int status)
    {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

package cn.com.ql.wiseBeijing.user.frontBean;

import java.util.Map;

/**
 * 用户报名所有活动下的相关用户列表
 * @author liumengwei
 * @Date 2017/9/2
 */
public class FUserListBean {
    private Long aid;
    private String subject;
    private Integer status;
    private Map<String, Object> user;
    private Boolean hasNext;

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Map<String, Object> getUser() {
        return user;
    }

    public void setUser(Map<String, Object> user) {
        this.user = user;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

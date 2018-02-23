package cn.com.ql.wiseBeijing.user.frontBean;

import java.util.List;

/**
 * 查询报名 该用户发布的活动 的用户 相关bean
 * @author liumengwei
 * @Date 2017/10/12
 */
public class FActivityRegistrationInformationBean {
    // 是否还有数据
    private Boolean hasNext;
    // 总页数
    private Integer totalPages;
    // 用户相关数据集合
    private List<Users> users;
    // 封面图
    private String coverImage;
    // 发布者头像
    private String headPic;
    private String registerEndTime;

    public Boolean getHasNext() {
        return hasNext;
    }
    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }
    public List<Users> getUsers() {
        return users;
    }
    public void setUsers(List<Users> users) {
        this.users = users;
    }
    public String getHeadPic() {
        return headPic;
    }
    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public String getRegisterEndTime() {
        return registerEndTime;
    }

    public void setRegisterEndTime(String registerEndTime) {
        this.registerEndTime = registerEndTime;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public class Users {
        // 用户头像
        private String headPic;
        // 用户id
        private Long uid;
        // 用户权限id
        private Long pid;
        // 用户自我介绍
        private Object selfIntroduction;
        // 用户报名状态
        private Integer status;
        // 用户名
        private Object username;

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public Object getSelfIntroduction() {
            return selfIntroduction;
        }

        public void setSelfIntroduction(Object selfIntroduction) {
            this.selfIntroduction = selfIntroduction;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Object getUsername() {
            return username;
        }

        public void setUsername(Object username) {
            this.username = username;
        }

        public Long getPid() {
            return pid;
        }

        public void setPid(Long pid) {
            this.pid = pid;
        }
    }
}

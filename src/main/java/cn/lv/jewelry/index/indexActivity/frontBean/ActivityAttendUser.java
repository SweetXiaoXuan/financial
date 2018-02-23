package cn.lv.jewelry.index.indexActivity.frontBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/15 0015.
 */
public class ActivityAttendUser {
    private Boolean hasNext;
    private Integer totalPages;
    private String headPic;
    private String coverImage;
    private String registerEndTime;
    private List<Users> users;

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public class Users {
        private Long uid;
        private Long pid;
        private String headPic;
        private String username;
        private String selfIntroduction;

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getSelfIntroduction() {
            return selfIntroduction;
        }

        public void setSelfIntroduction(String selfIntroduction) {
            this.selfIntroduction = selfIntroduction;
        }

        public Long getPid() {
            return pid;
        }

        public void setPid(Long pid) {
            this.pid = pid;
        }
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

    public String getRegisterEndTime() {
        return registerEndTime;
    }

    public void setRegisterEndTime(String registerEndTime) {
        this.registerEndTime = registerEndTime;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}

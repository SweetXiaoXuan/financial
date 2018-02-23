package cn.com.ql.wiseBeijing.user.frontBean;

import java.util.List;
import java.util.Map;

/**
 * 查询用户下 报名 参与 关注 发布的活动bean
 * @author liumengwei
 * @Date 2017/9/24
 */
public class  FUserActivitiesBean<T> {
    // 是否还有数据
    private Boolean hasNext;
    // 总页码
    private Long totalPages;
    // 活动相关数据集合
    private List<T> activities;

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<T> getActivities() {
        return activities;
    }

    public void setActivities(List<T> activities) {
        this.activities = activities;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public class Activities {
        // 活动id
        private Long aid;
        // 发布者权限id
        private Long pid;
        // 活动主题
        private String subject;
        // 活动首图
        private String indexPic;
        // 活动头图
        private String headPic;
        // 地图图片
        private String mapPic;
        // 活动状态
        private Integer status;
        // 用户id
        private Long uid;
        // 关注人数
        private Integer focusNum;
        private Integer checkStatus;
        // 报名人数
        private Integer attenceNum;
        // 标识：圈子或者活动
        private Integer groupOrActivity;
        // 用户对活动的状态：参与 报名 关注 入围 发布
        private Integer userActivityStatus;
        // 未读消息数
        private Integer infoNum;
        private List<Map<String, Object>> organizers;
        private String insertTime;
        private String beginDateTime;
        private String endDateTime;
        private String registerEndTime;
        private Integer prepassedNum;
        private Integer activityNum;

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

        public String getIndexPic() {
            return indexPic;
        }

        public void setIndexPic(String indexPic) {
            this.indexPic = indexPic;
        }

        public Integer getFocusNum() {
            return focusNum;
        }

        public void setFocusNum(Integer focusNum) {
            this.focusNum = focusNum;
        }

        public Integer getAttenceNum() {
            return attenceNum;
        }

        public void setAttenceNum(Integer attenceNum) {
            this.attenceNum = attenceNum;
        }

        public Integer getGroupOrActivity() {
            return groupOrActivity;
        }

        public void setGroupOrActivity(Integer groupOrActivity) {
            this.groupOrActivity = groupOrActivity;
        }

        public Integer getUserActivityStatus() {
            return userActivityStatus;
        }

        public void setUserActivityStatus(Integer userActivityStatus) {
            this.userActivityStatus = userActivityStatus;
        }

        public Integer getInfoNum() {
            return infoNum;
        }

        public void setInfoNum(Integer infoNum) {
            this.infoNum = infoNum;
        }

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<Map<String, Object>> getOrganizers() {
            return organizers;
        }

        public void setOrganizers(List<Map<String, Object>> organizers) {
            this.organizers = organizers;
        }

        public String getInsertTime() {
            return insertTime;
        }

        public void setInsertTime(String insertTime) {
            this.insertTime = insertTime;
        }

        public String getMapPic() {
            return mapPic;
        }

        public void setMapPic(String mapPic) {
            this.mapPic = mapPic;
        }

        public String getBeginDateTime() {
            return beginDateTime;
        }

        public void setBeginDateTime(String beginDateTime) {
            this.beginDateTime = beginDateTime;
        }

        public String getEndDateTime() {
            return endDateTime;
        }

        public void setEndDateTime(String endDateTime) {
            this.endDateTime = endDateTime;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public Long getPid() {
            return pid;
        }

        public void setPid(Long pid) {
            this.pid = pid;
        }

        public Integer getActivityNum() {
            return activityNum;
        }

        public void setActivityNum(Integer activityNum) {
            this.activityNum = activityNum;
        }

        public Integer getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(Integer checkStatus) {
            this.checkStatus = checkStatus;
        }

        public String getRegisterEndTime() {
            return registerEndTime;
        }

        public void setRegisterEndTime(String registerEndTime) {
            this.registerEndTime = registerEndTime;
        }

        public Integer getPrepassedNum() {
            return prepassedNum;
        }

        public void setPrepassedNum(Integer prepassedNum) {
            this.prepassedNum = prepassedNum;
        }
    }
}
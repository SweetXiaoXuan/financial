package cn.lv.jewelry.activity.daoBean;

import javax.persistence.*;

/**
 * Created by lixing on 16/3/19.
 */
@Table(name = "activity")
@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    // 发布者权限id
    @Column(name = "privilege_id")
    private Long privilegeId;
    private Integer draft;
    private String subject;
    private String organizer;
    @Column(name = "start_time")
    private long startTime;
    // 首页图片是否显示 0不显示  1显示
    @Column(name = "index_pic_is_show")
    private Integer indexPicIsShow;
    // 审核时间
    @Column(name = "review_time")
    private Long reviewTime;
    @Column(name="end_time")
    private long endTime;
    @Column(name="insert_time")
    private long insertTime;
    @Column(name="update_time")
    private long updateTime;
    @Column(name="video_image_index")
    private String videoImageIndex = "";
    @Column(name="video_image_logo")
    private String videoImageLogo = "";
    private String address;
    private int level;
    @Column(name = "activity_number")
    private int activityNum;
    @Column(name = "map_pic")
    private String mapPic;
    private String title;
    @Column(name="index_pic")
    private String indexPic;
    @Column(name="logo_pic")
    private String logoPic;
    private String description;
    private int status;
    private String extend;
    @Column(name = "e_slogan")
    private String eslogan;
    @Column(name="e_description")
    private String edescription;
    // 是有那个活动或者圈子分裂出来的id
    private Long pid;
    @Column(name="a_type")
    private Integer aType;
    @Column(name="e_name")
    private String ename;
    @Column(name="register_end_time")
    private String registerEndTime;
    private Double fee;
    @Column(name="up_time")
    private String upTime;
    @Column(name="check_status")
    private Integer checkStatus;
    // 推首页
    @Column(name="push_index_page")
    private Integer pushIndexPage;
    // 推分栏
    @Column(name="push_columns_page")
    private Integer pushColumnsPage;
    // 置顶分栏
    @Column(name="up_time_columns")
    private String upTimeColumns;
    // 删除标记
    @Column(name="is_remove")
    private Integer isRemove;
    // html页面地址
    @Column(name="html_address")
    private String htmlAddress;
    private String telephone;
    private String email;
    private String mobile;

    public String getRegisterEndTime() {
        return registerEndTime;
    }

    public void setRegisterEndTime(String registerEndTime) {
        this.registerEndTime = registerEndTime;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getEslogan() {
        return eslogan;
    }

    public void setEslogan(String eslogan) {
        this.eslogan = eslogan;
    }

    public String getEdescription() {
        return edescription;
    }

    public void setEdescription(String edescription) {
        this.edescription = edescription;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getActivityNum() {
        return activityNum;
    }

    public void setActivityNum(int activityNum) {
        this.activityNum = activityNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIndexPic() {
        return indexPic;
    }

    public void setIndexPic(String indexPic) {
        this.indexPic = indexPic;
    }

    public String getLogoPic() {
        return logoPic;
    }

    public void setLogoPic(String logoPic) {
        this.logoPic = logoPic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public long getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(long insertTime) {
        this.insertTime = insertTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getDraft() {
        return draft;
    }

    public void setDraft(Integer draft) {
        this.draft = draft;
    }

    public Integer getPushIndexPage() {
        return pushIndexPage;
    }

    public void setPushIndexPage(Integer pushIndexPage) {
        this.pushIndexPage = pushIndexPage;
    }

    public String getUpTimeColumns() {
        return upTimeColumns;
    }

    public void setUpTimeColumns(String upTimeColumns) {
        this.upTimeColumns = upTimeColumns;
    }

    public Integer getPushColumnsPage() {
        return pushColumnsPage;
    }

    public void setPushColumnsPage(Integer pushColumnsPage) {
        this.pushColumnsPage = pushColumnsPage;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getaType() {
        return aType;
    }

    public void setaType(Integer aType) {
        this.aType = aType;
    }

    public String getMapPic() {
        return mapPic;
    }

    public void setMapPic(String mapPic) {
        this.mapPic = mapPic;
    }

    public Integer getIsRemove() {
        return isRemove;
    }

    public void setIsRemove(Integer isRemove) {
        this.isRemove = isRemove;
    }

    public Integer getIndexPicIsShow() {
        return indexPicIsShow;
    }

    public void setIndexPicIsShow(Integer indexPicIsShow) {
        this.indexPicIsShow = indexPicIsShow;
    }

    public Long getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Long reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Long getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
        this.privilegeId = privilegeId;
    }

    public String getHtmlAddress() {
        return htmlAddress;
    }

    public void setHtmlAddress(String htmlAddress) {
        this.htmlAddress = htmlAddress;
    }

    public String getVideoImageLogo() {
        return videoImageLogo;
    }

    public void setVideoImageLogo(String videoImageLogo) {
        this.videoImageLogo = videoImageLogo;
    }

    public String getVideoImageIndex() {
        return videoImageIndex;
    }

    public void setVideoImageIndex(String videoImageIndex) {
        this.videoImageIndex = videoImageIndex;
    }
}

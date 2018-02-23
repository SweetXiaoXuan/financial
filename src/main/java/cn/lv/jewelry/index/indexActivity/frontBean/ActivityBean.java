package cn.lv.jewelry.index.indexActivity.frontBean;

import cn.com.ql.wiseBeijing.user.daoBean.User;
import cn.lv.jewelry.activity.daoBean.ActivityStatusType;

import javax.ws.rs.FormParam;
import java.util.List;
import java.util.Map;

public class ActivityBean {
	@FormParam("id")
	private String id;
	@FormParam("pid")
	private Long pid;
	private Long aid;
	private Integer checkStatus;
	private Integer focusNum;
	@FormParam("headPic")
	private String headPic;
	@FormParam("indexPic")
	private String indexPic;
	@FormParam("name")
	private String name;
	@FormParam("videoImage")
	private String videoImage;
	@FormParam("videoImageIndex")
	private String videoImageIndex = "";
	@FormParam("videoImageLogo")
	private String videoImageLogo;
	@FormParam("rid")
	private String rid;
	@FormParam("mapPic")
	private String mapPic;
	@FormParam("draft")
	private Integer draft;
	private Integer level;
    @FormParam("subject")
	private String subject;
	@FormParam("organizer")
	private String organizer;
	@FormParam("address")
	private String address;
	private Integer number;
	@FormParam("beginDateTime")
	private String beginDateTime;
	@FormParam("endDateTime")
	private String endDateTime;
	@FormParam("description")
	private String description;
	@FormParam("status")
	private Integer status = ActivityStatusType.UPCOMING.getV();
	@FormParam("registerEndTime")
	private String registerEndTime;
	private Integer readersNum;
	@FormParam("telephone")
	private String telephone;
	@FormParam("email")
	private String email;
	private Integer attenceNum;
	private Integer attenceNumber;
	private Integer prepassedNum;
	@FormParam("mobile")
	private String mobile;
	@FormParam("upTime")
	private String upTime;
	@FormParam("fee")
	private Double fee;
	private String insertTime;
	private Integer isSignUp;
	private Integer pushIndexPage;
	private Integer isAttention;
	private List<NodeBeanImage> relativeImages;
	private List<ActivityReader> readers;
	private List<Map<String, Object>> users;
	private List<User> listUser;
	private List<ActivityBean> activityBeanList;
	private Integer messageNumber;
	private String updateTime;
	@FormParam("platformPhone")
	private String platformPhone;
	private Integer isLogin;
	private Boolean hasNext;
	@FormParam("activityNum")
	private Integer activityNum;
	private String upTimeColumns;
	private Integer pushColumnsPage;
	// 主办方信息(用户、企业)
	private List<Map<String, Object>> organizers;
	// 首页图片是否显示 0不显示  1显示
	@FormParam("indexPicIsShow")
	private Integer indexPicIsShow;
	// 圈子下相关活动数量：次数
	private Integer frequency;
	// html页面地址
	private String htmlAddress;

	public String getRegisterEndTime() {
		return registerEndTime;
	}

	public void setRegisterEndTime(String registerEndTime) {
		this.registerEndTime = registerEndTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getReadersNum() {
		return readersNum;
	}
	public void setReadersNum(Integer readersNum) {
		this.readersNum = readersNum;
	}
	public List<NodeBeanImage> getRelativeImages() {
		return relativeImages;
	}
	public void setRelativeImages(List<NodeBeanImage> relativeImages) {
		this.relativeImages = relativeImages;
	}
	public List<ActivityReader> getReaders() {
		return readers;
	}
	public void setReaders(List<ActivityReader> readers) {
		this.readers = readers;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Map<String, Object>> getOrganizers() {
		return organizers;
	}

	public void setOrganizers(List<Map<String, Object>> organizers) {
		this.organizers = organizers;
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

	public String getPlatformPhone() {
		return platformPhone;
	}

	public void setPlatformPhone(String platformPhone) {
		this.platformPhone = platformPhone;
	}

	public List<Map<String, Object>> getUsers() {
		return users;
	}

	public void setUsers(List<Map<String, Object>> users) {
		this.users = users;
	}

	public List<User> getListUser() {
		return listUser;
	}

	public void setListUser(List<User> listUser) {
		this.listUser = listUser;
	}

	public Integer getMessageNumber() {
		return messageNumber;
	}

	public void setMessageNumber(Integer messageNumber) {
		this.messageNumber = messageNumber;
	}

	public Boolean getHasNext() {
		return hasNext;
	}

	public void setHasNext(Boolean hasNext) {
		this.hasNext = hasNext;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	public Integer getPushIndexPage() {
		return pushIndexPage;
	}

	public void setPushIndexPage(Integer pushIndexPage) {
		this.pushIndexPage = pushIndexPage;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public List<ActivityBean> getActivityBeanList() {
		return activityBeanList;
	}

	public void setActivityBeanList(List<ActivityBean> activityBeanList) {
		this.activityBeanList = activityBeanList;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getActivityNum() {
		return activityNum;
	}

	public void setActivityNum(Integer activityNum) {
		this.activityNum = activityNum;
	}

	public Integer getPushColumnsPage() {
		return pushColumnsPage;
	}

	public void setPushColumnsPage(Integer pushColumnsPage) {
		this.pushColumnsPage = pushColumnsPage;
	}

	public String getUpTimeColumns() {
		return upTimeColumns;
	}

	public void setUpTimeColumns(String upTimeColumns) {
		this.upTimeColumns = upTimeColumns;
	}

	public String getMapPic() {
		return mapPic;
	}

	public void setMapPic(String mapPic) {
		this.mapPic = mapPic;
	}

	public Integer getIndexPicIsShow() {
		return indexPicIsShow;
	}

	public void setIndexPicIsShow(Integer indexPicIsShow) {
		this.indexPicIsShow = indexPicIsShow;
	}

	public Integer getPrepassedNum() {
		return prepassedNum;
	}

	public void setPrepassedNum(Integer prepassedNum) {
		this.prepassedNum = prepassedNum;
	}

	public Integer getDraft() {
		return draft;
	}

	public void setDraft(Integer draft) {
		this.draft = draft;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public Integer getFocusNum() {
		return focusNum;
	}

	public void setFocusNum(Integer focusNum) {
		this.focusNum = focusNum;
	}

	public String getVideoImage() {
		return videoImage;
	}

	public void setVideoImage(String videoImage) {
		this.videoImage = videoImage;
	}

	public Integer getIsAttention() {
		return isAttention;
	}

	public void setIsAttention(Integer isAttention) {
		this.isAttention = isAttention;
	}

	public Integer getIsSignUp() {
		return isSignUp;
	}

	public void setIsSignUp(Integer isSignUp) {
		this.isSignUp = isSignUp;
	}

	public String getIndexPic() {
		return indexPic;
	}

	public void setIndexPic(String indexPic) {
		this.indexPic = indexPic;
	}

	public Long getAid() {
		return aid;
	}

	public void setAid(Long aid) {
		this.aid = aid;
	}

	public Integer getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(Integer isLogin) {
		this.isLogin = isLogin;
	}

	public Integer getAttenceNum() {
		return attenceNum;
	}

	public void setAttenceNum(Integer attenceNum) {
		this.attenceNum = attenceNum;
	}

	public Integer getAttenceNumber() {
		return attenceNumber;
	}

	public void setAttenceNumber(Integer attenceNumber) {
		this.attenceNumber = attenceNumber;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHtmlAddress() {
		return htmlAddress;
	}

	public void setHtmlAddress(String htmlAddress) {
		this.htmlAddress = htmlAddress;
	}

	public String getVideoImageIndex() {
		return videoImageIndex;
	}

	public void setVideoImageIndex(String videoImageIndex) {
		this.videoImageIndex = videoImageIndex;
	}

	public String getVideoImageLogo() {
		return videoImageLogo;
	}

	public void setVideoImageLogo(String videoImageLogo) {
		this.videoImageLogo = videoImageLogo;
	}
}

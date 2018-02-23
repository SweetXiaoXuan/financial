package cn.lv.jewelry.index.indexActivity.frontBean;

import javax.ws.rs.FormParam;

/**
 * 移动端活动发布前端bean
 */
public class MobileActivityBean {
	@FormParam("headPic")
	private String headPic;
	private Long aid;
	@FormParam("uid")
	private String uid;
	@FormParam("videoImageIndex")
	private String videoImageIndex = "";
	@FormParam("videoImageLogo")
	private String videoImageLogo = "";
	//用户类型
	@FormParam("userType")
	private Integer userType;
	//头像
	@FormParam("indexPic")
	private String indexPic;
	//地图图片
	@FormParam("mapPic")
	private String mapPic;
	//活动主题
	@FormParam("subject")
	private String subject;
	//活动标题
	@FormParam("description")
	private String description;
	//选择相关性
	@FormParam("rid")
	private String rid;
	//活动类型
	@FormParam("aType")
	private Integer aType;
	//协办方
	@FormParam("organizer")
	private String organizer;
	//活动时间(包括开始时间和结束时间)
	@FormParam("beginDateTime")
	private String beginDateTime;
	@FormParam("endDateTime")
	private String endDateTime;
	//活动地点
	@FormParam("address")
	private String address;
	//人数限制
	@FormParam("activityNum")
	private Integer activityNum;
	//活动收费
	@FormParam("fee")
	private Double fee;
	//报名截止时间
	@FormParam("registerEndTime")
	private String registerEndTime;
	//主办方联系电话
	@FormParam("telephone")
	private String telephone;
	//主办方联系邮箱
	@FormParam("email")
	private String email;
	//客服电话
	@FormParam("mobile")
	private String mobile;
	//发布内容(html字符串)
	@FormParam("content")
	private String content;
	// 是否存为草稿
	@FormParam("draft")
	private Integer draft;
	// 首页图片是否显示 0不显示  1显示
	@FormParam("indexPicIsShow")
	private Integer indexPicIsShow;
	@FormParam("privilegeId")
	private Long privilegeId;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIndexPic() {
		return indexPic;
	}

	public void setIndexPic(String indexPic) {
		this.indexPic = indexPic;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
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

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
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

	public String getRegisterEndTime() {
		return registerEndTime;
	}

	public void setRegisterEndTime(String registerEndTime) {
		this.registerEndTime = registerEndTime;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getDraft() {
		return draft;
	}

	public void setDraft(Integer draft) {
		this.draft = draft;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Long getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(Long privilegeId) {
		this.privilegeId = privilegeId;
	}

	public Integer getaType() {
		return aType;
	}

	public void setaType(Integer aType) {
		this.aType = aType;
	}

	public Integer getActivityNum() {
		return activityNum;
	}

	public void setActivityNum(Integer activityNum) {
		this.activityNum = activityNum;
	}

	public Long getAid() {
		return aid;
	}

	public void setAid(Long aid) {
		this.aid = aid;
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

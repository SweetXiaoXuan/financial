package cn.com.ql.wiseBeijing.user.frontBean;

import javax.ws.rs.FormParam;

/**
 * 用户相关参数Bean
 * @author liumengwei
 * @since 2018/1/11
 */
public class FNewUserBean {
    // 用户id
    @FormParam("uid")
    private String uid;
    // 性别
    @FormParam("strGender")
    private String strGender;
    @FormParam("gender")
    private Integer gender;
    // 证件类型 0身份证
    @FormParam("type")
    private Integer type;
    // 手机号
    @FormParam("phone")
    private String phone;
    // 用户名
    @FormParam("username")
    private String username;
    // 密码
    @FormParam("password")
    private String password;
    // 头像
    @FormParam("headPic")
    private String headPic;
    // 身份证号码
    @FormParam("idNumber")
    private String idNumber;
    // 姓名
    @FormParam("givename")
    private String givename;
    // 自我介绍
    @FormParam("selfIntroduction")
    private String selfIntroduction;
    @FormParam("pid")
    private Long pid;
    @FormParam("userType")
    private String userType;
    @FormParam("verified")
    private Boolean verified;
    @FormParam("coverImage")
    private String coverImage;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getGivename() {
        return givename;
    }

    public void setGivename(String givename) {
        this.givename = givename;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getStrGender() {
        return strGender;
    }

    public void setStrGender(String strGender) {
        this.strGender = strGender;
    }
}

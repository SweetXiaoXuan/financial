package cn.lv.jewelry.index.indexBrand.frontBean;

import javax.ws.rs.FormParam;

/**
 * Created by 24593 on 2018/2/7.
 */
public class FBrandBean {
    @FormParam("email")
    private String email;
    @FormParam("phone")
    private String phone;
    @FormParam("telephone")
    private String telephone;
    @FormParam("address")
    private String address;
    // 品牌名称
    @FormParam("brandName")
    private String brandName;
    @FormParam("description")
    private String description;
    // 联系人
    @FormParam("contact")
    private String contact;
    @FormParam("givename")
    private String givename;
    @FormParam("idNumber")
    private String idNumber;
    // 品牌图片，(类似企业logo)
    @FormParam("brandPic")
    private String brandPic;
    // 公司名称
    @FormParam("companyName")
    private String companyName;
    // 营业执照
    @FormParam("businessLicensePic")
    private String businessLicensePic;
    // 身份证正面
    @FormParam("idCardFront")
    private String idCardFront;
    // 身份证反面
    @FormParam("idCardBack")
    private String idCardBack;
    // 手持身份证
    @FormParam("idCardInHand")
    private String idCardInHand;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public String getBrandPic() {
        return brandPic;
    }

    public void setBrandPic(String brandPic) {
        this.brandPic = brandPic;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessLicensePic() {
        return businessLicensePic;
    }

    public void setBusinessLicensePic(String businessLicensePic) {
        this.businessLicensePic = businessLicensePic;
    }

    public String getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
    }

    public String getIdCardBack() {
        return idCardBack;
    }

    public void setIdCardBack(String idCardBack) {
        this.idCardBack = idCardBack;
    }

    public String getIdCardInHand() {
        return idCardInHand;
    }

    public void setIdCardInHand(String idCardInHand) {
        this.idCardInHand = idCardInHand;
    }
}

package cn.lv.jewelry.index.indexDesigner.frontBean;

import cn.lv.jewelry.index.indexProduct.frontBean.BrandItemBean;
import cn.lv.jewelry.index.indexProduct.frontBean.ProductItemBean;

import java.util.List;

/**
 * Created by lixing on 16/4/7.
 */
public class DesignerItemBean {
    private String id;
    private String name;
    private String avatar_url;
    private String nation;
    private BrandItemBean brandItemBean;
    private String story;
    private String style;
    private String verify_info;
    private String description;
    private String status;


    private List<ProductItemBean> products;
    private List<BrandItemBean> brands;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getVerify_info() {
        return verify_info;
    }

    public void setVerify_info(String verify_info) {
        this.verify_info = verify_info;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BrandItemBean getBrandItemBean() {
        return brandItemBean;
    }

    public void setBrandItemBean(BrandItemBean brandItemBean) {
        this.brandItemBean = brandItemBean;
    }

    public List<ProductItemBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductItemBean> products) {
        this.products = products;
    }

    public List<BrandItemBean> getBrands() {
        return brands;
    }

    public void setBrands(List<BrandItemBean> brands) {
        this.brands = brands;
    }
}

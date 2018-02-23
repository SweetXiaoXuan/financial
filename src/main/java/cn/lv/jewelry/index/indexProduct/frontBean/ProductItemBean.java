package cn.lv.jewelry.index.indexProduct.frontBean;

import cn.lv.jewelry.index.indexActivity.frontBean.ActivityBean;
import cn.lv.jewelry.index.indexActivity.frontBean.NodeBeanVideo;
import cn.lv.jewelry.index.indexDesigner.frontBean.DesignerItemBean;
import cn.lv.jewelry.product.daoBean.*;

import javax.ws.rs.FormParam;
import java.util.List;

/**
 * Created by lixing on 16/4/7.
 */
public class ProductItemBean {
    private String id;
    @FormParam("name")
    private String name;
    @FormParam("type")
    private String type;
    @FormParam("coverUrl")
    private String coverUrl;
    @FormParam("material")
    private String material;
    private ProductMaterialItemBean productMaterial;
    @FormParam("category")
    private String category;
    private ProductCategoryItemBean productCategory;
    @FormParam("brand")
    private String brand;
    // TODO 以下方法是之前的，用到了brand表，2018-2-9号修改表设计需注释掉该代码
//    private BrandItemBean brandObject;
    @FormParam("origin")
    private String origin;
    private ProductOriginItemBean productOrigin;
    @FormParam("embed")
    private String embed;
    private ProductEmbedItemBean productEmbed;
    @FormParam("style")
    private String style;
    private ProductStyleItemBean productStyle;
    @FormParam("length")
    private String length;
    @FormParam("size")
    private String size;
    @FormParam("description")
    private String description;
    @FormParam("price")
    private String price;
    @FormParam("postscript")
    private String postscript;
    @FormParam("isRent")
    private String isRent;
    @FormParam("status")
    private String status;
    @FormParam("tags")
    private String[] tags;
    @FormParam("designer")
    private String designer = "1";
    private List<DesignerItemBean> productDesigners;
    private List<ProductItemBean> relativeProductItemBeans;
    private List<ProductImageBean> productImageBeans;

    private List<NodeBeanVideo> productVideos;

    private List<ActivityBean> productActivityBeans;

    public List<ActivityBean> getProductActivityBeans() {
        return productActivityBeans;
    }

    public void setProductActivityBeans(List<ActivityBean> productActivityBeans) {
        this.productActivityBeans = productActivityBeans;
    }


    public void setProductCategory(ProductCategoryItemBean productCategory) {
        this.productCategory = productCategory;
    }

    // TODO 以下方法是之前的，用到了brand表，2018-2-9号修改表设计需注释掉该代码
//    public void setBrandObject(BrandItemBean brandObject) {
//        this.brandObject = brandObject;
//    }

    public void setProductOrigin(ProductOriginItemBean productOrigin) {
        this.productOrigin = productOrigin;
    }

     public void setProductEmbed(ProductEmbedItemBean productEmbed) {
        this.productEmbed = productEmbed;
    }

    public void setProductStyle(ProductStyleItemBean productStyle) {
        this.productStyle = productStyle;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public void setProductMaterial(ProductMaterialItemBean productMaterial) {

        this.productMaterial = productMaterial;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getEmbed() {
        return embed;
    }

    public void setEmbed(String embed) {
        this.embed = embed;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPostscript() {
        return postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getIsRent() {
        return isRent;
    }

    public void setIsRent(String isRent) {
        this.isRent = isRent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ProductMaterialItemBean getProductMaterial() {
        return productMaterial;
    }

    public void setProductMaterial(ProductMaterial productMaterial) {
        this.productMaterial = new ProductMaterialItemBean(productMaterial);
    }

    public ProductCategoryItemBean getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = new ProductCategoryItemBean(productCategory);
    }

    // TODO 以下方法是之前的，用到了brand表，2018-2-9号修改表设计需注释掉该代码
//    public BrandItemBean getBrandObject() {
//        return brandObject;
//    }

    // TODO 以下方法是之前的，用到了brand表，2018-2-9号修改表设计需注释掉该代码
//    public void setBrandObject(Brand brandObject) {
//
//        this.brandObject = new BrandItemBean(brandObject);
//    }

    public ProductOriginItemBean getProductOrigin() {
        return productOrigin;
    }

    public void setProductOrigin(ProductOrigin productOrigin) {
        this.productOrigin = new ProductOriginItemBean(productOrigin);
    }

    public ProductEmbedItemBean getProductEmbed() {
        return productEmbed;
    }

    public void setProductEmbed(ProductEmbed productEmbed) {
        this.productEmbed = new ProductEmbedItemBean(productEmbed);
    }

    public ProductStyleItemBean getProductStyle() {
        return productStyle;
    }

    public void setProductStyle(ProductStyle productStyle) {
        this.productStyle = new ProductStyleItemBean(productStyle);
    }

    public List<DesignerItemBean> getProductDesigners() {
        return productDesigners;
    }

    public void setProductDesigners(List<DesignerItemBean> productDesigners) {
        this.productDesigners = productDesigners;
    }



    public List<ProductImageBean> getProductImageBeans() {
        return productImageBeans;
    }

    public void setProductImageBeans(List<ProductImageBean> productImageBeans) {
        this.productImageBeans = productImageBeans;
    }

    public List<ProductItemBean> getRelativeProductItemBeans() {
        return relativeProductItemBeans;
    }

    public void setRelativeProductItemBeans(List<ProductItemBean> relativeProductItemBeans) {
        this.relativeProductItemBeans = relativeProductItemBeans;
    }

    public List<NodeBeanVideo> getProductVideos() {
        return productVideos;
    }

    public void setProductVideos(List<NodeBeanVideo> productVideos) {
        this.productVideos = productVideos;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}

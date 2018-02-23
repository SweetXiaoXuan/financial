package cn.lv.jewelry.index.indexProduct.frontBean;

import cn.lv.jewelry.index.indexActivity.frontBean.NodeBeanVideo;

import javax.ws.rs.FormParam;
import java.util.List;

/**
 * Created by lixing on 16/4/7.
 */
public class BrandItemBean {

    private String id;
    @FormParam("name")
    private String name;
    @FormParam("type")
    private String type;
    @FormParam("description")
    private String description;
    @FormParam("logo")
    private String logo;
    @FormParam("status")
    private int status;

    private List<NodeBeanVideo> brandVideos;
    private List<ProductItemBean> productItemBeans;

    public BrandItemBean()
    {

    }

    // TODO 以下方法是之前的，用到了brand表，2018-2-9号修改表设计需注释掉该代码
//    public BrandItemBean(Brand brandObject)
//    {
//        setType(String.valueOf(brandObject.getType()));
//        setId(String.valueOf(brandObject.getId()));
//        setDescription(brandObject.getDescription());
//        setLogo(brandObject.getLogo());
//        setName(brandObject.getName());
//        setBrandVideos(getNodeBeanVideos(brandObject.getBrandVideos()));
//    }

    private List<ProductBean> products;

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

    public List<ProductBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductBean> products) {
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    // TODO 以下方法是之前的，用到了brand表，2018-2-9号修改表设计需注释掉该代码
//    public List<NodeBeanVideo> getNodeBeanVideos(List<BrandVideo> brandVideos) {
//        List<NodeBeanVideo> nodeBeanVideos = new ArrayList<>();
//        if(null == brandVideos || brandVideos.size() < 1){
//            return nodeBeanVideos;
//        }
//        NodeBeanVideo nodeBeanVideo = null;
//        for(BrandVideo brandVideo:brandVideos) {
//            nodeBeanVideo = new NodeBeanVideo();
////            nodeBeanVideo.setDescription(brandVideo.get);
//            nodeBeanVideo.setId(String.valueOf(brandVideo.getId()));
//            nodeBeanVideo.setImg(brandVideo.getImg());
//            nodeBeanVideo.setPath(brandVideo.getVideo());
//            nodeBeanVideo.setStatus(String.valueOf(brandVideo.getStatus()));
//
//            nodeBeanVideos.add(nodeBeanVideo);
//        }
//        return nodeBeanVideos;
//    }



    public List<NodeBeanVideo> getBrandVideos() {
        return brandVideos;
    }

    public void setBrandVideos(List<NodeBeanVideo> brandVideos) {
        this.brandVideos = brandVideos;
    }

    public List<ProductItemBean> getProductItemBeans() {
        return productItemBeans;
    }

    public void setProductItemBeans(List<ProductItemBean> productItemBeans) {
        this.productItemBeans = productItemBeans;
    }
}

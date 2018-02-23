package cn.lv.jewelry.index.indexProduct.frontBean;

import cn.lv.jewelry.product.daoBean.ProductOriginItemBean;

import java.util.List;

/**
 * Created by lixing on 16/4/7.
 */
public class ProductOriginBean {

    private List<ProductOriginItemBean> productOriginBeans;


    public List<ProductOriginItemBean> getProductOriginBeans() {
        return productOriginBeans;
    }

    public void setProductOriginBeans(List<ProductOriginItemBean> productOriginBeans) {
        this.productOriginBeans = productOriginBeans;
    }
}

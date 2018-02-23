package cn.lv.jewelry.index.indexProduct.frontBean;

import cn.lv.jewelry.product.daoBean.ProductStyleItemBean;

import java.util.List;

/**
 * Created by lixing on 16/4/7.
 */
public class ProductStyleBean {

    private List<ProductStyleItemBean> productStyleBeans;


    public List<ProductStyleItemBean> getProductStyleBeans() {
        return productStyleBeans;
    }

    public void setProductStyleBeans(List<ProductStyleItemBean> productStyleBeans) {
        this.productStyleBeans = productStyleBeans;
    }
}

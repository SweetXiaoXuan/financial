package cn.lv.jewelry.index.indexProduct.frontBean;

import cn.lv.jewelry.product.daoBean.ProductEmbedItemBean;

import java.util.List;

/**
 * Created by lixing on 16/4/7.
 */
public class ProductEmbedBean {

    private List<ProductEmbedItemBean> productEmbedItemBeans;


    public List<ProductEmbedItemBean> getProductEmbedItemBeans() {
        return productEmbedItemBeans;
    }

    public void setProductEmbedItemBeans(List<ProductEmbedItemBean> productEmbedItemBeans) {
        this.productEmbedItemBeans = productEmbedItemBeans;
    }
}

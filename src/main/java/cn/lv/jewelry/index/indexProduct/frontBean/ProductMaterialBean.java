package cn.lv.jewelry.index.indexProduct.frontBean;

import cn.lv.jewelry.product.daoBean.ProductMaterialItemBean;

import java.util.List;

/**
 * Created by lixing on 16/4/7.
 */
public class ProductMaterialBean {

    private List<ProductMaterialItemBean> productMaterialItemBeans;


    public List<ProductMaterialItemBean> getProductMaterialItemBeans() {
        return productMaterialItemBeans;
    }

    public void setProductMaterialItemBeans(List<ProductMaterialItemBean> productMaterialItemBeans) {
        this.productMaterialItemBeans = productMaterialItemBeans;
    }
}

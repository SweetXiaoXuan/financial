package cn.lv.jewelry.index.indexProduct.frontBean;

import java.util.List;

/**
 * Created by lixing on 16/4/8.
 * 用来放精品首页跑马灯元素
 */
public class MarqueeBean<T> {

    private List<MarqueeItemBean> carousels;


    public List<MarqueeItemBean> getCarousels() {
        return carousels;
    }

    public void setCarousels(List<MarqueeItemBean> carousels) {
        this.carousels = carousels;
    }
}

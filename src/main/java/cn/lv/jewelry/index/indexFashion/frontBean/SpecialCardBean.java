package cn.lv.jewelry.index.indexFashion.frontBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lixing on 16/4/7.
 */
public class SpecialCardBean implements Serializable{
    private List<SpecialCardItemBean> specialCardItemBeans;

    public List<SpecialCardItemBean> getSpecialCardItemBeans() {
        return specialCardItemBeans;
    }

    public void setSpecialCardItemBeans(List<SpecialCardItemBean> specialCardItemBeans) {
        this.specialCardItemBeans = specialCardItemBeans;
    }
}

package cn.lv.jewelry.designer.service;

import javax.annotation.Resource;

import cn.lv.jewelry.designer.daoBean.Designer;
import cn.lv.jewelry.index.indexDesigner.frontBean.DesignerItemBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.lv.jewelry.designer.dao.DesignerDao;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class DesignerService {

	@Resource(name = "designerDao")
    private DesignerDao designerDao;

    public static List<DesignerItemBean> getDesignerItemBeans(List<Designer> designers){
        List<DesignerItemBean> designerItemBeans = new ArrayList<>();
        if(null == designers || designers.size() < 1){
            return designerItemBeans;
        }
        DesignerItemBean designerItemBean = null;
        for(Designer designer:designers){
            designerItemBean = new DesignerItemBean();
            designerItemBean.setName(designer.getName());
            designerItemBean.setStyle(designer.getStyle());
            designerItemBean.setStory(designer.getStory());
            designerItemBean.setStatus(String.valueOf(designer.getStatus()));
            designerItemBean.setAvatar_url(designer.getAvatar());
//            designerItemBean.setBrand(designer.getBrand());
            designerItemBean.setDescription(designer.getDescription());
            designerItemBean.setId(String.valueOf(designer.getId()));
            designerItemBean.setNation(designer.getNation());
            designerItemBean.setVerify_info(designer.getComposition());

            designerItemBeans.add(designerItemBean);
        }
        return designerItemBeans;
    }
}

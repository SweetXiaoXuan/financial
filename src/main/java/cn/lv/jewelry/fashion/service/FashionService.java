package cn.lv.jewelry.fashion.service;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.serviceUtil.PageMapUtil;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.lv.jewelry.activity.dao.LvTagDao;
import cn.lv.jewelry.activity.service.ActivityService;
import cn.lv.jewelry.fashion.dao.FashionDao;
import cn.lv.jewelry.fashion.dao.SpecialCardDao;
import cn.lv.jewelry.fashion.dao.SpecialDao;
import cn.lv.jewelry.fashion.daoBean.Fashion;
import cn.lv.jewelry.fashion.daoBean.Special;
import cn.lv.jewelry.fashion.daoBean.SpecialCard;
import cn.lv.jewelry.index.indexActivity.frontBean.*;
import cn.lv.jewelry.index.indexFashion.frontBean.FashionItemBean;
import cn.lv.jewelry.index.indexFashion.frontBean.SpecialCardBean;
import cn.lv.jewelry.index.indexFashion.frontBean.SpecialCardItemBean;
import cn.lv.jewelry.index.indexFashion.frontBean.SpecialItemBean;
import cn.lv.jewelry.product.service.ProductService;
import cn.xxtui.support.page.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class FashionService extends BasicDao<Special> {

    @Resource(name = "specialDao")
    private SpecialDao specialDao;
    @Resource(name = "specialCardDao")
    private SpecialCardDao specialCardDao;
    @Resource(name = "activityService")
    private ActivityService activityService;
    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "fashionDao")
    private FashionDao fashionDao;
    @Resource(name = "lvTagDao")
    private LvTagDao lvTagDao;

    /**
     * 保存专场基本信息
     *
     * @param specialItemBean
     * @return
     */
    public ReturnValue<SpecialItemBean> saveSpecial(SpecialItemBean specialItemBean) {
        ReturnValue<SpecialItemBean> rv = new ReturnValue<SpecialItemBean>();
        try {
            Special special = getSpeical(specialItemBean);
            Integer i = 0;

            if (specialDao.save(special) != i) {
                for(String tag:specialItemBean.getTags()){
                    lvTagDao.save(special.getId(), tag, 3);
                }
                specialItemBean.setId(String.valueOf(special.getId()));
                rv.setObject(specialItemBean);
                rv.setFlag(ReturnValue.FLAG_SUCCESS);
            }
        } catch (Exception ex) {
            rv.setFlag(ReturnValue.FLAG_EXCEPTION);
            ex.printStackTrace();
        }

        return rv;
    }

    private Special getSpeical(SpecialItemBean specialItemBean) {
        if (specialItemBean == null) {
            return null;
        }
        Special special = new Special();

        special.setBirthTime(specialItemBean.getBirthTime());
        special.setCoverUrl(specialItemBean.getCoverUrl());
        special.setDescription(specialItemBean.getDescription());
        special.setLid(Long.parseLong(specialItemBean.getLid()));
        special.setStatus(0);
        special.setSubject(specialItemBean.getSubject());

        return special;

    }

    /**
     * 插入专场卡片信息
     *
     * @param specialCardBean
     * @return
     */
    public ReturnValue<SpecialCardBean> saveSpecialCards(SpecialCardBean specialCardBean) {
        ReturnValue<SpecialCardBean> rv = new ReturnValue<SpecialCardBean>();
        try {
            List<SpecialCard> specialCards = getSpeicalCards(specialCardBean);
            Integer i = 0;
            for(SpecialCard specialCard : specialCards) {
                specialCardDao.save(specialCard);
            }
            rv.setObject(specialCardBean);
            rv.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception ex) {
            rv.setFlag(ReturnValue.FLAG_EXCEPTION);
            ex.printStackTrace();
        }

        return rv;
    }

    private List<SpecialCard> getSpeicalCards(SpecialCardBean specialCardBean) {
        List<SpecialCardItemBean> specialCardItemBeans = specialCardBean.getSpecialCardItemBeans();
        List<SpecialCard> specialCards = new ArrayList<SpecialCard>();
        for(SpecialCardItemBean specialCardItemBean : specialCardItemBeans) {
            specialCards.add(getSpecialCard(specialCardItemBean));
        }

        return specialCards;
    }

    private SpecialCard getSpecialCard(SpecialCardItemBean specialCardItemBean) {
        SpecialCard specialCard = new SpecialCard();
        //视频的地址存放在extend字段中
        specialCard.setExtend(specialCardItemBean.getPath());
        specialCard.setCardType(specialCardItemBean.getCardType());
        specialCard.setCoverUrl(specialCardItemBean.getCoverUrl());
        specialCard.setDescription(specialCardItemBean.getDescription());
        specialCard.setName(specialCardItemBean.getName());
        specialCard.setRid(specialCardItemBean.getRid());
        specialCard.setSid(specialCardItemBean.getSid());
        specialCard.setExtend("0");
        specialCard.setStatus(0);

        return specialCard;
    }

    /**
     * 通过专场id查询专场基础信息
     *
     * @param sid
     * @return
     */
    public ReturnValue<SpecialItemBean> querySpeical(String sid) {
        ReturnValue<SpecialItemBean> rv = new ReturnValue<SpecialItemBean>();
        try {
            long id = Long.parseLong(sid);
            Special special = specialDao.get(Special.class, id);
            SpecialItemBean specialItemBean = getSpeicalItemBean(special);
            rv.setObject(specialItemBean);
            rv.setFlag(ReturnValue.FLAG_SUCCESS);

        } catch (Exception e) {
            rv.setFlag(ReturnValue.FLAG_EXCEPTION);
            e.printStackTrace();
        }
        return rv;
    }

    /**
     * 查询所有专场基础信息
     *
     * @param
     * @return
     */
    public ReturnValue<Map> querySpeicals(int current) {
        ReturnValue<Map> rv = new ReturnValue<Map>();
        try {
            Page<Special> specials = specialDao.getList(current);
            Map<String,Object> map=PageMapUtil.getMap(specials);
            List<SpecialItemBean> specialItemBeans=new ArrayList<SpecialItemBean>();
            for(Special special:specials.getPageContent())
            {
                SpecialItemBean specialItemBean = getSpeicalItemBean(special);
                specialItemBeans.add(specialItemBean);
            }
            map.put("data",specialItemBeans);
            rv.setObject(map);
            rv.setFlag(ReturnValue.FLAG_SUCCESS);

        } catch (Exception e) {
            rv.setFlag(ReturnValue.FLAG_EXCEPTION);
            e.printStackTrace();
        }
        return rv;
    }
    private SpecialItemBean getSpeicalItemBean(Special special) {
        if (special == null) {
            return null;
        }
        SpecialItemBean specialItemBean = new SpecialItemBean();

        specialItemBean.setBirthTime(special.getBirthTime());
        specialItemBean.setCoverUrl(special.getCoverUrl());
        specialItemBean.setDescription(special.getDescription());
        specialItemBean.setLid(String.valueOf(special.getLid()));
        specialItemBean.setStatus(0);
        specialItemBean.setSubject(special.getSubject());
        specialItemBean.setId(String.valueOf(special.getId()));

        return specialItemBean;
    }

    /**
     * 通过专场信息查询专场卡片信息
     * @param sid
     * @return
     */
    public ReturnValue<SpecialCardBean> querySpeicalCards(String sid) {
        SpecialCardBean specialCardBean = new SpecialCardBean();
        ReturnValue<SpecialCardBean> rv = new ReturnValue<SpecialCardBean>();
        try {
            long id = Long.parseLong(sid);
            List<SpecialCard> speicalCards = specialCardDao.getSpeicalCards(id);
            List<SpecialCardItemBean> specialItemBeans = getSpeicalItemBeans(speicalCards);
            specialCardBean.setSpecialCardItemBeans(specialItemBeans);
            rv.setObject(specialCardBean);
            rv.setFlag(ReturnValue.FLAG_SUCCESS);

        } catch (Exception e) {
            rv.setFlag(ReturnValue.FLAG_EXCEPTION);
            e.printStackTrace();
        }
        return rv;
    }

    private List<SpecialCardItemBean> getSpeicalItemBeans(List<SpecialCard> speicalCards) {
        List<SpecialCardItemBean> SpecialCardItemBeans = new ArrayList<SpecialCardItemBean>(speicalCards.size());
        for(SpecialCard specialCard : speicalCards){
            SpecialCardItemBeans.add(getSpeicalCardItemBean(specialCard));
        }
        return SpecialCardItemBeans;
    }

    private SpecialCardItemBean getSpeicalCardItemBean(SpecialCard specialCard) {
        SpecialCardItemBean specialCardItemBean = new SpecialCardItemBean();

        specialCardItemBean.setCardType(specialCard.getCardType());
        specialCardItemBean.setCoverUrl(specialCard.getCoverUrl());
        specialCardItemBean.setDescription(specialCard.getDescription());
        specialCardItemBean.setExtend(specialCardType(specialCard.getCardType(), specialCard));
        specialCardItemBean.setId(specialCard.getId());
        specialCardItemBean.setName(specialCard.getName());
        specialCardItemBean.setRid(specialCard.getRid());
        specialCardItemBean.setSid(specialCard.getSid());
        specialCardItemBean.setStatus(specialCard.getStatus());

        return specialCardItemBean;
    }

    /**
     * 卡片类型文本0，图片1，视频2，活动3，商品4
     * @param cardType
     * @return
     */
    private Object specialCardType(int cardType, SpecialCard specialCard) {

        Object result = null;
        if(cardType == 0) {
            NodeBean nodeBean = new NodeBeanText();
            nodeBean.setDescription(specialCard.getDescription());
            nodeBean.setId(String.valueOf(specialCard.getId()));
            nodeBean.setPath(specialCard.getCoverUrl());
            nodeBean.setStatus(String.valueOf(specialCard.getStatus()));
            result = nodeBean;
        }
        else if(cardType == 1) {
            NodeBean nodeBean = new NodeBeanImage();
            nodeBean.setDescription(specialCard.getDescription());
            nodeBean.setId(String.valueOf(specialCard.getId()));
            nodeBean.setPath(specialCard.getCoverUrl());
            nodeBean.setStatus(String.valueOf(specialCard.getStatus()));
            result = nodeBean;
        }else if(cardType == 2) {//视频
            NodeBeanVideo nodeBean = new NodeBeanVideo();
            nodeBean.setPath(specialCard.getExtend());
            nodeBean.setDescription(specialCard.getDescription());
            nodeBean.setId(String.valueOf(specialCard.getId()));
            nodeBean.setPath(specialCard.getExtend());
            nodeBean.setStatus(String.valueOf(specialCard.getStatus()));
            nodeBean.setImg(specialCard.getCoverUrl());
            result = nodeBean;
        }else if(cardType == 3){
            result = activityService.getActivityBeanById(specialCard.getRid(),null, null);
        }else if(cardType == 4){
            result = productService.queryJewelrys(String.valueOf(specialCard.getRid()));
        }
        return result;
    }

    /**
     * 查询达人信息
     * @param sid
     * @return
     */
    public ReturnValue<FashionItemBean> queryFashionMan(String sid) {
        ReturnValue<FashionItemBean> rv = new ReturnValue<FashionItemBean>();
        try {
            long id = Long.parseLong(sid);
            Fashion fashion = fashionDao.get(Fashion.class, id);
            FashionItemBean fashionItemBean = getFasionItemBean(fashion);
            rv.setObject(fashionItemBean);
            rv.setFlag(ReturnValue.FLAG_SUCCESS);

        } catch (Exception e) {
            rv.setFlag(ReturnValue.FLAG_EXCEPTION);
            e.printStackTrace();
        }
        return rv;
    }

    private FashionItemBean getFasionItemBean(Fashion fashion) {
        if(fashion == null){
            return null;
        }
        FashionItemBean fashionItemBean = new FashionItemBean();

        fashionItemBean.setAvatar(fashion.getHeadpic());
        fashionItemBean.setId(String.valueOf(fashion.getId()));
        fashionItemBean.setLevel(fashion.getLevel());
        fashionItemBean.setNickname(fashion.getNickname());
        fashionItemBean.setStatus(fashion.getStatus());

        return fashionItemBean;
    }
}

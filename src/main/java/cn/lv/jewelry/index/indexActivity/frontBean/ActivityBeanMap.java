package cn.lv.jewelry.index.indexActivity.frontBean;

import cn.com.ql.wiseBeijing.serviceUtil.StringUtil;
import cn.lv.jewelry.activity.daoBean.ActivityReviewType;
import cn.lv.jewelry.activity.daoBean.ActivityStatusType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 24593 on 2018/1/17.
 */
public class ActivityBeanMap {
    public static Map<String, Object> getExplore(final ActivityBean bean, final String uid) {
        return new HashMap<String, Object>() {
            {
                put("aid", bean.getAid());
                put("subject", bean.getSubject());
                put("insertTime", bean.getInsertTime());
                put("status", bean.getStatus());
                put("organizers", bean.getOrganizers());
                put("readersNum", bean.getReadersNum());
                put("isAttention", bean.getIsAttention());
                put("htmlAddress", bean.getHtmlAddress());
                put("headPic", bean.getHeadPic());
                put("videoImage", bean.getVideoImage());
                if ("0".equals(uid)) {
                    put("indexPic", StringUtil.isEmpty(bean.getIndexPic()) ? "" : bean.getIndexPic());
                }
            }
        };
    }

    public static Map<String, Object> getResult(final ActivityBean bean, final Integer type, final Integer attenceNum) {
        return new HashMap<String, Object>() {
            {
                put("aid", bean.getAid());
                put("subject", bean.getSubject());
                put("insertTime", bean.getInsertTime());
                put("status", bean.getStatus());
                put("organizers", bean.getOrganizers());
                put("upTime", bean.getUpTime());
                put("upTimeColumns", bean.getUpTimeColumns());
                put("pushColumnsPage", bean.getPushColumnsPage());
                put("pushIndexPage", bean.getPushIndexPage());

                if (bean.getCheckStatus() == ActivityReviewType.DELETE.getType()) {
                    put("updateTime", bean.getUpdateTime());
                }
                if (type == ActivityStatusType.ONGOING.getV() || type == ActivityStatusType.COMPLETE.getV()) {
                    // 获取该活动参与人数
                    put("attenceNum", attenceNum);
                }
            }
        };
    }

    public static Map<String, Object> updateFindActivityStatusAndContent(final ActivityBean bean) {
        return new HashMap<String, Object>() {
            {
                put("headPic", bean.getHeadPic());
                put("indexPic", bean.getIndexPic());
                put("description", bean.getDescription());
                put("draft", bean.getDraft());
            }
        };
    }

    public static Map<String, Object> getFindMap(final ActivityBean bean) {
        return new HashMap<String, Object>() {
            {
                put("indexPicIsShow", bean.getIndexPicIsShow());
                put("activityNum", bean.getActivityNum());
                put("rid", bean.getRid());
                put("headPic", bean.getHeadPic());
                put("mapPic", bean.getMapPic());
                put("indexPic", bean.getIndexPic());
                put("subject", bean.getSubject());
                put("address", bean.getAddress());
                put("beginDateTime", bean.getBeginDateTime());
                put("endDateTime", bean.getEndDateTime());
                put("organizer", bean.getOrganizer());
                put("description", bean.getDescription());
                put("status", bean.getStatus());
                put("registerEndTime", bean.getRegisterEndTime());
                put("telephone", bean.getTelephone());
                put("email", bean.getEmail());
                put("mobile", bean.getMobile());
                put("fee", bean.getFee());
                put("draft", bean.getDraft());
            }
        };
    }
}

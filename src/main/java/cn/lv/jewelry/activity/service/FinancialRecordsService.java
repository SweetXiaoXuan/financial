package cn.lv.jewelry.activity.service;

import cn.lv.jewelry.activity.dao.FinancialRecordsDao;
import cn.xxtui.support.util.MeaasgeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

@Component
@Transactional
public class FinancialRecordsService {
    @Resource(name = "financialRecordsDao")
    private FinancialRecordsDao financialRecordsDao;
    private MeaasgeUtil me = new MeaasgeUtil();

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat daysFD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Bean
    public FinancialRecordsService activityService() {
        return new FinancialRecordsService();
    }

    private final static Logger logger = LoggerFactory.getLogger(FinancialRecordsService.class);


//    /**
//     * 查询用户是否关注/报名该活动
//     * @param aid 活动id
//     * @param pid 用户权限id
//     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
//     */
//    public ReturnValue getRC(Long aid, Long pid) {
//        ReturnValue returnValue = new ReturnValue();
//        try {
//            ActivityFocus focus = activityFocusDao.getActivityFocus(aid, pid);
//            ActivityAttence attence = activityAttenceDao.getActivityAttence(aid, pid, null);
//            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
//            if (focus == null && attence == null)
//                returnValue.setFlag(ReturnValue.FLAG_FAIL);
//        } catch (Exception e) {
//            return SystemException.setResult(returnValue, e, logger);
//        }
//        return returnValue;
//    }




//    /**
//     * 获取往期精彩
//     * @param aid 活动id
//     * @param page 页码
//     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
//     * @author liumengwei
//     * @date 2017-11-26
//     */
//    public ReturnValue<ActivityCommentBean> getWonderfulPast(String aid, Integer page) {
//        ReturnValue<ActivityCommentBean> returnValue = new ReturnValue<>();
//        ActivityCommentBean activityCommentBean = new ActivityCommentBean();
//        try {
//            // 获取往期精彩
//            List<Map<String, Object>> listMapComment = new ArrayList<>();
//            Page<Map<String,Object>> listComments = activityCommentDao.getWonderfulPast(Long.parseLong(aid), page);
//            List<Map<String, Object>> listComment = listComments.getPageContent();
//            if (listComment != null) {
//                for (Map<String, Object> map : listComment) {
//                    if (!"6".equals(map.get("commentType").toString())) {
//                        String uid = map.get("uid").toString();
//                        Map<String, Object> mapComment = putMap(map, 1);
//                        mapComment = putMapComment(mapComment, map, aid, uid);
//                        listMapComment.add(mapComment);
//                    }
//                }
//            }
//            activityCommentBean.setHasNext(listComments.isHasNextPage());
//            activityCommentBean.setComments(listMapComment);
//            returnValue.setObject(activityCommentBean);
//            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
//            returnValue.setMeg(me.getValue(ResultMsgConstant.querySuccess));
//        } catch (Exception ex) {
//            return SystemException.setResult(returnValue, ex, logger);
//        }
//        return returnValue;
//    }
}

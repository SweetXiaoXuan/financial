package cn.lv.jewelry.index.indexActivity.frontService;

import cn.lv.jewelry.activity.service.FinancialRecordsService;
import cn.xxtui.support.util.MeaasgeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.Path;

/**
 * 活动相关
 * @author
 * @Time
 *
 */
@Component
@Path("/lv/activity")
public class FFinancialRecordsService {
    private final static Logger logger = LoggerFactory.getLogger(FFinancialRecordsService.class);
    @Bean
    public FFinancialRecordsService fActivityService() {
        return new FFinancialRecordsService();
    }
    private MeaasgeUtil me = new MeaasgeUtil();

    @Resource(name = "financialRecordsService")
    private FinancialRecordsService financialRecordsService;
    /************************* query ****************************************************************/

//    /**
//     * 探索活动关注接口--关注发布者
//     * @param isAttention 是否关注
//     * @param ppid 活动发布者id
//     * @param pid 用户权限id
//     * @return javax.ws.rs.core.Response
//     * @author liumengwei
//     */
//    @Path("/focusOnThePublisher/{ppid}")
//    @POST
//    @Produces({XXMediaType.TEXTUTF8})
//    public Response focusOnThePublisher(
//            @PathParam("ppid") String ppid,
//            @FormParam("uid") String uid,
//            @FormParam("userType") String userType,
//            @FormParam("isAttention") String isAttention,
//            @FormParam("pid") String pid) {
//        ResultStruct resultStruct = new ResultStruct();
//        resultStruct.setStatus(ResultBean.OK);
//        try {
//            ReturnValue rv = activityService.updatePublishFocusInfo(ppid, pid, isAttention);
//            resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
//        } catch (Exception ex) {
//            return SystemException.setResult(resultStruct, ex, logger);
//        }
//        return Response.ok(resultStruct.toString()).build();
//    }


//    /**
//     * 活动报名
//     * @param aid 活动id
//     * @param uid 用户id
//     * @param selfIntroduction 自我介绍
//     * @param isSignUp 是否报名 0取消报名 1报名
//     * @param pid 权限id
//     * @return javax.ws.rs.core.Response
//     * @author liumengwei
//     */
//    @Path("/signUp/{aid}")
//    @POST
//    @Produces({XXMediaType.TEXTUTF8})
//    public Response signUp(
//            @PathParam("aid") String aid,
//            @FormParam("uid") String uid,
//            @FormParam("selfIntroduction") String selfIntroduction,
//            @FormParam("isSignUp") String isSignUp,
//            @FormParam("userType") String userType,
//            @FormParam("pid") String pid) {
//        ResultStruct resultStruct = new ResultStruct();
//        resultStruct.setStatus(ResultBean.OK);
//        try {
//            // 判断活动状态是否可报名
//            ReturnValue rvStatus =
//                    activityService.getActivityStatus(Long.parseLong(aid));
//            if (rvStatus.getObject() != null &&
//                Integer.parseInt(rvStatus.getObject().toString()) == ActivityStatusType.UPCOMING.getV()) {
//                // 查询用户是否认证
//                ReturnValue<ActivityCommentBean> returnValue =
//                        activityService.getPrivilegeStatus(Long.parseLong(pid));
//                if (returnValue.getFlag() == ReturnValue.FLAG_SUCCESS) {
//                    // 添加用户报名信息
//                    ReturnValue rvAttenceInfo = activityService.saveAttenceInfo(
//                            aid, uid, selfIntroduction, Integer.parseInt(isSignUp), pid);
//                    if (rvAttenceInfo.getFlag() == ReturnValue.FLAG_SUCCESS) {
//                        // 添加用户关注信息
//                        ReturnValue rvFocuInfo = activityService.operatingFocuInfo(aid, Integer.parseInt(isSignUp), pid);
//                        if (rvFocuInfo.getFlag() == ReturnValue.FLAG_SUCCESS) {
//                            resultStruct.setStatus(ResultStruct.OK);
//                            resultStruct.setBody(rvAttenceInfo.getObject());
//                        } else {
//                            resultStruct.setStatus(ResultStruct.ERROR);
//                            resultStruct.setMsg(rvFocuInfo.getMeg());
//                        }
//                    } else {
//                        resultStruct.setMsg(rvAttenceInfo.getMeg());
//                        resultStruct.setStatus(String.valueOf(rvAttenceInfo.getFlag()));
//                    }
//                } else {
//                    resultStruct.setMsg(me.getValue(ResultMsgConstant.unrealName));
//                    resultStruct.setStatus(ResultStruct.UNREAL_NAME);
//                }
//            } else {
//                resultStruct.setMsg(me.getValue(ResultMsgConstant.statusCanNotRegister));
//                resultStruct.setStatus(ResultStruct.ERROR);
//            }
//        } catch (Exception ex) {
//            return SystemException.setResult(resultStruct, ex, logger);
//        }
//        return Response.ok(resultStruct.toString()).build();
//    }

}

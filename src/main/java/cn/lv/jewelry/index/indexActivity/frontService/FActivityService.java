package cn.lv.jewelry.index.indexActivity.frontService;

import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.com.ql.wiseBeijing.serviceUtil.StringUtil;
import cn.com.ql.wiseBeijing.user.frontBean.FActivityRegistrationInformationBean;
import cn.com.ql.wiseBeijing.user.frontBean.FUserActivitiesBean;
import cn.lv.jewelry.activity.daoBean.ActivityStatusType;
import cn.lv.jewelry.activity.service.ActivityService;
import cn.lv.jewelry.index.indexActivity.frontBean.*;
import cn.lv.jewelry.index.indexProduct.frontBean.ProductBean;
import cn.lv.jewelry.index.indexProduct.frontBean.ProductItemBean;
import cn.lv.jewelry.product.service.ProductService;
import cn.xxtui.support.bean.ResultBean;
import cn.xxtui.support.bean.ResultStruct;
import cn.xxtui.support.util.MeaasgeUtil;
import cn.xxtui.support.util.ResultMsgConstant;
import cn.xxtui.support.util.SystemException;
import cn.xxtui.support.util.ValidateMode;
import cn.xxtui.support.util.XXMediaType;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动相关
 * @author
 * @Time
 *
 */
@Component
@Path("/lv/activity")
public class FActivityService {
    private final static Logger logger = LoggerFactory.getLogger(FActivityService.class);
    @Bean
    public FActivityService fActivityService() {
        return new FActivityService();
    }
    private MeaasgeUtil me = new MeaasgeUtil();

    @Resource(name = "activityService")
    private ActivityService activityService;
    /************************* query ****************************************************************/

    /**
     * 根据type和tag查询(活动|商品|人气专场)
     *
     * @param
     * @return
     */
    @Path("/queryTag/{type}/{tag}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response queryByTypeTag(@PathParam("type") String type, @PathParam("tag") String tag, @QueryParam("page") String page) {
        int p = 0;
        if (page != null) {
            try {
                p = Integer.parseInt(page);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        ResultBean bean = new ResultBean();
        bean.setStatus(ResultBean.OK);
        ReturnValue<Object> rv = activityService.getContentByTag(type, tag, p);
        if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
            String r = JSONObject.toJSONString(rv.getObject());
            bean.setBody(r);
        } else {
            bean.setStatus(ResultBean.ERROR);
        }
        return Response.ok(bean.toString()).build();
    }


    /**
     * 活动内容信息,一个活动可能存在多个内容信息
     * @param aid 活动id
     * @return javax.ws.rs.core.Response
     */
    @Path("/contents/{aid}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response content(@PathParam("aid") String aid) {
        ResultBean bean = new ResultBean();
        bean.setStatus(ResultBean.ERROR);
        long defaultAid = 0;
        try {
            defaultAid = Long.parseLong(aid);
        } catch (Exception ex) {
            bean.setMsg(ex.getMessage());
            ex.printStackTrace();
        }
        ReturnValue<ActivityContentBean> rv = activityService.getActivityContent(defaultAid);
        if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
            ActivityContentBean acBean = rv.getObject();
            String r = JSONObject.toJSONString(acBean);
            bean.setBody(r);
            bean.setStatus(ResultBean.OK);
        } else {
            bean.setStatus(ResultBean.ERROR);
        }

        return Response.ok(bean.toString()).build();
    }

    /**
     * 发现活动框架信息
     * @param aid 活动id
     * @param uid 用户id
     * @param pid 用户权限id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @Path("/details/{aid}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response details(
            @PathParam("aid") String aid,
            @FormParam("uid") String uid,
            @FormParam("userType") String userType,
            @FormParam("pid") String pid) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            ReturnValue<ActivityBean> rv = activityService.getActivity(Long.parseLong(aid), uid, pid);
            resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 发现草稿列表
     * @param uid 用户id
     * @param pid 权限id
     * @param page 页码
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/11
     */
    @GET
    @Produces({ XXMediaType.TEXTUTF8 })
    @Path("/findDraftList/{page}")
    public Response findDraftList(
            @FormParam("uid") String uid,
            @FormParam("userType") String userType,
            @PathParam("page") String page,
            @FormParam("pid") String pid) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            // 查询用户发布的发现草稿列表
            ReturnValue<FUserActivitiesBean> rv = activityService.getFindDraftList(
                    Long.parseLong(pid),
                    Integer.parseInt(page),
                    2);
            resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 探索草稿列表
     * @param uid 用户id
     * @param pid 权限id
     * @param page 页码
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/11
     */
    @GET
    @Produces({ XXMediaType.TEXTUTF8 })
    @Path("/exploreTheDraftList/{page}")
    public Response exploreTheDraftList(
            @FormParam("uid") String uid,
            @FormParam("userType") String userType,
            @PathParam("page") String page,
            @FormParam("pid") String pid) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            // 查询用户发布的探索草稿列表
            ReturnValue<FUserActivitiesBean> rv = activityService.getFindDraftList(
                    Long.parseLong(pid),
                    Integer.parseInt(page),
                    3);
            resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
            return Response.ok(resultStruct.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 获取用户关注报名状态
     * @param aid 活动id
     * @param uid 用户id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @Path("/getUserFocusOrSignUpStatus/{aid}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response getUserFocusOrSignUpStatus(
            @PathParam("aid") String aid,
            @FormParam("pid") String pid,
            @FormParam("userType") String userType,
            @FormParam("uid") String uid) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            ReturnValue<ActivityBean> rv =
                    activityService.getUserFocusOrSignUpStatus(Long.parseLong(aid), uid, pid, userType);
            resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 探索活动框架信息
     * @param id 活动id
     * @param pid 用户权限id
     * @param uid 用户id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @Path("/explore/{id}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response explore(
            @FormParam("pid") String pid,
            @PathParam("id") String id,
            @FormParam("uid") String uid) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            ReturnValue rv = activityService.getExplore(Long.parseLong(id), uid, pid);
            resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 围观活动框架信息
     * @param id 活动id
     * @param uid 用户id
     * @param pid 用户权限id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @Path("/onlookers/{id}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response onlookers(
            @PathParam("id") String id,
            @FormParam("pid") String pid,
            @FormParam("userType") String userType,
            @FormParam("uid") String uid) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            ReturnValue<ActivityBean> rv = activityService.getOnlookers(Long.parseLong(id), uid, pid);
            resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 探索活动关注接口--关注发布者
     * @param isAttention 是否关注
     * @param ppid 活动发布者id
     * @param pid 用户权限id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @Path("/focusOnThePublisher/{ppid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response focusOnThePublisher(
            @PathParam("ppid") String ppid,
            @FormParam("uid") String uid,
            @FormParam("userType") String userType,
            @FormParam("isAttention") String isAttention,
            @FormParam("pid") String pid) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            ReturnValue rv = activityService.updatePublishFocusInfo(ppid, pid, isAttention);
            resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 活动关注
     * @param aid 活动id
     * @param uid 用户id
     * @param isAttention 是否关注
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @Path("/attention/{aid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response attention(
            @PathParam("aid") String aid,
            @FormParam("isAttention") String isAttention,
            @FormParam("userType") String userType,
            @FormParam("pid") String pid,
            @FormParam("uid") String uid) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            // 对用户关注信息进行操作
            ReturnValue rv = activityService.operatingFocuInfo(aid, Integer.parseInt(isAttention), pid);
            resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 活动报名
     * @param aid 活动id
     * @param uid 用户id
     * @param selfIntroduction 自我介绍
     * @param isSignUp 是否报名 0取消报名 1报名
     * @param pid 权限id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @Path("/signUp/{aid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response signUp(
            @PathParam("aid") String aid,
            @FormParam("uid") String uid,
            @FormParam("selfIntroduction") String selfIntroduction,
            @FormParam("isSignUp") String isSignUp,
            @FormParam("userType") String userType,
            @FormParam("pid") String pid) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            // 判断活动状态是否可报名
            ReturnValue rvStatus =
                    activityService.getActivityStatus(Long.parseLong(aid));
            if (rvStatus.getObject() != null &&
                Integer.parseInt(rvStatus.getObject().toString()) == ActivityStatusType.UPCOMING.getV()) {
                // 查询用户是否认证
                ReturnValue<ActivityCommentBean> returnValue =
                        activityService.getPrivilegeStatus(Long.parseLong(pid));
                if (returnValue.getFlag() == ReturnValue.FLAG_SUCCESS) {
                    // 添加用户报名信息
                    ReturnValue rvAttenceInfo = activityService.saveAttenceInfo(
                            aid, uid, selfIntroduction, Integer.parseInt(isSignUp), pid);
                    if (rvAttenceInfo.getFlag() == ReturnValue.FLAG_SUCCESS) {
                        // 添加用户关注信息
                        ReturnValue rvFocuInfo = activityService.operatingFocuInfo(aid, Integer.parseInt(isSignUp), pid);
                        if (rvFocuInfo.getFlag() == ReturnValue.FLAG_SUCCESS) {
                            resultStruct.setStatus(ResultStruct.OK);
                            resultStruct.setBody(rvAttenceInfo.getObject());
                        } else {
                            resultStruct.setStatus(ResultStruct.ERROR);
                            resultStruct.setMsg(rvFocuInfo.getMeg());
                        }
                    } else {
                        resultStruct.setMsg(rvAttenceInfo.getMeg());
                        resultStruct.setStatus(String.valueOf(rvAttenceInfo.getFlag()));
                    }
                } else {
                    resultStruct.setMsg(me.getValue(ResultMsgConstant.unrealName));
                    resultStruct.setStatus(ResultStruct.UNREAL_NAME);
                }
            } else {
                resultStruct.setMsg(me.getValue(ResultMsgConstant.statusCanNotRegister));
                resultStruct.setStatus(ResultStruct.ERROR);
            }
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 活动点赞
     * @param aid 活动id
     * @param cid 图文id
     * @param uid 用户id
     * @param pid 权限id
     * @param isLike 是否点赞
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @Path("/like/{aid}/{cid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response like(
            @PathParam("aid") String aid,
            @PathParam("cid") String cid,
            @FormParam("uid") String uid,
            @FormParam("userType") String userType,
            @FormParam("isLike") String isLike,
            @FormParam("pid") String pid) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            // 判断活动状态是否可点赞
            ReturnValue rvStatus = activityService.getActivityStatus(Long.parseLong(aid));
            if (Integer.parseInt(rvStatus.getObject().toString()) == ActivityStatusType.ONGOING.getV() ||
                    Integer.parseInt(rvStatus.getObject().toString()) == ActivityStatusType.COMPLETE.getV()) {
                // 点赞 取消点赞
                ReturnValue rvLike = activityService.updateActivityLike(aid, uid, cid, Integer.parseInt(isLike), pid);
                resultStruct = ResultStruct.setResultStructInfo(rvLike, resultStruct);
            } else {
                resultStruct.setMsg(me.getValue(ResultMsgConstant.graphicCanNotLike));
                resultStruct.setStatus(ResultStruct.ERROR);
            }
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 活动查看评论
     * @param aid 活动id
     * @param page 页码
     * @param status 状态
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @Path("/comment/{aid}/{page}/{status}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response comment(
            @PathParam("aid") String aid,
            @PathParam("page") String page,
            @PathParam("status") String status) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            // 查看活动评论
            ReturnValue<ActivityCommentBean> rv = activityService.getComment(aid, page, status);
            resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 举办方发布图文查询
     * @param aid 活动id
     * @param page 页码
     * @param uid 用户id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @Path("/organizersGraphic/{aid}/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response organizersGraphic(
            @PathParam("aid") String aid,
            @FormParam("uid") String uid,
            @FormParam("pid") String pid,
            @FormParam("userType") String userType,
            @PathParam("page") String page) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            ReturnValue<ActivityCommentBean> rv = activityService.getOrganizersGraphic(aid, uid);
            resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 获取往期精彩
     * @param aid 活动id
     * @param page 页码
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @date 2017-11-26
     */
    @Path("/wonderfulPast/{aid}/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response wonderfulPast(
            @PathParam("aid") String aid,
            @PathParam("page") String page) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            ReturnValue<ActivityCommentBean> rv = activityService.getWonderfulPast(aid, Integer.parseInt(page));
            resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 添加往期精彩
     * @param aid 活动id
     * @param cid 评论id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @date 2017-11-26
     */
    @Path("/addWonderfulPast/{aid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response addWonderfulPast(
            @PathParam("aid") String aid,
            @FormParam("cid") String cid) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            ReturnValue rv = activityService.addWonderfulPast(Long.parseLong(aid), cid);
            resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 管理图文列表
     * @param pid 权限id
     * @param uid 用户id
     * @param page 页码
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/16
     */
    @GET
    @Produces({ XXMediaType.TEXTUTF8 })
    @Path("/releaseGraphicList/{page}")
    public Response releaseGraphicList(
            @FormParam("uid") String uid,
            @FormParam("pid") String pid,
            @FormParam("userType") String userType,
            @PathParam("page") String page) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            // 获取用户发布的所有图文信息
            ReturnValue rvReleaseGraphicList = activityService.getReleaseGraphicList(
                    Long.parseLong(pid), uid, Integer.parseInt(page), userType);
            resultStruct = ResultStruct.setResultStructInfo(rvReleaseGraphicList, resultStruct);
            return Response.ok(resultStruct.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 删除图文
     * @param pid 权限id
     * @param uid 用户id
     * @param cid 图文id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/17
     */
    @POST
    @Produces({ XXMediaType.TEXTUTF8 })
    @Path("/deleteGraphic/{cid}")
    public Response deleteGraphic(
            @FormParam("uid") String uid,
            @FormParam("pid") String pid,
            @FormParam("userType") String userType,
            @PathParam("cid") String cid) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            // 获取用户发布的所有图文信息
            ReturnValue rvDelete = activityService.deleteGraphic(
                    Long.parseLong(pid), Integer.parseInt(uid), Long.parseLong(cid));
            resultStruct = ResultStruct.setResultStructInfo(rvDelete, resultStruct);
            return Response.ok(resultStruct.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 用户发布图文查询
     * @param aid 活动id
     * @param uid 用户id
     * @param page 页码
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @Path("/userGraphic/{aid}/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response userGraphic(
            @PathParam("aid") String aid,
            @FormParam("pid") String pid,
            @FormParam("userType") String userType,
            @FormParam("uid") String uid,
            @PathParam("page") String page) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            ReturnValue<ActivityCommentBean> rv = activityService.getUserGraphic(aid, page, uid);
            resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 查询该图文所有信息
     * @param cid 图文id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2018/1/4
     */
    @Path("/getSingleGraphic/{cid}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response getSingleGraphic(
            @PathParam("cid") String cid) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            ReturnValue<ActivityCommentBean> rv = activityService.getSingleGraphic(cid);
            resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 探索 发现 添加活动评论
     * @param aid 活动id
     * @param comment 详情活动
     * @param activityCommentBean 评论相关参数
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @Path("/postComments/{aid}/{comment}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response postComments(
            @PathParam("aid") String aid,
            @PathParam("comment") String comment,
            @FormParam("userType") String userType,
            @BeanParam ActivityCommentBean activityCommentBean) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            // 查询发布者是否是活动举办方
            ReturnValue rv = activityService.getIsOrganizers(Long.parseLong(aid), activityCommentBean.getPid());
            if (rv.getFlag() == ReturnValue.FLAG_FAIL) {
                // 不是主办方，查询用户是否关注/报名该活动
                ReturnValue rvRC = activityService.getRC(Long.parseLong(aid), activityCommentBean.getPid());
                if (rvRC.getFlag() == ReturnValue.FLAG_SUCCESS) {
                    // 查询用户是否认证
                    ReturnValue<ActivityCommentBean> rvPrivilege =
                            activityService.getPrivilegeStatus(activityCommentBean.getPid());
                    if (rvPrivilege.getFlag() == ReturnValue.FLAG_SUCCESS) {
                        // 认证，添加评论信息
                        resultStruct = posComment(resultStruct, activityCommentBean, aid, comment);
                    } else {
                        resultStruct.setMsg(me.getValue(ResultMsgConstant.permissionDenied));
                        resultStruct.setStatus(ResultStruct.UNREAL_NAME);
                    }
                } else {
                    resultStruct.setMsg(me.getValue(ResultMsgConstant.canNotPostGraphic));
                    resultStruct.setStatus(ResultStruct.ERROR);
                }
            } else {
                // 是主办方，直接添加评论信息
                resultStruct = posComment(resultStruct, activityCommentBean, aid, comment);
            }
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 添加评论信息
     * @param resultStruct 返回结果集
     * @param activityCommentBean 活动评论相关参数
     * @param aid 活动id
     * @param comment 详情活动
     * @return cn.xxtui.support.bean.ResultStruct
     * @author liumengwei
     * @date 2017/11/25
     */
    private ResultStruct posComment(
            ResultStruct resultStruct, ActivityCommentBean activityCommentBean, String aid, String comment) {
        ReturnValue<ActivityCommentBean> rvComments =
                activityService.postComments(
                        activityCommentBean, Long.parseLong(aid), null, Integer.parseInt(comment));
        resultStruct = addResultStruct(resultStruct, activityCommentBean, rvComments, aid);
        return resultStruct;
    }

    private <T> ResultStruct addResultStruct(
            ResultStruct resultStruct, ActivityCommentBean activityCommentBean, ReturnValue<T> rvComments, String aid) {
        if (rvComments.getFlag() == ReturnValue.FLAG_SUCCESS) {
            ReturnValue mapComment =
                    activityService.addReturnValue(activityCommentBean, Long.parseLong(aid), rvComments.getObject());
            resultStruct.setBody(mapComment.getObject());
            resultStruct.setStatus(ResultStruct.OK);
            resultStruct.setMsg(rvComments.getMeg());
        }
        return resultStruct;
    }

    /**
     * 发布公告
     * @param aid 活动id
     * @param comment 详情活动
     * @param activityCommentBean 评论相关参数
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @Path("/postAnnouncement/{aid}/{comment}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response postAnnouncement(
            @PathParam("aid") String aid,
            @PathParam("comment") String comment,
            @FormParam("userType") String userType,
            @BeanParam ActivityCommentBean activityCommentBean) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            // 查询发布者是否是活动举办方
            ReturnValue rvRC = activityService.getIsOrganizers(Long.parseLong(aid),
                    Long.parseLong(activityCommentBean.getPid().toString()));
            if (rvRC.getFlag() == ReturnValue.FLAG_SUCCESS) {
                // 添加评论信息
                ReturnValue<ActivityCommentBean> rvComments =
                        activityService.postComments(
                                activityCommentBean, Long.parseLong(aid), null, Integer.parseInt(comment));
                resultStruct = addResultStruct(resultStruct, activityCommentBean, rvComments, aid);
            } else {
                resultStruct.setMsg(me.getValue(ResultMsgConstant.permissionDeniedPostAnnouncement));
                resultStruct.setStatus(ResultStruct.ERROR);
            }
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 围观 添加活动图文
     * @param aid 活动id
     * @param comment 活动页评论
     * @param activityCommentBean 评论相关参数
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/6
     */
    @Path("/postGraphic/{aid}/{comment}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response postGraphic(
            @PathParam("aid") String aid,
            @PathParam("comment") String comment,
            @FormParam("userType") String userType,
            @BeanParam ActivityCommentBean activityCommentBean) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            resultStruct = determineUserRights(resultStruct, Long.parseLong(aid), activityCommentBean, comment);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 用户转发活动
     * @param aid 活动id
     * @param comment 活动页评论
     * @param activityCommentBean 评论相关参数
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/27
     */
    @Path("/forwardingActivities/{aid}/{comment}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response forwardingActivities(
            @PathParam("aid") String aid,
            @FormParam("userType") String userType,
            @PathParam("comment") String comment,
            @BeanParam ActivityCommentBean activityCommentBean) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            resultStruct = determineUserRights(resultStruct, Long.parseLong(aid), activityCommentBean, comment);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 用户转发图文
     * @param aid 活动id
     * @param comment 活动页评论
     * @param activityCommentBean 评论相关参数
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/12/10
     */
    @Path("/forwardingGraphic/{aid}/{comment}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response forwardingGraphic(
            @PathParam("aid") String aid,
            @FormParam("userType") String userType,
            @PathParam("comment") String comment,
            @BeanParam ActivityCommentBean activityCommentBean) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            resultStruct = determineUserRights(resultStruct, Long.parseLong(aid), activityCommentBean, comment);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 添加图文 直播 视频
     * @param resultStruct 返回参数
     * @param aid 活动id
     * @param activityCommentBean 相关参数
     * @param comment 发布图文位置
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    private ResultStruct postGraphicAndVideoOrLive(
            ResultStruct resultStruct, Long aid, ActivityCommentBean activityCommentBean, String comment) {
        try {
            // 查询用户是否认证
            ReturnValue<ActivityCommentBean> rvPrivilege =
                    activityService.getPrivilegeStatus(activityCommentBean.getPid());
            if (rvPrivilege.getFlag() == ReturnValue.FLAG_SUCCESS) {
                // 认证，添加活动图文信息
                ReturnValue rvGraphic =
                        activityService.postComments(
                                activityCommentBean, aid, null, Integer.parseInt(comment));
                ReturnValue mapComment = activityService.addReturnValue(activityCommentBean, aid, rvGraphic.getObject());
                resultStruct = ResultStruct.setResultStructInfo(rvGraphic, resultStruct);
                resultStruct.setBody(mapComment.getObject());
            } else {
                resultStruct.setMsg(me.getValue(ResultMsgConstant.unrealNameAndPermissionDenied));
                resultStruct.setStatus(ResultStruct.UNREAL_NAME);
            }
        } catch (Exception ex) {
            SystemException.setResult(resultStruct, ex, logger);
            return resultStruct;
        }
        return resultStruct;
    }

    /**
     * 判断用户类型
     * @param resultStruct 返回参数
     * @param aid 活动id
     * @param activityCommentBean 相关参数
     * @param comment 发布图文位置
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/20
     */
    private ResultStruct determineUserRights(
            ResultStruct resultStruct, Long aid, ActivityCommentBean activityCommentBean, String comment) {
        try {
            // 查询用户是否为主办方
            Boolean rv = activityService.getUserPrivilegeInfo(
                    Long.parseLong(String.valueOf(activityCommentBean.getPid())), aid);
            if (rv) {
                // 普通用户
                // 查询用户是否参与该活动
                ReturnValue rvJoin = activityService.getJoin(aid, activityCommentBean.getPid());
                if (rvJoin.getFlag() == ReturnValue.FLAG_SUCCESS) {
                    resultStruct = postGraphicAndVideoOrLive(resultStruct, aid, activityCommentBean, comment);
                } else {
                    resultStruct.setMsg(me.getValue(ResultMsgConstant.canNotPostGraphic));
                    resultStruct.setStatus(ResultStruct.ERROR);
                }
            } else {
                // 主办方
                resultStruct = postGraphicAndVideoOrLive(resultStruct, aid, activityCommentBean, comment);
            }
        } catch (Exception ex) {
            SystemException.setResult(resultStruct, ex, logger);
            return resultStruct;
        }
        return resultStruct;
    }

    /**
     * 添加视频、直播
     * @param aid 活动id
     * @param activityCommentBean 评论相关参数
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/6
     */
    @Path("/postVideoOrLive/{aid}/{comment}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response postVideoOrLive(
            @PathParam("aid") String aid,
            @FormParam("userType") String userType,
            @PathParam("comment") String comment,
            @BeanParam ActivityCommentBean activityCommentBean) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            resultStruct = determineUserRights(resultStruct, Long.parseLong(aid), activityCommentBean, comment);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 添加活动评论的评论
     * @param aid 活动id
     * @param cid 评论id
     * @param activityCommentBean 评论相关参数
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @Path("/postCommentComment/{aid}/{cid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response postCommentComment(
            @PathParam("aid") String aid,
            @FormParam("userType") String userType,
            @PathParam("cid") String cid,
            @BeanParam ActivityCommentBean activityCommentBean) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.OK);
        try {
            // 添加评论信息
            ReturnValue<ActivityCommentBean> rvCommentComments =
                activityService.postComments(
                        activityCommentBean, Long.parseLong(aid), Long.parseLong(cid), null);
            if (rvCommentComments.getFlag() == ReturnValue.FLAG_SUCCESS) {
                ReturnValue mapComment = activityService.addReturnValue(
                        activityCommentBean, Long.parseLong(aid), rvCommentComments.getObject());
                resultStruct.setBody(mapComment.getObject());
                resultStruct.setStatus(String.valueOf(rvCommentComments.getFlag()));
                resultStruct.setMsg(rvCommentComments.getMeg());
            }
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 查询相关活动
     * @param aid 活动id
     * @param uid 用户id
     * @param pid 用户权限id
     * @return javax.ws.rs.core.Response
     */
    @Path("/queryRelativeActivity/{aid}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response relativeActivity(
            @PathParam("aid") String aid,
            @FormParam("uid") String uid,
            @FormParam("pid") String pid,
            @FormParam("userType") String userType
    ) {
        ResultStruct bean = new ResultStruct();
        try {
            ReturnValue<FUserActivitiesBean> returnValue =
                    activityService.getRelativeActivity(Long.parseLong(aid), uid, pid);
            bean = ResultStruct.setResultStructInfo(returnValue, bean);
        } catch (Exception ex) {
            return SystemException.setResult(bean, ex, logger);
        }
        return Response.ok(bean.toString()).build();
    }

    //插入活动信息
    @Path("/addActivity")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response saveActivity(@BeanParam ActivityBean activityBean) {
        ResultBean bean = new ResultBean();
        try {
            ReturnValue<ActivityBean> acrv = activityService.saveActivity(activityBean);
            if (acrv.getFlag() == ReturnValue.FLAG_SUCCESS) {
                bean.setStatus(ResultBean.OK);
                bean.setBody(JSONObject.toJSONString(acrv.getObject()));
            } else {
                bean.setStatus(ResultBean.ERROR);
            }
        } catch (Exception ex) {
            bean.setBody("");
            bean.setStatus(ResultBean.ERROR);
            bean.setMsg(ex.getMessage());
            return Response.ok(bean.toString()).build();
        }
        return Response.ok(bean.toString()).build();

    }

    /**
     * 移动端活动发布
     * CMS活动发布
     * @param mobileActivityBean 发布活动相关参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author huayang
     */
    @Path("/addMobileActivity")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response addMobileActivity(
            @BeanParam MobileActivityBean mobileActivityBean,
            @Context HttpServletRequest request) {
        ResultStruct bean = new ResultStruct();
        try {
            logger.info("request address:{}", request.getRequestURL().toString());
            logger.info("parameters:{}", mobileActivityBean.toString());
            // 参数格式判断
            bean = parameterFormat(bean, mobileActivityBean);
            if ("1".equals(bean.getStatus()))
                return Response.ok(bean.toString()).build();
            // 进行活动添加
            ReturnValue<MobileActivityBean> acrv = activityService.saveMobileActivity(mobileActivityBean);
            bean = ResultStruct.setResultStructInfo(acrv, bean);
        } catch (Exception ex) {
            return SystemException.setResult(bean, ex, logger);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 插入活动圈子信息
     *
     * @param activityCircleFront 活动圈子信息
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author huayang
     */
    @Path("/addActivityCircle")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response addActivityCircle(
            @BeanParam ActivityCircleFront activityCircleFront,
            @Context HttpServletRequest request) {
        ResultStruct bean = new ResultStruct();
        try {
            logger.info("request address:{}", request.getRequestURL().toString());
            logger.info("parameters:{}", activityCircleFront.toString());
            // 进行活动添加
            ReturnValue<MobileActivityBean> acrv = activityService.saveActivityCircle(activityCircleFront);
            bean = ResultStruct.setResultStructInfo(acrv, bean);
        } catch (Exception ex) {
            return SystemException.setResult(bean, ex, logger);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 圈子框架信息
     *
     * @param aid 圈子id
     * @param uid 用户id
     * @param pid 用户权限id
     * @return javax.ws.rs.core.Response
     * @author huayang
     */
    @Path("/activityCircleInfo/{aid}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response activityCircleInfo(
            @PathParam("aid") String aid,
            @FormParam("pid") String pid,
            @FormParam("uid") String uid,
            @FormParam("userType") String userType) {
        ResultStruct bean = new ResultStruct();
        try {
            ReturnValue<ActivityBean> returnValue = activityService.getActivityCircleInfo(Long.parseLong(aid), uid, pid);
            bean = ResultStruct.setResultStructInfo(returnValue, bean);
        } catch (Exception ex) {
            return SystemException.setResult(bean, ex, logger);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 圈子下用户列表
     * @param aid 圈子id
     * @param page 页码
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author huayang
     */
    @Path("/activityCircleForUsers/{aid}/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response activityCircleForUsers(
            @PathParam("aid") String aid,
            @PathParam("page") String page,
            @Context HttpServletRequest request) {
        ResultStruct bean = new ResultStruct();
        try {
            ReturnValue<FActivityRegistrationInformationBean> returnValue =
                    activityService.getActivityCircleForUsers(Long.parseLong(aid), Integer.parseInt(page));
            bean = ResultStruct.setResultStructInfo(returnValue, bean);
        } catch (Exception ex) {
            return SystemException.setResult(bean, ex, logger);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 发布探索活动接口
     * @param activityExploreFront 探索活动信息
     * @param uid 用户id
     * @param pid 用户权限id
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author huayang
     */
    @Path("/addExploreActivity")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response addExploreActivity(
            @FormParam("uid") String uid,
            @FormParam("userType") String userType,
            @FormParam("pid") String pid,
            @BeanParam ActivityExploreFront activityExploreFront,
            @Context HttpServletRequest request) {
            ResultStruct bean = new ResultStruct();
        try {
            // 进行活动添加
            ReturnValue rv = activityService.saveExploreActivity(activityExploreFront, uid, pid);
            bean = ResultStruct.setResultStructInfo(rv, bean);
        } catch (Exception ex) {
            return SystemException.setResult(bean, ex, logger);
        }
        return Response.ok(bean.toString()).build();
    }

    private ResultStruct parameterFormat(ResultStruct resultStruct, MobileActivityBean mobileActivityBean) {
        resultStruct.setStatus(ResultStruct.OK);
        if (!ValidateMode.digital(mobileActivityBean.getActivityNum().toString())) {
            resultStruct.setStatus(ResultStruct.ERROR);
            resultStruct.setMsg(me.getValue(ResultMsgConstant.activityNumFormatError));
            return resultStruct;
        }
        if (!ValidateMode.digitalDouble(mobileActivityBean.getFee().toString())) {
            resultStruct.setStatus(ResultStruct.ERROR);
            resultStruct.setMsg(me.getValue(ResultMsgConstant.feeFormatError));
            return resultStruct;
        }
        if (StringUtil.isEmpty(mobileActivityBean.getMobile()) &&
                StringUtil.isEmpty(mobileActivityBean.getTelephone())) {
            resultStruct.setStatus(ResultStruct.ERROR);
            resultStruct.setMsg(me.getValue(ResultMsgConstant.phoneNumberOrtelephoneCanNotBeAllEmpty));
            return resultStruct;
        }
        if (!ValidateMode.email(mobileActivityBean.getEmail())) {
            resultStruct.setStatus(ResultStruct.ERROR);
            resultStruct.setMsg(me.getValue(ResultMsgConstant.emailFormatError));
            return resultStruct;
        }
        return resultStruct;
    }

    /**
     * 活动审核查询
     *
     * @param status 审核状态
     * @param type 活动状态
     * @param page 页码
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/10
     */
    @Path("/activityReview/{status}/{type}/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response activityReview(
            @PathParam("status") String status,
            @PathParam("type") String type,
            @PathParam("page") String page) {
        //判断条件
        ResultStruct bean = new ResultStruct();
        try {
            // 审核列表
            ReturnValue<Map<String, Object>> acrv = activityService.getActivityReviewList(
                    Integer.parseInt(status), Integer.parseInt(type), Integer.parseInt(page), null);
//            SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Activity.class,
//                    "id", "subject", "insertTime");
            bean = ResultStruct.setResultStructInfo(acrv, bean);
        } catch (Exception ex) {
            return SystemException.setResult(bean, ex, logger);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 活动审核查询-->已通过
     *
     * @param status 审核状态
     * @param top 置顶/推首页
     * @param type 活动状态
     * @param page 页码
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/10
     */
    @Path("/activityReviewTop/{status}/{top}/{type}/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response activityReviewTop(
            @PathParam("status") String status,
            @PathParam("top") String top,
            @PathParam("type") String type,
            @PathParam("page") String page) {
        ResultStruct bean = new ResultStruct();
        try {
            // 已通过列表
            ReturnValue<Map<String, Object>> acrv = activityService.getActivityReviewList(
                    Integer.parseInt(status), Integer.parseInt(type), Integer.parseInt(page), Integer.parseInt(top));
            bean = ResultStruct.setResultStructInfo(acrv, bean);
        } catch (Exception ex) {
            return SystemException.setResult(bean, ex, logger);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 活动审核修改
     *
     * @param aid 活动状态
     * @param status 审核状态
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/10
     */
    @Path("/modifyTheAuditStatus/{aid}/{status}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response modifyTheAuditStatus(
            @PathParam("aid") String aid, @PathParam("status") String status) {
        //判断条件
        ResultStruct bean = new ResultStruct();
        try {
            // 修改审核状态
            ReturnValue acrv = activityService.modifyTheAuditStatus(
                    Long.parseLong(aid), Integer.parseInt(status));
            bean = ResultStruct.setResultStructInfo(acrv, bean);
        } catch (Exception ex) {
            return SystemException.setResult(bean, ex, logger);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 活动置顶 推首页
     *
     * @param aid 活动状态
     * @param status 审核状态
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/15
     */
    @Path("/stickyOrPushIndex/{aid}/{status}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response stickyOrPushIndex(
            @PathParam("aid") String aid, @PathParam("status") String status) {
        ResultStruct bean = new ResultStruct();
        try {
            // 修改活动置顶 推首页状态
            ReturnValue acrv = activityService.updateActivityStickyOrPushIndex(Long.parseLong(aid), Integer.parseInt(status));
            bean = ResultStruct.setResultStructInfo(acrv, bean);
        } catch (Exception ex) {
            return SystemException.setResult(bean, ex, logger);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 活动审核搜索 根据关键词模糊查询活动列表
     *
     * @param name 活动名称关键词
     * @param page 页码
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/10
     */
    @Path("/searchActivityList/{name}/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response searchActivityList(
            @PathParam("name") String name,
            @PathParam("page") String page) {
        ResultStruct bean = new ResultStruct();
        try {
            ReturnValue<Map<String, Object>> acrv = activityService.searchActivityList(name, Integer.parseInt(page));
            bean = ResultStruct.setResultStructInfo(acrv, bean);
        } catch (Exception ex) {
            return SystemException.setResult(bean, ex, logger);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 我参与的活动下其他用户
     *
     * @param aid 活动id
     * @param pid 用户权限id
     * @param uid 用户id
     * @param page 页码
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/15
     */
    @Path("/activityAttendUserList/{aid}/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response activityAttendUserList(
            @FormParam("uid") String uid,
            @FormParam("pid") String pid,
            @FormParam("userType") String userType,
            @PathParam("aid") String aid,
            @PathParam("page") String page) {
        ResultStruct bean = new ResultStruct();
        try {
            ReturnValue<ActivityAttendUser> acrv = activityService.getActivityAttendUserList(
                    Long.parseLong(pid), uid, Long.parseLong(aid), Integer.parseInt(page), Integer.parseInt(userType));
            bean = ResultStruct.setResultStructInfo(acrv, bean);
        } catch (Exception ex) {
            return SystemException.setResult(bean, ex, logger);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 退出我参与的活动或者圈子
     *
     * @param aid 活动id
     * @param pid 用户权限id
     * @param uid 用户id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/15
     */
    @Path("/quitActivity/{aid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response quitActivity(
            @FormParam("uid") String uid,
            @FormParam("userType") String userType,
            @FormParam("pid") String pid,
            @PathParam("aid") String aid) {
        ResultStruct bean = new ResultStruct();
        try {
            ReturnValue acrv = activityService.quitActivity(
                    uid, Long.parseLong(pid), Long.parseLong(aid));
            bean = ResultStruct.setResultStructInfo(acrv, bean);
        } catch (Exception ex) {
            return SystemException.setResult(bean, ex, logger);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 删除我发起的圈子
     * @param aid 活动id
     * @param pid 用户权限id
     * @param uid 用户id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/15
     */
    @Path("/deleteInitiatedActivityCircle/{aid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response deleteInitiatedActivityCircle(
            @FormParam("uid") String uid,
            @FormParam("pid") String pid,
            @FormParam("userType") String userType,
            @PathParam("aid") String aid) {
        ResultStruct bean = new ResultStruct();
        try {
            ReturnValue acrv = activityService.quitInitiatedActivityCircle(
                    Long.parseLong(uid), Long.parseLong(pid), Long.parseLong(aid));
            bean = ResultStruct.setResultStructInfo(acrv, bean);
        } catch (Exception ex) {
            return SystemException.setResult(bean, ex, logger);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 删除评论的评论
     * @param ccid 评论的评论id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/12/14
     */
    @Path("/deleteGraphicComment/{ccid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response deleteGraphicComment(
            @PathParam("ccid") String ccid) {
        ResultStruct bean = new ResultStruct();
        try {
            ReturnValue acrv = activityService.deleteGraphicComment(Long.parseLong(ccid));
            bean = ResultStruct.setResultStructInfo(acrv, bean);
        } catch (Exception ex) {
            return SystemException.setResult(bean, ex, logger);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 更新活动主体信息
     *
     * @param id
     * @param activityBean
     * @return
     */
    @Path("/updateActivity/{aid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response updateActivity(@PathParam("aid") String id, @BeanParam ActivityBean activityBean) {
        //判断条件
        ResultBean bean = new ResultBean();
        activityBean.setId(id);
        ReturnValue acrv = activityService.updateActivity(activityBean);
        if (acrv.getFlag() == ReturnValue.FLAG_SUCCESS) {
            bean.setStatus(ResultBean.OK);
            bean.setBody(JSONObject.toJSONString(acrv.getObject()));
        } else {
            bean.setStatus(ResultBean.ERROR);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 更新发现活动状态信息
     *
     * @param aid
     * @param activityBean
     * @return
     * @see cn.lv.jewelry.activity.daoBean.ActivityStatusType
     */
    @Path("/updateActivityStatus/{aid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response updateActivityStatus(
            @PathParam("aid") String aid,
            @FormParam("pid") String pid,
            @FormParam("userType") String userType,
            @BeanParam ActivityContentString activityContentString,
            @BeanParam ActivityBean activityBean) {
        //判断条件
        ResultBean bean = new ResultBean();
        activityBean.setId(aid);
        ReturnValue acrv = activityService.updateActivityStatusWithContent(aid, Long.parseLong(pid), activityContentString, activityBean);
        if (acrv.getFlag() == ReturnValue.FLAG_SUCCESS) {
            bean.setStatus(ResultBean.OK);
            bean.setBody(JSONObject.toJSONString(acrv.getObject()));
        } else {
            bean.setStatus(ResultBean.ERROR);
            bean.setMsg(acrv.getMeg());
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 发现升级围观 围观升圈子
     * @param aid 活动id
     * @param activityContentString 活动详情相关参数
     * @param activityBean 活动信息相关参数
     * @param pid 发布者权限id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/12/22
     */
    @Path("/updateFindActivityStatusAndContent/{aid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response updateFindActivityStatusAndContent(
            @PathParam("aid") String aid,
            @FormParam("pid") String pid,
            @FormParam("userType") String userType,
            @FormParam("uid") String uid,
            @BeanParam ActivityContentString activityContentString,
            @BeanParam ActivityBean activityBean) {
        ResultStruct bean = new ResultStruct();
        ReturnValue<ActivityBean> acrv = activityService.updateFindActivityStatusAndContent(
                activityContentString, aid, activityBean);
        bean = ResultStruct.setResultStructInfo(acrv, bean);
        return Response.ok(bean.toString()).build();
    }

    /**
     * 更新探索活动状态信息
     * @param aid 活动id
     * @param activityContentString 活动详细内容相关参数
     * @param activityBean 活动框架信息相关参数
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/22
     */
    @Path("/updateExploreActivityStatus/{aid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response updateExploreActivityStatus(
            @PathParam("aid") String aid,
            @BeanParam ActivityContentString activityContentString,
            @BeanParam ActivityExploreFront activityBean) {
        //判断条件
        ResultStruct bean = new ResultStruct();
        try {
            ReturnValue acrv =
                    activityService.updateExploreActivityStatusWithContent(
                            activityContentString, activityBean, Long.parseLong(aid));
            bean = ResultStruct.setResultStructInfo(acrv, bean);
        } catch (Exception ex) {
            return SystemException.setResult(bean, ex, logger);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 修改活动状态
     * @param aid 活动id
     * @param status 状态
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Time 2017/9/2
     */
    @Path("/updateActivity/{aid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response updateActivity(
            @PathParam("aid") String aid,
            @FormParam("status") String status) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            ActivityBean activityBean = new ActivityBean();
            activityBean.setStatus(Integer.parseInt(status));
            activityBean.setId(aid);
            // 修改活动状态信息
            ReturnValue<ActivityBean> acrv = activityService.updateActivityStatus(activityBean);
            resultStruct = ResultStruct.setResultStructInfo(acrv, resultStruct);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
        return Response.ok(resultStruct.toString()).build();
    }

    //插入活动详情信息
    @Path("/addActivityDetail")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response addActivityDetail(@BeanParam ActivityContentString activityContent) {
        //接受用ActivityContent 展示输出用ActivityContentBean
        //判断条件
        ResultBean bean = new ResultBean();
        System.out.println(activityContent.getContent());
        ReturnValue<ActivityContentBean> acrv = activityService.saveActivityContent(activityContent);
        if (acrv.getFlag() == ReturnValue.FLAG_SUCCESS) {
            bean.setStatus(ResultBean.OK);
            bean.setBody(JSONObject.toJSONString(acrv.getObject()));
        } else {
            bean.setStatus(ResultBean.ERROR);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 更新活动详情信息
     *
     * @param cid             内容块的id
     * @param activityContent
     * @return
     */
    @Path("/updateActivityDetail/{cid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response updateActivityDetail(@PathParam("cid") String cid, @BeanParam ActivityContentString activityContent) {
        //接受用ActivityContent 展示输出用ActivityContentBean
        //判断条件
        ResultBean bean = new ResultBean();
        ReturnValue<ActivityContentBean> acrv = activityService.updateActivityContent(activityContent);
        if (acrv.getFlag() == ReturnValue.FLAG_SUCCESS) {
            bean.setStatus(ResultBean.OK);
            bean.setBody(JSONObject.toJSONString(acrv.getObject()));
        } else {
            bean.setStatus(ResultBean.ERROR);
        }
        return Response.ok(bean.toString()).build();
    }

    //查询相关商品
    @Path("/queryRelativeProduct/{aid}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response relativeProduct(@PathParam("aid") String aid) {
        ResultBean bean = new ResultBean();
        ProductBean productBean = new ProductBean();
        //FIXME 重新建一个展示的Bean brand应该是一个类`
        List<ProductItemBean> relateProduct = activityService.getRelativeProduct(Long.parseLong(aid), 3);
        productBean.setProducts(relateProduct);
        bean.setStatus(ResultBean.OK);
        String r = JSONObject.toJSONString(productBean);
        bean.setBody(r);
        return Response.ok(bean.toString()).build();
    }

    //插入相关产品
    @Path("/addRelativeProduct/{aid}/{pid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response addRelativeProduct(@PathParam("aid") String aid, @PathParam("pid") String pid) {
        ResultBean bean = new ResultBean();
        activityService.saveRealtiveProduct(aid, pid);
        bean.setStatus(ResultBean.OK);
        String r = JSONObject.toJSONString(new HashMap<String, String>() {{
            put("status", "0");
        }});
        bean.setBody(r);

        return Response.ok(bean.toString()).build();
    }

    //插入相关活动
    @Path("/addRelativeActivity")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response addRelativeActivity(@FormParam("maid") String maid, @FormParam("raid") String raid) {
        ResultBean bean = new ResultBean();
        int flag = activityService.saveRelativeActivity(maid, raid);
        if (flag != 0)
            bean.setStatus(ResultBean.ERROR);
        else {
            bean.setStatus(ResultBean.OK);
            String r = JSONObject.toJSONString(new HashMap<String, String>() {{
                put("status", "0");
            }});
            bean.setBody(r);
        }
        return Response.ok(bean.toString()).build();
    }

    //查询所有未完成的活动
    @Path("/listActivities/upcoming/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response getActivityUpcomingList(@PathParam("page") String page) {
        return getActivityList(null, page, ActivityStatusType.UPCOMING.getV());
    }

    //查询所有完成的活动
    @Path("/listActivities/done/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response getActivityDoneList(@PathParam("page") String page) {
        return getActivityList(null, page, ActivityStatusType.COMPLETE.getV());
    }

    /**
     * 查询的活动
     * @param uid 用户id
     * @param type 活动类型
     * @param page 页码
     * @return
     */
    @Path("/activities/{uid}/{type}/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response getActivityList(
            @PathParam("uid") String uid,
            @PathParam("type") String type,
            @PathParam("page") String page) {
        int t = -1;
        if ("all".equals(type)) {
            t = -1;
        } else {
            try {
                t = Integer.parseInt(type);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return getActivityList(uid, page, t);
    }

    private Response getActivityList(String uid, String page, int status) {
        ResultBean bean = new ResultBean();
        bean.setStatus(ResultBean.ERROR);
        try {
            int p = Integer.parseInt(page);
            ReturnValue<Map<String, Object>> rv = activityService.getActivityList(uid, p, status);
            bean.setBody(JSONObject.toJSONString(rv.getObject()));
            bean.setStatus(ResultBean.OK);
        } catch (Exception ex) {
            bean.setBody(ex.getMessage());
            ex.printStackTrace();
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 首页接口查询活动
     * @param uid 用户id
     * @param type 活动类型
     * @param page 页码
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @date 2017-11-17
     */
    @Path("/appActivities/{type}/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response getAppActivityList(
            @FormParam("uid") String uid,
            @FormParam("pid") String pid,
            @FormParam("userType") String userType,
            @PathParam("type") String type,
            @PathParam("page") String page) {
        int t = -1;
        if ("all".equals(type)) {
            t = -1;
        } else {
            try {
                t = Integer.parseInt(type);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return getAppActivityList(uid, page, t, userType);
    }

    /**
     * 首页接口查询活动
     * @param uid 用户id
     * @param status 活动类型
     * @param page 页码
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @date 2017-11-17
     */
    private Response getAppActivityList(String uid, String page, int status, String userType) {
        ResultBean bean = new ResultBean();
        bean.setStatus(ResultBean.ERROR);
        try {
            int p = Integer.parseInt(page);
            ReturnValue<Map<String, Object>> rv = activityService.getAppActivityList(uid, p, status, userType);
            bean.setBody(JSONObject.toJSONString(rv.getObject()));
            bean.setStatus(ResultBean.OK);
        } catch (Exception ex) {
            bean.setBody(ex.getMessage());
            ex.printStackTrace();
        }
        return Response.ok(bean.toString()).build();
    }

    //按照活动名称查询活动
    @Path("/queryActivityByName")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response getActivityByName(@FormParam("page") String page, @FormParam("name") String name) {
        ResultBean bean = new ResultBean();
        bean.setStatus(ResultBean.ERROR);
        try {
            int p = Integer.parseInt(page);
            ReturnValue<Map<String, Object>> rv = activityService.getActivityList(p, name);
            bean.setBody(JSONObject.toJSONString(rv.getObject()));
            bean.setStatus(ResultBean.OK);
        } catch (Exception ex) {
            bean.setBody(ex.getMessage());
            ex.printStackTrace();
        }
        return Response.ok(bean.toString()).build();
    }


    /**
     * 查询活动的相关商品
     *
     * @param aid
     * @return
     */
    @Path("/queryActivityProduct/{aid}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response realtiveActivity(@PathParam("aid") String aid) {
        ActivityRelativeProductBean activityRelativeProduct = new ActivityRelativeProductBean();
        List<ActivityRelativeProductItemBean> activityRelativeProductItemBeans = activityService.queryActivityRelativeProduct(aid);
        activityRelativeProduct.setRelateProducts(activityRelativeProductItemBeans);
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
        resultBean.setBody(JSONObject.toJSONString(activityRelativeProduct));
        return Response.ok(resultBean.toString()).build();
    }

    /*************************
     * insert
     ****************************************************************/

    @Resource(name = "productService")
    private ProductService productService;

    /**
     * 插入商品(珠宝)信息
     *
     * @param productItemBean
     * @return
     */
    @Path("/addJewelry")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response saveJewelry(@BeanParam ProductItemBean productItemBean) {
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
        ReturnValue<ProductItemBean> rv = productService.saveProduct(productItemBean);
        if (rv.getFlag() == 0) {
            resultBean.setStatus(ResultBean.OK);
            resultBean.setBody(rv.getObject());
        } else {
            resultBean.setStatus(ResultBean.ERROR);
        }

        return Response.ok(resultBean.toString()).build();
    }

    /**
     * 插入报名(参加)活动信息
     *
     * @param takeActivityBean
     * @return
     */
    @Path("/addTakeActivity")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response addTakeActivity(@BeanParam TakeActivityBean takeActivityBean) {
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
        ReturnValue<ProductItemBean> rv = activityService.saveTakeactivity(takeActivityBean);
        if (rv.getFlag() == 0) {
            resultBean.setStatus(ResultBean.OK);
//            resultBean.setBody(rv.getObject());
        } else {
            resultBean.setStatus(ResultBean.ERROR);
        }

        return Response.ok(resultBean.toString()).build();
    }
}

package cn.lv.jewelry.activity.service;

import cn.com.ql.wiseBeijing.news.dao.LikesDao;
import cn.com.ql.wiseBeijing.news.daoBean.Likes;
import cn.com.ql.wiseBeijing.serviceUtil.PageMapUtil;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.com.ql.wiseBeijing.serviceUtil.StringUtil;
import cn.com.ql.wiseBeijing.user.dao.MyInfoDao;
import cn.com.ql.wiseBeijing.user.dao.UserBindDao;
import cn.com.ql.wiseBeijing.user.dao.UserDao;
import cn.com.ql.wiseBeijing.user.daoBean.MyInfo;
import cn.com.ql.wiseBeijing.user.daoBean.User;
import cn.com.ql.wiseBeijing.user.daoBean.UserBind;
import cn.com.ql.wiseBeijing.user.frontBean.FActivityRegistrationInformationBean;
import cn.com.ql.wiseBeijing.user.frontBean.FUserActivitiesBean;
import cn.lv.jewelry.activity.dao.*;
import cn.lv.jewelry.activity.daoBean.*;
import cn.lv.jewelry.activity.daoBean.ActivityContent;
import cn.lv.jewelry.enterprise.dao.EnterpriseDao;
import cn.lv.jewelry.enterprise.daoBean.Enterprise;
import cn.lv.jewelry.fashion.service.FashionService;
import cn.lv.jewelry.index.indexActivity.frontBean.*;
import cn.lv.jewelry.index.indexDesigner.frontBean.DesignerItemBean;
import cn.lv.jewelry.index.indexFashion.frontBean.SpecialItemBean;
import cn.lv.jewelry.index.indexProduct.frontBean.ProductItemBean;
import cn.lv.jewelry.product.dao.ProductActivityDao;
import cn.lv.jewelry.product.dao.ProductDao;
import cn.lv.jewelry.product.daoBean.Product;
import cn.lv.jewelry.product.daoBean.ProductActivity;
import cn.lv.jewelry.product.service.ProductService;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWrap;
import cn.xxtui.support.util.Constant;
import cn.xxtui.support.util.DateUtil;
import cn.xxtui.support.util.ImageUtils;
import cn.xxtui.support.util.MeaasgeUtil;
import cn.xxtui.support.util.ResultMsgConstant;
import cn.xxtui.support.util.SystemException;
import cn.xxtui.support.util.TransferUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by lixing on 16/3/19.
 */
@Component
@Transactional
public class ActivityService {
    @Resource(name = "activityDao")
    private ActivityDao activityDao;
    @Resource(name = "activityCommentReadDao")
    private ActivityCommentReadDao activityCommentReadDao;
    @Resource(name = "activityContentDao")
    private ActivityContentDao activityContentDao;
    @Resource(name = "enterpriseDao")
    private EnterpriseDao enterpriseDao;
    @Resource(name = "relativeProductDao")
    private RelativeProductDao relativeProductDao;
    @Resource(name = "productDao")
    private ProductDao productDao;
    @Resource(name = "activityAttenceDao")
    private ActivityAttenceDao activityAttenceDao;
    @Resource(name = "relativeActivityDao")
    private RelativeActivityDao relativeActivityDao;
    @Resource(name = "activityImageDao")
    private ActivityImageDao activityImageDao;
    @Resource(name = "activityFocusDao")
    private ActivityFocusDao activityFocusDao;
    @Resource(name = "productActivityDao")
    private ProductActivityDao productActivityDao;
    @Resource(name = "activityPrivilegeDao")
    private ActivityPrivilegeDao activityPrivilegeDao;
    @Resource(name = "lvTagDao")
    private LvTagDao lvTagDao;
    @Resource(name = "activityTakeDao")
    private ActivityTakeDao activityTakeDao;
    @Resource(name = "activityCommentDao")
    private ActivityCommentDao activityCommentDao;
    @Resource(name = "activityCommentCommentDao")
    private ActivityCommentCommentDao activityCommentCommentDao;
    @Resource(name = "activityCommentTextDao")
    private ActivityCommentTextDao activityCommentTextDao;
    @Resource(name = "likesDao")
    private LikesDao likesDao;
    @Resource(name = "activityCircleDao")
    private ActivityCircleDao activityCircleDao;
    @Resource(name = "activityFocusExploreDao")
    private ActivityFocusExploreDao activityFocusExploreDao;
    @Resource(name = "activityWonderfulPastDao")
    private ActivityWonderfulPastDao activityWonderfulPastDao;
    @Resource(name = "userDao")
    private UserDao userDao;
    @Resource(name = "userBindDao")
    private UserBindDao userBindDao;
    @Resource(name = "myInfoDao")
    private MyInfoDao myInfoDao;
    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "fashionService")
    private FashionService fashionService;
    private MeaasgeUtil me = new MeaasgeUtil();

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat daysFD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Bean
    public ActivityService activityService() {
        return new ActivityService();
    }

    private final static Logger logger = LoggerFactory.getLogger(ActivityService.class);

    public ReturnValue<ActivityContentBean> getActivityContent(long aid) {
        ReturnValue<ActivityContentBean> returnValue = new ReturnValue();
        try {
            List<cn.lv.jewelry.activity.daoBean.ActivityContent> contents = activityDao.getActivityContent(aid);
            ActivityContentBean acBean = new ActivityContentBean();
            acBean.setAid(aid);
            Map<String, cn.lv.jewelry.index.indexActivity.frontBean.ActivityContent> contentss = new HashMap<>();
            for (cn.lv.jewelry.activity.daoBean.ActivityContent ac : contents) {
                contentss.put(String.valueOf(ac.getActivityStatus()), produceActiviyContent(ac.getContent(), ac.getType()));
            }
            acBean.setContents(contentss);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setObject(acBean);
        } catch (Exception ex) {
            ex.printStackTrace();
            returnValue.setFlag(ReturnValue.FLAG_EXCEPTION);
        }
        return returnValue;
    }

    /**
     * 添加用户权限信息
     * @param uid 用户id
     * @param userType 用户类型
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     */
    public ReturnValue saveActivityPrivilege(Long uid, Integer userType) {
        ReturnValue returnValue = new ReturnValue();
        try {
            ActivityPrivilege activityPrivilege = activityPrivilegeDao.getId(uid, userType);
            if (activityPrivilege == null) {
                activityPrivilege = new ActivityPrivilege();
                activityPrivilege.setUid(uid);
                activityPrivilege.setStatus(ActivityPrivilegeStatus.NORMAL.getStatus());
                activityPrivilege.setPublishActivityPrivilege(ActivityPrivilegeStatus.CANRELEASED.getStatus().toString());
                activityPrivilege.setTakeAcitivityPrivilege(ActivityPrivilegeStatus.CANCONTRACTED.getStatus().toString());
                activityPrivilege.setUserType(userType.toString());
                // 保存权限信息
                activityPrivilegeDao.save(activityPrivilege).toString();
            } else {
                activityPrivilege.setStatus(ActivityPrivilegeStatus.NORMAL.getStatus());
                activityPrivilegeDao.getSession().update(activityPrivilege);
            }
            returnValue.setObject(activityPrivilege.getId());
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.saveSuccess));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 添加用户已读消息信息
     * @param uid 用户id
     * @param pid 权限id
     * @param aid 活动id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     */
    public ReturnValue addUserReadMessages(String pid, String uid, String aid) {
        ReturnValue returnValue = new ReturnValue();
        try {
            // 查询该活动未读消息id
            List<Map<String, Object>> activityCommentList = activityCommentDao.getCommentByUid(Long.parseLong(pid), Long.parseLong(aid));
            if (activityCommentList != null) {
                for (Map<String, Object> activityComment : activityCommentList) {
                    // 将未读消息id添加到已读消息表
                    ActivityCommentRead read = new ActivityCommentRead();
                    ActivityComment comment = new ActivityComment();
                    comment.setId(Long.parseLong(activityComment.get("id").toString()));
                    read.setCid(comment);
                    read.setReadTime(String.valueOf(System.currentTimeMillis() / 1000));
                    ActivityPrivilege privilege = activityPrivilegeDao.get(ActivityPrivilege.class, Long.parseLong(pid));
                    read.setPrivilegeId(privilege);
                    activityCommentReadDao.save(read);
                }
            }
            returnValue.setMeg(me.getValue(ResultMsgConstant.addSuccess));
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 修改权限状态
     * @param pid 用户权限id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     */
    public ReturnValue updateActivityPrivilege(Long pid, Integer userType) {
        ReturnValue returnValue = new ReturnValue<>();
        returnValue.setObject("");
        try {
            Integer result = activityPrivilegeDao.updateActivityPrivilege(pid, userType);
            if (result != null && result > 0) {
                returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
                returnValue.setMeg(me.getValue(ResultMsgConstant.modifySuccess));
            } else {
                returnValue.setFlag(ReturnValue.FLAG_FAIL);
                returnValue.setMeg(me.getValue(ResultMsgConstant.modifyFail));
            }
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 对用户关注信息进行操作
     * @param aid 活动id
     * @param pid 用户权限id
     * @param isAttention 是否关注
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ReturnValue operatingFocuInfo(String aid, Integer isAttention, String pid) {
        ReturnValue returnValue = new ReturnValue<>();
        try {
            Long laid = Long.parseLong(aid);
            Long lpid = Long.parseLong(pid);
            ActivityFocus activityFocus = activityFocusDao.getActivityFocus(laid, lpid);
            if (isAttention == 1) {
                if (activityFocus == null) {
                    // 查询活动状态
                    ActivityFocus focus = new ActivityFocus();
                    Activity activity = activityDao.get(Activity.class, laid);
                    focus.setAid(activity);
                    focus.setPrivilegeId(activityPrivilegeDao.get(ActivityPrivilege.class, lpid));
                    focus.setaStatus(activity.getStatus());
                    focus.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000));
                    // 保存关注信息
                    activityFocusDao.save(focus);
                }
                returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
                returnValue.setMeg(me.getValue(ResultMsgConstant.attentionSuccess));
            } else if(isAttention == 0) {
                if (activityFocus != null) {
                    activityFocusDao.getSession().delete(activityFocus);
                }
                returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
                returnValue.setMeg(me.getValue(ResultMsgConstant.cancelAttentionSuccess));
            } else {
                returnValue.setFlag(ReturnValue.FLAG_FAIL);
                returnValue.setMeg(me.getValue(ResultMsgConstant.canNotBeConcerned));
            }
        } catch (Exception e) {
            return SystemException.setResult(returnValue, e, logger);
        }
        return returnValue;
    }

    /**
     * 添加活动报名信息
     * @param aid 活动id
     * @param selfIntroduction 自我介绍
     * @param uid 用户id
     * @param isSignUp 是否报名
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ReturnValue saveAttenceInfo(String aid, String uid, String selfIntroduction, Integer isSignUp, String pid) {
        ReturnValue returnValue = new ReturnValue<>();
        try {
            // 查看用户报名信息
            ActivityAttence activityAttence = activityAttenceDao.getActivityAttence(
                    Long.parseLong(aid), Long.parseLong(pid), null);
            if (isSignUp == 1) {
                if (activityAttence == null) {
                    ActivityAttence attence = new ActivityAttence();
                    Activity activity = activityDao.get(Activity.class, Long.parseLong(aid));
                    attence.setAid(activity);
                    attence.setPrivilegeId(activityPrivilegeDao.get(ActivityPrivilege.class, Long.parseLong(pid)));
                    attence.setStatus(ActivityAttenceStatus.SIGNUP.getStatus());
                    attence.setAttenceTime(System.currentTimeMillis() / 1000);
                    attence.setSelfIntroduce(selfIntroduction);
                    activityAttenceDao.save(attence);
                } else {
                    if (activityAttence.getStatus() == ActivityAttenceStatus.ABNORMAL.getStatus() ||
                            activityAttence.getStatus() == ActivityAttenceStatus.DELETE.getStatus()) {
                        activityAttence.setStatus(ActivityAttenceStatus.SIGNUP.getStatus());
                        activityAttenceDao.getSession().update(activityAttence);
                    }
                }
                ActivityPrivilege privilege = activityPrivilegeDao.get(ActivityPrivilege.class, Long.parseLong(pid));
                Integer userType = Integer.parseInt(privilege.getUserType());
                String headPic = "";
                String username = "";
                if (userType == 0) {
                    User user = userDao.get(User.class, Integer.parseInt(uid.substring(2)));
                    headPic = user.getHeadpic();
                    username = user.getUsername();
                } else if (userType == 2) {
                    UserBind user = userBindDao.get(UserBind.class, Integer.parseInt((uid.substring(2))));
                    headPic = user.getHeadpic();
                    username = user.getNickname();
                }
                ActivityAttendUser.Users users = new ActivityAttendUser().new Users();
                users.setSelfIntroduction(selfIntroduction);
                users.setUid(Long.parseLong(uid));
                users.setHeadPic(StringUtil.isEmpty(headPic) ? "" : headPic);
                users.setUsername(StringUtil.isEmpty(username) ? "" : username);
                returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
                returnValue.setMeg(me.getValue(ResultMsgConstant.signupSuccess));
                returnValue.setObject(users);
            } else if (isSignUp == 0) {
                if (activityAttence != null) {
                    activityAttenceDao.getSession().delete(activityAttence);
                }
                returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
                returnValue.setMeg(me.getValue(ResultMsgConstant.cancelSignupSuccess));
                returnValue.setObject("null");
            } else {
                returnValue.setFlag(ReturnValue.FLAG_FAIL);
                returnValue.setMeg(me.getValue(ResultMsgConstant.canNotBeSignup));
                returnValue.setObject("null");
            }
        } catch (Exception e) {
            return SystemException.setResult(returnValue, e, logger);
        }
        return returnValue;
    }

    /**
     * 添加活动点赞信息
     * @param aid 活动id
     * @param cid 图文id
     * @param privilegeId 用户id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ReturnValue saveLikeInfo(Long aid, Long privilegeId, Long cid) {
        ReturnValue returnValue = new ReturnValue<>();
        try {
            Likes likes = new Likes();
            Activity activity = new Activity();
            activity.setId(aid);
            likes.setAid(activity);
            ActivityComment comment = new ActivityComment();
            comment.setId(cid);
            likes.setAcid(comment);
            likes.setPrivilegeId(activityPrivilegeDao.get(ActivityPrivilege.class, privilegeId));
            likes.setStatus(ActivityCommentStatus.NORMAL.getStatus().toString());
            likes.setCid(cid);
            likesDao.save(likes);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.likeSuccess));
        } catch (Exception e) {
            return SystemException.setResult(returnValue, e, logger);
        }
        return returnValue;
    }

    /**
     * 对该cid进行点赞操作
     * @param aid 活动id
     * @param cid 图文id
     * @param uid 用户id
     * @param isLike 是否点赞
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ReturnValue updateActivityLike(String aid, String uid, String cid, Integer isLike, String pid) {
        ReturnValue returnValue = new ReturnValue<>();
        try {
            Long Laid = Long.parseLong(aid);
            Long Lpid = Long.parseLong(pid);
            Long Lcid = Long.parseLong(cid);
            Likes likes = likesDao.getLikesByAidUidCid(Laid, Lpid, Lcid);
            if (isLike == 1) {
                if (likes == null) {
                    returnValue = saveLikeInfo(Laid, Lpid, Lcid);
                }
                returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
                returnValue.setMeg(me.getValue(ResultMsgConstant.likeSuccess));
            } else if (isLike == 0) {
                if (likes != null) {
                    likesDao.getSession().delete(likes);
                }
                returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
                returnValue.setMeg(me.getValue(ResultMsgConstant.cancelLikeSuccess));
            } else {
                returnValue.setFlag(ReturnValue.FLAG_FAIL);
                returnValue.setMeg(me.getValue(ResultMsgConstant.canNotBeLike));
            }
        } catch (Exception e) {
            return SystemException.setResult(returnValue, e, logger);
        }
        return returnValue;
    }

    /**
     * 查询用户是否关注/报名该活动
     * @param aid 活动id
     * @param pid 用户权限id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ReturnValue getRC(Long aid, Long pid) {
        ReturnValue returnValue = new ReturnValue();
        try {
            ActivityFocus focus = activityFocusDao.getActivityFocus(aid, pid);
            ActivityAttence attence = activityAttenceDao.getActivityAttence(aid, pid, null);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            if (focus == null && attence == null)
                returnValue.setFlag(ReturnValue.FLAG_FAIL);
        } catch (Exception e) {
            return SystemException.setResult(returnValue, e, logger);
        }
        return returnValue;
    }

    /**
     * 查询用户是否参与该活动
     * @param aid 活动id
     * @param pid 用户权限id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ReturnValue getJoin(Long aid, Long pid) {
        ReturnValue returnValue = new ReturnValue();
        try {
            ActivityAttence attence = activityAttenceDao.getActivityAttence(aid, pid, 0);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            if (attence == null)
                returnValue.setFlag(ReturnValue.FLAG_FAIL);
        } catch (Exception e) {
            return SystemException.setResult(returnValue, e, logger);
        }
        return returnValue;
    }

    /**
     * 修改用户关注信息
     * @param ppid 活动发布者id
     * @param pid 用户qxid
     * @param isAttention 是否关注
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2017/10/18
     */
    public ReturnValue updatePublishFocusInfo(String ppid, String pid, String isAttention) {
        ReturnValue returnValue = new ReturnValue();
        try {
            if (!StringUtil.isEmpty(isAttention)) {
                Integer attention = Integer.parseInt(isAttention);
                Long lPpid = Long.parseLong(ppid);
                Long lPid = Long.parseLong(pid);
                ActivityFocusExplore activityFocusExplore = activityFocusExploreDao.getActivityFocusExplore(lPpid, lPid);
                if (attention == 1) {
                    if (activityFocusExplore == null) {
                        ActivityFocusExplore focusExplore = new ActivityFocusExplore();
                        focusExplore.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000));
                        ActivityPrivilege privilege = activityPrivilegeDao.get(ActivityPrivilege.class, lPpid);
                        ActivityPrivilege activityPrivilege = activityPrivilegeDao.get(ActivityPrivilege.class, lPid);
                        if (privilege == null || activityPrivilege == null) {
                            returnValue.setMeg(me.getValue(ResultMsgConstant.usersNotHereCanNotBeConcerned));
                            returnValue.setFlag(ReturnValue.FLAG_FAIL);
                            return returnValue;
                        }
                        focusExplore.setPrivilegePuid(privilege);
                        focusExplore.setPrivilegeId(activityPrivilege);
                        activityFocusExploreDao.save(focusExplore);
                    }
                    returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
                    returnValue.setMeg(me.getValue(ResultMsgConstant.saveSuccess));
                } else if (attention == 0) {
                    if (activityFocusExplore != null) {
                        activityFocusExploreDao.getSession().delete(activityFocusExplore);
                    }
                    returnValue.setMeg(me.getValue(ResultMsgConstant.cancelAttentionSuccess));
                } else {
                    returnValue.setFlag(ReturnValue.FLAG_FAIL);
                    returnValue.setMeg(me.getValue(ResultMsgConstant.operationError));
                }
            }
        } catch (Exception e) {
            return SystemException.setResult(returnValue, e, logger);
        }
        return returnValue;
    }

    /**
     * 判断活动状态是否可报名
     * @param aid 活动id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2017/9/7
     */
    public ReturnValue getActivityStatus(Long aid) {
        ReturnValue returnValue = new ReturnValue();
        try {
            Activity activity = activityDao.get(Activity.class, aid);
            if (activity != null)
                returnValue.setObject(activity.getStatus());
        } catch (Exception e) {
            return SystemException.setResult(returnValue, e, logger);
        }
        return returnValue;
    }

    /**
     * 查询用户是否有权限报名
     * @param pid 权限id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ReturnValue getPrivilegeStatus(Long pid) {
        ReturnValue returnValue = new ReturnValue();
        try {
            ActivityPrivilege privilege = activityPrivilegeDao.get(ActivityPrivilege.class, pid);
            if (privilege != null) {
                returnValue.setFlag(
                        ActivityPrivilegeStatus.CANNOTPUBLISHED.getStatus().toString()
                                .equals(privilege.getPublishActivityPrivilege())
                                ? ReturnValue.FLAG_SUCCESS
                                : ReturnValue.FLAG_FAIL);
            }
        } catch (Exception e) {
            return SystemException.setResult(returnValue, e, logger);
        }
        return returnValue;
    }

    /**
     * 查询发布者是否是活动主办方
     * @param aid 活动id
     * @param pid 权限id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2017/9/17
     */
    public ReturnValue getIsOrganizers(Long aid, Long pid) {
        ReturnValue returnValue = new ReturnValue();
        try {
            ActivityTake takeList = activityTakeDao.getActivityTake(aid, pid);
            Activity activity = activityDao.get(Activity.class, aid);
            returnValue.setFlag(takeList != null || activity.getPrivilegeId() == pid ? ReturnValue.FLAG_SUCCESS : ReturnValue.FLAG_FAIL);
        } catch (Exception e) {
            return SystemException.setResult(returnValue, e, logger);
        }
        return returnValue;
    }

    /**
     * 添加用户评论信息
     * @param activityCommentBean 信息流相关参数
     * @param aid 活动id
     * @param cid 评论id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     */
    public ReturnValue postComments(ActivityCommentBean activityCommentBean, Long aid, Long cid, Integer commentType) {
        ReturnValue returnValue = new ReturnValue();
        try {
            // set评论信息
            Integer type = activityCommentBean.getType();
            ActivityComment comment = new ActivityComment();
            Activity activity = activityDao.get(Activity.class, aid);
            ActivityPrivilege privilege = activityPrivilegeDao.get(ActivityPrivilege.class, activityCommentBean.getPid());
            comment.setAid(activity);
            comment.setPrivilegeId(privilege);
            comment.setCommentType(type);
            comment.setCommentTime(System.currentTimeMillis() / 1000);
            comment.setCommentStatus(ActivityCommentStatus.NORMAL.getStatus());
            comment.setaStatus(activity.getStatus());
            comment.setIsDelete(0);
            if (commentType == ActivityCommentType.DETAILS.getType())
                comment.setComment(ActivityCommentType.DETAILS.getType());
            else
                comment.setComment(ActivityCommentType.ACTIVITY.getType());
            if (cid != null) {
                // 如果评论id != null，添加评论的评论信息
                ActivityComment activityComment = new ActivityComment();
                activityComment.setId(cid);
                ActivityCommentComment activityCommentComment = new ActivityCommentComment();
                activityCommentComment.setCid(activityComment);
                activityCommentComment.setaStatus(activity.getStatus());
                activityCommentComment.setCommentContent(activityCommentBean.getText());
                activityCommentComment.setCommentStatus(ActivityCommentStatus.NORMAL.getStatus());
                activityCommentComment.setPrivilegeId(privilege);
                activityCommentComment.setIsDelete(0);
                activityCommentComment.setCommentTime(System.currentTimeMillis() / 1000);
                // 添加二级评论信息
                String accid = activityCommentCommentDao.save(activityCommentComment).toString();
                if (accid == null) {
                    returnValue.setFlag(ReturnValue.FLAG_EXCEPTION);
                    returnValue.setMeg(me.getValue(ResultMsgConstant.releaseFail));
                }
                returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
                returnValue.setMeg(me.getValue(ResultMsgConstant.releaseSuccess));
                returnValue.setObject(cid);
            } else {
                // 添加一级评论信息
                String acid = activityCommentDao.save(comment).toString();
                if (!StringUtil.isEmpty(acid)) {
                    ActivityCommentText commentText = new ActivityCommentText();
                    comment.setId(Long.parseLong(acid));
                    if (ActivityCommentType.ACTIVITY.getType().equals(commentType)) {
                        if (ActivityCommentType.PIC.getType().equals(type)) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("text", activityCommentBean.getText());
                            Object pic = activityCommentBean.getPic();
                            String replacePic = "";
                            if (pic != null) {
                                replacePic = pic.toString().replace(" ", "");
                            }
                            map.put("pic", replacePic);
                            map.put("graphicType", activityCommentBean.getGraphicType());
                            String content = activityCommentBean.getContent();
                            map.put("content", StringUtil.isEmpty(content) ? "" : HtmlUtils.htmlEscape(content));
                            commentText.setCommentContent(JSON.toJSONString(map));
                        } else if (ActivityCommentType.LIVE.getType().equals(type) ||
                                ActivityCommentType.VIDEO.getType().equals(type)) {
                            commentText.setCommentContent(activityCommentBean.getUrl());
                        } else if (ActivityCommentType.FORWARDING_ACTIVIGTIES.getType().equals(type) ||
                                ActivityCommentType.ANNOUNCEMENT.getType().equals(type)) {
                            commentText.setCommentContent(activityCommentBean.getText().toString());
                        } else if (ActivityCommentType.SHARECOMMENTS.getType().equals(type) ||
                                ActivityCommentType.ACTIVITY.getType().equals(commentType)) {
                            commentText.setCommentContent(activityCommentBean.getCid().toString());
                        }
                    } else {
                        if (ActivityCommentType.TEXT.getType().equals(type)) {
                            commentText.setCommentContent(activityCommentBean.getText());
                        }
                    }
                    commentText.setCid(comment);
                    commentText.setaStatus(activity.getStatus());
                    commentText.setCommentTime(System.currentTimeMillis() / 1000);
                    commentText.setCommentStatus(ActivityCommentStatus.NORMAL.getStatus());
                    activityCommentTextDao.save(commentText);
                    returnValue.setObject(acid);
                    returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
                    returnValue.setMeg(me.getValue(ResultMsgConstant.commentSuccess));
                } else {
                    returnValue.setFlag(ReturnValue.FLAG_EXCEPTION);
                    returnValue.setMeg(me.getValue(ResultMsgConstant.commentFail));
                }
            }
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    public ReturnValue addReturnValue(ActivityCommentBean activityCommentBean, Long aid, Object cid) {
        ReturnValue returnValue = new ReturnValue();
        Integer type = activityCommentBean.getType();
        ActivityPrivilege privilege = activityPrivilegeDao.get(ActivityPrivilege.class, activityCommentBean.getPid());
        Integer userType = Integer.parseInt(privilege.getUserType());
        String headPic = "";
        String username = "";
        if (userType == ActivityPrivilegeStatus.PERSONAL.getStatus()) {
            User ur = userDao.get(User.class, Integer.parseInt(privilege.getUid().toString()));
            headPic = ur.getHeadpic();
            username = ur.getUsername();
        } else if (userType == ActivityPrivilegeStatus.RHIRD_PARTY.getStatus()) {
            UserBind ur = userBindDao.get(UserBind.class, Integer.parseInt(privilege.getUid().toString()));
            headPic = ur.getHeadpic();
            username = ur.getNickname();
        }
        Map<String, Object> mapComment = new HashMap<>();
        if (cid != null && !"".equals(cid)) {
            Long acid = Long.parseLong(cid.toString());
            ActivityComment comment = activityCommentDao.get(ActivityComment.class, acid);
            Map<String, Object> listComments = activityCommentDao.getComments(acid, Integer.parseInt(comment.getPrivilegeId().getUserType()));
            if (listComments != null) {
                mapComment.put("commentType", listComments.get("commentType"));
                mapComment.put("userType", Integer.parseInt(listComments.get("userType").toString()));
                Integer isDelete = Integer.parseInt(listComments.get("isDelete").toString());
                mapComment.put("isDelete", isDelete);
                Integer likeNum;
                // 获取点赞信息
                likeNum = likesDao.getLikesNum(Integer.parseInt(acid.toString()));
                mapComment.put("likeNum", likeNum);
                List<Map<String, Object>> listMapLikes = likesDao.getLikesById(
                        Integer.parseInt(acid.toString()), aid.toString());
                mapComment.put("likes", listMapLikes);
                Boolean isLike = false;
                isLike = sameUid(activityCommentBean.getUid().toString(), isLike, listMapLikes);
                mapComment.put("isLike", 0);
                if (isLike) {
                    mapComment.put("isLike", 1);
                }
                mapComment.put("uid", activityCommentBean.getUid());
                mapComment.put("headPic", StringUtil.isEmpty(headPic) ? "" : headPic);
                mapComment.put("username", StringUtil.isEmpty(username) ? "" : username);
                mapComment.put("cid", acid);
                List<Map<String, Object>> maps = getCommentComment(acid);
                mapComment.put("secondCommend", maps);
                if (type == null) {
                    returnValue.setObject(maps);
                } else if (type == 5 || type == 8 || type == 2) {
                    mapComment = putMap(listComments, 1);
                    mapComment = putMapComment(mapComment, listComments, aid.toString(), activityCommentBean.getUid().toString());
                    returnValue.setObject(mapComment);
                } else if(type == 3 || type == 4) {
                    mapComment.put("commentContent", activityCommentBean.getUrl());
                    mapComment.put("commentTime",
                            DateUtil.getDateTime(listComments.get("commentTime").toString()));
                    returnValue.setObject(mapComment);
                } else if (type == 1 || type == 6) {
                    mapComment.put("commentContent", activityCommentBean.getText());
                    mapComment.put("commentTime",
                            DateUtil.getDateTime(listComments.get("commentTime").toString()));
                    returnValue.setObject(mapComment);
                }
            }
        }
        return returnValue;
    }

    /**
     * 围观活动框架信息
     * @param aid 活动id
     * @param uid 用户id
     * @param pid 用户权限id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ReturnValue<ActivityBean> getOnlookers(Long aid, String uid, String pid) {
        ReturnValue<ActivityBean> returnValue = new ReturnValue();
        try {
            ActivityBean activityBean = getOnlookersById(aid, uid, pid);
            returnValue.setObject(activityBean);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.querySuccess));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 获取用户关注报名状态
     * @param aid 活动id
     * @param uid 用户id
     * @param pid 用户权限id
     * @param userType 用户类型
     * @return cn.lv.jewelry.index.indexActivity.frontBean.ActivityBean
     * @author liumengwei
     * @Date 2017/9/13
     */
    public ReturnValue<ActivityBean> getUserFocusOrSignUpStatus(Long aid, String uid, String pid, String userType) {
        ReturnValue<ActivityBean> returnValue = new ReturnValue<>();
        ActivityBean activityBean = new ActivityBean();
        try {
            // 查询用户是否关注参与
            Activity activity = activityDao.get(Activity.class, aid);
            activityBean.setIsLogin(0);
            activityBean.setIsAttention(0);
            activityBean.setIsSignUp(0);
            if (!StringUtil.isEmpty(uid)) {
                if (activity != null) {
                    ActivityPrivilege privilege = activityPrivilegeDao.get(ActivityPrivilege.class, Long.parseLong(pid));
                    if (activity.getStatus() != ActivityStatusType.UNPUBLISHED.getV()) {
                        // 活动状态不是探索
                        if (!StringUtil.isEmpty(uid) && privilege != null) {
                            ActivityAttence attence =
                                    activityAttenceDao.getActivityAttence(aid, privilege.getId(), null);
                            activityBean.setIsSignUp(0);
                            if (attence != null) {
                                Integer attenceStatus = Integer.parseInt(String.valueOf(attence.getStatus()));
                                Integer status = 0;
                                if (attenceStatus == ActivityAttenceStatus.SIGNUP.getStatus()) {
                                    status = 1;
                                } else if (attenceStatus == ActivityAttenceStatus.PARTICIPATE.getStatus()) {
                                    status = 2;
                                } else if (attenceStatus == ActivityAttenceStatus.ABNORMAL.getStatus()) {
                                    status = 3;
                                }
                                activityBean.setIsSignUp(status);
                            }
                            ActivityFocus focus = activityFocusDao.getActivityFocus(aid, privilege.getId());
                            activityBean.setIsAttention(focus != null ? 1 : 0);
                            activityBean.setIsLogin(1);
                        }
                    } else {
                        // 活动状态为探索
                        ActivityFocusExplore focusExplore =
                                activityFocusExploreDao.getActivityFocusExplore(
                                        activity.getPrivilegeId(), activityPrivilegeDao.getId(Long.parseLong(uid)).getId());
                        if (focusExplore != null) {
                            activityBean.setIsAttention(focusExplore != null ? 1 : 0);
                            activityBean.setIsLogin(1);
                        }
                    }
                }
            }
            activityBean.setStatus(null);
            returnValue.setMeg(me.getValue(ResultMsgConstant.querySuccess));
            returnValue.setObject(activityBean);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 围观活动框架信息
     * @param aid 活动id
     * @param uid 用户id
     * @param pid 用户权限id
     * @return cn.lv.jewelry.index.indexActivity.frontBean.ActivityBean
     */
    public ActivityBean getOnlookersById(Long aid, String uid, String pid) {
        ActivityBean activityBean = new ActivityBean();
        try {
            // 获取活动信息
            Activity activity = activityDao.get(Activity.class, aid);
            // 获取发布方信息
            List<Map<String, Object>> objects = getTheOrganizerInformation(aid);
            activityBean.setOrganizers(objects);
            activityBean.setSubject(activity.getSubject());
            activityBean.setDescription(StringUtil.isEmpty(activity.getDescription()) ? "" : activity.getDescription());
            activityBean.setAid(activity.getId());
            activityBean.setHeadPic(StringUtil.isEmpty(activity.getLogoPic()) ? "" : activity.getLogoPic());
            String htmlAddress = activity.getHtmlAddress();
            activityBean.setHtmlAddress(StringUtil.isEmpty(htmlAddress) ? "" : htmlAddress);
            activityBean.setIsAttention(0);
            if (!StringUtil.isEmpty(uid)) {
                ActivityFocus activityFocus = activityFocusDao.getActivityFocus(aid, Long.parseLong(pid));
                activityBean.setIsAttention(activityFocus != null ? 1 : 0);
            }
            if ("0".equals(uid)) {
                String video = activity.getVideoImageIndex();
                activityBean.setIndexPic(activity.getIndexPic());
                activityBean.setVideoImageIndex(StringUtil.isEmpty(video) ? "" : video);
            }
            String video = activity.getVideoImageLogo();
            activityBean.setVideoImage(StringUtil.isEmpty(video) ? "" : video);
            // 获取关注人数
            Integer readerNumber = activityFocusDao.getActivityFocusNumber(aid);
            activityBean.setFocusNum(readerNumber);
            // 获取圈子成员头像
            List<Map<String, Object>> listMapUser = activityDao.getUserPic(aid);
            activityBean.setUsers(listMapUser);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return activityBean;
    }

    /**
     * 探索活动框架信息
     * @param aid 活动id
     * @param uid 用户id
     * @param pid 用户权限id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ReturnValue getExplore(Long aid, String uid, String pid) {
        ReturnValue returnValue = new ReturnValue();
        try {
            ActivityBean activityBean = getExploreById(aid, uid, pid);
            returnValue.setObject(ActivityBeanMap.getExplore(activityBean, uid));
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.querySuccess));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 探索活动框架信息
     * @param id 活动id
     * @param uid 用户id
     * @param pid 用户权限id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ActivityBean getExploreById(long id, String uid, String pid) {
        ActivityBean activityBean = new ActivityBean();
        try {
            // 获取阅读人数
            Integer readerNumber = activityDao.getActivityReaderNumber(id);
            // 获取发布方信息
            Activity activity = activityDao.get(Activity.class, id);
            if (activity != null) {
                List<Map<String, Object>> objects = getTheOrganizerInformation(id);
                // 判断参数数据
                if ("0".equals(uid)) {
                    String indexPic = activity.getIndexPic();
                    activityBean.setIndexPic(StringUtil.isEmpty(indexPic) ? "" : indexPic);
                    String video = activity.getVideoImageIndex();
                    activityBean.setVideoImageIndex(StringUtil.isEmpty(video) ? "" : video);
                }
                activityBean.setIsAttention(0);
                if (!StringUtil.isEmpty(uid)) {
                    ActivityFocusExplore activityFocusExplore =
                            activityFocusExploreDao.getActivityFocusExplore(activity.getPrivilegeId(), Long.parseLong(pid));
                    activityBean.setIsAttention(activityFocusExplore != null ? 1 : 0);
                }

                activityBean.setReadersNum(readerNumber);
                activityBean.setHeadPic(StringUtil.isEmpty(activity.getLogoPic()) ? "" : activity.getLogoPic());
                activityBean.setInsertTime(DateUtil.getFromUnix(activity.getInsertTime()));
                activityBean.setOrganizers(objects);
                activityBean.setAid(activity.getId());
                String htmlAddress = activity.getHtmlAddress();
                activityBean.setHtmlAddress(StringUtil.isEmpty(htmlAddress) ? "" : htmlAddress);
                activityBean.setSubject(activity.getSubject());
                String video = activity.getVideoImageLogo();
                activityBean.setVideoImage(StringUtil.isEmpty(video) ? "" : video);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return activityBean;
    }

    /**
     * 获取主办方信息
     *
     * @param aid 活动id
     * @return
     */
    private List<Map<String, Object>> getTheOrganizerInformation(Long aid) {
        List<Map<String, Object>> objects = new ArrayList();
        List<Map<String, Object>> organizers = new ArrayList<>();
        List listEid = activityTakeDao.getActivityEnterprise(aid);
        for (int j = 0; j < listEid.size(); j++) {
            Map<String, Object> map = new HashMap<>();
            Long eid = Long.parseLong(listEid.get(j).toString());
            List list = activityTakeDao.getActivityTakeInfo(aid, eid);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Integer status = Integer.parseInt(list.get(i).toString());
                    if (status == 0 || status == 2) {//个人 第三方
                        continue;
//                        map = activityTakeDao.getTakeUserInfo(aid, eid).get(0);
                    } else if (status == 1) {// 企业
                        map = activityTakeDao.getTakeEnterpriseInfo(aid, eid).get(0);
                    } else {
                        return null;
                    }
                    organizers.add(map);
                }
            }
        }
        if (organizers != null && organizers.size() != 0) {
            for (int i = 0; i < organizers.size(); i++) {
                Map<String, Object> mapInfo = new HashMap<>();
                Object headpic = organizers.get(i).get("headpic");
                if (headpic != null && !"".equals(headpic))
                    mapInfo.put("headPic", headpic);
                else
                    mapInfo.put("headPic", "");
                mapInfo.put("pid", organizers.get(i).get("pid"));
                mapInfo.put("level", 0);
                mapInfo.put("username", organizers.get(i).get("username"));
                objects.add(mapInfo);
            }
        }
        return objects;
    }

    /**
     * 发现草稿列表
     * @param pid 权限id
     * @param page 页码
     * @param status 活动状态 3探索 2发现
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2017/11/11
     */
    @Transactional
    public ReturnValue<FUserActivitiesBean> getFindDraftList(Long pid, Integer page, Integer status) {
        ReturnValue<FUserActivitiesBean> rv = new ReturnValue<>();
        try {
            ActivityPrivilege privilege = activityPrivilegeDao.get(ActivityPrivilege.class, pid);
            String username = "";
            if (ActivityPrivilegeStatus.PERSONAL.getStatus().toString().equals(privilege.getUserType())) {
                User user = userDao.get(User.class, Integer.parseInt(String.valueOf(privilege.getUid())));
                username = user.getUsername();
            } else if (ActivityPrivilegeStatus.PERSONAL.getStatus().toString().equals(privilege.getUserType())) {
                UserBind userBind = userBindDao.get(UserBind.class, Integer.parseInt(String.valueOf(privilege.getUid())));
                username = userBind.getNickname();
            }
//            if ("admin".equals(username)) {
//                ActivityPrivilege activityPrivilege = activityPrivilegeDao.getId(1L, 1);
//                pid = activityPrivilege.getId();
//            }
            FUserActivitiesBean fUserActivitiesBean = new FUserActivitiesBean();
            List<FUserActivitiesBean.Activities> list = new ArrayList<>();
            // 查询列表总页数
            Long totalPages = activityDao.getDraftPages(pid, status);
            // 获取草稿列表
            Page<Activity> activityPage = activityDao.getFindDraftList(pid, page, status);
            List<Activity> activityList = activityPage.getPageContent();
            fUserActivitiesBean.setHasNext(activityPage.isHasNextPage());
            for (Activity activity : activityList) {
                FUserActivitiesBean.Activities activities = new FUserActivitiesBean().new Activities();
                activities.setStatus(activity.getStatus());
                activities.setSubject(activity.getSubject());
                activities.setAid(activity.getId());
                activities.setPid(activity.getPrivilegeId());
                // 活动举办方信息
                List<Map<String, Object>> objects = getTheOrganizerInformation(activity.getId());
                activities.setOrganizers(objects);
                String indexPic = activity.getIndexPic();
                String logoPic = activity.getLogoPic();
                String mapPic = activity.getMapPic();
                activities.setHeadPic(StringUtil.isEmpty(logoPic) ? "" : logoPic);
                activities.setMapPic(StringUtil.isEmpty(mapPic) ? "" : mapPic);
                activities.setIndexPic(StringUtil.isEmpty(indexPic) ? "" : indexPic);
                activities.setInsertTime(DateUtil.getDateTime(String.valueOf(activity.getInsertTime())));
                list.add(activities);
            }
            fUserActivitiesBean.setActivities(list);
            fUserActivitiesBean.setTotalPages(totalPages);
            rv.setObject(fUserActivitiesBean);
            rv.setMeg(me.getValue(ResultMsgConstant.querySuccess));
            rv.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception ex) {
            return SystemException.setResult(rv, ex, logger);
        }
        return rv;
    }

    /**
     * 查询发现活动框架信息
     * @param id 活动id
     * @param uid 用户id
     * @param pid 用户权限id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ReturnValue<ActivityBean> getActivity(long id, String uid, String pid) {
        ReturnValue<ActivityBean> returnValue = new ReturnValue();
        try {
            ActivityBean activityBean = getActivityBeanById(id, uid, pid);
            returnValue.setObject(activityBean);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.querySuccess));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 查询发现活动框架信息
     * @param aid 活动id
     * @param uid 用户id
     * @param pid 用户权限id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ActivityBean getActivityBeanById(long aid, String uid, String pid) {
        ActivityBean activityBean = new ActivityBean();
        try {
            // 获取关注人数
            Integer readerNumber = activityFocusDao.getActivityFocusNumber(aid);
            // 获取报名人数
            Integer attenceNumber = activityAttenceDao.getActivityAttenceNumber(aid, null);
            // 查询活动信息
            Activity activity = activityDao.get(Activity.class, aid);
            activityBean.setPlatformPhone(Constant.PLATFORM_PHONE);
            String logoPic = activity.getLogoPic();
            activityBean.setHeadPic(StringUtil.isEmpty(logoPic) ? "" : logoPic);
            activityBean.setSubject(activity.getSubject());
            activityBean.setAddress(activity.getAddress());
            activityBean.setBeginDateTime(DateUtil.getFromUnix(activity.getStartTime()));
            activityBean.setEndDateTime(DateUtil.getFromUnix(activity.getEndTime()));
            activityBean.setActivityNum(activity.getActivityNum());// 活动要求人数
            activityBean.setEmail(activity.getEmail());
            activityBean.setIsAttention(0);
            activityBean.setIsSignUp(0);
            if (!StringUtil.isEmpty(pid)) {
                ActivityFocus activityFocus = activityFocusDao.getActivityFocus(aid, Long.parseLong(pid));
                ActivityAttence activityAttence = activityAttenceDao.getActivityAttence(aid, Long.parseLong(pid), null);
                activityBean.setIsAttention(activityFocus != null ? 1 : 0);
                activityBean.setIsSignUp(activityAttence != null ? 1 : 0);
            }
            String video = activity.getVideoImageLogo();
            activityBean.setVideoImage(StringUtil.isEmpty(video) ? "" : video);
            if ("0".equals(uid)) {
                activityBean.setVideoImage(null);
                activityBean.setVideoImageLogo(StringUtil.isEmpty(video) ? "" : video);
                String indexPic = activity.getIndexPic();
                activityBean.setIndexPic(StringUtil.isEmpty(indexPic) ? "" : indexPic);
                String videoIndex = activity.getVideoImageIndex();
                activityBean.setVideoImageIndex(StringUtil.isEmpty(videoIndex) ? "" : videoIndex);
            }
            activityBean.setMobile(activity.getMobile());
            activityBean.setTelephone(activity.getTelephone());
            activityBean.setFee(activity.getFee());
            String description = activity.getDescription();
            activityBean.setDescription(StringUtil.isEmpty(description) ? "" : description);
            activityBean.setAid(activity.getId());
            String mapPic = activity.getMapPic();
            activityBean.setMapPic(StringUtil.isEmpty(mapPic) ? "" : mapPic);
            activityBean.setRegisterEndTime(DateUtil.getDateTime(activity.getRegisterEndTime()));
            activityBean.setFocusNum(readerNumber);
            activityBean.setAttenceNum(attenceNumber);
            String htmlAddress = activity.getHtmlAddress();
            activityBean.setHtmlAddress(StringUtil.isEmpty(htmlAddress) ? "" : htmlAddress);
            // 活动举办方信息
            List<Map<String, Object>> objects = getTheOrganizerInformation(aid);
            activityBean.setOrganizers(objects);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return activityBean;
    }

    /**
     * 查询该图文所有信息
     * @param cid 图文id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2018/1/4
     */
    public ReturnValue<ActivityCommentBean> getSingleGraphic(String cid) {
        ReturnValue<ActivityCommentBean> returnValue = new ReturnValue();
        try {
            ActivityCommentBean activityCommentBean = new ActivityCommentBean();

            // 获取图文信息
            List<Map<String, Object>> listMapComment = new ArrayList<>();
            Map<String,Object> listComments =
                    activityCommentDao.getComments(Long.parseLong(cid), 0);
            // 状态不为公告
            if (listComments != null) {
                if (!ActivityCommentType.ANNOUNCEMENT.equals(listComments.get("commentType"))) {
                    Map<String, Object> mapComment = putMap(listComments, 1);
                    mapComment = putMapComment(mapComment, listComments, null, listComments.get("uid").toString());
                    listMapComment.add(mapComment);
                }
            }
            activityCommentBean.setHasNext(false);
            activityCommentBean.setComments(listMapComment);

            returnValue.setObject(activityCommentBean);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.querySuccess));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 用户发布图文查询
     * @param aid 活动id
     * @param page 页码
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ReturnValue<ActivityCommentBean> getUserGraphic(String aid, String page, String uid) {
        ReturnValue<ActivityCommentBean> returnValue = new ReturnValue();
        try {
            ActivityCommentBean activityCommentBean = getUserGraphicById(aid, Integer.parseInt(page), uid);
            returnValue.setObject(activityCommentBean);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.querySuccess));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 查询用户是否为发布人
     * @param aid 活动id
     * @param pid 权限id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2017/10/13
     */
    public ReturnValue getActivityPublish(Long pid, Long aid) {
        ReturnValue returnValue = new ReturnValue();
        returnValue.setFlag(ReturnValue.FLAG_FAIL);
        returnValue.setMeg(me.getValue(ResultMsgConstant.error));
        try {
            Activity activity = activityDao.get(Activity.class, aid);
            if (activity != null) {
                Long privilegeId = activity.getPrivilegeId();
                Boolean result = privilegeId.equals(pid);
                returnValue.setFlag(result ? ReturnValue.FLAG_SUCCESS : ReturnValue.FLAG_FAIL);
                returnValue.setMeg(result ? me.getValue(ResultMsgConstant.querySuccess) : me.getValue(ResultMsgConstant.noPermissionToReviewMembers));
            }
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 用户发布图文查询
     * @param aid 活动id
     * @param page 页码
     * @return cn.lv.jewelry.index.indexActivity.frontBean.ActivityCommentBean
     */
    public ActivityCommentBean getUserGraphicById(String aid, Integer page, String uid) {
        ActivityCommentBean activityCommentBean = new ActivityCommentBean();
        try {
            // 获取主办方发布的最新一条图文id
            Page<Map<String,Object>> listOrganizer =
                    activityCommentDao.getCommentsForType(
                            1, Long.parseLong(aid), uid, page, null);
            List<Map<String,Object>> listOrganizersComments = listOrganizer.getPageContent();
            Object organizersCid = null;
            if (listOrganizersComments != null && listOrganizersComments.size() > 0) {
                organizersCid = listOrganizersComments.get(0).get("cid");
            }
            // 获取图文信息
            List<Map<String, Object>> listMapComment = new ArrayList<>();
            Page<Map<String,Object>> listUserComments =
                    activityCommentDao.getCommentsForType(
                            2, Long.parseLong(aid), uid, page, organizersCid);
            List<Map<String, Object>> listUserComment = listUserComments.getPageContent();
            if (listUserComment != null) {
                for (Map<String, Object> map : listUserComment) {
                    String commentType = map.get("commentType").toString();
                    if (!ActivityCommentType.ANNOUNCEMENT.getType().toString().equals(commentType)) {
                        Map<String, Object> mapComment = putMap(map, 1);
                        mapComment = putMapComment(mapComment, map, aid, uid);
                        listMapComment.add(mapComment);
                    }
                }
            }
            activityCommentBean.setHasNext(listUserComments.isHasNextPage());
            activityCommentBean.setComments(listMapComment);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return activityCommentBean;
    }

    /**
     * 发布的图文列表
     * @param pid 权限id
     * @param uid 用户id
     * @param page 页码
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @date 2017-11-16
     */
    public ReturnValue<ActivityCommentBean> getReleaseGraphicList(Long pid, String uid, Integer page, String userType) {
        ReturnValue<ActivityCommentBean> returnValue = new ReturnValue();
        try {
            ActivityCommentBean activityCommentBean = new ActivityCommentBean();
            List<Map<String, Object>> listMapComment = new ArrayList<>();

            String subuid = uid.substring(2);
            // 获取图文信息
            Page<Map<String, Object>> listComments =
                    activityCommentDao.getCommentsForType(0, null, subuid, page, null);
            List<Map<String, Object>> listComment = listComments.getPageContent();
            for (Map<String, Object> text : listComment) {
                Map<String, Object> map = putMap(text, 1);
                putMapComment(map, text, null, subuid);
                Activity activity = activityDao.get(Activity.class, Long.parseLong(text.get("aid").toString()));
                map.put("groupOrActivity", activity.getStatus() == ActivityStatusType.COMPLETE.getV() ? 1 : 0);
                map.put("subject", activity.getSubject());
                listMapComment.add(map);
            }
            activityCommentBean.setHasNext(listComments.isHasNextPage());
            activityCommentBean.setComments(listMapComment);
            returnValue.setObject(activityCommentBean);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.querySuccess));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 删除图文
     * @param pid 权限id
     * @param uid 用户id
     * @param cid 图文id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @date 2017-11-17
     */
    public ReturnValue deleteGraphic(Long pid, Integer uid, Long cid) {
        ReturnValue returnValue = new ReturnValue();
        try {
            ActivityComment comment = activityCommentDao.get(ActivityComment.class, cid);
            comment.setIsDelete(1);
            activityCommentDao.getSession().update(comment);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.deleteSuccess));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 删除评论的评论
     * @param ccid 评论的评论id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @date 2017-12-14
     */
    public ReturnValue deleteGraphicComment(Long ccid) {
        ReturnValue returnValue = new ReturnValue();
        try {
            ActivityCommentComment comment = activityCommentCommentDao.get(ActivityCommentComment.class, ccid);
            comment.setIsDelete(1);
            activityCommentDao.getSession().update(comment);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.deleteSuccess));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 举办方发布图文查询
     * @param aid 活动id
     * @param uid 用户id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ReturnValue<ActivityCommentBean> getOrganizersGraphic(String aid, String uid) {
        ReturnValue<ActivityCommentBean> returnValue = new ReturnValue();
        try {
            ActivityCommentBean activityCommentBean = getOrganizersGraphicById(aid, uid);
            returnValue.setObject(activityCommentBean);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.querySuccess));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 举办方发布图文查询
     * @param aid 活动id
     * @param uid 用户id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    private ActivityCommentBean getOrganizersGraphicById(String aid, String uid) {
        ReturnValue returnValue = new ReturnValue();
        ActivityCommentBean activityCommentBean = new ActivityCommentBean();
        try {
            // 获取图文信息
            List<Map<String, Object>> listMapComment = new ArrayList<>();
            Page<Map<String,Object>> page =
                    activityCommentDao.getCommentsForType(
                            1, Long.parseLong(aid), uid, 0, null);
            List<Map<String,Object>> mapList = page.getPageContent();
            // 状态不为公告
            if (mapList != null && mapList.size() > 0) {
                Map<String, Object> listComments = mapList.get(0);
                if (!ActivityCommentType.ANNOUNCEMENT.getType().toString()
                        .equals(listComments.get("commentType").toString())) {
                    Map<String, Object> mapComment = putMap(listComments, 1);
                    mapComment = putMapComment(mapComment, listComments, aid, uid);
                    listMapComment.add(mapComment);
                }
            }
            activityCommentBean.setHasNext(false);
            activityCommentBean.setComments(listMapComment);
        } catch (Exception ex) {
            ex.printStackTrace();
            returnValue.setFlag(ReturnValue.FLAG_EXCEPTION);
            returnValue.setMeg(SystemException.SYSTEM_EXCEPTION.getDescription());
        }
        return activityCommentBean;
    }

    /**
     * 将信息添加进map
     * @param mapComment 单个图文信息集合
     * @param listComments 所有图文信息集合
     * @param aid 活动id
     * @param uid 用户id
     * @author liumengwei
     * @date 2017/12/27
     */
    private Map<String, Object> putMapComment(
            Map<String, Object> mapComment, Map<String,Object> listComments, String aid, String uid) {
        mapComment.put("commentType", listComments.get("commentType"));
        // 评论id
        String cid = listComments.get("cid").toString();
        mapComment.put("cid", Long.parseLong(cid));
        // 查询评论的评论
        List<Map<String, Object>> maps = putSecondCommend(cid);
        mapComment.put("secondCommend", maps);
        Integer likeNum;
        // 获取点赞信息
        likeNum = likesDao.getLikesNum(Integer.parseInt(cid));
        mapComment.put("likeNum", likeNum);
        List<Map<String, Object>> listMapLikes = likesDao.getLikesById(Integer.parseInt(cid), aid);
        mapComment.put("likes", listMapLikes);
        Boolean isLike = false;
        isLike = sameUid(uid, isLike, listMapLikes);
        mapComment.put("isLike", 0);
        if (isLike) {
            mapComment.put("isLike", 1);
        }
        return mapComment;
    }

    /**
     * 添加二级评论信息
     * @param cid 图文id
     * @return java.util.Map
     * @author liumengwei
     * @date 2018/1/4
     */
    private List<Map<String, Object>> putSecondCommend(String cid) {
        // 查询评论的评论
        return getCommentComment(Long.parseLong(cid));
    }

    /**
     * 查看用户是否点赞该图文
     * @param uid 用户id
     * @param isLike 点赞状态
     * @param listMapLikes 点赞信息集合
     * @return java.langBoolean
     * @author liumengwei
     * @date 2017/12/28
     */
    private Boolean sameUid(String uid, Boolean isLike, List<Map<String, Object>> listMapLikes) {
        if (!StringUtil.isEmpty(uid)) {
            Long longUid = Long.parseLong(uid);
            for (Map<String, Object> mapLikes : listMapLikes) {
                Object likeUid = mapLikes.get("uid");
                if (likeUid != null) {
                    Long lUid = Long.parseLong(likeUid.toString());
                    if (lUid == longUid) {
                        isLike = true;
                        break;
                    }
                }
            }
        }
        return isLike;
    }

    /**
     * 获取往期精彩
     * @param aid 活动id
     * @param page 页码
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @date 2017-11-26
     */
    public ReturnValue<ActivityCommentBean> getWonderfulPast(String aid, Integer page) {
        ReturnValue<ActivityCommentBean> returnValue = new ReturnValue<>();
        ActivityCommentBean activityCommentBean = new ActivityCommentBean();
        try {
            // 获取往期精彩
            List<Map<String, Object>> listMapComment = new ArrayList<>();
            Page<Map<String,Object>> listComments = activityCommentDao.getWonderfulPast(Long.parseLong(aid), page);
            List<Map<String, Object>> listComment = listComments.getPageContent();
            if (listComment != null) {
                for (Map<String, Object> map : listComment) {
                    if (!"6".equals(map.get("commentType").toString())) {
                        String uid = map.get("uid").toString();
                        Map<String, Object> mapComment = putMap(map, 1);
                        mapComment = putMapComment(mapComment, map, aid, uid);
                        listMapComment.add(mapComment);
                    }
                }
            }
            activityCommentBean.setHasNext(listComments.isHasNextPage());
            activityCommentBean.setComments(listMapComment);
            returnValue.setObject(activityCommentBean);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.querySuccess));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 添加往期精彩
     * @param aid 活动id
     * @param cid 评论id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @date 2017-11-26
     */
    public ReturnValue addWonderfulPast(Long aid, String cid) {
        ReturnValue returnValue = new ReturnValue<>();
        try {
            // 添加往期精彩
            if (StringUtil.isEmpty(cid)) {
                returnValue.setFlag(ReturnValue.FLAG_FAIL);
            }
            String[] sCid = cid.split(",");
            Activity activity = new Activity();
            activity.setId(aid);
            for (String commentId : sCid) {
                ActivityWonderfulPast past = activityWonderfulPastDao.getInfoByAidCid(aid, cid);
                if (past == null) {
                    past = new ActivityWonderfulPast();
                    ActivityComment comment = new ActivityComment();
                    comment.setId(Long.parseLong(commentId));
                    past.setCid(comment);
                    past.setAid(activity);
                    past.setIsDelete(ActivityWonderfulPastDelete.NOT_DELETE.getStatus());
                    past.setInsertTime(System.currentTimeMillis() / 1000);
                    activityWonderfulPastDao.save(past);
                }
            }
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.addSuccess));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 获取评论的评论
     *
     * @param cid 评论id
     * @return java.util.List
     */
    private List<Map<String, Object>> getCommentComment(Long cid) {
        List<Map<String, Object>> maps = new ArrayList<>();
        // 获取评论的评论的数量
        Integer commentCommentNumber =
                activityCommentCommentDao.getCommentNumber(cid);
        if (commentCommentNumber != null && commentCommentNumber > 0) {
            // 获取评论的评论信息
            List<Map<String, Object>> listMapCommentCommentUser =
                    activityCommentCommentDao.getCommentCommentByAid(cid);
            if (listMapCommentCommentUser != null && listMapCommentCommentUser.size() > 0) {
                for (Map<String, Object> map1 : listMapCommentCommentUser) {
                    Map<String, Object> mapCommentCommentUser = putMap(map1, 2);
                    maps.add(mapCommentCommentUser);
                }
            }
        }
        return maps;
    }

    /**
     * 我参与的活动下其他用户
     * @param aid 活动id
     * @param page 页码
     * @param pid 用户权限id
     * @param uid 用户id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2017/11/15
     */
    public ReturnValue<ActivityAttendUser> getActivityAttendUserList(Long pid, String uid, Long aid, Integer page, Integer userType) {
        ReturnValue<ActivityAttendUser> returnValue = new ReturnValue();
        try {
            ActivityAttendUser activityAttendUser = new ActivityAttendUser();
            List<ActivityAttendUser.Users> list = new ArrayList<>();
            // 获取总页数
            Integer totalPages = activityAttenceDao.getActivityAttendUserListTotalPages(pid, aid);
            String subuid = uid.substring(2);
            String headPic = "";
            if (userType == ActivityPrivilegeStatus.PERSONAL.getStatus()) {
                User use = userDao.get(User.class, Integer.parseInt(subuid));
                headPic = use.getHeadpic();
            } else if (userType == ActivityPrivilegeStatus.RHIRD_PARTY.getStatus()) {
                UserBind use = userBindDao.get(UserBind.class, Integer.parseInt(subuid));
                headPic = use.getHeadpic();
            }

            MyInfo myInfo = myInfoDao.getMyInfo(pid);
            // 获取用户列表
            Page<Map<String, Object>> usersPage = activityAttenceDao.getAttendUserList(pid, aid, page);
            List<Map<String, Object>> usersList = usersPage.getPageContent();
            if (usersList != null) {
                for (Map<String, Object> users1 : usersList) {
                    ActivityAttendUser.Users users = new ActivityAttendUser().new Users();
                    ActivityPrivilege privilege =
                            activityPrivilegeDao.get(
                                    ActivityPrivilege.class, Long.parseLong(users1.get("privilege_id").toString()));
                    String userHeadPic = "";
                    String username = "";
                    Long userUid = null;
                    if (Integer.parseInt(privilege.getUserType()) == ActivityPrivilegeStatus.PERSONAL.getStatus()) {
                        User user = userDao.get(User.class, Integer.parseInt(privilege.getUid().toString()));
                        userHeadPic = user.getHeadpic();
                        username = user.getUsername();
                        userUid = Long.parseLong(String.valueOf(user.getId()));
                    } else if (Integer.parseInt(privilege.getUserType()) == ActivityPrivilegeStatus.RHIRD_PARTY.getStatus()) {
                        UserBind user = userBindDao.get(UserBind.class, Integer.parseInt(privilege.getUid().toString()));
                        userHeadPic = user.getHeadpic();
                        username = user.getNickname();
                        userUid = Long.parseLong(String.valueOf(user.getId()));
                    }
                    users.setHeadPic(StringUtil.isEmpty(userHeadPic) ? "" : userHeadPic);
                    String self_introduce = String.valueOf(users1.get("self_introduce"));
                    users.setSelfIntroduction(StringUtil.isEmpty(self_introduce) ? "" : self_introduce);
                    users.setUsername(StringUtil.isEmpty(username) ? "" : username);
                    users.setUid(userUid);
                    users.setPid(privilege.getId());
                    list.add(users);
                }
            }
            activityAttendUser.setRegisterEndTime(
                    DateUtil.getDateTime(activityDao.get(Activity.class, aid).getRegisterEndTime()));
            activityAttendUser.setHeadPic(StringUtil.isEmpty(headPic) ? "" : headPic);
            activityAttendUser.setCoverImage(myInfo.getCoverImage());
            activityAttendUser.setUsers(list);
            activityAttendUser.setTotalPages(totalPages);
            activityAttendUser.setHasNext(usersPage.isHasNextPage());
            returnValue.setObject(activityAttendUser);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.querySuccess));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 退出我参与的活动或圈子
     * @param aid 活动id
     * @param pid 权限id
     * @param uid 用户id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2017/11/15
     */
    public ReturnValue quitActivity(String uid, Long pid, Long aid) {
        ReturnValue returnValue = new ReturnValue();
        try {
            String[] strPid = new String[1];
            strPid[0] = pid.toString();
            Boolean result =
                    activityAttenceDao.updateUserAttentStatus(
                            strPid, aid, ActivityAttenceStatus.DROP_OUT.getStatus());
            returnValue.setFlag(result ? ReturnValue.FLAG_SUCCESS : ReturnValue.FLAG_FAIL);
            returnValue.setMeg(result ? me.getValue(ResultMsgConstant.dropOutSuccess) : me.getValue(ResultMsgConstant.dropOutFail));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 我参与的活动下其他用户
     * @param aid 活动id
     * @param pid 权限id
     * @param uid 用户id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2017/11/24
     */
    public ReturnValue quitInitiatedActivityCircle(Long uid, Long pid, Long aid) {
        ReturnValue returnValue = new ReturnValue();
        try {
            Activity activity = activityDao.get(Activity.class, aid);
            // 修改活动状态
            activity.setStatus(ActivityStatusType.DELETE.getV());
            activityDao.getSession().update(activity);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.deleteSuccess));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 查看活动评论
     * @param aid 活动id
     * @param page 页码
     * @param status 状态
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ReturnValue<ActivityCommentBean> getComment(String aid, String page, String status) {
        ReturnValue<ActivityCommentBean> returnValue = new ReturnValue();
        try {
            ActivityCommentBean activityCommentBean = getCommentBeanById(
                    aid, Integer.parseInt(page), Integer.parseInt(status));
            returnValue.setObject(activityCommentBean);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.querySuccess));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 查看活动评论
     * @param aid 活动id
     * @param page 页码
     * @param status 状态
     * @return cn.lv.jewelry.index.indexActivity.frontBean.ActivityCommentBean
     */
    public ActivityCommentBean getCommentBeanById(String aid, Integer page, Integer status) {
        ReturnValue returnValue = new ReturnValue();
        ActivityCommentBean activityCommentBean = new ActivityCommentBean();
        activityCommentBean.setHasNext(false);
        List<Map<String, Object>> listMap = new ArrayList<>();
        Page<Map<String, Object>> listMapCommentUser;
        try {
            // 获取该活动评论数量
            Integer commentNumber = activityCommentDao.getCommentNumber(Long.parseLong(aid), status);
            activityCommentBean.setCommentNumber(commentNumber);
            if (commentNumber != null) {
                // 查询一级评论信息
                listMapCommentUser =
                        activityCommentDao.getCommentByAid(Long.parseLong(aid), page, status);
                List<Map<String, Object>> list = listMapCommentUser.getPageContent();
                if (list != null && list.size() > 0) {
                    for (Map<String, Object> map : list) {
                        Map<String, Object> mapCommentUser = putMap(map, 1);
                        // 查询评论的评论
                        String cid = map.get("cid").toString();
                        mapCommentUser.put("cid", Long.parseLong(cid));
                        List<Map<String, Object>> maps = getCommentComment(Long.parseLong(cid));
                        mapCommentUser.put("secondCommend", maps);
                        listMap.add(mapCommentUser);
                    }
                }
                // 是否有下一页
                activityCommentBean.setHasNext(listMapCommentUser.isHasNextPage());
            }
            activityCommentBean.setComments(listMap);
        } catch (Exception ex) {
            ex.printStackTrace();
            returnValue.setFlag(ReturnValue.FLAG_EXCEPTION);
            returnValue.setMeg(SystemException.SYSTEM_EXCEPTION.getDescription());
        }
        return activityCommentBean;
    }

    /**
     * 添加一级评论内容
     * @param map 参数集
     * @return java.util.Map
     */
    private Map<String, Object> putMap(Map<String, Object> map, Integer level) {
        Map<String, Object> mapCommentCommentUser = new HashMap<>();
        Long mapcid = Long.parseLong(map.get("cid").toString());
        mapCommentCommentUser.put("cid", mapcid);
        Object username = map.get("username");
        Object headPic = map.get("headPic");
        mapCommentCommentUser.put("username", username == null ? "" : username);
        mapCommentCommentUser.put("headPic", headPic == null ? "" : headPic);
        String uid = map.get("uid").toString();
        mapCommentCommentUser.put("uid", Long.parseLong(uid));
        Object utype = map.get("userType");
        if (level == 1) {
            Integer isDelete = Integer.parseInt(map.get("isDelete").toString());
            Integer userType = Integer.parseInt(map.get("userType").toString());
            ActivityPrivilege privilege = activityPrivilegeDao.getId(Long.parseLong(uid), userType);
            List<ActivityPrivilege> getActivityTakePrivilege = activityCommentDao.getUserOrOrganizer(mapcid);
            Integer getUserOrOrganizer = 0;
            for (ActivityPrivilege privilege1 : getActivityTakePrivilege) {
                if (privilege1.getId() == privilege.getId()) {
                    getUserOrOrganizer = 1;
                    break;
                }
            }
            mapCommentCommentUser.put("userType", getUserOrOrganizer);
            mapCommentCommentUser.put("isDelete", isDelete);
        }
        mapCommentCommentUser.put("commentTime", DateUtil.getDateTime(map.get("commentTime").toString()));
        Object commentContent = map.get("commentContent");
        mapCommentCommentUser.put("commentContent", "");
        Object type = map.get("commentType");
        if (commentContent != null) {
            if (ActivityCommentType.PIC.getType().equals(type)) {
                JSONObject obj = JSON.parseObject(commentContent.toString());
                String content = HtmlUtils.htmlUnescape(obj.getString("content"));
                String pic = obj.getString("pic");
                String text = obj.getString("text");
                String graphicType = obj.getString("graphicType");
                mapCommentCommentUser.put("isShare", "1".equals(graphicType) ? 0 : 1);
                List<Map<String, Object>> list = new ArrayList<>();
                Map<String, Object> stringObjectMap = new HashMap<>();
                stringObjectMap.put("content", StringUtil.isEmpty(content) ? "" : content);
                Integer isDelete = Integer.parseInt(map.get("isDelete").toString());
                if (isDelete == 1) {
                    stringObjectMap.put("content", "该图文已被删除，不能查看");
                    mapCommentCommentUser.put("isShare", 0);
                }
                stringObjectMap.put("text", text);
                stringObjectMap.put("pic", pic);
                stringObjectMap.put("graphicType", StringUtil.isEmpty(graphicType) ? 0 : Integer.parseInt(graphicType));
                list.add(stringObjectMap);
                mapCommentCommentUser.put("commentContent", list);
            } else if (ActivityCommentType.FORWARDING_ACTIVIGTIES.getType().equals(type)) {
                // 状态为转发活动
                mapCommentCommentUser.put("isShare", 1);
                Long id = Long.parseLong(commentContent.toString());
                ActivityBean activityBean = new ActivityBean();
                Activity activity = activityDao.get(Activity.class, id);
                Integer userType = Integer.parseInt(map.get("userType").toString());
                ActivityBean bean = addActivityBean(activityBean, activity, uid, String.valueOf(userType));
                mapCommentCommentUser.put("commentContent", JSONObject.toJSON(bean));
            } else if (ActivityCommentType.SHARECOMMENTS.getType().equals(type)) {
                // 状态为转发图文
                mapCommentCommentUser.put("isShare", 1);
                Long cid = Long.parseLong(commentContent.toString());
                List<Map<String, Object>> text = activityCommentTextDao.getActivityComment(cid);
                Map<String, Object> mapComment = new HashMap<>();
                Map<String, Object> mapText = text.get(0);
                Object forwardType = mapText.get("commentType");
                if (text != null) {
                    mapComment = putMap(mapText, 1);
                    mapComment.put("commentType", forwardType);
                    mapComment.put("cid", cid);
                    // 获取点赞信息
                    Integer likeNum = likesDao.getLikesNum(Integer.parseInt(cid.toString()));
                    mapComment.put("likeNum", likeNum);
                    List<Map<String, Object>> listMapLikes =
                            likesDao.getLikesById(Integer.parseInt(cid.toString()),
                                    mapText.get("aid").toString());
                    mapComment.put("likes", listMapLikes);
                }
                mapCommentCommentUser.put("commentContent", mapComment);
            } else {
                mapCommentCommentUser.put("commentContent", commentContent);
            }
        } else {
            mapCommentCommentUser.put("commentContent", "");
        }
        return mapCommentCommentUser;
    }

    public List<ActivityBean> getActivitingTop(int n, int status) {
        List<Activity> activityList = activityDao.getACtivityTop(n, status);
        return getActivityTop(activityList);

    }

    private List<ActivityBean> getActivityTop(List<Activity> activityList) {
        List<ActivityBean> list = new ArrayList<>();
        for (int i = 0; i < activityList.size(); i++) {
            Activity activity = activityList.get(i);
            ActivityBean activityBean = new ActivityBean();
            activityBean.setId(String.valueOf(activity.getId()));
            activityBean.setHeadPic(activity.getIndexPic());
            activityBean.setName(activity.getEname());
            activityBean.setOrganizer(activity.getOrganizer());
            activityBean.setSubject(activity.getSubject());
            activityBean.setAddress(activity.getAddress());
            activityBean.setBeginDateTime(DateUtil.getFromUnix(activity.getStartTime()));
            activityBean.setEndDateTime(DateUtil.getFromUnix(activity.getEndTime()));
            activityBean.setNumber(activity.getActivityNum());
            activityBean.setStatus(activity.getStatus());
            list.add(activityBean);
        }
        return list;
    }

    public List<ActivityBean> getCompleteActivity(int n, int status) {
        List<Activity> activityList = activityDao.getCompleteACtivityTop(n - 1, status);
        List<Activity> unactivityList = activityDao.getACtivityTop(6, ActivityStatusType.UPCOMING.getV());
        Random random = new Random();
        int i = random.nextInt(5);
        activityList.add(0, unactivityList.get(i));

        return getActivityTop(activityList);
    }

    private cn.lv.jewelry.index.indexActivity.frontBean.ActivityContent produceActiviyContent(String content, int type) {
        if (type == ActivityContentType.TEXT.getV()) {
            ActivityContentTextType tt = new ActivityContentTextType();
            cn.lv.jewelry.index.indexActivity.frontBean.ActivityContent<ActivityContentTextType> c =
                    new cn.lv.jewelry.index.indexActivity.frontBean.ActivityContent<>();
            tt.setText(content);
            c.setContent(tt);
            c.setType(type);
            return c;
        }
        if (type == ActivityContentType.IMG.getV()) {
            ActivityContentImageType imageType = new ActivityContentImageType();
            imageType.setUrl(content);
            cn.lv.jewelry.index.indexActivity.frontBean.ActivityContent<ActivityContentImageType> c =
                    new cn.lv.jewelry.index.indexActivity.frontBean.ActivityContent<>();
            c.setContent(imageType);
            c.setType(type);

            return c;
        }
        return null;

    }

    /**
     * 保存活动主体信息
     *
     * @param activityBean
     * @return
     */
    public ReturnValue<ActivityBean> saveActivity(ActivityBean activityBean) {
        ReturnValue<ActivityBean> rv = new ReturnValue<>();
        try {
            Activity ac = new Activity();
            ac.setActivityNum(0);
            ac.setAddress(activityBean.getAddress());
            ac.setDescription(activityBean.getSubject());
            Enterprise enterprise = enterpriseDao.get(Enterprise.class, activityBean.getPid());
            ac.setPrivilegeId(activityBean.getPid());
//            ac.setEid(enterprise);
            ac.setEname(enterprise.getName());
            ac.setEslogan(enterprise.getSlogan());
            ac.setEdescription(enterprise.getDescription());
            ac.setEndTime(DateUtil.getDateUnix(activityBean.getEndDateTime(), DateUtil.formatDay));
            ac.setStartTime(DateUtil.getDateUnix(activityBean.getBeginDateTime(), DateUtil.formatDay));
            ac.setIndexPic(activityBean.getHeadPic());
            ac.setOrganizer(activityBean.getOrganizer());
            ac.setLogoPic(activityBean.getHeadPic());
            ac.setSubject(activityBean.getSubject());
            ac.setTitle(activityBean.getSubject());
            ac.setLevel(activityBean.getLevel());
            ac.setStatus(ActivityStatusType.UNPUBLISHED.getV());
            ac.setIndexPic(activityBean.getHeadPic());
            ac.setInsertTime(System.currentTimeMillis() / 1000);
            Integer i = 0;
            if (activityDao.save(ac) != i) {
//                //添加tag信息
//                for (String tag : activityBean.getTags()) {
//                    lvTagDao.save(ac.getId(), tag, 1);
//                }
                activityBean.setId(String.valueOf(ac.getId()));
                rv.setObject(activityBean);
                rv.setFlag(ReturnValue.FLAG_SUCCESS);
            }
        } catch (Exception ex) {
            rv.setFlag(ReturnValue.FLAG_EXCEPTION);
            ex.printStackTrace();
        }
        return rv;
    }


    /**
     * 更新活动主体信息
     *
     * @param activityBean
     * @return
     */
    public ReturnValue updateActivity(ActivityBean activityBean) {
        ReturnValue rv = new ReturnValue();
        try {
            Activity ac = activityDao.get(Activity.class, Long.parseLong(activityBean.getId()));
            if (ac != null) {
                ac.setStatus(activityBean.getStatus());
                ac.setAddress(activityBean.getAddress());
                ac.setDescription(activityBean.getSubject());
                Enterprise enterprise = enterpriseDao.get(Enterprise.class, activityBean.getPid());
                ac.setPrivilegeId(activityBean.getPid());
                ac.setEname(enterprise.getName());
                ac.setEslogan(enterprise.getSlogan());
                ac.setEdescription(enterprise.getDescription());
                ac.setEndTime(DateUtil.getDateUnix(activityBean.getEndDateTime(), DateUtil.formatDay));
                ac.setStartTime(DateUtil.getDateUnix(activityBean.getBeginDateTime(), DateUtil.formatDay));
                ac.setIndexPic(activityBean.getHeadPic());
                ac.setOrganizer(activityBean.getOrganizer());
                ac.setLogoPic(activityBean.getHeadPic());
                ac.setSubject(activityBean.getSubject());
                ac.setTitle(activityBean.getSubject());
                ac.setLevel(activityBean.getLevel());
                ac.setUpdateTime(System.currentTimeMillis() / 1000);
                ac.setIndexPic(activityBean.getHeadPic());
                activityDao.getSession().update(ac);
                rv.setObject(activityBean);
                rv.setFlag(ReturnValue.FLAG_SUCCESS);
            } else {
                rv.setFlag(ReturnValue.FLAG_FAIL);
            }

        } catch (Exception ex) {
            rv.setFlag(ReturnValue.FLAG_EXCEPTION);
            ex.printStackTrace();
        }
        return rv;
    }

    /**
     * 更新活动主体状态信息
     *
     * @param activityBean 接收活动相关参数
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ReturnValue<ActivityBean> updateActivityStatus(ActivityBean activityBean) {
        ReturnValue<ActivityBean> rv = new ReturnValue<>();
        try {
            // 获取活动信息
            Activity ac = activityDao.get(Activity.class, Long.parseLong(activityBean.getId()));
            if (ac != null) {
                // 获取活动状态
                ActivityStatusType statusType = ActivityStatusType.type(activityBean.getStatus());
                if (statusType == null) {
                    rv.setFlag(ReturnValue.FLAG_FAIL);
                    rv.setMeg(me.getValue(ResultMsgConstant.activityStatusError));
                } else {
                    // 修改状态
                    ac.setStatus(statusType.getV());
                    ac.setIndexPic(activityBean.getHeadPic());
                    activityDao.getSession().update(ac);
                    rv.setObject(activityBean);
                    rv.setFlag(ReturnValue.FLAG_SUCCESS);
                    rv.setMeg(me.getValue(ResultMsgConstant.modifySuccess));
                }
            } else {
                rv.setMeg(me.getValue(ResultMsgConstant.activityError));
                rv.setFlag(ReturnValue.FLAG_FAIL);
            }

        } catch (Exception ex) {
            return SystemException.setResult(rv, ex, logger);
        }
        return rv;
    }

    /**
     * 更新发布活动主体状态信息,同时添加一篇新的内容
     *
     * @param activityBean
     * @return
     */
    public ReturnValue updateActivityStatusWithContent(
            String aid, Long pid, ActivityContentString activityContentString, ActivityBean activityBean) {
        ReturnValue rv = new ReturnValue();
        try {
            Activity ac = activityDao.get(Activity.class, Long.parseLong(aid));
            if (ac != null) {
                ActivityStatusType statusType = ActivityStatusType.type(activityBean.getStatus());
                Boolean draft = activityBean.getDraft() == 1 ? true : false;
                Integer status = activityBean.getStatus();
                rv = isDraft(draft, ac, statusType, status, rv);
                if (rv.getFlag() == ReturnValue.FLAG_FAIL)
                    return rv;
                ac.setPushColumnsPage(ActivityReviewType.NOT_PUSH_COLUMNS.getType());
                ac.setPushIndexPage(ActivityReviewType.NOT_PUSH_INDEX.getType());
                ac.setUpTime(null);
                ac.setUpTimeColumns(null);
                ac.setMapPic(activityBean.getMapPic());
                ac.setSubject(activityBean.getSubject());
                ac.setTitle(activityBean.getDescription());
                ac.setIndexPic(activityBean.getIndexPic());
                ac.setLogoPic(activityBean.getHeadPic());
                ac.setAddress(activityBean.getAddress());
                ac.setStartTime(Long.parseLong(activityBean.getBeginDateTime()));
                ac.setEndTime(Long.parseLong(activityBean.getEndDateTime()));
                ac.setPrivilegeId(pid);
                ac.setDescription(activityBean.getDescription());
                ac.setRegisterEndTime(activityBean.getRegisterEndTime());
                ac.setTelephone(activityBean.getTelephone());
                ac.setEmail(activityBean.getEmail());
                ac.setMobile(activityBean.getMobile());
                ac.setFee(activityBean.getFee());
                ac.setIndexPicIsShow(activityBean.getIndexPicIsShow());
                ac.setActivityNum(activityBean.getActivityNum());
                ac.setVideoImageIndex(activityBean.getVideoImageIndex());
                ac.setVideoImageLogo(activityBean.getVideoImage());
                String[] organizers = activityBean.getOrganizer().split(",");
                rv = addOrganizers(organizers, rv);
                if (rv.getFlag() == ReturnValue.FLAG_FAIL) {
                    return rv;
                }
                activityDao.getSession().update(ac);
                cn.lv.jewelry.activity.daoBean.ActivityContent activityContent =
                        activityContentDao.getActivityContent(ac.getId());
                if (activityContent == null) {
                    activityContent =  new ActivityContent();
                    activityContent.setAid(ac);
                    activityContent.setType(activityContentString.getType());
                    activityContent.setContent(activityContentString.getContent());
                    activityContent.setActivityStatus(ac.getStatus());
                    if (!draft) {
                        activityContentDao.save(activityContent);
                    }
                }
                activityContentDao.getSession().update(activityContent);

                String rid = activityBean.getRid();
                rv = addRelativeActivity(rid, rv, ac);
                if (rv.getFlag() == ReturnValue.FLAG_FAIL) {
                    return rv;
                }
                Map<String, Object> map = new HashMap<>();
                map.putAll(ActivityBeanMap.getFindMap(activityBean));
                map.putAll(activityContentString.getMap());
                if (StringUtil.isEmpty(activityBean.getRid())) {
                    map.put("rid", "");
                }
                map.put("aid", ac.getId());
                map.put("pid", pid);
                rv.setObject(map);
                rv.setFlag(ReturnValue.FLAG_SUCCESS);
            } else {
                rv.setFlag(ReturnValue.FLAG_FAIL);
                rv.setMeg(me.getValue(ResultMsgConstant.activityDoesNotExist));
                return rv;
            }
        } catch (Exception ex) {
            rv.setFlag(ReturnValue.FLAG_EXCEPTION);
            rv.setMeg(ex.getMessage());
            ex.printStackTrace();
        }
        return rv;
    }

    /**
     * 发现升级圈子
     * @param aid 活动id
     * @param activityContentString 活动详情相关参数
     * @param activityBean 活动信息相关参数
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/12/22
     */
    public ReturnValue<ActivityBean> updateFindActivityStatusAndContent(
            ActivityContentString activityContentString, String aid, ActivityBean activityBean) {
        ReturnValue rv = new ReturnValue<>();
        try {
            Long Laid = Long.parseLong(aid);
            Activity ac = activityDao.get(Activity.class, Laid);
            if (ac != null) {
                String draft = activityBean.getDraft().toString();
                Integer updateStatus = activityContentString.getStatus();
                rv = modifyDraftOrStatus(ac, draft, updateStatus, rv);
                if (rv.getFlag() == ReturnValue.FLAG_FAIL)
                    return rv;
                else
                    ac.setStatus(updateStatus);
                String indexPic = activityBean.getIndexPic();
                String logoPic = activityBean.getHeadPic();

                List<String> listPic = new ArrayList<>();
                listPic.add(indexPic);
                listPic.add(logoPic);
                // 访问图片 查看图片是否真实存在
                rv = ImageUtils.accessImage(rv, listPic);
                if (rv.getFlag() == ReturnValue.FLAG_FAIL) {
                    return rv;
                }
                ac.setIndexPic(indexPic);
                ac.setLogoPic(logoPic);

                String subject = activityBean.getSubject();
                if (!StringUtil.isEmpty(subject)) {
                    ac.setSubject(subject);
                }
                String description = activityBean.getDescription();
                if (!StringUtil.isEmpty(description)) {
                    ac.setTitle(description);
                }
//                ac.setPushColumnsPage(ActivityReviewType.NOT_PUSH_COLUMNS.getType());
//                ac.setPushIndexPage(ActivityReviewType.NOT_PUSH_INDEX.getType());
//                ac.setUpTime(null);
//                ac.setUpTimeColumns(null);
                ac.setCheckStatus(ActivityReviewType.BY.getType());
                ac.setDraft("true".equals(draft) ? 1 : 0);
                activityDao.getSession().update(ac);

                cn.lv.jewelry.activity.daoBean.ActivityContent content =
                        activityContentDao.getActivityContent(ac.getId());
                if (content != null) {
                    if (content.getActivityStatus() == updateStatus) {
                        rv.setMeg(me.getValue(ResultMsgConstant.modifyFail));
                        rv.setFlag(ReturnValue.FLAG_FAIL);
                        return rv;
                    }
                }
                ActivityContent activityContent = new ActivityContent();
                activityContent.setAid(ac);
                activityContent.setType(activityContentString.getType());
                String accontent = activityContentString.getContent();
                if (!StringUtil.isEmpty(accontent)) {
                    activityContent.setContent(accontent);
                }
                activityContent.setActivityStatus(ac.getStatus());
                activityContentDao.save(activityContent);
                Map<String, Object> map = new HashMap<>();
                map.putAll(ActivityBeanMap.updateFindActivityStatusAndContent(activityBean));
                map.putAll(activityContentString.getMap());
                map.put("subject", ac.getSubject());
                map.put("aid", ac.getId());
                rv.setObject(map);
                rv.setFlag(ReturnValue.FLAG_SUCCESS);
            } else {
                rv.setMeg(me.getValue(ResultMsgConstant.modifyFail));
                rv.setFlag(ReturnValue.FLAG_FAIL);
                return rv;
            }
        } catch (Exception ex) {
            rv.setFlag(ReturnValue.FLAG_EXCEPTION);
            rv.setMeg(ex.getMessage());
            ex.printStackTrace();
        }
        return rv;
    }

    /**
     * 根据用户输入draft updateSatus判断是否可更改活动状态
     * @param activity 库中活动信息
     * @param draft 用户输入草稿状态
     * @param updateSatus 用户输入状态
     * @param rv 结果集
     * @param <T>
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @date 2017-12-24
     */
    private <T> ReturnValue<T> modifyDraftOrStatus(Activity activity, String draft, Integer updateSatus, ReturnValue<T> rv) {
        Integer dataDraft = activity.getDraft();
        Integer dataStatus = activity.getStatus();
        Integer inputDraft = "true".equals(draft) ? 1 : 0;
        if (dataDraft == ActivityDraftType.FALSE.getType()) {
            if (ActivityDraftType.TRUE.getType().equals(inputDraft)) {
                rv = ComparedActivityDataStatusAndInputStatus(dataStatus, updateSatus, rv);
                if (rv.getFlag() == ReturnValue.FLAG_FAIL) return rv;
            } else {
                Integer checkStatus = activity.getCheckStatus();
                if (ActivityReviewType.PENDING_REVIEW.getType() == checkStatus ||
                        ActivityReviewType.DID_NOT_PASS.getType() == checkStatus ||
                        ActivityReviewType.DELETE.getType() == checkStatus) {
                    rv.setFlag(ReturnValue.FLAG_FAIL);
                    rv.setMeg(me.getValue(ResultMsgConstant.thisReviewStatusCanNotBeUpgraded));
                    return rv;
                }
                rv = ComparedActivityDataStatusAndInputStatus(dataStatus, updateSatus, rv);
                if (rv.getFlag() == ReturnValue.FLAG_FAIL) return rv;
            }
        } else {
            if (dataStatus == ActivityStatusType.UPCOMING.getV() ||
                    dataStatus == ActivityStatusType.UNPUBLISHED.getV()) {
                rv.setFlag(ReturnValue.FLAG_FAIL);
                rv.setMeg(me.getValue(ResultMsgConstant.draftStatusCanNotBeUpgraded));
                return rv;
            }
            if (ActivityDraftType.FALSE.getType().equals(inputDraft)) {
                rv.setFlag(ReturnValue.FLAG_SUCCESS);
                return rv;
            } else {
                rv.setFlag(ReturnValue.FLAG_SUCCESS);
                return rv;
            }
        }
        return rv;
    }

    /**
     * 对比库中状态和用户输入状态是否可行
     * @param dataStatus 库中状态
     * @param updateSatus 用户输入状态
     * @param rv 结果集
     * @param <T>
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @date 2017-12-24
     */
    private <T> ReturnValue<T> ComparedActivityDataStatusAndInputStatus(
            Integer dataStatus, Integer updateSatus, ReturnValue<T> rv) {
        if (dataStatus == ActivityStatusType.UPCOMING.getV()) {
            if (updateSatus == ActivityStatusType.ONGOING.getV()) {
                rv.setFlag(ReturnValue.FLAG_SUCCESS);
                return rv;
            } else {
                rv.setFlag(ReturnValue.FLAG_FAIL);
                rv.setMeg(me.getValue(ResultMsgConstant.thisActivityStatusIsNotUpgradeable));
                return rv;
            }
        } else if(dataStatus == ActivityStatusType.ONGOING.getV()) {
            if (updateSatus == ActivityStatusType.COMPLETE.getV()) {
                rv.setFlag(ReturnValue.FLAG_SUCCESS);
                return rv;
            } else {
                rv.setFlag(ReturnValue.FLAG_FAIL);
                rv.setMeg(me.getValue(ResultMsgConstant.thisActivityStatusIsNotUpgradeable));
                return rv;
            }
        } else {
            rv.setFlag(ReturnValue.FLAG_FAIL);
            rv.setMeg(me.getValue(ResultMsgConstant.activityStatusError));
            return rv;
        }
    }

    private ReturnValue isDraft(
            Boolean draft, Activity ac, ActivityStatusType statusType, Integer status, ReturnValue rv) {
        // 如果存为草稿
        if (draft) {
            ac.setDraft(ActivityDraftType.TRUE.getType());
        }
        // 如果不存为草稿
        if (!draft) {
            if (statusType == null) {
                // 如果活动状态为空
                rv.setFlag(ReturnValue.FLAG_FAIL);
            } else if (ac.getStatus() == ActivityStatusType.UNPUBLISHED.getV() &&
                    ac.getDraft() == ActivityDraftType.TRUE.getType() &&
                    status == ActivityStatusType.UNPUBLISHED.getV()) {
                // 如果 数据库中活动已为草稿状态 活动状态为探索，传入活动状态也为探索
                ac.setDraft(ActivityDraftType.FALSE.getType());
            } else if (ac.getStatus() == ActivityStatusType.UNPUBLISHED.getV() &&
                    ac.getDraft() == ActivityDraftType.FALSE.getType() &&
                    status == ActivityStatusType.UNPUBLISHED.getV()) {
                // 如果 活动状态为探索，传入活动状态也为探索
            } else if (ac.getStatus() == ActivityStatusType.UPCOMING.getV() &&
                    ac.getDraft() == ActivityDraftType.TRUE.getType() &&
                    status == ActivityStatusType.UPCOMING.getV()) {
                // 如果 数据库中活动已为草稿状态 活动状态为发现，传入活动状态也为发现
                ac.setDraft(ActivityDraftType.FALSE.getType());
            } else if (ActivityStatusType.isAvailStatus(ac.getStatus(), status)) {
                // 其他类型情况 判断类型修改是否正确
                ac.setDraft(ActivityDraftType.FALSE.getType());
                ac.setStatus(status);
                if (activityContentDao.existContent(ac.getId(), ac.getStatus())) {
                    throw new RuntimeException("there has exist the content under the aid");
                }
            } else {
                // 其他活动状态
                rv.setFlag(ReturnValue.FLAG_FAIL);
                rv.setMeg(me.getValue(ResultMsgConstant.activityStatusError));
                return rv;
            }
        }
        return rv;
    }

    /**
     * 更新探索活动主体状态信息,同时添加一篇新的内容
     * @param activityContentString 活动详细内容相关参数
     * @param activityBean 活动框架信息相关参数
     * @param aid 活动id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2017/11/22
     */
    public ReturnValue updateExploreActivityStatusWithContent(
            ActivityContentString activityContentString, ActivityExploreFront activityBean, Long aid) {
        ReturnValue rv = new ReturnValue();
        try {
            Activity ac = activityDao.get(Activity.class, aid);
            if (ac != null) {
                ActivityStatusType statusType = ActivityStatusType.type(activityBean.getStatus());
                Integer draft = activityBean.getDraft();
                Integer status = activityBean.getStatus();
                Boolean bDraft = draft == 1 ? true : false;
                rv = isDraft(bDraft, ac, statusType, status, rv);
                if (rv.getFlag() == ReturnValue.FLAG_FAIL)
                    return rv;
                ac.setPushColumnsPage(ActivityReviewType.NOT_PUSH_COLUMNS.getType());
                ac.setPushIndexPage(ActivityReviewType.NOT_PUSH_INDEX.getType());
                ac.setUpTime(null);
                ac.setUpTimeColumns(null);
                ac.setSubject(activityBean.getSubject());
                ac.setIndexPic(activityBean.getIndexPic());
                ac.setLogoPic(activityBean.getHeadPic());
                ac.setIndexPicIsShow(activityBean.getIndexPicIsShow());
                activityDao.getSession().update(ac);

                List<ActivityContent> contentList = activityContentDao.selectContent(aid, status);
                if (contentList == null || contentList.size() < 1) {
                    ActivityContent activityContent = new ActivityContent();
                    activityContent.setAid(ac);
                    activityContent.setType(activityContentString.getType());
                    activityContent.setContent(activityContentString.getContent());
                    activityContent.setActivityStatus(ac.getStatus());
                    activityContentDao.save(activityContent);
                } else {
                    for (int i = 0; i < contentList.size(); i++) {
                        ActivityContent activityContent = contentList.get(i);
                        activityContent.setContent(activityContentString.getContent());
                        activityContentDao.getSession().update(activityContent);
                    }
                }

                Map<String, Object> map = new HashMap<>();
                map.putAll(activityBean.getMap());
                map.putAll(activityContentString.getMap());
                map.put("aid", aid);
                rv.setObject(map);
                rv.setMeg(me.getValue(ResultMsgConstant.modifySuccess));
                rv.setFlag(ReturnValue.FLAG_SUCCESS);
            } else {
                rv.setMeg(me.getValue(ResultMsgConstant.modifyFail));
                rv.setFlag(ReturnValue.FLAG_FAIL);
                return rv;
            }
        } catch (Exception ex) {
            return SystemException.setResult(rv, ex, logger);
        }
        return rv;
    }

    /**
     * 添加主办方信息
     * @param organizers 主办方
     * @param rv 返回结果
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @date 2017-11-19
     */
    private ReturnValue addOrganizers(String[] organizers, ReturnValue rv) {
        for (String str : organizers) {
            // 查询库中是否有该主办方
            Enterprise enterprise = enterpriseDao.getEnterprise(str);
            if (enterprise == null) {
                // 没有添加数据
                Enterprise enter = new Enterprise();
                enter.setName(str);
                enter.setLevel(0);
                enter.setRegisterTime(System.currentTimeMillis() / 1000);
                enter.setStatus(ActivityTakeStatus.NORMAL.getStatus());
                String eid = enterpriseDao.save(enter).toString();
                if (StringUtil.isEmpty(eid)) {
                    rv.setFlag(ReturnValue.FLAG_FAIL);
                    rv.setMeg(me.getValue(ResultMsgConstant.organizerWrong));
                    return rv;
                }
                // 添加主办方权限信息
                ActivityPrivilege activityPrivilege = new ActivityPrivilege();
                activityPrivilege.setTakeAcitivityPrivilege(
                        String.valueOf(ActivityPrivilegeStatus.CANNOTCONTRACTED.getStatus()));
                activityPrivilege.setPublishActivityPrivilege(
                        String.valueOf(ActivityPrivilegeStatus.CANNOTPUBLISHED.getStatus()));
                activityPrivilege.setUserType(String.valueOf(ActivityPrivilegeStatus.ENTERPRISE.getStatus()));
                activityPrivilege.setStatus(ActivityPrivilegeStatus.NORMAL.getStatus());
                activityPrivilege.setUid(Long.parseLong(eid));
                String pid = activityPrivilegeDao.save(activityPrivilege).toString();
                if (StringUtil.isEmpty(pid)) {
                    rv.setFlag(ReturnValue.FLAG_FAIL);
                    rv.setMeg(me.getValue(ResultMsgConstant.organizerWrong));
                    return rv;
                }
            }
        }
        return rv;
    }

    /**
     * 保存活动详细信息
     *
     * @param
     * @return
     */
    public ReturnValue<ActivityContentBean> saveActivityContent(
            cn.lv.jewelry.index.indexActivity.frontBean.ActivityContent activityContent) {
        ReturnValue<ActivityContentBean> rv = new ReturnValue<>();
        ActivityContentBean at = new ActivityContentBean();
        try {
            cn.lv.jewelry.activity.daoBean.ActivityContent ac = new cn.lv.jewelry.activity.daoBean.ActivityContent();
            Activity activity = activityDao.get(Activity.class, activityContent.getAid());
            ac.setAid(activity);
            ac.setContent(activityContent.getContent().toString());
            ac.setType(activityContent.getType());
            ac.setActivityStatus(activity.getStatus());
            Integer i = 0;
            if (activityContentDao.save(ac) != i) {
                at.setAid(ac.getId());
                activityContent.setAid(ac.getAid().getId());
                activityContent.setActivityStatus(ac.getAid().getStatus());
                Map<String, cn.lv.jewelry.index.indexActivity.frontBean.ActivityContent> list = new HashMap<>();
                list.put(String.valueOf(ac.getActivityStatus()), activityContent);
                at.setContents(list);
                rv.setObject(at);
                rv.setFlag(ReturnValue.FLAG_SUCCESS);
            }
        } catch (Exception ex) {
            rv.setFlag(ReturnValue.FLAG_EXCEPTION);
            ex.printStackTrace();
        }
        return rv;
    }

    /**
     * 更新活动详细信息
     *
     * @param
     * @return
     */
    public ReturnValue<ActivityContentBean> updateActivityContent(
            cn.lv.jewelry.index.indexActivity.frontBean.ActivityContent activityContent) {
        ReturnValue<ActivityContentBean> rv = new ReturnValue<>();
        ActivityContentBean at = new ActivityContentBean();
        try {
            cn.lv.jewelry.activity.daoBean.ActivityContent ac = activityContentDao.get(
                    cn.lv.jewelry.activity.daoBean.ActivityContent.class, activityContent.getId());
            if (ac != null) {
                ac.setAid(activityDao.get(Activity.class, activityContent.getAid()));
                ac.setContent(activityContent.getContent().toString());
                ac.setType(activityContent.getType());
                activityContentDao.getSession().update(ac);
                Map<String, cn.lv.jewelry.index.indexActivity.frontBean.ActivityContent> list = new HashMap<>();
                list.put(String.valueOf(ac.getActivityStatus()), activityContent);
                at.setContents(list);
                rv.setObject(at);
                rv.setFlag(ReturnValue.FLAG_SUCCESS);
            } else {
                rv.setFlag(ReturnValue.FLAG_FAIL);
            }
        } catch (Exception ex) {
            rv.setFlag(ReturnValue.FLAG_EXCEPTION);
            ex.printStackTrace();
        }
        return rv;
    }


    public int saveRealtiveProduct(String aid, String pid) {
        int flag = 1;
        try {
            RelativeProduct relativeProduct = new RelativeProduct();
            relativeProduct.setAid(Long.parseLong(aid));
            relativeProduct.setPid(Long.parseLong(pid));
            Integer i = 0;
            if (relativeProductDao.save(relativeProduct) != i) {
                flag = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 保存活动相关
     * @param maid 活动id
     * @param raid 相关活动id
     * @return int
     */
    public int saveRelativeActivity(String maid, String raid) {
        int flag = 1;
        try {
            RelativeActivity relativeActivity = new RelativeActivity();
            // 活动id
            Activity mactivity = activityDao.get(Activity.class, Long.parseLong(maid));
            Activity ractivity = null;
            // 相关活动id
            if (!StringUtil.isEmpty(raid))
                ractivity = activityDao.get(Activity.class, Long.parseLong(raid));
            if (ractivity != null)
                relativeActivity.setRaid(ractivity);
            relativeActivity.setMaid(mactivity);
            if (relativeActivityDao.exist(relativeActivity))
                return flag;
            else {
                long i = (Long) relativeActivityDao.save(relativeActivity);
                if (i != 0) {
                    flag = 0;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    /**
     * 相关商品
     *
     * @param aid
     * @param num
     * @return
     */
    public List<ProductItemBean> getRelativeProduct(long aid, int num) {
        List<ProductItemBean> productItemBeans = new ArrayList<>();
        List<RelativeProduct> relativeProducts = relativeProductDao.getProduct(aid, num);
        for (RelativeProduct relativeProduct : relativeProducts) {
            Product product = productDao.getProductToAid(relativeProduct.getPid());
            productItemBeans.add(productToProductItemBean(product));
        }

        return productItemBeans;
    }

    // TODO 以下方法是之前的，用到了brand表，2018-2-9号修改表设计需注释掉该代码
    private ProductItemBean productToProductItemBean(Product product) {
        ProductItemBean productItemBean = new ProductItemBean();
//        productItemBean.setBrand(product.getBrand().getName());
        productItemBean.setCategory(product.getCategory().getName());
        productItemBean.setCoverUrl(product.getCoverUrl());
        productItemBean.setDescription(product.getDescription());
        productItemBean.setEmbed(product.getEmbed().getName());
        productItemBean.setId(String.valueOf(product.getId()));
        productItemBean.setLength(product.getLength());
        productItemBean.setMaterial(product.getMaterial().getName());
        productItemBean.setOrigin(product.getOrigin().getName());
        productItemBean.setPostscript(product.getPostscript());
        productItemBean.setPrice(String.valueOf(product.getPrice()));
        productItemBean.setName(product.getName());
        productItemBean.setStatus(String.valueOf(product.getStatus()));
        productItemBean.setStyle(product.getStyle().getName());
        productItemBean.setSize(product.getSize());

        return productItemBean;
    }

    /**
     * 查询相关活动
     * @param aid 活动id
     * @param uid 用户id
     * @param pid 用户权限id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     */
    public ReturnValue<FUserActivitiesBean> getRelativeActivity(long aid, String uid, String pid) {
        ReturnValue<FUserActivitiesBean> returnValue = new ReturnValue<>();
        try {
            FUserActivitiesBean fUserActivitiesBean = new FUserActivitiesBean();
            List<RelativeActivity> relativeActivities = relativeActivityDao.getRelativeActivity(aid);

            List<FUserActivitiesBean.Activities> list = new ArrayList<>();
            for (RelativeActivity relativeActivity : relativeActivities) {
                Activity activity = relativeActivity.getRaid();
                Integer status = activity.getStatus();
                if (ActivityStatusType.DELETE.getV() != status
                        || ActivityStatusType.DRAFT.getV() != status) {
                    FUserActivitiesBean.Activities activities = new FUserActivitiesBean().new Activities();
                    activities.setAid(activity.getId());
                    if (ActivityStatusType.COMPLETE.getV() == status) {
                        activities.setGroupOrActivity(1);
                    } else {
                        activities.setGroupOrActivity(0);
                    }
                    activities.setIndexPic(activity.getIndexPic());
                    activities.setSubject(activity.getSubject());
                    activities.setStatus(activity.getStatus());
                    Integer focusNum = activityFocusDao.getActivityFocusNumber(aid);
                    Integer attenceNum = activityAttenceDao.getActivityAttenceNumber(aid, null);
                    activities.setAttenceNum(attenceNum);
                    activities.setFocusNum(focusNum);
                    activities.setInfoNum(0);
                    activities.setUserActivityStatus(5);
                    ActivityFocus focus = null;
                    Integer attenceStatus = null;
                    Integer infoNum = 0;
                    activities.setUid(0L);
                    if (!StringUtil.isEmpty(pid)) {
                        activities.setUid(Long.parseLong(uid));
                        Long lpid = Long.parseLong(pid);

                        focus = activityFocusDao.getActivityFocus(aid, lpid);
                        attenceStatus = activityAttenceDao.getActivityAttenceStatusById(pid, aid);
                        infoNum = activityCommentReadDao.getInfoNum(lpid, aid);
                    }
                    activities.setInfoNum(infoNum);
                    if (attenceStatus != null) {
                        if (ActivityAttenceStatus.ABNORMAL.getStatus() == attenceStatus
                                || ActivityAttenceStatus.DELETE.getStatus() == attenceStatus
                                || ActivityAttenceStatus.DROP_OUT.getStatus() == attenceStatus) {
                            if (focus != null) {
                                activities.setUserActivityStatus(0);
                            }
                        } else if (ActivityAttenceStatus.PARTICIPATE.getStatus() == attenceStatus) {
                            activities.setUserActivityStatus(2);
                        }
                    }
                    list.add(activities);
                }
            }
            fUserActivitiesBean.setActivities(list);
            returnValue.setObject(fUserActivitiesBean);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception ex) {
            returnValue.setFlag(ReturnValue.FLAG_EXCEPTION);
            returnValue.setMeg(ex.getMessage());
            ex.printStackTrace();
        }
        return returnValue;
    }

    /**
     * 根据活动撞他获取首页活动列表
     * @param uid 用户id
     * @param status 活动类型
     * @param page 页码
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @date 2017-11-17
     */
    public ReturnValue<Map<String, Object>> getAppActivityList(String uid, int page, int status, String userType) {
        ReturnValue<Map<String, Object>> rb = new ReturnValue();
        rb.setFlag(ReturnValue.FLAG_FAIL);
        try {
            Page<Activity> pageResult = activityDao.getAppActivityList(page, status);
            List<ActivityBean> list = getActivityBean(uid, pageResult.getPageContent(), userType);
            PageWrap<ActivityBean> p = new PageWrap<>();
            p.setHasNextPage(pageResult.isHasNextPage());
            p.setList(list);
            p.setCount(pageResult.getCount());
            p.setCurrent(pageResult.getCurrent());

            rb.setObject(PageMapUtil.getMap(p));
            rb.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception ex) {
            rb.setFlag(ReturnValue.FLAG_EXCEPTION);
            ex.printStackTrace();
        }
        return rb;
    }

    /**
     * 根据状态获取活动
     *
     * @param page
     * @param status
     * @return
     */
    public ReturnValue<Map<String, Object>> getActivityList(String uid, int page, int status) {
        ReturnValue<Map<String, Object>> rb = new ReturnValue();
        rb.setFlag(ReturnValue.FLAG_FAIL);
        try {
            Page<Activity> pageResult = activityDao.getActivityList(page, status);
            List<ActivityBean> list = getActivityBean(uid, pageResult.getPageContent(), "0");
            PageWrap<ActivityBean> p = new PageWrap<>();
            p.setHasNextPage(pageResult.isHasNextPage());
            p.setList(list);
            p.setCount(pageResult.getCount());
            p.setCurrent(pageResult.getCurrent());

            rb.setObject(PageMapUtil.getMap(p));
            rb.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception ex) {
            rb.setFlag(ReturnValue.FLAG_EXCEPTION);
            ex.printStackTrace();
        }
        return rb;
    }

    /**
     * 根据活动名称获取活动
     *
     * @param page
     * @param name
     * @return
     */
    public ReturnValue<Map<String, Object>> getActivityList(int page, String name) {
        ReturnValue<Map<String, Object>> rb = new ReturnValue();
        rb.setFlag(ReturnValue.FLAG_FAIL);
        try {
            Page<Activity> pageResult = activityDao.getActivityByName(page, name);
            List<ActivityBean> list = getActivityBean(null, pageResult.getPageContent(), "0");
            PageWrap<ActivityBean> p = new PageWrap<>();
            p.setHasNextPage(pageResult.isHasNextPage());

            p.setList(list);
            p.setCount(pageResult.getCount());
            p.setCurrent(pageResult.getCurrent());
            rb.setObject(PageMapUtil.getMap(p));
            rb.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception ex) {
            rb.setFlag(ReturnValue.FLAG_EXCEPTION);
            ex.printStackTrace();
        }
        return rb;
    }

    /**
     * 后台daobean to frontBean
     *
     * @param activities
     * @param id
     * @return
     */
    public List<ActivityBean> getActivityBean(List<Activity> activities, long id) {
        List<ActivityBean> activityBeans = new ArrayList<>(activities.size());
        for (Activity activity : activities) {

            activityBeans.add(parseActivityToActivityBean(activity, id));
        }
        return activityBeans;
    }

    private ActivityBean parseActivityToActivityBean(Activity activity, long id) {
        ActivityBean activityBean = new ActivityBean();
        activityBean.setStatus(activity.getStatus());
        activityBean.setAddress(activity.getAddress());
        activityBean.setBeginDateTime(DateUtil.getDateTime(String.valueOf(activity.getStartTime())));
        activityBean.setEndDateTime(DateUtil.getDateTime(String.valueOf(activity.getEndTime())));
        List<Map<String, Object>> objects = getTheOrganizerInformation(id);
        activityBean.setHeadPic(activity.getIndexPic());
        activityBean.setOrganizers(objects);
        activityBean.setId(String.valueOf(activity.getId()));
        activityBean.setLevel(activity.getLevel());
        activityBean.setHeadPic(activity.getLogoPic());
        activityBean.setIndexPic(activity.getIndexPic());
        activityBean.setName(activity.getEname());
        activityBean.setOrganizer(activity.getOrganizer());
        //TODO reader
        activityBean.setReaders(new ArrayList<ActivityReader>());
        activityBean.setReadersNum(activity.getActivityNum());
        //查询相关图片表
        activityBean.setRelativeImages(getNodeBeanImage(activityImageDao.getActivityImage(id)));
        activityBean.setSubject(activity.getTitle());
        return activityBean;
    }

    /**
     * 后台daobean to frontBean
     *
     * @param activities
     * @return
     */
    public List<ActivityBean> getActivityBean(String uid, List<Activity> activities, String userType) {
        List<ActivityBean> activityBeans = new ArrayList<>(activities.size());
        ActivityBean activityBean;
        for (Activity activity : activities) {
            activityBean = new ActivityBean();
            activityBean = addActivityBean(activityBean, activity, uid, userType);
            String videoImage = activity.getVideoImageIndex();
            activityBean.setVideoImage(StringUtil.isEmpty(videoImage) ? "" : videoImage);
            activityBeans.add(activityBean);
        }
        return activityBeans;
    }

    private ActivityBean addActivityBean(ActivityBean activityBean, Activity activity, String uid, String userType) {
        Integer status = activity.getStatus();
        if (activity.getIsRemove() == 1 || activity.getCheckStatus() == 1) {
            activityBean.setStatus(4);
        }
        activityBean.setStatus(status);
        activityBean.setAddress(activity.getAddress());
        activityBean.setDescription(activity.getDescription());
        activityBean.setBeginDateTime(DateUtil.getFromUnix(activity.getStartTime()));
        activityBean.setEndDateTime(DateUtil.getFromUnix(activity.getEndTime()));
        List<Map<String, Object>> objects = getTheOrganizerInformation(activity.getId());

        if (StringUtil.isEmpty(uid) || StringUtil.isEmpty(userType)) {
            activityBean.setIsSignUp(0);
            activityBean.setIsLogin(0);
            activityBean.setIsAttention(0);
        } else {
            String subuid = "";
            String platform = uid.substring(0, 2);
            if ("00".equals(platform)) {
                subuid = uid.substring(2);
            } else if ("01".equals(platform) || "02".equals(platform) || "03".equals(platform)) {
                subuid = uid.substring(2);
            }
            ActivityPrivilege privilege = activityPrivilegeDao.getId(Long.parseLong(StringUtil.isEmpty(subuid) ? uid : subuid), Integer.parseInt(userType));
            Object signUp =
                    activityAttenceDao.getAttenceStatusByAidAndUid(
                            activity.getId(), privilege.getId(), null);
            String attention = activityFocusDao.getActivityFocusStatusById(privilege.getId(), activity.getId());
            Boolean attentionExpore =
                    activityFocusExploreDao.getUserAttentionExpore(uid, activity.getPrivilegeId());

            activityBean.setIsSignUp(signUp != null ? 1 : 0);
            activityBean.setIsLogin(1);
            if (activity.getStatus() == ActivityStatusType.UNPUBLISHED.getV()) {
                activityBean.setIsAttention(StringUtil.isEmpty(attentionExpore.toString()) ? 0 : 1);
            }
            if (activity.getStatus() != ActivityStatusType.UNPUBLISHED.getV()) {
                activityBean.setIsAttention(StringUtil.isEmpty(attention) ? 0 : 1);
            }
        }
        activityBean.setReadersNum(0);
        activityBean.setInsertTime(DateUtil.getDateTime(String.valueOf(activity.getInsertTime())));
        activityBean.setOrganizers(objects);
        activityBean.setAid(activity.getId());
        activityBean.setIndexPic(StringUtil.isEmpty(activity.getIndexPic()) ? "" : activity.getIndexPic());
        activityBean.setIndexPicIsShow(activity.getIndexPicIsShow());
        if (!StringUtil.isEmpty(activity.getUpTime()))
            activityBean.setUpTime(DateUtil.getDateTime(activity.getUpTime()));
        else
            activityBean.setUpTime("");
        activityBean.setActivityNum(activity.getActivityNum());
        activityBean.setPushIndexPage(activity.getPushIndexPage());
        if (activity.getPushColumnsPage() == null)
            activityBean.setPushColumnsPage(0);
        else
            activityBean.setPushColumnsPage(activity.getPushColumnsPage());
        if (!StringUtil.isEmpty(activity.getUpTimeColumns()))
            activityBean.setUpTimeColumns(DateUtil.getDateTime(activity.getUpTimeColumns()));
        else
            activityBean.setUpTimeColumns("");
        activityBean.setSubject(activity.getSubject());
        // 获取关注人数
        Integer foucsNumber = activityFocusDao.getActivityFocusNumber(activity.getId());
        // 获取报名人数
        Integer attenceNumber = activityAttenceDao.getActivityAttenceNumber(activity.getId(), null);
        activityBean.setAttenceNum(attenceNumber);
        activityBean.setFocusNum(foucsNumber);
        return activityBean;
    }

    private List<NodeBeanImage> getNodeBeanImage(List<ActivityImage> activityImages) {
        List<NodeBeanImage> nodeBeanImages = new ArrayList<>(activityImages.size());
        if (null == activityImages || activityImages.size() < 1) {
            return nodeBeanImages;
        }
        NodeBeanImage nodeBeanImage = null;
        for (ActivityImage activityImage : activityImages) {
            nodeBeanImage = new NodeBeanImage();
            nodeBeanImage.setId(String.valueOf(activityImage.getId()));
            nodeBeanImage.setPath(activityImage.getUrl());
            nodeBeanImage.setPath_big(activityImage.getUrl());
            nodeBeanImage.setPath_small(activityImage.getUrl());
            nodeBeanImage.setRid(String.valueOf(activityImage.getAid()));
            nodeBeanImages.add(nodeBeanImage);
        }
        return nodeBeanImages;
    }


    public static void main(String[] args) {
        cn.lv.jewelry.index.indexActivity.frontBean.ActivityContent a =
                new cn.lv.jewelry.index.indexActivity.frontBean.ActivityContent();
        a.setAid(1L);
        a.setContent("test content");
        a.setType(1);
        ActivityService service = new ActivityService();
        ReturnValue<ActivityContentBean> rs = service.saveActivityContent(a);
        System.out.println(rs.getFlag());
        System.out.println(rs.getObject().getContents());
        System.out.println(rs.getObject().getAid());
    }

    public List<ActivityRelativeProductItemBean> queryActivityRelativeProduct(String aid) {
        long aidType = Long.parseLong(aid);
        List<ProductActivity> productActivities = productActivityDao.getActivityRelativeProduct(aidType);
        List<ActivityRelativeProductItemBean> activityRelativeProductItemBeans = new ArrayList<>();
        for (ProductActivity productActivity : productActivities) {
            Product product = productDao.get(Product.class, productActivity.getPid());
            activityRelativeProductItemBeans.add(getActivityReplativeProductItemBean(product));
        }

        return activityRelativeProductItemBeans;
    }

    private ActivityRelativeProductItemBean getActivityReplativeProductItemBean(Product product) {
        ActivityRelativeProductItemBean activityRelativeProductItemBean = new ActivityRelativeProductItemBean();
        activityRelativeProductItemBean.setId(String.valueOf(product.getId()));
        activityRelativeProductItemBean.setCoverUrl(product.getCoverUrl());
        activityRelativeProductItemBean.setName(product.getName());

        return activityRelativeProductItemBean;
    }

    public ReturnValue<Object> getContentByTag(String type, String tag, int page) {
        ReturnValue<Object> returnValue = null;
        Object o = null;
        try {
            returnValue = new ReturnValue<Object>();
            int typeNum = Integer.parseInt(type);
            int tagNum = Integer.parseInt(tag);
            Page<LvTag> pages = lvTagDao.getLvTagBytag(typeNum, tagNum, page);
            List<LvTag> lvTags = pages.getPageContent();
            if (lvTags == null) {
                throw new RuntimeException("不存在这样的记录tag记录");
            }
            if (1 == typeNum) { //活动
                int i = 0;

                List<Object> results = new ArrayList<Object>();
                for (LvTag lvTag : lvTags) {
                    if (i > 3)
                        break;
                    i++;
                    results.add(getActivityBeanById(lvTag.getRid(), null, null));

                }
                o = results;
            } else if (2 == typeNum) {//商品
                List<Object> results = new ArrayList<Object>();
                for (LvTag lvTag : lvTags) {
                    results.add(productService.queryJewelrys(String.valueOf(lvTag.getRid())));
                }
                Map<String, Object> m = PageMapUtil.getMap(pages);
                m.put("data", results);
                o = m;
            } else if (3 == typeNum) {//人气专场
                List<Object> results = new ArrayList<Object>();
                //results.add(fashionService.queryFashionMan(String.valueOf(lvTag.getRid())));
                int i = 0;
                for (LvTag lvTag : lvTags) {
                    if (i > 3) {
                        break;
                    }
                    ReturnValue<SpecialItemBean> rv = fashionService.querySpeical(String.valueOf(lvTag.getRid()));
                    if (rv != null && rv.getObject() != null)
                        results.add(rv.getObject());
                    i++;
                }
                o = results;
            } else if (4 == typeNum) { //设计师
                if (lvTags.size() >= 1) {
                    LvTag lvTag = lvTags.get(0);
                    String t = String.valueOf(lvTag.getRid());
                    List<DesignerItemBean> list = (productService.queryDesigner(t, "1"));
                    if (list.size() > 0)
                        o = list.get(0);
                }
            } else if (5 == typeNum) { //品牌
                List<Object> results = new ArrayList<Object>();
                int i = 0;
                for (LvTag lvTag : lvTags) {
                    if (i > 3) {
                        break;
                    }
                    // TODO 以下方法是之前的，用到了brand表，2018-2-9号修改表设计需注释掉该代码
                    String t = String.valueOf(lvTag.getRid());
//                    results.addAll(productService.queryBrands(t, "1"));
                    i++;
                }
                o = results;
            }


            /*for (LvTag lvTag : lvTags) {
                String t = String.valueOf(lvTag.getRid());
                if (1 == typeNum) { //活动
                    results.add(getActivityBeanById(lvTag.getRid()));
                } else if (2 == typeNum) {//商品
                    results.add(productService.queryJewelrys(String.valueOf(lvTag.getRid())));
                } else if (3 == typeNum) {//人气专场
                    //results.add(fashionService.queryFashionMan(String.valueOf(lvTag.getRid())));
                    ReturnValue<SpecialItemBean> rv = fashionService.querySpeical(String.valueOf(lvTag.getRid()));
                    if (rv != null && rv.getObject() != null)
                        results.add(rv.getObject());
                } else if (4 == typeNum) { //设计师
                    results.addAll(productService.queryDesigner(t, "1"));
                } else if (5 == typeNum) { //品牌
                    results.addAll(productService.queryBrands(t, "1"));
                }
            }*/
            returnValue.setObject(o);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnValue.setFlag(ReturnValue.FLAG_EXCEPTION);
        }

        return returnValue;
    }

    /**
     * 插入活动举办信息
     * @param organizer 主办方名称
     * @param aid 活动id
     * @param privilegeId 发布者权限id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @date 2017-11-5
     */
    public ReturnValue<ProductItemBean> saveTakeactivity(String organizer, Integer aid, Integer privilegeId) {
        ReturnValue<ProductItemBean> rv = new ReturnValue<>();
        try {
            saveTakeInfo(privilegeId, aid);
            String[] organizers = organizer.split(",");
            for (String str : organizers) {
                Enterprise enterprise = enterpriseDao.getEnterprise(str);
                ActivityPrivilege privilege =
                        activityPrivilegeDao.getId(
                                enterprise.getId(), ActivityPrivilegeStatus.WRITEOFF.getStatus());
                saveTakeInfo(Integer.parseInt(privilege.getId().toString()), aid);
            }
            rv.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception ex) {
            rv.setFlag(ReturnValue.FLAG_EXCEPTION);
            ex.printStackTrace();
        }
        return rv;
    }

    private void saveTakeInfo(Integer privilegeId, Integer aid) {
        TakeActivityBean activityBean = new TakeActivityBean();
        activityBean.setAid(aid);
        activityBean.setEid(privilegeId);
        activityBean.setLevel(0);
        activityBean.setTime(System.currentTimeMillis() / 1000);
        activityBean.setStatus(ActivityPrivilegeStatus.NORMAL.getStatus());
        ActivityTake activityTake = new ActivityTake(activityBean);
        activityTakeDao.save(activityTake).toString();
    }

    public ReturnValue<ProductItemBean> saveTakeactivity(TakeActivityBean takeActivityBean) {

        ReturnValue<ProductItemBean> rv = new ReturnValue<ProductItemBean>();
        try {
            ActivityTake activityTake = new ActivityTake(takeActivityBean);
            activityTakeDao.save(activityTake);
            rv.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception ex) {
            rv.setFlag(ReturnValue.FLAG_EXCEPTION);
            ex.printStackTrace();
        }
        return rv;
    }

    public ReturnValue<MobileActivityBean> saveMobileActivity(MobileActivityBean activityBean) {
        ReturnValue<MobileActivityBean> rv = new ReturnValue();

        try {
            String subuid = activityBean.getUid().substring(2);
            Integer userType = activityBean.getUserType();
            //get privilege id
            ActivityPrivilege privilege =
                    activityPrivilegeDao.getId(Long.parseLong(subuid), userType);
            if (privilege == null) {
                logger.error("get privilege id error:{},{}", subuid, userType);
                rv.setMeg("get privilege id error, uid " + subuid + ", userType:" + userType);
                throw new RuntimeException("there has not exist user or privilege");
            }
            //save activity
            Activity ac = new Activity();
            if ("0".equals(privilege.getPublishActivityPrivilege().toString())) {
                logger.error("No permission to publish activity:{},{}", subuid, userType);
                rv.setMeg(me.getValue(ResultMsgConstant.noPermissionToPublishActivity));
                throw new RuntimeException("No permission to publish activity");
            }
            ac.setPrivilegeId(privilege.getId());
            ac.setIndexPic(activityBean.getIndexPic());
            ac.setLogoPic(activityBean.getHeadPic());
            ac.setSubject(activityBean.getSubject());
            ac.setDescription(activityBean.getDescription());
            ac.setStartTime(Long.parseLong(activityBean.getBeginDateTime()));
            ac.setEndTime(Long.parseLong(activityBean.getEndDateTime()));
            ac.setRegisterEndTime(activityBean.getRegisterEndTime());
            ac.setAddress(activityBean.getAddress());
            ac.setActivityNum(activityBean.getActivityNum());
            ac.setIndexPicIsShow(activityBean.getIndexPicIsShow());
            ac.setFee(activityBean.getFee());
            ac.setMapPic(activityBean.getMapPic());
            ac.setTelephone(activityBean.getTelephone());
            ac.setEmail(activityBean.getEmail());
            String videoImage = activityBean.getVideoImageIndex();
            ac.setVideoImageIndex(StringUtil.isEmpty(videoImage) ? "" : videoImage);
            String video = activityBean.getVideoImageLogo();
            ac.setVideoImageLogo(StringUtil.isEmpty(video) ? "" : video);
            ac.setMobile(activityBean.getMobile());
            ac.setIsRemove(ActivityReviewType.NOT_DELETED.getType());
            ac.setInsertTime(System.currentTimeMillis() / 1000);
            ac.setPushIndexPage(ActivityReviewType.NOT_PUSH_INDEX.getType());
            ac.setCheckStatus(ActivityReviewType.PENDING_REVIEW.getType());
            ac.setPushColumnsPage(ActivityReviewType.NOT_PUSH_COLUMNS.getType());
            // 添加主办方
            String[] organizers = activityBean.getOrganizer().split(",");
            rv = addOrganizers(organizers, rv);
            if (rv.getFlag() == ReturnValue.FLAG_FAIL) {
                return rv;
            }

//            ac.setEndTime(DateUtil.getDateUnix(activityBean.getEndDateTime(), DateUtil.formatDay));
            if (ActivityDraftType.TRUE.getType().equals(activityBean.getDraft()))
                ac.setDraft(ActivityDraftType.TRUE.getType());
            else
                ac.setDraft(ActivityDraftType.FALSE.getType());
            ac.setStatus(ActivityStatusType.UPCOMING.getV());
            String aid = activityDao.save(ac).toString();
            String htmlAddress = String.format(Constant.HTML_ADDRESS_FIND, aid);
            ac.setHtmlAddress(htmlAddress);
            activityDao.getSession().update(ac);
            if (!StringUtil.isEmpty(aid)) {
                // save take activity
                // TODO liumengwei 2017-11-5 主办方协办方级别有待修改-----2017-11-6~8
                saveTakeactivity(activityBean.getOrganizer(), Integer.parseInt(aid),
                        Integer.parseInt(activityBean.getPrivilegeId().toString()));

                //save relative activity
                if (!StringUtil.isEmpty(activityBean.getRid())) {
                    int relativeFlag = saveRelativeActivity(String.valueOf(ac.getId()), activityBean.getRid());
                    if (relativeFlag != 0) {
                        logger.error("save relative activity error:{},{} ", ac.getId(), activityBean.getRid());
                        rv.setMeg(me.getValue(ResultMsgConstant.saveRelativeActivityError));
                        throw new RuntimeException("there has exist the relative activity");
                    }
                }

                    //save activity content
                cn.lv.jewelry.activity.daoBean.ActivityContent activityContent =
                        new cn.lv.jewelry.activity.daoBean.ActivityContent();
                activityContent.setAid(ac);
                activityContent.setContent(activityBean.getContent());
                activityContent.setActivityStatus(ac.getStatus());
//                if (activityContentDao.existContent(ac.getId(), ac.getStatus())) {
//                    logger.error("activity content is exist:{},{} ", ac.getId(), ac.getStatus());
//                    throw new RuntimeException("there has exist the content under the aid");
//                }
                String acid = activityContentDao.save(activityContent).toString();
                if (StringUtil.isEmpty(acid)) {
                    logger.error("save activity content  error:{},{} ", ac.getId(), ac.getStatus(), activityBean.getContent());
                    rv.setMeg(me.getValue(ResultMsgConstant.saveRelativeActivityError));
                    throw new RuntimeException("save activity content  error");
                }
//                String rid = activityBean.getRid();
//                rv = addRelativeActivity(rid, rv, ac);
//                if (rv.getFlag() == ReturnValue.FLAG_FAIL) {
//                    return rv;
//                }
                //返回结果数据
                activityBean.setAid(ac.getId());
                activityBean.setUid(null);
                activityBean.setPrivilegeId(null);
                activityBean.setUserType(null);
                activityBean.setBeginDateTime(daysFD.format(Long.parseLong(activityBean.getEndDateTime())));
                activityBean.setEndDateTime(daysFD.format(Long.parseLong(activityBean.getEndDateTime()) * 1000));
                activityBean.setRegisterEndTime(daysFD.format(Long.parseLong(activityBean.getRegisterEndTime()) * 1000));
//                activityBean.setContent(HtmlUtils.htmlEscape(activityBean.getContent()));
                rv.setObject(activityBean);
                rv.setFlag(ReturnValue.FLAG_SUCCESS);
            }
        } catch(Exception ex){
            return SystemException.setResult(rv, ex, logger);
        }
        return rv;
    }

    private ReturnValue addRelativeActivity(String rid, ReturnValue rv, Activity ac) {
        if (!StringUtil.isEmpty(rid)) {
            Long rrid = Long.parseLong(rid);
            Activity activity = activityDao.get(Activity.class, rrid);
            if (activity == null) {
                rv.setFlag(ReturnValue.FLAG_FAIL);
                rv.setMeg(me.getValue(ResultMsgConstant.saveRelativeActivityError));
                return rv;
            }
            RelativeActivity relativeActivity = new RelativeActivity();
            relativeActivity.setMaid(ac);
            relativeActivity.setRaid(activity);
            relativeActivityDao.save(relativeActivity);
        }
        return rv;
    }

    /**
     * 获取审核列表
     * @param status 审核状态
     * @param type 活动类型
     * @param page 页码
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2017/9/13
     */
    public ReturnValue<Map<String, Object>> getActivityReviewList(Integer status, Integer type, Integer page, Integer top) {
        ReturnValue<Map<String, Object>> rv = new ReturnValue<>();
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> activityBean = new ArrayList<>();
        try {
            // 获取总页数
            Long totalPages = activityDao.getTotalPages(status, type, top);
            // 获取各状态下活动数量
            ActivityNumsBean activityNumsBean = new ActivityNumsBean();
            Map<String, Object> mapNums = activityDao.getActivityNumsByStatus(status, top);
            if (mapNums != null) {
                TransferUtils.BUTransMapToBean(mapNums, activityNumsBean);
                map.put("UPCOMING", activityNumsBean.getUPCOMING());
                map.put("UNPUBLISHED", activityNumsBean.getUNPUBLISHED());
                map.put("ONGOING", activityNumsBean.getONGOING());
                map.put("COMPLETE", activityNumsBean.getCOMPLETE());
            } else {
                map.put("UPCOMING", 0);
                map.put("UNPUBLISHED", 0);
                if (status == ActivityReviewType.BY.getType()) {
                    map.put("ONGOING", 0);
                    map.put("COMPLETE", 0);
                }
            }
            // 查询审核列表
            Page<Activity> listActivity = activityDao.getActivityReviewList(status, type, page, top);
            List<Activity> activityBeanList = listActivity.getPageContent();
            if (activityBeanList != null) {
                for (Activity activity : activityBeanList) {
                    ActivityBean activity1 = new ActivityBean();
                    activity1.setAid(activity.getId());
                    // 获取主办方
                    List<Map<String, Object>> organizers =
                            getTheOrganizerInformation(activity.getId());
                    activity1.setSubject(activity.getSubject());
                    activity1.setInsertTime(DateUtil.getFromUnix(activity.getInsertTime()));
                    activity1.setStatus(activity.getStatus());
                    activity1.setOrganizers(organizers);
                    String upTime = activity.getUpTime();
                    String upTimeColumns = activity.getUpTimeColumns();
                    Integer pushIndexPage = activity.getPushIndexPage();
                    Integer pushColumnsPage = activity.getPushColumnsPage();
                    activity1.setCheckStatus(activity.getCheckStatus());
                    activity1.setUpTime(
                            StringUtil.isEmpty(upTime) ? "" : DateUtil.getFromUnix(Long.parseLong(upTime)));
                    activity1.setUpTimeColumns(
                            StringUtil.isEmpty(upTimeColumns) ? "" : DateUtil.getFromUnix(Long.parseLong(upTimeColumns)));
                    activity1.setPushIndexPage(pushIndexPage == null ? 0 : pushIndexPage);
                    activity1.setPushColumnsPage(pushColumnsPage == null ? 0 : pushColumnsPage);
                    activity1.setUpdateTime(DateUtil.getFromUnix(activity.getUpdateTime()));
                    Integer attenceNum = activityAttenceDao.getActivityAttenceNumber(activity.getId(), 1);
                            // 获取该活动参与人数
                    activity1.setAttenceNum(attenceNum);
                    activityBean.add(ActivityBeanMap.getResult(activity1, type, attenceNum));
                }
            }
            map.put("hasNext", listActivity.isHasNextPage());
            map.put("activities", activityBean);
            map.put("totalPages", totalPages);
            rv.setObject(map);
            rv.setMeg(me.getValue(ResultMsgConstant.querySuccess));
            rv.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception ex) {
            return SystemException.setResult(rv, ex, logger);
        }
        return rv;
    }

    /**
     * 修改审核状态
     * @param aid 活动id
     * @param status 审核状态
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2017/9/13
     */
    public ReturnValue modifyTheAuditStatus(Long aid, Integer status) {
        ReturnValue rv = new ReturnValue();
        try {
            Boolean updateResult = activityDao.modifyTheAuditStatus(aid, status);
            if (status == ActivityReviewType.DELETE.getType()) {
                Activity ac = activityDao.get(Activity.class, aid);
                ac.setIsRemove(ActivityReviewType.DELETED.getType());
                activityDao.getSession().update(ac);
            }
            rv.setFlag(updateResult == true ? ReturnValue.FLAG_SUCCESS : ReturnValue.FLAG_FAIL);
            rv.setMeg(updateResult == true ? "修改成功" : "修改失败");
        } catch (Exception ex) {
            return SystemException.setResult(rv, ex, logger);
        }
        return rv;
    }

    /**
     * 活动置顶 推首页
     * @param aid 活动id
     * @param status 1推首 2置顶
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2017/9/15
     */
    public ReturnValue updateActivityStickyOrPushIndex(Long aid, Integer status) {
        ReturnValue rv = new ReturnValue();
        rv.setFlag(ReturnValue.FLAG_FAIL);
        rv.setMeg(me.getValue(ResultMsgConstant.modifySuccess));
        try {
            Activity ac = activityDao.get(Activity.class, aid);
            if (status == ActivityReviewType.PUSH_INDEX.getType()){
                // 推首页
                if (!StringUtil.isEmpty(ac.getUpTime())) {
                    rv.setMeg(me.getValue(ResultMsgConstant.canNotPushTheFirst));
                    return rv;
                }
                if (ac.getPushIndexPage() == ActivityReviewType.NOT_PUSH_INDEX.getType()) {
                    ac.setPushIndexPage(ActivityReviewType.IS_PUSH_INDEX.getType());
                } else {
                    ac.setPushIndexPage(ActivityReviewType.NOT_PUSH_INDEX.getType());
                }
            } else if (status == ActivityReviewType.STICKY.getType()){
                // 置顶
                if (ac.getUpTime() == null) {
                    ac.setUpTime(String.valueOf(System.currentTimeMillis() / 1000));
                } else {
                    ac.setUpTime(null);
                }
            } else if (status == ActivityReviewType.PUSH_COLUMNS.getType()) {
                if (!StringUtil.isEmpty(ac.getUpTime())
                        || ActivityReviewType.IS_PUSH_INDEX.getType() == ac.getPushIndexPage()
                        || !StringUtil.isEmpty(ac.getUpTimeColumns())) {
                    rv.setMeg(me.getValue(ResultMsgConstant.canNotPushColumns));
                    return rv;
                }
                // 推分栏
                if (ac.getPushColumnsPage() == ActivityReviewType.NOT_PUSH_COLUMNS.getType()) {
                    ac.setPushColumnsPage(ActivityReviewType.IS_PUSH_COLUMNS.getType());
                } else {
                    ac.setPushColumnsPage(ActivityReviewType.NOT_PUSH_COLUMNS.getType());
                }
            } else if (status == ActivityReviewType.STICKY_COLUMNS.getType()) {
                if (!StringUtil.isEmpty(ac.getUpTime())
                        || ActivityReviewType.IS_PUSH_INDEX.getType() == ac.getPushIndexPage()) {
                    rv.setMeg(me.getValue(ResultMsgConstant.unableToPutTheTopColumn));
                    return rv;
                }
                // 置顶分栏
                if (ac.getUpTimeColumns() == null) {
                    ac.setUpTimeColumns(String.valueOf(System.currentTimeMillis() / 1000));
                } else {
                    ac.setUpTimeColumns(null);
                }
            } else {
                rv.setFlag(ReturnValue.FLAG_FAIL);
                rv.setMeg(me.getValue(ResultMsgConstant.failedToChangeStatus));
            }
            activityDao.getSession().update(ac);
            rv.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception ex) {
            return SystemException.setResult(rv, ex, logger);
        }
        return rv;
    }

    /**
     * 根据关键词模糊查询活动列表
     * @param name 活动名称关键词
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2017/9/15
     */
    public ReturnValue<Map<String, Object>> searchActivityList(String name, Integer page) {
        ReturnValue<Map<String, Object>> rv = new ReturnValue<>();
        Map<String, Object> map = new HashMap<>();
        List<ActivityBean> activityBeanList = new ArrayList<>();
        try {
            // 查询
            Page<Activity> activityPage = activityDao.searchActivityList(name, page);
            List<Activity> activityList = activityPage.getPageContent();
            // 遍历取出数据
            for (Activity ac : activityList) {
                ActivityBean bean = new ActivityBean();
                bean.setId(String.valueOf(ac.getId()));
                bean.setSubject(ac.getSubject());
                bean.setStatus(ac.getStatus());
                bean.setInsertTime(DateUtil.getFromUnix(ac.getInsertTime()));
                List<Map<String, Object>> objects = getTheOrganizerInformation(ac.getId());
                bean.setOrganizers(objects);
                bean.setCheckStatus(ac.getCheckStatus());
                bean.setUpTime(!StringUtil.isEmpty(ac.getUpTime()) ? DateUtil.getDateTime(ac.getUpTime()) : ac.getUpTime());
                bean.setPushIndexPage(ac.getPushIndexPage());
                activityBeanList.add(bean);
            }
            map.put("hasNext", activityPage.isHasNextPage());
            map.put("activities", activityBeanList);
            rv.setFlag(ReturnValue.FLAG_SUCCESS);
            rv.setMeg(me.getValue(ResultMsgConstant.querySuccess));
            rv.setObject(map);
        } catch (Exception ex) {
            return SystemException.setResult(rv, ex, logger);
        }
        return rv;
    }

    /**
     * 是否有发布活动权限
     * @param pid 权限id
     * @return java.lang.Boolean
     * @author liumengwei
     */
    public Boolean getActivityPrivilege(Long pid) {
        Boolean result = true;
        try {
            ActivityPrivilege activityPrivilege = activityPrivilegeDao.get(ActivityPrivilege.class, pid);
            result = Integer.parseInt(activityPrivilege.getPublishActivityPrivilege()) ==
                    ActivityPrivilegeStatus.CANNOTPUBLISHED.getStatus();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return  result;
    }

    /**
     * 查看用户权限信息
     * @param pid 权限id
     * @return java.lang.Boolean
     * @author liumengwei
     */
    public Boolean getUserPrivilegeInfo(Long pid, Long aid) {
        Boolean result = true;
        try {
            ActivityTake activityTake = activityTakeDao.getActivityTake(aid, pid);
            Activity activity = activityDao.get(Activity.class, aid);
            result = activityTake == null && activity.getPrivilegeId() != pid ? true : false;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return  result;
    }

    /**
     * 保存活动圈子信息
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     */
    public ReturnValue saveActivityCircle(ActivityCircleFront activityCircleFront) {
        ReturnValue returnValue = new ReturnValue();
        try {
            ActivityCircle activityCircle = new ActivityCircle();
            activityCircle.setName(activityCircleFront.getName());
            activityCircle.setNotice(activityCircleFront.getNotice());
            activityCircle.setPic(activityCircleFront.getPic());
            activityCircle.setStatus(0);
            activityCircle.setUid(activityCircleFront.getUid());
            activityCircle.setCreateTime(activityCircleFront.getCreateTime());
            if(activityCircleFront.getPid() != null && !"".equals(activityCircleFront.getPid()))
                activityCircle.setPid(activityCircleFront.getPid());

            // 保存活动圈子信息
            activityCircleDao.save(activityCircle);
            returnValue.setObject(activityCircle);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            returnValue.setMeg(me.getValue(ResultMsgConstant.querySuccess));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 获取圈子框架信息
     * @param aid 圈子id
     * @param uid 用户id
     * @param pid 用户权限id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @date 2017-11-23
     */
    public ReturnValue<ActivityBean> getActivityCircleInfo(Long aid, String uid, String pid) {
        ReturnValue<ActivityBean> returnValue = new ReturnValue<>();
        try {
            ActivityBean activityBean = new ActivityBean();
            Activity activity = activityDao.get(Activity.class, aid);
            List<Map<String, Object>> objects = getTheOrganizerInformation(aid);
            activityBean.setSubject(activity.getSubject());
            if ("0".equals(uid)) {
                activityBean.setIndexPic(activity.getIndexPic());
                activityBean.setHeadPic(activity.getLogoPic());
                activityBean.setActivityNum(activity.getActivityNum());
            } else {
                activityBean.setHeadPic(activity.getLogoPic());
            }
            activityBean.setIsAttention(0);
            if (!StringUtil.isEmpty(uid)) {
                ActivityFocus activityFocus = activityFocusDao.getActivityFocus(aid, Long.parseLong(pid));
                activityBean.setIsAttention(activityFocus != null ? 1 : 0);
            }
            String description = activity.getDescription();
            activityBean.setDescription(StringUtil.isEmpty(description) ? "" : description);
            activityBean.setAid(aid);
            String htmlAddress = activity.getHtmlAddress();
            activityBean.setHtmlAddress(StringUtil.isEmpty(htmlAddress) ? "" : htmlAddress);
            String video = activity.getVideoImageLogo();
            activityBean.setVideoImage(StringUtil.isEmpty(video) ? "" : video);
            // 关注人数
            Integer foucsNumber = activityFocusDao.getActivityFocusNumber(activity.getId());
            activityBean.setFocusNum(foucsNumber);
            activityBean.setOrganizers(objects);
            activityBean.setStatus(activity.getStatus());
            List<RelativeActivity> relativeActivities = relativeActivityDao.getRelativeActivity(aid, null);
            activityBean.setFrequency(relativeActivities.size());
            returnValue.setObject(activityBean);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 圈子下用户列表
     * @param aid 圈子id
     * @param page 页码
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @date 2017-11-23
     */
    public ReturnValue<FActivityRegistrationInformationBean> getActivityCircleForUsers(Long aid, Integer page) {
        ReturnValue<FActivityRegistrationInformationBean> returnValue = new ReturnValue<>();
        try {
            FActivityRegistrationInformationBean informationBean = new FActivityRegistrationInformationBean();
            List<FActivityRegistrationInformationBean.Users> usersArrayList = new ArrayList<>();
            List<Map<String, Object>> usersPage = activityAttenceDao.getUsers(aid, page);
            for (Map<String, Object> user : usersPage) {
                FActivityRegistrationInformationBean.Users users = new FActivityRegistrationInformationBean().new Users();
                users.setUid(Long.parseLong(String.valueOf(user.get("uid"))));
                Object username = user.get("username");
                Object headpic = user.get("headpic");
                users.setUsername(StringUtil.isEmpty(username + "") ? "" : username.toString());
                users.setHeadPic(StringUtil.isEmpty(headpic + "") ? "" : headpic.toString());
                users.setPid(Long.parseLong(user.get("pid").toString()));
                usersArrayList.add(users);
            }
            informationBean.setHasNext(false);
            informationBean.setUsers(usersArrayList);
            returnValue.setObject(informationBean);
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

    /**
     * 添加探索活动信息
     * @param activityExploreFront 活动参数
     * @param uid 用户id
     * @param pid 权限id
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2017/10/22
     */
    public ReturnValue saveExploreActivity(ActivityExploreFront activityExploreFront, String uid, String pid) {
        ReturnValue returnValue = new ReturnValue();
        try {
            ActivityPrivilege privilege = activityPrivilegeDao.get(ActivityPrivilege.class, Long.parseLong(pid));
            Integer userType = Integer.parseInt(privilege.getUserType());
            User user = null;
            UserBind userBind = null;
            String subuid = uid.substring(2);
            if (userType == ActivityPrivilegeStatus.PERSONAL.getStatus()) {
                user = userDao.get(User.class, Integer.parseInt(subuid));
            } else if (userType == ActivityPrivilegeStatus.RHIRD_PARTY.getStatus()) {
                userBind = userBindDao.get(UserBind.class, Integer.parseInt(subuid));
            }
            Activity activity = new Activity();
            activity.setSubject(activityExploreFront.getSubject());
            activity.setIndexPic(activityExploreFront.getIndexPic());
            activity.setLogoPic(activityExploreFront.getHeadPic());
            activity.setPrivilegeId(Long.parseLong(pid));
            activity.setCheckStatus(ActivityReviewType.PENDING_REVIEW.getType());
            activity.setPushIndexPage(ActivityReviewType.NOT_PUSH_INDEX.getType());
            activity.setPushColumnsPage(ActivityReviewType.NOT_PUSH_COLUMNS.getType());
            activity.setStatus(ActivityStatusType.UNPUBLISHED.getV());
            activity.setInsertTime(System.currentTimeMillis() / 1000);
            activity.setIsRemove(ActivityReviewType.NOT_DELETED.getType());
            activity.setFee(0D);
            Integer draft = activityExploreFront.getDraft();
            activity.setDraft(draft);
            activity.setIndexPicIsShow(activityExploreFront.getIndexPicIsShow());
            String videoImage = activityExploreFront.getVideoImageIndex();
            activity.setVideoImageIndex(StringUtil.isEmpty(videoImage) ? "" : videoImage);
            String video = activityExploreFront.getVideoImageLogo();
            activity.setVideoImageLogo(StringUtil.isEmpty(video) ? "" : video);
            String aid = activityDao.save(activity).toString();
            String htmlAddress = String.format(Constant.HTML_ADDRESS_EXPLORE, aid);
            activity.setHtmlAddress(htmlAddress);
            activityDao.getSession().update(activity);
            activity.setId(Long.parseLong(aid));

            cn.lv.jewelry.activity.daoBean.ActivityContent activityContent =
                    new cn.lv.jewelry.activity.daoBean.ActivityContent();
            activityContent.setAid(activity);
            activityContent.setContent(activityExploreFront.getContent());
            activityContent.setActivityStatus(activity.getStatus());
            activityContentDao.save(activityContent);

            ActivityTake take = new ActivityTake();
            take.setAid(Integer.parseInt(aid));
            take.setInsertTime(System.currentTimeMillis() / 1000);
            take.setStatus(0);
            take.setPrivilegeId(privilege);
            activityTakeDao.save(take);
            returnValue.setMeg(ActivityDraftType.TRUE.getType().equals(draft)
                    ? me.getValue(ResultMsgConstant.saveExproleDraftSuccess)
                    : me.getValue(ResultMsgConstant.postExproleActivitySuccess)
            );
            returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
            activityExploreFront.setDraft(draft);
            returnValue.setObject(activityExploreFront.getCmsMap(user, userBind));
        } catch (Exception ex) {
            return SystemException.setResult(returnValue, ex, logger);
        }
        return returnValue;
    }

}

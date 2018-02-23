package cn.xxtui.support.util;

/**
 * Created by 24593 on 2018/1/24.
 */
public class ResultMsgConstant {
    /** 用户 */
    // 没有实名
    public final static String unrealName = "unrealName";
    // 该活动状态下不可报名
    public final static String statusCanNotRegister = "statusCanNotRegister";
    // 该图文不可点赞
    public final static String graphicCanNotLike = "graphicCanNotLike";
    // 没有权限
    public final static String permissionDenied = "permissionDenied";
    // 您还没有关注或报名该活动，不能发表评论或图文消息
    public final static String canNotPostGraphic = "canNotPostGraphic";
    // 没有权限发布活动公告
    public final static String permissionDeniedPostAnnouncement = "permissionDeniedPostAnnouncement";
    // 需要实名认证，没有权限
    public final static String unrealNameAndPermissionDenied = "unrealNameAndPermissionDenied";
    // 手机号已经注册
    public final static String phoneHasBeenRegistered = "phoneHasBeenRegistered";
    // 手机号还未注册
    public final static String phoneIsNotRegistered = "phoneIsNotRegistered";
    // 注册失败
    public final static String registFailed = "registFailed";
    // 登陆成功
    public final static String loginSuccess = "loginSuccess";
    // 登陆失败
    public final static String loginFail = "loginFail";
    // 已经认证成功，请勿重复认证
    public final static String verified = "verified";
    // 该用户名已被注册
    public final static String usernameHasBeenRegistered = "usernameHasBeenRegistered";
    // 注册成功
    public final static String registSuccess = "registSuccess";
    // 没有此用户
    public final static String withoutThisUser = "withoutThisUser";
    // 手机号格式错误
    public final static String phoneFormatError = "phoneFormatError";
    // 密码错误
    public final static String passError = "passError";
    // 查询成功
    public final static String querySuccess = "querySuccess";
    // 查询失败
    public final static String queryFail = "queryFail";
    // 身份证号码出错
    public final static String idNumberErrpr = "idNumberErrpr";
    // 用户名长度
    public final static String usernameLength = "usernameLength";
    // 性别
    public final static String genderFormat = "genderFormat";
    // 删除成功
    public final static String deleteSuccess = "deleteSuccess";
    // 删除失败
    public final static String deleteFail = "deleteFail";
    // 审核成功
    public final static String auditSuccess = "auditSuccess";
    // 审核失败
    public final static String auditFailure = "auditFailure";
    // 用户名已存在，请重新输入
    public final static String usernameAlreadyExists = "usernameAlreadyExists";
    // 修改成功
    public final static String modifySuccess = "modifySuccess";
    // 修改失败
    public final static String modifyFail = "modifyFail";
    // 密码修改失败
    public final static String passModifySuccess = "passModifySuccess";
    // 密码修改失败
    public final static String passModifyFail = "passModifyFail";
    // 没有权限删除成员
    public final static String noPermissionToDeleteMembers = "noPermissionToDeleteMembers";

    /** 活动 */
    // 人数限制格式输入错误
    public final static String activityNumFormatError = "activityNumFormatError";
    // 活动费用格式输入错误
    public final static String feeFormatError = "feeFormatError";
    // 主办方移动电话或客服电话不能全部为空，必须填写其中一个
    public final static String phoneNumberOrtelephoneCanNotBeAllEmpty = "phoneNumberOrtelephoneCanNotBeAllEmpty";
    // 邮箱格式错误
    public final static String emailFormatError = "emailFormatError";
    // 保存成功
    public final static String saveSuccess = "saveSuccess";
    // 添加成功
    public final static String addSuccess = "addSuccess";
    // 关注成功
    public final static String attentionSuccess = "attentionSuccess";
    // 取消关注成功
    public final static String cancelAttentionSuccess = "cancelAttentionSuccess";
    // 操作错误，不能关注
    public final static String canNotBeConcerned = "canNotBeConcerned";
    // 报名成功
    public final static String signupSuccess = "signupSuccess";
    // 取消报名成功
    public final static String cancelSignupSuccess = "cancelSignupSuccess";
    // 操作错误，不能报名
    public final static String canNotBeSignup = "canNotBeSignup";
    // 点赞成功
    public final static String likeSuccess = "likeSuccess";
    // 权限点赞成功
    public final static String cancelLikeSuccess = "cancelLikeSuccess";
    // 操作错误，不能点赞
    public final static String canNotBeLike = "canNotBeLike";
    // 操作错误
    public final static String operationError = "operationError";
    // 该用户不存在，不能关注
    public final static String usersNotHereCanNotBeConcerned = "usersNotHereCanNotBeConcerned";
    // 发布成功
    public final static String releaseSuccess = "releaseSuccess";
    // 发布失败
    public final static String releaseFail = "releaseFail";
    // 评论成功
    public final static String commentSuccess = "commentSuccess";
    // 评论失败
    public final static String commentFail = "commentFail";
    // 没有权限审核成员
    public final static String noPermissionToReviewMembers = "noPermissionToReviewMembers";
    // 退出成功
    public final static String dropOutSuccess = "dropOutSuccess";
    // 退出失败
    public final static String dropOutFail = "dropOutFail";
    // 活动状态错误
    public final static String activityStatusError = "activityStatusError";
    // 活动错误
    public final static String activityError = "activityError";
    // 活动不存在
    public final static String activityDoesNotExist = "activityDoesNotExist";
    // 首图地址错误
    public final static String indexPicError = "indexPicError";
    // 头图地址错误
    public final static String headPicError = "headPicError";
    // 该活动为待审核或未通过审核活动，不可升级
    public final static String thisReviewStatusCanNotBeUpgraded = "thisReviewStatusCanNotBeUpgraded";
    // 该活动为草稿状态，不可升级
    public final static String draftStatusCanNotBeUpgraded = "draftStatusCanNotBeUpgraded";
    // 该活动状态下不可升级
    public final static String thisActivityStatusIsNotUpgradeable = "thisActivityStatusIsNotUpgradeable";
    // 主办方错误
    public final static String organizerWrong = "organizerWrong";
    // 保存相关活动错误
    public final static String saveRelativeActivityError = "saveRelativeActivityError";
    // 无法推首
    public final static String canNotPushTheFirst = "canNotPushTheFirst";
    // 无法推分栏
    public final static String canNotPushColumns = "canNotPushColumns";
    // 无法置顶分栏
    public final static String unableToPutTheTopColumn = "unableToPutTheTopColumn";
    // 修改状态失败
    public final static String failedToChangeStatus = "failedToChangeStatus";
    // 存为草稿成功
    public final static String saveExproleDraftSuccess = "saveExproleDraftSuccess";
    // 发布探索活动成功
    public final static String postExproleActivitySuccess = "postExproleActivitySuccess";
    // 没有权限发布活动
    public final static String noPermissionToPublishActivity = "noPermissionToPublishActivity";
    // 错误
    public final static String error = "error";
    // 图片地址错误
    public final static String picError = "picError";

    /** 其他 */
    // success
    public final static String success = "success";

    /** 品牌 */
}

package cn.com.ql.wiseBeijing.user.frontservice;

import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.com.ql.wiseBeijing.serviceUtil.StringUtil;
import cn.com.ql.wiseBeijing.user.frontBean.FActivityRegistrationInformationBean;
import cn.com.ql.wiseBeijing.user.frontBean.FNewUserBean;
import cn.com.ql.wiseBeijing.user.frontBean.FUserActivitiesBean;
import cn.com.ql.wiseBeijing.user.frontBean.FUserBean;
import cn.com.ql.wiseBeijing.user.frontBean.FUserBindBean;
import cn.com.ql.wiseBeijing.user.service.UserService;
import cn.lv.jewelry.activity.daoBean.ActivityPrivilegeStatus;
import cn.lv.jewelry.activity.service.ActivityService;
import cn.xxtui.support.bean.ResultBean;
import cn.xxtui.support.bean.ResultStruct;
import cn.xxtui.support.session.AccessType;
import cn.xxtui.support.session.ResourceAccess;
import cn.xxtui.support.util.DateUtil;
import cn.xxtui.support.util.MeaasgeUtil;
import cn.xxtui.support.util.ResultMsgConstant;
import cn.xxtui.support.util.SystemException;
import cn.xxtui.support.util.ValidateMode;
import cn.xxtui.support.util.XXMediaType;
import com.alibaba.fastjson.JSON;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
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
import java.util.Map;

/**
 * 用户相关接口
 * @author
 * @Time
 *
 */
@Path("/u/")
@Component
public class FUserService {
	private final static Logger logger = LoggerFactory.getLogger(FUserService.class);
	@Resource(name = "userService")
	private UserService us;
	@Resource(name = "activityService")
	private ActivityService activityService;
	private MeaasgeUtil me = new MeaasgeUtil();
	@Bean
	public FUserService fuserService() {
		return new FUserService();
	}

	/**
	 * 查询手机号是否注册
	 * @param user 接收用户相关参数
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/phoneExist")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response phoneExist(
			@BeanParam FNewUserBean user) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			// 查询手机号是否注册
			String userPhone = us.getUserByPhone(user);
			if (!StringUtil.isEmpty(userPhone)) {
				resultStruct.setMsg(me.getValue(ResultMsgConstant.phoneHasBeenRegistered));
				resultStruct.setStatus(ResultStruct.OK);
			} else {
				resultStruct.setMsg(me.getValue(ResultMsgConstant.phoneIsNotRegistered));
				resultStruct.setStatus(ResultStruct.ERROR);
			}
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * user regist
	 * @param user 接收用户相关参数
	 * @param request 请求体
	 * @return javax.ws.rs.core.Response
	 * @throws org.codehaus.jettison.json.JSONException
	 * @author liumengwei
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/userRegist")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response userRegist(
			@BeanParam FNewUserBean user,
			@Context HttpServletRequest request) throws JSONException {
		ResultStruct resultStruct = new ResultStruct();
		resultStruct.setStatus(ResultBean.ERROR);
		resultStruct.setMsg(me.getValue(ResultMsgConstant.registFailed));
		try {
			// 添加用户信息
			ReturnValue<FNewUserBean> rv = us.regist(user);
			JSONObject o = new JSONObject(JSON.toJSONString(rv.getObject()));
			if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
				Long uid = o.getLong("uid");
				// 添加用户权限信息
				ReturnValue rvPrivilege = activityService.saveActivityPrivilege(
						uid,
						ActivityPrivilegeStatus.PERSONAL.getStatus()
				);
				if (rvPrivilege.getFlag() == ReturnValue.FLAG_SUCCESS) {
					return loginPass(user, request);
				} else {
					resultStruct.setStatus(ResultBean.ERROR);
					resultStruct.setMsg(rvPrivilege.getMeg());
					return Response.ok(resultStruct.toString()).build();
				}
			} else {
				resultStruct.setMsg(rv.getMeg());
				return Response.ok(resultStruct.toString()).build();
			}
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	public static JSONObject error(String rv) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("error", rv);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo;
	}

	/**
	 * 密码登陆
	 * @param user 接收用户相关参数
	 * @param request 请求体
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/loginPass")
	public Response loginPass(@BeanParam FNewUserBean user,
							  @Context HttpServletRequest request) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			// 密码登陆
			ReturnValue<FNewUserBean> fuser = us.login(user);
			if (fuser.getFlag() == ReturnValue.FLAG_SUCCESS) {
				// 登陆成功拼接返回数据
				user = fuser.getObject();
				resultStruct.setMsg(me.getValue(ResultMsgConstant.loginSuccess));
				Map<String, Object> map = new HashMap<>();
				Map<String, Object> mapUser = new HashMap<>();
				map.put("uid", "00" + fuser.getObject().getUid());// 00表示自已平台
				map.put("login_time",
						String.valueOf(DateUtil.getCurrentDate().getTime()));
				mapUser.put("phone", user.getPhone());
				mapUser.put("platform_uid", fuser.getObject().getUid());
				mapUser.put("givename", "");
				mapUser.put("verified", fuser.getObject().getVerified());
				if (user.getHeadPic() != null)
					mapUser.put("headPic", user.getHeadPic());
				else
					mapUser.put("headPic", "");
				map.put("user", mapUser);
				map.put("pid", user.getPid());// 权限id
				map.put("userType", user.getUserType());// 用户类型
				resultStruct.setBody(map);
				resultStruct.setStatus(ResultBean.OK);
				return Response.ok(resultStruct.toString()).build();
			} else {
				resultStruct.setStatus(ResultStruct.ERROR);
				resultStruct.setMsg(fuser.getMeg());
				Map<String, Object> map = new HashMap<>();
				map.put("error", fuser.getMeg());
				resultStruct.setBody(map);
				return Response.ok(resultStruct.toString()).build();
			}
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * 修改密码
	 * @param user 相关参数
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/changePass")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response changePass(
			@BeanParam FNewUserBean user) {
		ResultStruct resultStruct = new ResultStruct();
		ReturnValue rv = us.changePass(user.getPassword(), user.getPhone());
		try {
			// 返回相应数据
			resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * 实名认证
	 * @param verified 接收实名认证相关参数
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/verified")
	public Response verified(@BeanParam FNewUserBean verified) {
		ResultStruct resultStruct = new ResultStruct();
		resultStruct.setStatus(ResultStruct.OK);
		try {
			// 查询用户是否实名
			Boolean verifiedResult = activityService.getActivityPrivilege(verified.getPid());
			if (verifiedResult == false) {
				// 实名认证
				String uid = verified.getUid();
				ReturnValue rv = us.verified(verified, uid);
				// 查询用户信息
				ReturnValue<FNewUserBean> fuserBean = us.getUser(verified.getPid());
				try {
					if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
						// 验证成功修改权限信息
						ReturnValue rvPrivilege = activityService.updateActivityPrivilege(
								verified.getPid(),
								StringUtil.isEmpty(verified.getUserType()) ? null : Integer.parseInt(verified.getUserType()));
						if (rvPrivilege.getFlag() == ReturnValue.FLAG_SUCCESS) {
							// 修改成功返回相应数据
							FNewUserBean userBean = fuserBean.getObject();
							Map<String, Object> map1 = new HashMap<>();
							map1.put("headPic", userBean.getHeadPic());
							String givename = userBean.getGivename();
							map1.put("givename", StringUtil.isEmpty(givename) ? "" : givename);
							map1.put("gender", userBean.getGender());
							map1.put("verified", userBean.getVerified());
							resultStruct.setBody(map1);
							resultStruct.setStatus(ResultStruct.OK);
							resultStruct.setMsg(rvPrivilege.getMeg());
						} else {
							resultStruct.setStatus(ResultBean.ERROR);
							resultStruct.setMsg(rvPrivilege.getMeg());
						}
					} else {
						resultStruct.setStatus(ResultBean.ERROR);
						resultStruct.setMsg(rv.getMeg());
					}
				} catch (Exception ex) {
					SystemException.setResult(resultStruct, ex, logger);
					return Response.ok(resultStruct.toString()).build();
				}
			} else {
				resultStruct.setMsg(me.getValue(ResultMsgConstant.verified));
				resultStruct.setStatus(ResultStruct.ERROR);
			}
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
		return Response.ok(resultStruct.toString()).build();
	}

	/**
	 * 查询用户报名所有活动下的相关用户：0好友(同一个活动参与者)，1粉丝(同一个活动非参与者)
	 * @param uid 用户id
	 * @param userType 用户类型
	 * @param page 页码
	 * @param type 查看的用户类型：0好友(同一个活动参与者)，1粉丝(同一个活动非参与者)
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/9/2
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/activeUserList/{type}/{page}")
	public Response activeUserList(
			@FormParam("uid") String uid,
			@FormParam("pid") String pid,
			@FormParam("userType") String userType,
			@PathParam("type") String type,
			@PathParam("page") String page) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			String uuid = uid.substring(2);
			ReturnValue rv = new ReturnValue();
			// 查询用户报名所有活动下的相关用户
			if ("0".equals(type)) {
				rv = us.getActiveUserList(
						Long.parseLong(uuid), Integer.parseInt(page), Integer.parseInt(type), Long.parseLong(pid), userType);
			}
			if ("1".equals(type)) {
				rv = us.getFansData(Long.parseLong(uuid));
			}
			resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * 查询用户发布活动
	 * @param pid 权限id
	 * @param page 页码
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/9/2
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/queryPublishingActivity/{page}")
	public Response queryPublishingActivity(
			@FormParam("pid") String pid,
			@FormParam("userType") String userType,
			@FormParam("uid") String uid,
			@PathParam("page") String page) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			// 查询用户发布的所有活动
			ReturnValue rvActivity = us.getPublishingActivity(
					Long.parseLong(pid), Integer.parseInt(page));
			resultStruct = ResultStruct.setResultStructInfo(rvActivity, resultStruct);
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * 查询用户发布圈子
	 * @param pid 权限id
	 * @param page 页码
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/9/2
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/queryPublishingActivityCircle/{page}")
	public Response queryPublishingActivityCircle(
			@FormParam("pid") String pid,
			@FormParam("uid") String uid,
			@FormParam("userType") String userType,
			@PathParam("page") String page) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			// 查询用户发布的所有活动
			ReturnValue rvActivity = us.getPublishingActivityCircle(
					Long.parseLong(pid), Integer.parseInt(page));
			resultStruct = ResultStruct.setResultStructInfo(rvActivity, resultStruct);
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * 查询用户参与圈子下用户列表
	 * @param aid 活动id
	 * @param page 页码
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/9/2
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/participateCircleForUserList/{aid}/{page}")
	public Response participateCircleForUserList(
			@PathParam("aid") String aid,
			@PathParam("page") String page) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			// 查询用户参与圈子下用户列表
			ReturnValue rvActivity = us.getParticipateCircleForUserList(
					Long.parseLong(aid), Integer.parseInt(page));
			resultStruct = ResultStruct.setResultStructInfo(rvActivity, resultStruct);
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * 查询用户发起圈子下用户列表
	 * @param aid 活动id
	 * @param pid 权限id
	 * @param page 页码
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/9/2
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/initiatedCircleForUserList/{aid}/{page}")
	public Response initiatedCircleForUserList(
			@FormParam("pid") String pid,
			@FormParam("uid") String uid,
			@FormParam("userType") String userType,
			@PathParam("aid") String aid,
			@PathParam("page") String page) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			// 查询用户发起圈子下用户列表
			ReturnValue rvActivity = us.getInitiatedCircleForUserList(
					Long.parseLong(aid), Long.parseLong(pid), Integer.parseInt(page));
			resultStruct = ResultStruct.setResultStructInfo(rvActivity, resultStruct);
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * 将用户从圈子中删除
	 * @param uid 用户id
	 * @param pid 用户权限id
	 * @param aid 活动id
	 * @param deletePid 被删除用户pid
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/11/24
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/deleteMember/{aid}")
	public Response deleteMember(
			@FormParam("pid") String pid,
			@FormParam("userType") String userType,
			@FormParam("deletePid") String deletePid,
			@PathParam("aid") String aid,
			@FormParam("uid") String uid) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			ReturnValue rvActivity = us.deleteMember(deletePid, Long.parseLong(pid), uid, Long.parseLong(aid));
			resultStruct = ResultStruct.setResultStructInfo(rvActivity, resultStruct);
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * 查询用户参与的活动
	 * @param pid 权限id
	 * @param uid 用户id
	 * @param status 活动状态
	 * @param page 页码
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/11/11
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/getTheParticipationList/{status}/{page}")
	public Response getTheParticipationList(
			@FormParam("uid") String uid,
			@FormParam("userType") String userType,
			@PathParam("status") String status,
			@FormParam("pid") String pid,
			@PathParam("page") String page) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			// 查询用户发布的所有活动
			ReturnValue rvActivity = us.getTheParticipationList(
					Long.parseLong(pid), Long.parseLong(uid), Integer.parseInt(page), status);
			resultStruct = ResultStruct.setResultStructInfo(rvActivity, resultStruct);
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * 添加用户已读消息信息
	 * @param pid 权限id
	 * @param uid 用户id
	 * @param aid 活动id
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/10/15
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/addUserReadMessages/{aid}")
	public Response addUserReadMessages(
			@FormParam("pid") String pid,
			@FormParam("uid") String uid,
			@FormParam("userType") String userType,
			@PathParam("aid") String aid) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			// 查询用户发布的所有活动
			ReturnValue rvActivity = activityService.addUserReadMessages(pid, uid, aid);
			resultStruct = ResultStruct.setResultStructInfo(rvActivity, resultStruct);
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * 查询用户下 报名 参与 关注 发布的活动
	 * @param uid 用户id
	 * @param pid 权限id
	 * @param page 页码
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/9/24
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/userActivities/{page}")
	public Response userActivities(
			@FormParam("uid") String uid,
			@FormParam("userType") String userType,
			@PathParam("page") String page,
			@FormParam("pid") String pid) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			// 查询用户发布的所有活动
			ReturnValue<FUserActivitiesBean> rv = us.getUserActivities(
					Long.parseLong(pid),
					Long.parseLong(uid),
					Integer.parseInt(page),
					null);
			resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * 添加封面图
	 * @param uid 用户id
	 * @param pid 用户权限id
	 * @param user 相关参数
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/9/24
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/uploadCoverMap")
	public Response uploadCoverMap(
			@FormParam("uid") String uid,
			@FormParam("userType") String userType,
			@FormParam("pid") String pid,
			@BeanParam FNewUserBean user) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			// 添加封面图
			ReturnValue rv = us.saveMyInfo(uid, pid, user);
			resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * 修改用户信息
	 * @param uid 用户id
	 * @param pid 用户权限id
	 * @param fUserInfoBean 用户信息相关参数
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/11/25
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/changeUserInfo")
	public Response changeUserInfo(
			@FormParam("uid") String uid,
			@FormParam("userType") String userType,
			@FormParam("pid") String pid,
			@BeanParam FNewUserBean fUserInfoBean) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			ReturnValue rv = us.updateUserInfo(uid, pid, fUserInfoBean, Integer.parseInt(userType));
			resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * 获取用户信息
	 * @param uid 用户id
	 * @param pid 用户权限id
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/12/15
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/getUserInfo")
	public Response getUserInfo(
			@FormParam("uid") String uid,
			@FormParam("userType") String userType,
			@FormParam("pid") String pid) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			ReturnValue rv = us.getUserInfo(uid, pid);
			resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * 根据活动主题关键词查询用户下 报名 参与 关注 发布的活动
	 * @param uid 用户id
	 * @param page 页码
	 * @param activitySubject 活动主题关键词
	 * @param pid 权限id
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/10/9
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/searchUserActivities/{page}")
	public Response searchUserActivities(
			@FormParam("uid") String uid,
			@FormParam("userType") String userType,
			@PathParam("page") String page,
			@FormParam("activitySubject") String activitySubject,
			@FormParam("pid") String pid) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			// 查询用户发布的所有活动
			String utf8Text = new String(activitySubject.getBytes("ISO-8859-1"),"UTF-8");
			ReturnValue<FUserActivitiesBean> rv = us.getUserActivities(
					Long.parseLong(pid),
					Long.parseLong(uid),
					Integer.parseInt(page),
					utf8Text);
			resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * 获取发布的活动报名人信息
	 * @param uid 用户id
	 * @param page 页码
	 * @param aid 活动id
	 * @param pid 权限id
	 * @param status 通过状态 0已报名和预通过 1已通过
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/10/9
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/activityRegistrationInformation/{aid}/{status}/{page}")
	public Response activityRegistrationInformation(
			@FormParam("uid") String uid,
			@FormParam("userType") String userType,
			@PathParam("status") String status,
			@PathParam("page") String page,
			@PathParam("aid") String aid,
			@FormParam("pid") String pid) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			// 查询用户发布的所有活动
			ReturnValue<FActivityRegistrationInformationBean> rv = us.getActivityRegistrationInformation(
					Long.parseLong(pid),
					uid,
					Integer.parseInt(userType),
					Integer.parseInt(page),
					Long.parseLong(aid),
					Integer.parseInt(status));
			resultStruct = ResultStruct.setResultStructInfo(rv, resultStruct);
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * 对发布的活动报名人审核
	 * @param uid 用户id
	 * @param signUpPid 报名人uid
	 * @param aid 活动id
	 * @param status 审核状态：0不通过 1通过 3预通过
	 * @param pid 权限id
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/10/13
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/userRegistrationInformationAudit/{aid}/{status}")
	public Response userRegistrationInformationAudit(
			@FormParam("uid") String uid,
			@FormParam("userType") String userType,
			@PathParam("status") String status,
			@PathParam("aid") String aid,
			@FormParam("signUpPid") String signUpPid,
			@FormParam("pid") String pid) {
		ResultStruct resultStruct = new ResultStruct();
		try {
			// 查询用户是否为发布人
			ReturnValue rv = activityService.getActivityPublish(Long.parseLong(pid), Long.parseLong(aid));
			if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
				// 对发布的活动报名人审核
				ReturnValue rvUpdate = us.updateUserAttentStatus(
						signUpPid, Long.parseLong(aid), Integer.parseInt(status));
				resultStruct = ResultStruct.setResultStructInfo(rvUpdate, resultStruct);
			} else {
				resultStruct.setStatus(ResultStruct.ERROR);
				resultStruct.setMsg(rv.getMeg());
			}
			return Response.ok(resultStruct.toString()).build();
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/profile/{id}")
	// @ResourceAccess(accessType=AccessType.AUTHORIZE)
	public Response getUser(@PathParam("id") String uid) {
		ResultBean result = new ResultBean();
		Integer user_id = null;
		try {
			
			if (uid.length() < 3) {
				result.setStatus(ResultBean.ERROR);
				JSONObject jo = error("uid id is wrong");
				result.setBody(jo.toString());
			}
			String platform = uid.substring(0, 2);
			user_id = Integer.parseInt(uid.substring(2, uid.length()));
			result.setStatus(ResultBean.OK);
			if (platform.equals("00")) {
				// TODO uid应改为pid
				ReturnValue<FNewUserBean> re = us.getUser(Long.parseLong(user_id.toString()));
				
				if (re.getFlag() != ReturnValue.FLAG_EXCEPTION) {
					/*
					 * JSONObject jo = new JSONObject( re.getObject(), new
					 * String[] { "id", "avatar", "nickname", "phone" });
					 */
					re.getObject().setUid(uid);
					result.setBody(re.getObject());
				} else {
					result.setStatus(ResultBean.ERROR);
					JSONObject jo = error("user id is wrong");
					result.setBody(jo.toString());
				}
			} else {
				ReturnValue<FUserBindBean> re = us.getBindUser(user_id);
				
				if (re.getFlag() != ReturnValue.FLAG_EXCEPTION) {
					/*
					 * JSONObject jo = new JSONObject( re.getObject(), new
					 * String[] { "id", "avatar", "nickname", "phone" });
					 */
					re.getObject().setId(uid);
					result.setBody(re.getObject());
					// result.setBody(jo.toString());
				} else {
					result.setStatus(ResultBean.ERROR);
					JSONObject jo = error("user id is wrong or not exist");
					result.setBody(jo.toString());
				}
			}

		} catch (NumberFormatException ex) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("uid is wrong");
			result.setBody(jo.toString());
		}
		return Response.ok(result.toString()).build();
	}

	/**
	 * 修改密码
	 * @param phone
	 * @param check
	 * @param pwd
	 * @param confirmPwd
	 * @return
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/resetPwd")
	public Response resetPwd(@FormParam("phone") String phone,
			@FormParam("check") String check, @FormParam("pwd") String pwd,
			@FormParam("confirmPwd") String confirmPwd) {
		ResultBean result = new ResultBean();
		if (!ValidateMode.digital(phone) || !ValidateMode.length(phone, 11, 11)) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("phone number must digital and  length 11");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
		if (ValidateMode.isNull(pwd)) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("password not allow empty");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
		if (!pwd.equals(confirmPwd)) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("password not match");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
		if (!ValidateMode.length(pwd, 6, 16)) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("password must range in 6-16");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
		FUserBean fuser = new FUserBean();
		fuser.setPhone(phone);
		fuser.setPassword(pwd);
		ReturnValue<FUserBean> rv = us.resetPwd(fuser);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			result.setStatus(ResultBean.OK);
			return Response.ok(result.toString()).build();
		} else {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("update fail");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
	}

	/**
	 * 修改密码
	 * @param uid
	 * @param check
	 * @param pwd
	 * @param confirmPwd
	 * @return
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/modifyPwd")
	public Response modifyPwd(@FormParam("uid") String uid,
			@FormParam("check") String check, @FormParam("pwd") String pwd,
			@FormParam("confirmPwd") String confirmPwd) {
		ResultBean result = new ResultBean();
		if (ValidateMode.isNull(uid)) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("uid not allow empty");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
		if (uid.length() < 3) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("uid id is wrong");
			result.setBody(jo.toString());
		}
		String platform = uid.substring(0, 2);// 前面为00的才可以修改密码
		if (!platform.equals("00")) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("can't modify password");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
		if (ValidateMode.isNull(check)) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("old password not allow empty value");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
		if (ValidateMode.isNull(pwd)) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("password not allow empty value");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
		if (!pwd.equals(confirmPwd)) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("password not match");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
		if (!ValidateMode.length(pwd, 6, 16)) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("password must range in 6-16");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
		FUserBean fuser = new FUserBean();
		fuser.setId(uid.substring(2, uid.length()));
		fuser.setPassword(pwd);
		ReturnValue<FUserBean> rv = us.modifyPwd(fuser, check);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			result.setStatus(ResultBean.OK);
			return Response.ok(result.toString()).build();
		} else {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("update fail");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
	}

	/**
	 * 修改姓名
	 * @param uid
	 * @param nickname
	 * @return
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/modifyNickName")
	public Response modifyNickName(@FormParam("uid") String uid,
			@FormParam("nickname") String nickname) {
		ResultBean result = new ResultBean();
		if (ValidateMode.isNull(uid)) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("uid not allow empty");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
		if (uid.length() < 3) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("uid id is wrong");
			result.setBody(jo.toString());
		}
		String platform = uid.substring(0, 2);// 前面为00的才可以修改密码
		if (!platform.equals("00")) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("can't modify password");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
		if (ValidateMode.isNull(nickname)) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("nickname not allow empty value");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}

		FUserBean fuser = new FUserBean();
		fuser.setId(uid.substring(2, uid.length()));
		fuser.setNickname(nickname);
		ReturnValue<FUserBean> rv = us.modifyNickName(fuser);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			result.setStatus(ResultBean.OK);
			return Response.ok(result.toString()).build();
		} else {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("update fail");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
	}

	/**
	 * 修改头像
	 * @param uid
	 * @param avatar
	 * @return
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/modifyAvatar")
	public Response modifyAvatar(@FormParam("uid") String uid,
			@FormParam("avatar") String avatar) {
		ResultBean result = new ResultBean();
		if (ValidateMode.isNull(uid)) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("uid not allow empty");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
		if (uid.length() < 3) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("uid id is wrong");
			result.setBody(jo.toString());
		}
		String platform = uid.substring(0, 2);// 前面为00的才可以修改密码
		if (!platform.equals("00")) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("can't modify password");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
		if (ValidateMode.isNull(avatar)) {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("avatar not allow empty value");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}

		FUserBean fuser = new FUserBean();
		fuser.setId(uid.substring(2, uid.length()));
		fuser.setAvatar(avatar);
		ReturnValue<FUserBean> rv = us.modifyHeadPic(fuser);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			result.setStatus(ResultBean.OK);
			return Response.ok(result.toString()).build();
		} else {
			result.setStatus(ResultBean.ERROR);
			JSONObject jo = error("update fail");
			result.setBody(jo.toString());
			return Response.ok(result.toString()).build();
		}
	}

	private static ResultStruct paramsRightOrWrong(FUserBindBean bean, ResultStruct resultStruct) {
		// 判断登陆平台
		if (!"QQ".equals(bean.getPlatform())
				&& !"WECHAT".equals(bean.getPlatform())
				&& !"WEIBO".equals(bean.getPlatform())) {
			JSONObject jo = error("不能绑定平台，目前只支持QQ,WECHAT,WEIBO");
			resultStruct.setBody(jo.toString());
			resultStruct.setStatus(ResultStruct.ERROR);
			return resultStruct;
		}
		// 判断参数为空
		if (ValidateMode.isNull(bean.getPlatform(), bean.getPlatform_uid())) {
			JSONObject jo = error("请确保所有字段不为空");
			resultStruct.setBody(jo.toString());
			resultStruct.setStatus(ResultStruct.ERROR);
			return resultStruct;
		}
		return resultStruct;
	}

	/**
	 * 返回uid（session里是加入了平台信息的前缀可看出）
	 * @param bean 接收第三方登陆相关参数
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/9/1
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/userBindLogin")
	public Response userBindLogin(
			@BeanParam FUserBindBean bean,
			@BeanParam FNewUserBean fNewUserBean) {
		ResultStruct resultStruct = new ResultStruct();
		resultStruct.setStatus(ResultStruct.OK);
		try {
			// 判断登陆平台
			resultStruct = paramsRightOrWrong(bean, resultStruct);
			if (resultStruct.getStatus() == ResultStruct.ERROR)
				return Response.ok(resultStruct.toString()).build();
			// 添加第三方登陆信息
			ReturnValue<FUserBindBean> re = us.bindUser(bean, fNewUserBean);
			if (re.getFlag() == ReturnValue.FLAG_SUCCESS) {
				// 添加用户权限信息
				ReturnValue rvPrivilege = activityService.saveActivityPrivilege(
						Long.parseLong(re.getObject().getId()),
						ActivityPrivilegeStatus.RHIRD_PARTY.getStatus()
				);
				if (rvPrivilege.getFlag() == ReturnValue.FLAG_SUCCESS) {
					FUserBindBean fb = re.getObject();
					String prefix = "00";
					if (fb.getPlatform().equals("QQ"))
						prefix = "01";
					if (fb.getPlatform().equals("WECHAT"))
						prefix = "02";
					if (fb.getPlatform().equals("WEIBO"))
						prefix = "03";
					Map<String, Object> map = new HashMap<>();
					Map<String, Object> mapUser = new HashMap<>();
					String uid = prefix + re.getObject().getId();
					Integer userType = ActivityPrivilegeStatus.RHIRD_PARTY.getStatus();
					String pid = rvPrivilege.getObject().toString();
					map.put("uid", uid);
					map.put("userType", userType);// 用户类型
					map.put("pid", pid);// 权限id
					map.put("login_time",
							String.valueOf(DateUtil.getCurrentDate().getTime()));
					mapUser.put("headPic", fb.getAvatar());
					mapUser.put("givename", StringUtil.isEmpty(fb.getNickname()) ? "" : fb.getNickname());
					mapUser.put("platform_uid", fb.getPlatform_uid());

					mapUser.put("verified", fb.getVerified());
					mapUser.put("phone", StringUtil.isEmpty(fb.getPhone()) ? "" : fb.getPhone());
					map.put("user", mapUser);
					resultStruct.setBody(map);
					resultStruct.setMsg(me.getValue(ResultMsgConstant.loginSuccess));
					resultStruct.setStatus(ResultBean.OK);
					return Response.ok(resultStruct.toString()).build();
				} else {
					resultStruct.setStatus(ResultBean.ERROR);
					resultStruct.setMsg(rvPrivilege.getMeg());
					return Response.ok(resultStruct.toString()).build();
				}
			} else {
				resultStruct.setStatus(ResultBean.ERROR);
				resultStruct.setMsg(re.getMeg());
				return Response.ok(resultStruct.toString()).build();
			}
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
	}

	/**
	 * FUserBindBean 下的uid请使用第三方平台所提供的uid,而不是加了前缀后的id,平台类型要指定
	 * 第三方解绑
	 * @param bean 接收第三方解绑相关参数
	 * @return javax.ws.rs.core.Response
	 * @author liumengwei
	 * @Date 2017/9/1
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/userUnBind")
	public Response userUnBind(@BeanParam FUserBindBean bean) {
		ResultStruct resultStruct = new ResultStruct();
		resultStruct.setStatus(ResultStruct.OK);
		try {
			// 判断登陆平台 // 判断参数为空
			resultStruct = paramsRightOrWrong(bean, resultStruct);
			if (resultStruct.getStatus() == ResultStruct.ERROR)
				return Response.ok(resultStruct.toString()).build();
			// 进行解绑操作
			ReturnValue<FUserBindBean> re = us.unbindUser(bean);
			resultStruct = ResultStruct.setResultStructInfo(re, resultStruct);
		} catch (Exception ex) {
			return SystemException.setResult(resultStruct, ex, logger);
		}
		return Response.ok(resultStruct.toString()).build();
	}

	/**
	 * 检查登陆状态，如果能进入到这个地方说明已经是登陆的
	 * 
	 * @return
	 */
	@Path("/logon_check")
	@Produces({ XXMediaType.TEXTUTF8 })
	@GET
	@ResourceAccess(accessType = AccessType.AUTHORIZE)
	public Response checkAuthorize() {
		ResultBean result = new ResultBean();
		result.setStatus(ResultBean.OK);
		result.setMsg("you have logon");
		return Response.ok(result.toString()).build();
	}
	
	/**
	 * 查找用户的所有评论
	 * @param userid
	 * @return
	 */
	@Path("/comment/{userid}")
	@Produces({ XXMediaType.TEXTUTF8 })
	@GET
	//@ResourceAccess(accessType = AccessType.AUTHORIZE)
	public Response getComments(@PathParam("userid") String userid,@QueryParam("page") String page) {
		ResultBean result = new ResultBean();
		int current=0;
		try {
			current = Integer.parseInt(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ReturnValue<Map> rv = us.getComment(userid, current);
		try {
			if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
				result.setStatus(ResultBean.OK);
				result.setMsg("ok");
				result.setBody(rv.getObject());
			} else {
				result.setStatus(ResultBean.ERROR);
				result.setMsg("select failed");
			}
			return Response.ok(result.toString()).build();
		} catch (Exception ex) {
			result.setStatus(ResultBean.ERROR);
			result.setMsg(ex.getMessage());
			return Response.ok(result.toString()).build();
		}
	}
	/**
	 * 查找用户的所有点赞
	 * @param userid
	 * @return
	 */
	@Path("/likes/{userid}")
	@Produces({ XXMediaType.TEXTUTF8 })
	@GET
	@ResourceAccess(accessType = AccessType.AUTHORIZE)
	public Response getLikes(@PathParam("userid") String userid,@QueryParam("page") String page) {
		ResultBean result = new ResultBean();
		int current=0;
		try {
			current = Integer.parseInt(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ReturnValue<Map> rv = us.getLikes(userid, current);
		try {
			if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
				result.setStatus(ResultBean.OK);
				result.setMsg("ok");
				result.setBody(rv.getObject());
			} else {
				result.setStatus(ResultBean.ERROR);
				result.setMsg("select failed");
			}
			return Response.ok(result.toString()).build();
		} catch (Exception ex) {
			result.setStatus(ResultBean.ERROR);
			result.setMsg(ex.getMessage());
			return Response.ok(result.toString()).build();
		}
	}
	
	/**
	 * 查找用户的所有收藏
	 * @param userid
	 * @return
	 */
	@Path("/collects/{userid}")
	@Produces({ XXMediaType.TEXTUTF8 })
	@GET
	@ResourceAccess(accessType = AccessType.AUTHORIZE)
	public Response getCollect(@PathParam("userid") String userid,@QueryParam("page") String page) {
		ResultBean result = new ResultBean();
		int current=0;
		try {
			current = Integer.parseInt(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ReturnValue<Map> rv = us.getCollect(userid, current);
		try {
			if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
				result.setStatus(ResultBean.OK);
				result.setMsg("ok");
				result.setBody(rv.getObject());
			} else {
				result.setStatus(ResultBean.ERROR);
				result.setMsg("select failed");
			}
			return Response.ok(result.toString()).build();
		} catch (Exception ex) {
			result.setStatus(ResultBean.ERROR);
			result.setMsg(ex.getMessage());
			return Response.ok(result.toString()).build();
		}
	}
	/**
	 * 查找用户列表
	 * @param
	 * @return
	 */
	@Path("/list")
	@Produces({ XXMediaType.TEXTUTF8 })
	@GET
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
//	@ResourceAccess(accessType = AccessType.AUTHORIZE)
	public Response getUserList(@QueryParam("current") String cur,@QueryParam("amount") String amo,@QueryParam("status") String status,@QueryParam("nickname") String nickname) {
		ResultBean result = new ResultBean();
		int current=0,amount;
		try {
			current = Integer.parseInt(cur);
			amount  = Integer.parseInt(amo);
		} catch (Exception e) {

			result.setMsg("please check input page info,it's convert error");
			return Response.ok(result.toString()).build();
		}
		ReturnValue<Map<String, Object>> rv= us.getUserList(nickname, status, current, amount);
		System.out.println(rv.getObject());
		try {
			if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
				result.setStatus(ResultBean.OK);
				result.setMsg(rv.getMeg());
				result.setBody(rv.getObject());
			} else {
				result.setStatus(ResultBean.ERROR);
				result.setMsg("select failed");
			}
			System.err.println(result);
			return Response.ok(result.toString()).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setStatus(ResultBean.ERROR);
			result.setMsg(ex.getMessage());
			return Response.ok(result.toString()).build();
		}
	}
	/**
	 * 修改用户状态
	 * @param
	 * @return
	 */
	@Path("/updateStatus")
	@Produces({ XXMediaType.TEXTUTF8 })
	@POST
	@ResourceAccess(accessType = AccessType.AUTHORIZE)
	public Response updateStatus(@FormParam("id") String id,@FormParam("status") String status) {
		ResultBean result = new ResultBean();
		ReturnValue<String> rv= us.updateStatus(id, status);
		try {
			if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
				result.setStatus(rv.getFlag()+"");
				result.setMsg(rv.getMeg());
			} else {
				result.setStatus(ResultBean.ERROR);
				result.setMsg(rv.getMeg());
			}
			return Response.ok(result.toString()).build();
		} catch (Exception ex) {
			result.setStatus(ResultBean.ERROR);
			result.setMsg(ex.getMessage());
			return Response.ok(result.toString()).build();
		}
	}
	@Path("/reset")
	@Produces({ XXMediaType.TEXTUTF8 })
	@POST
//	@ResourceAccess(accessType = AccessType.AUTHORIZE)
	public Response reset(@FormParam("id") String id) {
		ResultBean result = new ResultBean();
		ReturnValue<Object> rv= us.resetNickname(id);
		try {
			if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
				result.setStatus(rv.getFlag()+"");
				result.setMsg(rv.getMeg());
				result.setBody(rv.getObject());
			} else {
				result.setStatus(ResultBean.ERROR);
				result.setMsg(rv.getMeg());
			}
			return Response.ok(result.toString()).build();
		} catch (Exception ex) {
			result.setStatus(ResultBean.ERROR);
			result.setMsg(ex.getMessage());
			return Response.ok("重置失败").build();
		}
	}
	public static void main(String[] args) {
		FUserBindBean bb = new FUserBindBean();
		bb.setAvatar("11");
		bb.setNickname("333");

	}

	public ActivityService getActivityService() {
		return activityService;
	}

	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}
}

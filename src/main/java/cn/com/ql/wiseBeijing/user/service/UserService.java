package cn.com.ql.wiseBeijing.user.service;

import cn.com.ql.wiseBeijing.news.dao.CollectDao;
import cn.com.ql.wiseBeijing.news.dao.CommentDao;
import cn.com.ql.wiseBeijing.news.dao.LikesDao;
import cn.com.ql.wiseBeijing.serviceUtil.PageMapUtil;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.com.ql.wiseBeijing.serviceUtil.StringUtil;
import cn.com.ql.wiseBeijing.user.dao.UserDao;
import cn.com.ql.wiseBeijing.user.daoBean.User;
import cn.com.ql.wiseBeijing.user.frontBean.FActivityRegistrationInformationBean;
import cn.com.ql.wiseBeijing.user.frontBean.FNewUserBean;
import cn.com.ql.wiseBeijing.user.frontBean.FUserActivitiesBean;
import cn.com.ql.wiseBeijing.user.frontBean.FUserBean;
import cn.com.ql.wiseBeijing.user.frontBean.FUserBindBean;
import cn.com.ql.wiseBeijing.user.frontBean.FUserListBean;
import cn.com.ql.wiseBeijing.user.frontBean.FanData;
import cn.lv.jewelry.enterprise.dao.EnterpriseDao;
import cn.lv.jewelry.enterprise.daoBean.Enterprise;
import cn.lv.jewelry.index.indexActivity.frontBean.ActivityBean;
import cn.xxtui.support.bean.ResultStruct;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.util.CertCodeUtil;
import cn.xxtui.support.util.DateUtil;
import cn.xxtui.support.util.EncryptionByMD5;
import cn.xxtui.support.util.MeaasgeUtil;
import cn.xxtui.support.util.ResultMsgConstant;
import cn.xxtui.support.util.SystemException;
import cn.xxtui.support.util.ValidateMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserService {
	private final static Logger logger = LoggerFactory.getLogger(UserService.class);
	@Resource(name = "userDao")
	private UserDao ud;
	@Resource(name = "userBindDao")
	private UserBindDao ubd;
	@Resource(name="commentDao")
	private CommentDao cd;
	@Resource(name="likesDao")
	private LikesDao likesDao;
	@Resource(name="collectDao")
	private CollectDao collectDao;
	@Resource(name="activityCommentReadDao")
	private ActivityCommentReadDao activityCommentReadDao;
	@Resource(name = "activityPrivilegeDao")
	private ActivityPrivilegeDao activityPrivilegeDao;
	@Resource(name = "activityCommentDao")
	private ActivityCommentDao activityCommentDao;
	@Resource(name = "activityAttenceDao")
	private ActivityAttenceDao activityAttenceDao;
	@Resource(name = "activityDao")
	private ActivityDao activityDao;
	@Resource(name="activityFocusDao")
	private ActivityFocusDao activityFocusDao;
	@Resource(name="myInfoDao")
	private MyInfoDao myInfoDao;
	@Resource(name="activityTakeDao")
	private ActivityTakeDao activityTakeDao;
	@Resource(name="enterpriseDao")
	private EnterpriseDao enterpriseDao;
	private MeaasgeUtil me = new MeaasgeUtil();
	@Bean
	public UserService userService() {
		return new UserService();
	}

	@Transactional
	public List getVerified(Long uid) {
		return ud.getVerified(uid);
	}

	/**
	 * 查询手机号是否注册
	 * @param phone 接收相关参数
	 * @return java.lang.String
	 * @author liumengwei
	 */
	@Transactional
	public String getUserByPhone(FNewUserBean phone) {
		User user_ = ud.getUserByPhone(phone.getPhone());
		UserBind userBind = ubd.getByPhone(phone.getPhone());
		if (user_ == null && userBind == null) {
			return "";
		} else {
			String usermobile = "";
			String ubmobile = "";
			if (user_ != null) {
				usermobile = user_.getMobile();
			}
			if (userBind != null) {
				ubmobile = userBind.getMobile();
			}
			if (StringUtil.isEmpty(usermobile) && StringUtil.isEmpty(ubmobile)) {
				return "";
			} else {
				return StringUtil.isEmpty(usermobile) ? ubmobile : usermobile;
			}
		}
	}

	/**
	 * 用户注册
	 * @param fuser 接收相关参数
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 * @Date 17/8/5
	 */
	@Transactional
	public ReturnValue<FNewUserBean> regist(FNewUserBean fuser) {
		ReturnValue<FNewUserBean> re = new ReturnValue<>();
		try {
			// 查询用户名是否被注册
			User user_ = ud.getUserByUserName(fuser.getUsername());
			if (user_ != null) {
				re.setFlag(ReturnValue.FLAG_EXCEPTION);
				re.setMeg(me.getValue(ResultMsgConstant.usernameHasBeenRegistered));
			} else {
				// 查询手机号是否被注册
				user_ = ud.getUserByPhone(fuser.getPhone());
				if (user_ != null) {
					re.setFlag(ReturnValue.FLAG_EXCEPTION);
					re.setMeg(me.getValue(ResultMsgConstant.phoneHasBeenRegistered));
				} else {
					// 保存用户注册信息
					User user = new User();
					user.setCreatetime(DateUtil.getCurrentDateForSql());
					user.setMobile(fuser.getPhone());
					user.setStatus("1");
					user.setPassword(EncryptionByMD5.getMD5(fuser.getPassword()
							.getBytes()));
					user.setGender(fuser.getGender());
					user.setUsername(fuser.getUsername());
					String id = ud.save(user).toString();
					fuser.setUid(id);
					re.setFlag(ReturnValue.FLAG_SUCCESS);
					re.setMeg(me.getValue(ResultMsgConstant.registSuccess));
				}
			}
			re.setObject(fuser);
		} catch (Exception ex) {
			return SystemException.setResult(re, ex, logger);
		}
		return re;
	}

	/**
	 * 用户登陆
	 * @param fuser 接收相关参数
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 * @Date 2017/8/5
	 */
	@Transactional
	public ReturnValue<FNewUserBean> login(FNewUserBean fuser) {
		ReturnValue<FNewUserBean> re = new ReturnValue<>();
		try {
			// 根据用户名查询
			User u1 = ud.getUserByUserName(fuser.getUsername());
			// 根据手机号查询
			User u2 = ud.getUserByPhone(fuser.getUsername());
			User u3 = ud.getUserByPhone(fuser.getPhone());
			User u4;
			String password;
			if (u1 != null) {
				password = u1.getPassword();
				u4 = u1;
			} else if (u2 != null) {
				password = u2.getPassword();
				u4 = u2;
			} else if (u3 != null) {
				password = u3.getPassword();
				u4 = u3;
			} else {
				re.setFlag(ReturnValue.FLAG_EXCEPTION);
				re.setMeg(me.getValue(ResultMsgConstant.withoutThisUser));
				re.setObject(fuser);
				return re;
			}
			if (password.equals(EncryptionByMD5.getMD5(fuser.getPassword()
					.getBytes()))) {
				// 获取用户id
				ActivityPrivilege listMap = activityPrivilegeDao.getId(
						Long.parseLong(String.valueOf(u4.getId())),
						ActivityPrivilegeStatus.PERSONAL.getStatus());
				fuser.setPhone(u4.getMobile());
				fuser.setUid(String.valueOf(u4.getId()));
				if (listMap != null) {
					fuser.setPid(listMap.getId());
					fuser.setUserType(listMap.getUserType());
					// 查询用户是否实名
					if (Integer.parseInt(listMap.getPublishActivityPrivilege()) ==
							ActivityPrivilegeStatus.CANNOTPUBLISHED.getStatus())
						fuser.setVerified(true);
					else
						fuser.setVerified(false);
				} else {
					fuser.setPid(0L);
					fuser.setUserType("0");
				}
				fuser.setHeadPic(u4.getHeadpic());
				re.setFlag(ReturnValue.FLAG_SUCCESS);
				re.setMeg(me.getValue(ResultMsgConstant.loginSuccess));
			} else {
				re.setFlag(ReturnValue.FLAG_EXCEPTION);
				re.setMeg(me.getValue(ResultMsgConstant.passError));
			}
			re.setObject(fuser);
		} catch (Exception ex) {
			return SystemException.setResult(re, ex, logger);
		}
		return re;
	}

	/**
	 * 获取用户列表
	 * @param uid 用户id
	 * @param page 页码
	 * @param userType 用户类型
	 * @param type 查看的用户类型：0好友(同一个活动参与者)，1粉丝(同一个活动非参与者)
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 */
	@Transactional
	public ReturnValue getActiveUserList(Long uid, Integer page, Integer type, Long pid, String userType) {
		ReturnValue<List<FUserListBean>> re = new ReturnValue<>();
		try {
			List<FUserListBean> listFUserListBean = new ArrayList<>();
			// 获取用户参与的所有活动id集合
			Page<Map<String, Object>> attence = activityAttenceDao.getActivitys(page, pid);
			ActivityPrivilege privilege = activityPrivilegeDao.getId(uid, Integer.parseInt(userType));
			Page<Map<String, Object>> publishingActivity = activityDao.getActivitys(page, privilege.getId());
			if (attence != null) {
				List<Map<String, Object>> attencesActivities = attence.getPageContent();
				if (publishingActivity != null) {
					attencesActivities.addAll(publishingActivity.getPageContent());
					if (attencesActivities != null) {
						List<Object> listAid = new ArrayList<>();
						for (Map<String, Object> mapActivitys : attencesActivities) {
							FUserListBean fUserListBean = new FUserListBean();
							Object aid = mapActivitys.get("aid");
							Boolean sameAid = false;
							if (aid != null && !"null".equals(aid)) {
								for (Object oAid : listAid) {
									if (oAid.equals(aid)) {
										sameAid = true;
										continue;
									}
								}
								if (sameAid) continue;
								listAid.add(aid);
								// 获取参与该活动的所有用户集合
								Page<Map<String, Object>> listMapUsers =
										activityAttenceDao.getUsers(Integer.parseInt(aid.toString()), page, type);
								List<Map<String, Object>> mapListUsers = listMapUsers.getPageContent();
								Map<String, Object> map = new HashMap<>();
								List<Map<String, Object>> listUsers = new ArrayList<>();
								if (mapListUsers != null) {
									for (Map<String, Object> mapUsers : mapListUsers) {
										Map<String, Object> user = new HashMap<>();
										Integer userId = Integer.parseInt(mapUsers.get("uid").toString());
										// 如果查询出的id != 该用户id，添加信息
										if (userId != Integer.parseInt(uid.toString())) {
											user.put("uid", userId);
											user.put("pid", mapUsers.get("pid"));
											user.put("username", mapUsers.get("username").toString());
											if (mapUsers.get("headPic") == null)
												user.put("headPic", "");
											else
												user.put("headPic", mapUsers.get("headPic").toString());
											listUsers.add(user);
										}
									}
								}
								map.put("hasNext", listMapUsers.isHasNextPage());
								map.put("users", listUsers);
								// 如果用户集合长度>0，添加信息
								if (listUsers.size() > 0) {
									fUserListBean.setAid(Long.parseLong(aid.toString()));
									fUserListBean.setSubject(mapActivitys.get("subject").toString());
									Object status = mapActivitys.get("status");
									fUserListBean.setStatus(Integer.parseInt(status.toString()));
									fUserListBean.setUser(map);
								}
							}
							fUserListBean.setHasNext(attence.isHasNextPage());
							if (type == 0 && fUserListBean.getAid() != null)
								listFUserListBean.add(fUserListBean);
						}
					} else {
						return null;
					}
				}
			}
			re.setObject(listFUserListBean);
			re.setFlag(Integer.parseInt(ResultStruct.OK));
			re.setMeg(me.getValue(ResultMsgConstant.querySuccess));
		} catch (Exception ex) {
			return SystemException.setResult(re, ex, logger);
		}
		return re;
	}

	/**
	 * 获取粉丝数据
	 * @param uid 用户id
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 * @data 2017-11-15
	 */
	@Transactional
	public ReturnValue<FanData> getFansData(Long uid) {
		ReturnValue<FanData> re = new ReturnValue<>();
		try {
			// 获取用户参与的所有活动id集合
			List<Map<String, Object>> attence = activityAttenceDao.getActivitys( uid);
			// 总人数
			Integer totalPeople = 0;
			// 男生总数
			Integer numberOfBoys = 0;
			// 女生总数
			Integer numberOfGirls = 0;
			// 15岁一下人数
			Integer numberOfUnder15YearsOld = 0;
			// 16-25人数
			Integer numberOf16_25earsOld = 0;
			// 26-40人数
			Integer numberOf26_40earsOld = 0;
			// 40以上人数
			Integer numberOfOver40YearsOld = 0;
			if (attence != null) {
				for (Map<String, Object> mapActivitys : attence) {
					Object aid = mapActivitys.get("aid");
					if (aid != null) {
						Long lAid = Long.parseLong(aid.toString());
						// 获取用参与该活动的粉丝数量
						List<Map<String, Object>> listForAttence = activityAttenceDao.getTotalPeopleForAttence(uid, lAid);
						List<Map<String, Object>> listForFocus = activityFocusDao.getTotalPeopleForFocus(uid, lAid);
						totalPeople += (listForAttence.size() + listForFocus.size());
						if (totalPeople > 0) {
							// 获取男生总人数
							Integer numberOfBoysForAttence = activityAttenceDao.getNumberOfBoysForAttence(uid, lAid);
							Integer numberOfBoysForFocus = activityFocusDao.getNumberOfBoysForFocus(uid, lAid);
							numberOfBoys += (numberOfBoysForAttence + numberOfBoysForFocus);
							// 获取女生总人数
							numberOfGirls += (totalPeople - numberOfBoys);
							// 获取15岁以下人数
							listForAttence.addAll(listForFocus);
							for (Map<String, Object> user : listForAttence) {
								Object IDNumber = user.get("IDNumber");
								if (IDNumber == null ||
										IDNumber == "" ||
										IDNumber == "null" ||
										"".equals(IDNumber)) {
									continue;
								}
								int age = CertCodeUtil.getAge(IDNumber.toString());
								if (age != -1) {
									if (age <= 15) {
										numberOfUnder15YearsOld++;
									}
									if (age > 15 && age <= 25) {
										numberOf16_25earsOld++;
									}
									if (age > 25 && age <= 40) {
										numberOf26_40earsOld++;
									}
									if (age > 40) {
										numberOfOver40YearsOld++;
									}
								} else {
									re.setFlag(Integer.parseInt(ResultStruct.ERROR));
									re.setMeg(me.getValue(ResultMsgConstant.idNumberErrpr));
									return re;
								}
							}
						}
					}
				}
			}else {
				return null;
			}
			FanData fanData = new FanData();
			fanData.setNumberOf16_25earsOld(numberOf16_25earsOld);
			fanData.setNumberOf26_40earsOld(numberOf26_40earsOld);
			fanData.setNumberOfOver40YearsOld(numberOfOver40YearsOld);
			fanData.setNumberOfUnder15YearsOld(numberOfUnder15YearsOld);
			fanData.setNumberOfBoys(numberOfBoys);
			fanData.setNumberOfGirls(numberOfGirls);
			fanData.setTotalPeople(totalPeople);
			re.setObject(fanData);
			re.setFlag(Integer.parseInt(ResultStruct.OK));
			re.setMeg(me.getValue(ResultMsgConstant.querySuccess));
        } catch (Exception ex) {
			return SystemException.setResult(re, ex, logger);
		}
		return re;
	}

	/**
	 * 查询用户发布的活动
	 * @param pid 权限id
	 * @param page 页码
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 * @Date 2017/9/9
	 */
	@Transactional
	public ReturnValue<Map<String, Object>> getPublishingActivity(Long pid, Integer page) {
		ReturnValue<Map<String, Object>> rv = new ReturnValue<>();
		try {
			Map<String, Object> mapActivity = new HashMap<>();
			List<ActivityBean> list = new ArrayList<>();
			Page<Map<String, Object>> listMapPublishingActivity = activityDao.getPublishingActivity(pid, page);
			List<Map<String, Object>> listMap = listMapPublishingActivity.getPageContent();
			for (Map<String, Object> map : listMap) {
				ActivityBean activity = new ActivityBean();
				Object aid = map.get("id");
				if (aid != null) {
					// 获取已通过人数
					Integer attentNum = activityAttenceDao.getAttenceNumByAidPid(Long.parseLong(aid.toString()), pid);
					// 获取预通过人数
					Integer prepassedNum = activityAttenceDao.getPrepassedNumByAidPid(Long.parseLong(aid.toString()), pid);
					activity.setAttenceNumber(attentNum);
					activity.setPrepassedNum(prepassedNum);
					activity.setAid(Long.parseLong(aid.toString()));
					Object end_time = map.get("end_time");
					activity.setEndDateTime(end_time == null?
							"" : DateUtil.getFromUnix(Long.parseLong(end_time.toString())));
					Object start_time = map.get("start_time");
					activity.setBeginDateTime(start_time == null ?
							"" : DateUtil.getFromUnix(Long.parseLong(start_time.toString())));
					Object register_end_time = map.get("register_end_time");
					activity.setRegisterEndTime(register_end_time == null?
							"" : DateUtil.getDateTime(register_end_time.toString()));
					activity.setSubject(map.get("subject") == null ? "" : map.get("subject") + "");
					activity.setStatus(Integer.parseInt(map.get("status") + ""));
					activity.setActivityNum(Integer.parseInt(map.get("activity_number") + ""));
					activity.setCheckStatus(Integer.parseInt(map.get("check_status").toString()));
					list.add(activity);
				}
				continue;
			}
			mapActivity.put("activities", list);
			mapActivity.put("hasNext", listMapPublishingActivity.isHasNextPage());
			rv.setObject(mapActivity);
			rv.setMeg(me.getValue(ResultMsgConstant.querySuccess));
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
		} catch (Exception ex) {
			return SystemException.setResult(rv, ex, logger);
		}
		return rv;
	}

	/**
	 * 查询用户发布的圈子
	 * @param pid 权限id
	 * @param page 页码
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 * @Date 2017/11/24
	 */
	@Transactional
	public ReturnValue<Map<String, Object>> getPublishingActivityCircle(Long pid, Integer page) {
		ReturnValue<Map<String, Object>> rv = new ReturnValue<>();
		try {
			Map<String, Object> mapActivity = new HashMap<>();
			List<ActivityBean> list = new ArrayList<>();
			Page<Activity> listMapPublishingActivity = activityDao.getPublishingActivityCircle(pid, page);
			List<Activity> listMap = listMapPublishingActivity.getPageContent();
			for (Activity map : listMap) {
				ActivityBean activity = new ActivityBean();
				Long aid = map.getId();
				if (aid != null) {
					// 获取参与人数
					Integer attentNum = activityAttenceDao.getAttenceNumByAidPid(Long.parseLong(aid.toString()), pid);
					// 获取粉丝人数
					Long uid = activityPrivilegeDao.get(ActivityPrivilege.class, pid).getUid();
					List<Map<String, Object>> listForAttence = activityAttenceDao.getTotalPeopleForAttence(uid, aid);
					List<Map<String, Object>> listForFocus = activityFocusDao.getTotalPeopleForFocus(uid, aid);
					Integer totalPeople = (listForAttence.size() + listForFocus.size());
					activity.setCheckStatus(map.getCheckStatus());
					String registerEndTime = map.getRegisterEndTime();
					Long beginDateTime = map.getStartTime();
					Long endDateTime = map.getEndTime();
					activity.setBeginDateTime(endDateTime == null ? "" : DateUtil.getDateTime(beginDateTime.toString()));
					activity.setPrepassedNum(0);
					activity.setEndDateTime(endDateTime == null ? "" : DateUtil.getDateTime(endDateTime.toString()));
					activity.setRegisterEndTime(StringUtil.isEmpty(registerEndTime) ? "" : DateUtil.getDateTime(registerEndTime));
					activity.setAttenceNum(attentNum);
					activity.setActivityNum(totalPeople);
					activity.setAid(Long.parseLong(aid.toString()));
					String subject = map.getSubject();
					activity.setSubject(StringUtil.isEmpty(subject) ? "" : subject);
					activity.setStatus(map.getStatus());
					list.add(activity);
				}
				continue;
			}
			mapActivity.put("activities", list);
			mapActivity.put("hasNext", listMapPublishingActivity.isHasNextPage());
			rv.setObject(mapActivity);
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
			rv.setMeg(me.getValue(ResultMsgConstant.querySuccess));
		} catch (Exception ex) {
			return SystemException.setResult(rv, ex, logger);
		}
		return rv;
	}

	/**
	 * 查询用户参与圈子下用户列表
	 * @param aid 活动id
	 * @param page 页码
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 * @Date 2017/11/24
	 */
	@Transactional
	public ReturnValue<FActivityRegistrationInformationBean> getParticipateCircleForUserList(Long aid, Integer page) {
		ReturnValue<FActivityRegistrationInformationBean> rv = new ReturnValue<>();
		try {
			FActivityRegistrationInformationBean informationBean = new FActivityRegistrationInformationBean();
			List<FActivityRegistrationInformationBean.Users> list = new ArrayList<>();
			// 查询用户列表

			List<Map<String, Object>> userPage = activityAttenceDao.getUsers(aid, page);
//			Page<User> userPage = activityAttenceDao.getUsers(aid, page);
//			List<User> userList = userPage.getPageContent();
//			list = traverseUserList(userList, list);
			list = traverseUserList(userPage, list);

			informationBean.setUsers(list);
			informationBean.setHasNext(false);
			rv.setObject(informationBean);
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
			rv.setMeg(me.getValue(ResultMsgConstant.querySuccess));
		} catch (Exception ex) {
			return SystemException.setResult(rv, ex, logger);
		}
		return rv;
	}

	/**
	 * 查询用户发起圈子下用户列表
	 * @param aid 活动id
	 * @param pid 权限id
	 * @param page 页码
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 * @Date 2017/11/24
	 */
	@Transactional
	public ReturnValue<FActivityRegistrationInformationBean> getInitiatedCircleForUserList(Long aid, Long pid, Integer page) {
		ReturnValue<FActivityRegistrationInformationBean> rv = new ReturnValue<>();
		try {
			FActivityRegistrationInformationBean informationBean = new FActivityRegistrationInformationBean();
			List<FActivityRegistrationInformationBean.Users> list = new ArrayList<>();
			// 查询用户列表
			List<Map<String, Object>> userPage = activityDao.getInitiatedCircleForUserList(aid, pid, page);
//			Page<User> userPage = activityDao.getInitiatedCircleForUserList(aid, pid, page);
//			List<User> userList = userPage.getPageContent();
			list = traverseUserList(userPage, list);

			informationBean.setUsers(list);
			informationBean.setHasNext(false);
			rv.setObject(informationBean);
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
			rv.setMeg(me.getValue(ResultMsgConstant.querySuccess));
		} catch (Exception ex) {
			return SystemException.setResult(rv, ex, logger);
		}
		return rv;
	}

	private List<FActivityRegistrationInformationBean.Users> traverseUserList(
			List<Map<String, Object>> userList, List<FActivityRegistrationInformationBean.Users> list) {
		for (Map<String, Object> user : userList) {
			FActivityRegistrationInformationBean.Users users = new FActivityRegistrationInformationBean().new Users();
			String headpic = String.valueOf(user.get("headpic"));
			String username = String.valueOf(user.get("username"));
			users.setHeadPic(StringUtil.isEmpty(headpic) ? "" : headpic);
			users.setUsername(StringUtil.isEmpty(username) ? "" : username);
			users.setUid(Long.parseLong(String.valueOf(user.get("uid"))));
			users.setPid(Long.parseLong(String.valueOf(user.get("pid"))));
			list.add(users);
		}
		return list;
	}

	/**
	 * 将用户从圈子中删除
	 * @param uid 用户id
	 * @param pid 用户权限id
	 * @param deletePid 被删除用户权限id
	 * @param aid 活动id
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 * @Date 2017/11/24
	 */
	@Transactional
	public ReturnValue deleteMember(String deletePid, Long pid, String uid, Long aid) {
		ReturnValue rv = new ReturnValue<>();
		rv.setFlag(ReturnValue.FLAG_FAIL);
		rv.setMeg(me.getValue(ResultMsgConstant.noPermissionToDeleteMembers));
		try {
			Activity activity = activityDao.getActivityByPidAid(aid, pid);
			// TODO uid换成pid数组，防止第三方用户id和普通用户id重复
			if (activity != null) {
				String[] result = deletePid.split(",");
				String[] strPid = new String[result.length];
				for (int i = 0; i <result.length; i++) {
					strPid[i] = result[i];
				}
				Boolean resule = activityAttenceDao.updateUserAttentStatus(
						strPid, aid, ActivityAttenceStatus.DELETE.getStatus());
				rv.setFlag(resule ? ReturnValue.FLAG_SUCCESS : ReturnValue.FLAG_FAIL);
				rv.setMeg(resule ? me.getValue(ResultMsgConstant.deleteSuccess) : me.getValue(ResultMsgConstant.deleteFail));
			}
		} catch (Exception ex) {
			return SystemException.setResult(rv, ex, logger);
		}
		return rv;
	}

	/**
	 * 查询用户参与的活动
	 * @param pid 权限id
	 * @param uid 用户id
	 * @param page 页码
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 * @Date 2017/11/11
	 */
	@Transactional
	public ReturnValue<FUserActivitiesBean> getTheParticipationList(Long pid, Long uid, Integer page, String status) {
		ReturnValue<FUserActivitiesBean> rv = new ReturnValue<>();
		try {
			FUserActivitiesBean fUserActivitiesBean = new FUserActivitiesBean();
			List<FUserActivitiesBean.Activities> activitiesList = new ArrayList<>();
			Page<Map<String, Object>> mapPage = activityDao.getTheParticipationList(pid, page, status);
			List<Map<String, Object>> mapList = mapPage.getPageContent();
			for(Map<String, Object> map : mapList) {
				FUserActivitiesBean.Activities activities = new FUserActivitiesBean().new Activities();
				Long aid = Long.parseLong(map.get("id").toString());
				activities.setAid(aid);
				Integer attenceNum = activityAttenceDao.getActivityAttenceNumber(aid, 0);
				activities.setAttenceNum(attenceNum);
				activities.setPrepassedNum(0);
				activities.setStatus(Integer.parseInt(map.get("status").toString()));
				activities.setSubject(map.get("subject").toString());
				activities.setCheckStatus(Integer.parseInt(map.get("check_status").toString()));
				activities.setActivityNum(Integer.parseInt(map.get("activity_number").toString()));
				Object registerEndTime = map.get("register_end_time");
				Object endTime = map.get("end_time");
				Object startTime = map.get("start_time");
				activities.setRegisterEndTime(
						registerEndTime == null ? "" :
								DateUtil.getFromUnix(Long.parseLong(registerEndTime.toString())));
				activities.setEndDateTime(
						endTime == null ? "" :
								DateUtil.getFromUnix(Long.parseLong(endTime.toString())));
				activities.setBeginDateTime(
						startTime == null ? "" :
								DateUtil.getFromUnix(Long.parseLong(startTime.toString())));
				activitiesList.add(activities);
			}
			fUserActivitiesBean.setActivities(activitiesList);
			fUserActivitiesBean.setHasNext(mapPage.isHasNextPage());
			rv.setObject(fUserActivitiesBean);
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
			rv.setMeg(me.getValue(ResultMsgConstant.querySuccess));
		} catch (Exception ex) {
			return SystemException.setResult(rv, ex, logger);
		}
		return rv;
	}

	/**
	 * 对发布的活动报名人审核
	 * @param signUpPid 用户id
	 * @param aid 活动id
	 * @param status 审核状态：0不通过 1通过 3预通过
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 * @Date 2017/9/9
	 */
	@Transactional
	public ReturnValue<Map<String, Object>> updateUserAttentStatus(String signUpPid, Long aid, Integer status) {
		ReturnValue<Map<String, Object>> rv = new ReturnValue<>();
		try {
			String[] szPid = signUpPid.split(",");
			Boolean updateResult = activityAttenceDao.updateUserAttentStatus(szPid, aid, status);
			rv.setFlag(updateResult == true ? ReturnValue.FLAG_SUCCESS : ReturnValue.FLAG_FAIL);
			rv.setMeg(updateResult == true ? me.getValue(ResultMsgConstant.auditSuccess) : me.getValue(ResultMsgConstant.auditFailure));
		} catch (Exception ex) {
			return SystemException.setResult(rv, ex, logger);
		}
		return rv;
	}

	/**
	 * 获取发布的活动报名人信息
	 * @param pid 权限id
	 * @param uid 用户id
	 * @param page 页码
	 * @param aid 活动id
	 * @param registraStatus 通过状态 0已报名和预通过 1已通过
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 * @Date 2017/10/12
	 */
	@Transactional
	public ReturnValue<FActivityRegistrationInformationBean> getActivityRegistrationInformation(
			Long pid, String uid, Integer userType, Integer page, Long aid, Integer registraStatus) {
		ReturnValue<FActivityRegistrationInformationBean> rv = new ReturnValue<>();
		try {
			FActivityRegistrationInformationBean fActivityRegistrationInformationBean =
					new FActivityRegistrationInformationBean();
			// 根据活动id 发布者id查询该用户下的该活动下报名的用户
			List<FActivityRegistrationInformationBean.Users> usersList = new ArrayList<>();
			// 查询用户信息
			String subuid = uid.substring(2);
			String username = "";
			String headPic = "";
			if (userType == ActivityPrivilegeStatus.PERSONAL.getStatus()) {
				User user = ud.get(User.class, Integer.parseInt(subuid));
				headPic = user.getHeadpic();
				username = user.getUsername();
			} else if (userType == ActivityPrivilegeStatus.PERSONAL.getStatus()) {
				UserBind user = ubd.get(UserBind.class, Integer.parseInt(subuid));
				headPic = user.getHeadpic();
				username = user.getUsername();
			}
			if ("admin".equals(username)) {
				Enterprise enterprise = enterpriseDao.getEnterprise("新犀国际");
				ActivityPrivilege enterPrivilege = activityPrivilegeDao.getId(enterprise.getId(), 1);
				pid = enterPrivilege.getId();
			}
			Page<Map<String, Object>> usersPage =
					activityAttenceDao.getActivityRegistrationInformation(aid, pid, registraStatus, page);
			List<Map<String, Object>> mapList = usersPage.getPageContent();
			Object registerEndTime = null;
			if (mapList != null) {
				for (Map<String, Object> map : mapList) {
					Integer status = Integer.parseInt(map.get("status").toString());
					if (status == ActivityAttenceStatus.ABNORMAL.getStatus()) {
						continue;
					}
					FActivityRegistrationInformationBean.Users users =
							new FActivityRegistrationInformationBean().new Users();
					users.setHeadPic(map.get("headpic") == null ||
							map.get("headpic") == "" ||
							map.get("headpic") ==  "null"
							? "" : map.get("headpic").toString());
					users.setPid(Long.parseLong(map.get("pid").toString()));
					users.setSelfIntroduction(map.get("self_introduce"));
					users.setUsername(map.get("username"));
					users.setStatus(status);
					users.setUid(Long.parseLong(map.get("id").toString()));
					usersList.add(users);
					registerEndTime =  map.get("registerEndTime");
				}
			}
			// 添加发布者头像
			fActivityRegistrationInformationBean.setHeadPic(StringUtil.isEmpty(headPic) ? "" : headPic);
			fActivityRegistrationInformationBean.setRegisterEndTime(
					registerEndTime == null
							? "" : DateUtil.getDateTime(registerEndTime.toString()));
			// 添加封面图
			ActivityPrivilege privilege = activityPrivilegeDao.get(ActivityPrivilege.class, pid);
			MyInfo myInfo = myInfoDao.getMyInfo(privilege.getId());
			fActivityRegistrationInformationBean.setCoverImage(
					myInfo == null ? "" : myInfo.getCoverImage()
			);
			// 总页数
			Integer totalPages = activityAttenceDao.getTotalPages(pid, aid);
			fActivityRegistrationInformationBean.setTotalPages(totalPages);
			// 用户报名信息
			fActivityRegistrationInformationBean.setUsers(usersList);
			// 是否还有数据
			fActivityRegistrationInformationBean.setHasNext(usersPage.isHasNextPage());
			rv.setObject(fActivityRegistrationInformationBean);
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
			rv.setMeg(me.getValue(ResultMsgConstant.querySuccess));
		} catch (Exception ex) {
			return SystemException.setResult(rv, ex, logger);
		}
		return rv;
	}

	/**
	 * 添加封面图
	 * @param uid 用户id
	 * @param pid 用户权限id
	 * @param user 相关参数
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 * @Date 2017/10/15
	 */
	@Transactional
	public ReturnValue saveMyInfo(String uid, String pid, FNewUserBean user) {
		ReturnValue rv = new ReturnValue<>();
		try {
			MyInfo myInfo = new MyInfo();
			String coverImage = user.getCoverImage();
			myInfo.setCoverImage(coverImage);
			myInfo.setStatus(ActivityCommentStatus.NORMAL.getStatus());
			ActivityPrivilege privilege = activityPrivilegeDao.get(ActivityPrivilege.class, Long.parseLong(pid));
			myInfo.setPrivilegeId(privilege);
			myInfo.setInsertTime(System.currentTimeMillis() / 1000);

			MyInfo myInfo1 = myInfoDao.getMyInfo(privilege.getId());
			if (myInfo1 == null) {
				myInfoDao.save(myInfo);
			} else {
				myInfo1.setCoverImage(coverImage);
				myInfoDao.getSession().update(myInfo1);
			}
			rv.setObject(getUserInfo(uid, pid).getObject());
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
			rv.setMeg(me.getValue(ResultMsgConstant.addSuccess));
		} catch (Exception ex) {
			return SystemException.setResult(rv, ex, logger);
		}
		return rv;
	}

	/**
	 * 修改用户信息
	 * @param uid 用户id
	 * @param pid 用户权限id
	 * @param fUserInfoBean 用户信息相关参数
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 * @Date 2017/11/25
	 */
	@Transactional
	public ReturnValue updateUserInfo(String uid, String pid, FNewUserBean fUserInfoBean, Integer userType) {
		ReturnValue rv = new ReturnValue();
		try {
			String subuid = uid.substring(2);
			Integer lUid = Integer.parseInt(subuid);
			String headpic = fUserInfoBean.getHeadPic();
			String username = fUserInfoBean.getUsername();
			String selfIntroduction = fUserInfoBean.getSelfIntroduction();
			String gender = fUserInfoBean.getStrGender();
			User user = null;
			UserBind userBind = null;
			Integer usernameSame = (ud.getUserName(username)) + (ubd.getUserName(username));
			if (userType == ActivityPrivilegeStatus.PERSONAL.getStatus()) {
				user = ud.get(User.class, lUid);
				user.setId(lUid);
				user.setHeadpic(StringUtil.isEmpty(headpic) ? user.getHeadpic() : headpic);
				user.setGender(
						StringUtil.isEmpty(gender) ? user.getGender() : Integer.parseInt(gender));
				if (!StringUtil.isEmpty(username)) {
					if (usernameSame <= 0) {
						user.setUsername(username);
					} else {
						rv.setMeg(me.getValue(ResultMsgConstant.usernameAlreadyExists));
						rv.setFlag(ReturnValue.FLAG_FAIL);
						return rv;
					}
				}
				ud.getSession().update(user);
			} else if (userType == ActivityPrivilegeStatus.RHIRD_PARTY.getStatus()) {
				userBind = ubd.get(UserBind.class, lUid);
				userBind.setId(lUid);
				userBind.setHeadpic(StringUtil.isEmpty(headpic) ? userBind.getHeadpic() : headpic);
				String mobile = fUserInfoBean.getPhone();
				userBind.setMobile(StringUtil.isEmpty(mobile) ? userBind.getMobile() : mobile);
				userBind.setGender(
						StringUtil.isEmpty(gender) ? userBind.getGender() : Integer.parseInt(gender));
				if (!StringUtil.isEmpty(username)) {
					if (usernameSame <= 0) {
						userBind.setUsername(username);
					} else {
						rv.setMeg(me.getValue(ResultMsgConstant.usernameAlreadyExists));
						rv.setFlag(ReturnValue.FLAG_FAIL);
						return rv;
					}
				}
				ubd.getSession().update(userBind);
			}

			ActivityPrivilege privilege = activityPrivilegeDao.get(ActivityPrivilege.class, Long.parseLong(pid));
			MyInfo myInfo = myInfoDao.getMyInfo(privilege.getId());
			if(myInfo != null) {
				myInfo.setPrivilegeId(privilege);
				myInfo.setSlogan(
						StringUtil.isEmpty(selfIntroduction)
								? myInfo.getSlogan() : selfIntroduction);
				myInfoDao.getSession().update(myInfo);
			} else {
				FNewUserBean fNewUserBean=  new FNewUserBean();
				fNewUserBean.setSelfIntroduction(selfIntroduction);
				fNewUserBean.setCoverImage(null);
				saveMyInfo(uid, pid, fNewUserBean);
			}
			rv.setObject(getUserInfo(uid, pid).getObject());
			rv.setMeg(me.getValue(ResultMsgConstant.modifySuccess));
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
		} catch (Exception ex) {
			return SystemException.setResult(rv, ex, logger);
		}
		return rv;
	}

	/**
	 * 获取用户信息
	 * @param uid 用户id
	 * @param pid 用户权限id
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 * @Date 2017/12/15
	 */
	@Transactional
	public ReturnValue getUserInfo(String uid, String pid) {
		ReturnValue rv = new ReturnValue<>();
		try {
			ActivityPrivilege privilege = activityPrivilegeDao.get(ActivityPrivilege.class, Long.parseLong(pid));
			MyInfo myInfo = myInfoDao.getMyInfo(Long.parseLong(pid));
			String headPic = "";
			String idNumber = "";
			String mobile = "";
			String username = "";
			Integer gender = null;
			FNewUserBean fNewUserBean = new FNewUserBean();
			if (ActivityPrivilegeStatus.PERSONAL.getStatus().toString().equals(privilege.getUserType())) {
				User uu = ud.get(User.class, Integer.parseInt(uid));
				headPic = uu.getHeadpic();
				idNumber = uu.getIDNumber();
				mobile = uu.getMobile();
				username = uu.getUsername();
				gender = uu.getGender();
			} else if(ActivityPrivilegeStatus.RHIRD_PARTY.getStatus().toString().equals(privilege.getUserType())) {
				UserBind uu = ubd.get(UserBind.class, Integer.parseInt(privilege.getUid().toString()));
				headPic = uu.getHeadpic();
				idNumber = uu.getIdNumber();
				mobile = uu.getMobile();
				username = uu.getUsername();
				gender = uu.getGender();
			}
			fNewUserBean.setIdNumber(StringUtil.isEmpty(idNumber) ? "" : idNumber);
			fNewUserBean.setPhone(StringUtil.isEmpty(mobile) ? "" : mobile);
			fNewUserBean.setUsername(StringUtil.isEmpty(username) ? "" : username);
			fNewUserBean.setHeadPic(StringUtil.isEmpty(headPic) ? "" : headPic);
			fNewUserBean.setGender(gender);
			fNewUserBean.setUid(uid);
			if (myInfo != null) {
				String slogan = myInfo.getSlogan();
				fNewUserBean.setSelfIntroduction(StringUtil.isEmpty(slogan) ? "" : slogan);
				String coverImage = myInfo.getCoverImage();
				fNewUserBean.setCoverImage(StringUtil.isEmpty(coverImage) ? "" : coverImage);
			} else {
				fNewUserBean.setSelfIntroduction("");
				fNewUserBean.setCoverImage("");
			}
			rv.setObject(fNewUserBean);
			rv.setMeg(me.getValue(ResultMsgConstant.querySuccess));
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
		} catch (Exception ex) {
			return SystemException.setResult(rv, ex, logger);
		}
		return rv;
	}

	/**
	 * 查询用户发布的活动
	 * @param pid 权限id
	 * @param uid 用户id
	 * @param page 页码
	 * @param activitySubject 活动主题关键词
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 * @Date 2017/9/9
	 */
//	@Transactional
//	public ReturnValue<FUserActivitiesBean> getUserActivities(Long pid, Long uid, Integer page, String activitySubject) {
//		ReturnValue<FUserActivitiesBean> rv = new ReturnValue<>();
//		FUserActivitiesBean fUserActivitiesBean = new FUserActivitiesBean();
//		List<FUserActivitiesBean.Activities> activitiesList = new ArrayList<>();
//		rv.setFlag(ReturnValue.FLAG_SUCCESS);
//		rv.setMeg("查询成功");
//		try {
//			// 查询用户下发布的活动
//			Query activityPrivilege = activityPrivilegeDao.getPrivilegeByPid(pid, activitySubject);
//			if (activityPrivilege != null) {
//				Page<Map<String, Object>> activityPage = activityDao.getActivityByEid(pid, page, activitySubject);
//				List<Map<String, Object>> mapList = activityPage.getPageContent();
//				Long aid;
//				List<Long> listFAid = new ArrayList<>();
//				List<Long> listAid = new ArrayList<>();
//				if (mapList != null) {
//					for (Map<String, Object> map : mapList) {
//						FUserActivitiesBean.Activities activities = new FUserActivitiesBean().new Activities();
//						aid = Long.parseLong(map.get("aid").toString());
//						listAid.add(aid);
//						activities.setAid(Long.parseLong(aid.toString()));
//						activities.setIndexPic(map.get("indexPic").toString());
//						activities.setUid(uid);
//						activities.setSubject(map.get("subject").toString());
//						Integer focusNum = activityFocusDao.getActivityFocusNumber(aid);
//						Integer attenceParticipateNum = activityAttenceDao.getActivityAttenceNumber(aid, page);
//						Integer attenceNum = activityAttenceDao.getActivityAttenceNumber(aid, null);
//						activities.setFocusNum(((focusNum + (attenceNum - attenceParticipateNum))));
//						activities.setAttenceNum(attenceParticipateNum);
//						// 未读消息数
//						Integer infoNum = activityCommentReadDao.getInfoNum(pid, aid);
//						Integer activityPicTextNum = activityCommentDao.getCommentNumber(aid, pid);
//						activities.setInfoNum(activityPicTextNum - infoNum);
//						Integer status = Integer.parseInt(map.get("status").toString());
//						activities.setGroupOrActivity(status == ActivityStatusType.COMPLETE.getV() ? 1 : 0);
//						activities.setStatus(Integer.parseInt(map.get("status").toString()));
//						activities.setUserActivityStatus(UserActivityStatus.RELEASE.getStatus());
//						activitiesList.add(activities);
//					}
//				}
//				if (page == 1) {
//					// 查询用户下关注的活动
//					List<ActivityFocus> activityFocusPage = activityFocusDao.getActivityFocus(pid, activitySubject);
//					if (activityFocusPage != null) {
//						for (ActivityFocus activityFocus : activityFocusPage) {
//							Activity activity = activityDao.get(Activity.class, activityFocus.getAid().getId());
//							Long fAid = activity.getId();
//							listFAid.add(fAid);
//							if (ActivityReviewType.DELETE.getType().toString().equals(activity.getCheckStatus()) ||
//									ActivityReviewType.DID_NOT_PASS.getType().toString().equals(activity.getCheckStatus()) ||
//									ActivityReviewType.DELETED.getType().equals(activity.getIsRemove())) {
//								continue;
//							}
//							FUserActivitiesBean.Activities activities = new FUserActivitiesBean().new Activities();
//							activities = setParams(activities, activity, fAid, uid, pid, page);
//							activities.setUserActivityStatus(UserActivityStatus.FOCUS.getStatus());
//							Boolean result = false;
//							for (int l = 0; l < listAid.size(); l++) {
//								if (listAid.get(l).equals(fAid) || fAid == listAid.get(l)) {
//									result = true;
//									break;
//								}
//							}
//							if (result) continue;
//							int num = 0;
//							for (int l = 0; l < listFAid.size(); l++) {
//								Long fLFAid = listFAid.get(l);
//								if (fLFAid.equals(fAid) || fAid == fLFAid) {
//									num++;
//									if (num == 2) {
//										result = true;
//										break;
//									}
//								}
//							}
//							if (result) continue;
//							activitiesList.add(activities);
//						}
//					}
//					// 查询用户下报名的活动
//					List<ActivityAttence> activityAttenceList = activityAttenceDao.getActivityAttence(activitySubject, pid);
//					if (activityAttenceList != null) {
//						for (ActivityAttence attence : activityAttenceList) {
//							Activity activity = activityDao.get(Activity.class, attence.getAid().getId());
//							Long aAid = activity.getId();
//							if (ActivityReviewType.DELETE.getType().toString().equals(activity.getCheckStatus()) ||
//									ActivityReviewType.DID_NOT_PASS.getType().toString().equals(activity.getCheckStatus()) ||
//									ActivityReviewType.DELETED.getType().equals(activity.getIsRemove())) {
//								continue;
//							}
//							FUserActivitiesBean.Activities activities = new FUserActivitiesBean().new Activities();
//							activities = setParams(activities, activity, aAid, uid, pid, page);
//							// 查询用户参与情况
//							Object object = activityAttenceDao.getAttenceStatusByAidAndUid(aAid, pid, activitySubject);
//							Integer attenceStatus = null;
//							if (object != null) {
//								attenceStatus = Integer.parseInt(object.toString());
//							}
//							// 用户对活动的状态的添加
//							if (attenceStatus != null) {
//								if (attenceStatus == ActivityAttenceStatus.SIGNUP.getStatus()) {
//									// 报名
//									activities.setUserActivityStatus(UserActivityStatus.JOIN.getStatus());
//								} else if (attenceStatus == ActivityAttenceStatus.PARTICIPATE.getStatus()) {
//									if (activity.getEndTime() > System.currentTimeMillis() / 1000) {
//										// 入围
//										activities.setUserActivityStatus(UserActivityStatus.SHORTLISTED.getStatus());
//									} else {
//										// 参与
//										activities.setUserActivityStatus(UserActivityStatus.ATTENCE.getStatus());
//									}
//								}
//							} else {
//								// 状态出错
//								continue;
//							}
//							Boolean result = false;
//							for (int l = 0; l < listAid.size(); l++) {
//								Long fLAid = listAid.get(l);
//								if (fLAid.equals(aAid) || aAid == fLAid) {
//									result = true;
//									break;
//								}
//							}
//							if (result) continue;
//							activitiesList.add(activities);
//						}
//					}
//				}
//				fUserActivitiesBean.setActivities(activitiesList);
//				fUserActivitiesBean.setHasNext(activityPage.isHasNextPage());
//				rv.setObject(fUserActivitiesBean);
//				rv.setMeg("查询成功");
//			} else {
//				rv.setFlag(ReturnValue.FLAG_FAIL);
//				rv.setMeg("查询失败");
//			}
//		} catch (Exception ex) {
//			return SystemException.setResult(rv, ex, logger);
//		}
//		return rv;
//	}

	/**
	 * 查询用户发布的活动
	 * @param pid 权限id
	 * @param uid 用户id
	 * @param page 页码
	 * @param activitySubject 活动主题关键词
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 * @Date 2017/9/9
	 */
	@Transactional
	public ReturnValue<FUserActivitiesBean> getUserActivities(Long pid, Long uid, Integer page, String activitySubject) {
		ReturnValue<FUserActivitiesBean> rv = new ReturnValue<>();
		FUserActivitiesBean fUserActivitiesBean = new FUserActivitiesBean();
		List<FUserActivitiesBean.Activities> activitiesList = new ArrayList<>();
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		rv.setMeg(me.getValue(ResultMsgConstant.querySuccess));
		try {
			// 查询用户下发布的活动
			Query activityPrivilege = activityPrivilegeDao.getPrivilegeByPid(pid, activitySubject);
			if (activityPrivilege != null) {
				Page<Map<String, Object>> activityPage = activityDao.getUserActivitie(pid, page, activitySubject);
				List<Map<String, Object>> mapList = activityPage.getPageContent();
				if (mapList != null) {
					for (Map<String, Object> map : mapList) {
						FUserActivitiesBean.Activities activities = new FUserActivitiesBean().new Activities();
						Long aid = Long.parseLong(map.get("aid").toString());
						activities.setAid(Long.parseLong(aid.toString()));
						activities.setIndexPic(map.get("indexPic").toString());
						activities.setUid(uid);
						activities.setSubject(map.get("subject").toString());
						Integer focusNum = activityFocusDao.getActivityFocusNumber(aid);
						Integer attenceParticipateNum = activityAttenceDao.getActivityAttenceNumber(aid, page);
						Integer attenceNum = activityAttenceDao.getActivityAttenceNumber(aid, null);
						activities.setFocusNum(((focusNum + (attenceNum - attenceParticipateNum))));
						activities.setAttenceNum(attenceParticipateNum);
						// 未读消息数
						Integer infoNum = activityCommentReadDao.getInfoNum(pid, aid);
						Integer activityPicTextNum = activityCommentDao.getCommentNumber(aid, pid);
						activities.setInfoNum(activityPicTextNum - infoNum);
						Integer status = Integer.parseInt(map.get("activityStatus").toString());
						activities.setGroupOrActivity(status == ActivityStatusType.COMPLETE.getV() ? 1 : 0);
						activities.setStatus(status);
						activities.setUserActivityStatus(Integer.parseInt(map.get("status").toString()));
						activitiesList.add(activities);
					}
				}
				fUserActivitiesBean.setActivities(activitiesList);
				fUserActivitiesBean.setHasNext(activityPage.isHasNextPage());
				rv.setObject(fUserActivitiesBean);
				rv.setMeg(me.getValue(ResultMsgConstant.querySuccess));
			} else {
				rv.setFlag(ReturnValue.FLAG_FAIL);
				rv.setMeg(me.getValue(ResultMsgConstant.queryFail));
			}
		} catch (Exception ex) {
			return SystemException.setResult(rv, ex, logger);
		}
		return rv;
	}

	/**
	 * 添加相关参数值
	 * @param activities 返回的数据结果集
	 * @param activity 活动信息
	 * @param aid 活动id
	 * @param uid 用户id
	 * @param pid 用户权限id
	 * @param page 页码
	 * @return cn.com.ql.wiseBeijing.user.frontBean.FUserActivitiesBean.Activities
	 * @author liumengwei
	 * @Date 2017/9/24
	 */
	private FUserActivitiesBean.Activities setParams(
			FUserActivitiesBean.Activities activities, Activity activity, Long aid, Long uid, Long pid, Integer page) {
		activities.setAid(Long.parseLong(aid.toString()));
		activities.setIndexPic(activity.getIndexPic());
		activities.setUid(uid);
		activities.setSubject(activity.getSubject());
		Integer focusNum = activityFocusDao.getActivityFocusNumber(aid);
		Integer attenceParticipateNum = activityAttenceDao.getActivityAttenceNumber(aid, page);
		Integer attenceNum = activityAttenceDao.getActivityAttenceNumber(aid, null);
		activities.setFocusNum((focusNum + (attenceNum - attenceParticipateNum)));
		activities.setAttenceNum(attenceParticipateNum);
		// 未读消息数
		Integer infoNum = activityCommentReadDao.getInfoNum(pid, aid);
		Integer activityPicTextNum = activityCommentDao.getCommentNumber(aid, pid);
		activities.setInfoNum(activityPicTextNum - infoNum);
		activities.setStatus(activity.getStatus());
		Integer status = activity.getStatus();
		activities.setGroupOrActivity(status == ActivityStatusType.COMPLETE.getV() ? 1 : 0);
		return activities;
	}

	/**
	 * 实名认证
	 * @param info 用户相关认证信息
	 * @param uid 用户id
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 */
	@Transactional
	public ReturnValue verified(FNewUserBean info, String uid) {
		ReturnValue re = new ReturnValue();
		try {
			int result = ud.updateVerified(info, uid);
			re.setFlag(result > 0 ? ReturnValue.FLAG_SUCCESS : ReturnValue.FLAG_FAIL);
			re.setMeg(result > 0 ? me.getValue(ResultMsgConstant.modifySuccess): me.getValue(ResultMsgConstant.modifyFail));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return re;
	}

	/**
	 * 修改密码
	 * @param password 密码
	 * @param phone 手机号
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 * @author liumengwei
	 */
	@Transactional
	public ReturnValue changePass(String password, String phone) {
		ReturnValue re = new ReturnValue();
		try {
			int result = ud.updatePassword(EncryptionByMD5.getMD5(password.getBytes()), phone);
			re.setFlag(result > 0 ? ReturnValue.FLAG_SUCCESS : ReturnValue.FLAG_FAIL);
			re.setMeg(result > 0 ? me.getValue(ResultMsgConstant.passModifySuccess) : me.getValue(ResultMsgConstant.passModifyFail));
		} catch (Exception ex) {
			return SystemException.setResult(re, ex, logger);
		}
		return re;
	}

	@Transactional
	public ReturnValue<FUserBean> resetPwd(FUserBean fuser) {
		ReturnValue<FUserBean> re = new ReturnValue<FUserBean>();
		User u = ud.getUserByPhone(fuser.getPhone());
		if (u == null) {
			re.setFlag(ReturnValue.FLAG_EXCEPTION);
			re.setMeg("phone error");
		} else {
			ud.updatePwd(u.getId(),
					EncryptionByMD5.getMD5(fuser.getPassword().getBytes()));
			re.setFlag(ReturnValue.FLAG_SUCCESS);
			re.setMeg("ok");
		}
		re.setObject(fuser);
		return re;

	}

	/**
	 *
	 * @param fuser
	 * @param check_code
	 *            原始密码
	 * @return
	 */
	@Transactional
	public ReturnValue<FUserBean> modifyPwd(FUserBean fuser, String check_code) {
		ReturnValue<FUserBean> re = new ReturnValue<FUserBean>();
		User u = ud.get(User.class, Integer.parseInt(fuser.getId()));

		if (u == null) {
			re.setFlag(ReturnValue.FLAG_EXCEPTION);
			re.setMeg("uid error");
		} else {
			if (!u.getPassword().equals(
					EncryptionByMD5.getMD5(check_code.getBytes()))) {
				re.setFlag(ReturnValue.FLAG_EXCEPTION);
				re.setMeg("origin password error");
			} else {
				ud.updatePwd(u.getId(),
						EncryptionByMD5.getMD5(fuser.getPassword().getBytes()));
				re.setFlag(ReturnValue.FLAG_SUCCESS);
				re.setMeg("ok");
			}
		}
		re.setObject(fuser);
		return re;

	}

	@Transactional
	public ReturnValue<FUserBean> modifyNickName(FUserBean fuser) {
		ReturnValue<FUserBean> re = new ReturnValue<FUserBean>();
		User u = ud.get(User.class, Integer.parseInt(fuser.getId()));

		if (u == null) {
			re.setFlag(ReturnValue.FLAG_EXCEPTION);
			re.setMeg("uid error");
		} else {
			ud.updateNickName(u.getId(), fuser.getNickname());
			re.setFlag(ReturnValue.FLAG_SUCCESS);
			re.setMeg("ok");
		}
		re.setObject(fuser);
		return re;

	}

	@Transactional
	public ReturnValue<FUserBean> modifyHeadPic(FUserBean fuser) {
		ReturnValue<FUserBean> re = new ReturnValue<FUserBean>();
		User u = ud.get(User.class, Integer.parseInt(fuser.getId()));

		if (u == null) {
			re.setFlag(ReturnValue.FLAG_EXCEPTION);
			re.setMeg("uid error");
		} else {
			ud.updateHeadpic(u.getId(), fuser.getAvatar());
			re.setFlag(ReturnValue.FLAG_SUCCESS);
			re.setMeg("ok");
		}
		re.setObject(fuser);
		return re;

	}

	/**
	 * 获取用户信息
	 * @param pid 用户权限id
	 * @return cn.com.ql.wiseBeijing.serviceUtil
	 */
	@Transactional
	public ReturnValue<FNewUserBean> getUser(Long pid) {
		ReturnValue<FNewUserBean> re = new ReturnValue<>();
		try {
			ActivityPrivilege privilege = activityPrivilegeDao.get(ActivityPrivilege.class, pid);
			String headPic = "";
			String mobile = "";
			String givename = "";
			Integer gender = null;
			if (ActivityPrivilegeStatus.PERSONAL.getStatus().toString().equals(privilege.getUserType())) {
				User u = ud.get(User.class, Integer.parseInt(privilege.getUid().toString()));
				headPic = u.getHeadpic();
				mobile = u.getMobile();
				givename = u.getGivename();
				gender = u.getGender();
			} else if(ActivityPrivilegeStatus.RHIRD_PARTY.getStatus().toString().equals(privilege.getUserType())) {
				UserBind u = ubd.get(UserBind.class, Integer.parseInt(privilege.getUid().toString()));
				headPic = u.getHeadpic();
				mobile = u.getMobile();
				givename = u.getGivename();
				gender = 2;
			}
			FNewUserBean fu = new FNewUserBean();
			if (fu == null)
				fu.setVerified(false);
			else
				fu.setVerified(true);
			fu.setHeadPic(StringUtil.isEmpty(headPic) ? "" : headPic);
			fu.setPhone(StringUtil.isEmpty(mobile) ? "" : mobile);
			fu.setGivename(StringUtil.isEmpty(givename) ? "" : givename);
			fu.setGender(gender);
			re.setFlag(ReturnValue.FLAG_SUCCESS);
			re.setObject(fu);
		} catch (Exception ex) {
			return SystemException.setResult(re, ex, logger);
		}
		return re;
	}

	@Transactional
	public ReturnValue<FUserBindBean> getBindUser(int uid) {
		ReturnValue<FUserBindBean> re = new ReturnValue<FUserBindBean>();
		UserBind u = ubd.get(UserBind.class, uid);
		if (u != null) {
			FUserBindBean fu = new FUserBindBean();
			fu.setAvatar(u.getHeadpic());
			fu.setNickname(u.getNickname());
			re.setFlag(ReturnValue.FLAG_SUCCESS);
			re.setObject(fu);
		} else {
			re.setFlag(ReturnValue.FLAG_EXCEPTION);
			re.setMeg("not exist this user");
		}
		return re;
	}

	@Transactional
	public ReturnValue<FUserBean> resetPwd(FUserBean fuser, String oldPassword) {
		ReturnValue<FUserBean> re = new ReturnValue<FUserBean>();
		User u = ud.getUserByPhone(fuser.getPhone());
		if (u == null) {
			re.setFlag(ReturnValue.FLAG_EXCEPTION);
			re.setMeg("phone error");
		} else {

			String password = u.getPassword();
			if (!password
					.equals(EncryptionByMD5.getMD5(oldPassword.getBytes()))) {
				re.setFlag(ReturnValue.FLAG_EXCEPTION);
				re.setMeg("old password error");
			} else {

				ud.updatePwd(u.getId(),
						EncryptionByMD5.getMD5(fuser.getPassword().getBytes()));
				re.setFlag(ReturnValue.FLAG_SUCCESS);
				re.setMeg("ok");
			}
		}
		re.setObject(fuser);
		return re;
	}

	/**
	 * 添加第三方登陆信息
	 * @param bean 接收相关参数
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 */
	@Transactional
	public ReturnValue<FUserBindBean> bindUser(FUserBindBean bean, FNewUserBean fNewUserBean) {
		ReturnValue<FUserBindBean> re = new ReturnValue<>();
		try {
			UserBind ub = new UserBind();
			ub.setCreatetime(DateUtil.getCurrentDateForSql());
			ub.setHeadpic(bean.getAvatar());
			ub.setNickname(bean.getNickname());
			ub.setPlatform(bean.getPlatform());
			ub.setPlatform_uid(bean.getPlatform_uid());
			UserBind ubsave = ubd.getByPlatform(ub);
			if (ubsave == null) {
				String gender = fNewUserBean.getStrGender();
				String username  = fNewUserBean.getUsername();
				String phone = fNewUserBean.getPhone();
				//  Determine the username length
				if (!ValidateMode.lengthMixChinese(username, 2, 24)) {
					re.setMeg(me.getValue(ResultMsgConstant.usernameLength));
					re.setFlag(Integer.parseInt(ResultStruct.ERROR));
					return re;
				}
				// Determine sex
				if (!"0".equals(gender) && !"1".equals(gender)) {
					re.setFlag(Integer.parseInt(ResultStruct.ERROR));
					re.setMeg(me.getValue(ResultMsgConstant.genderFormat));
					return re;
				}
				// Determine the phone number format and length
				re = StringUtil.phoneFormat(phone, re);
				if (re.getFlag() == ReturnValue.FLAG_FAIL) {
					return re;
				}
				Integer usernameSame = (ud.getUserName(username)) + (ubd.getUserName(username));
				if (!StringUtil.isEmpty(username)) {
					if (usernameSame <= 0) {
						ub.setUsername(username);
					} else {
						re.setMeg(me.getValue(ResultMsgConstant.usernameAlreadyExists));
						re.setFlag(ReturnValue.FLAG_FAIL);
						return re;
					}
				}

				ub.setMobile(phone);
				ub.setUsername(username);
				ub.setGender(Integer.parseInt(gender));
				String id = ubd.save(ub).toString();
				ActivityPrivilege privilege =
						activityPrivilegeDao.getId(
								Long.parseLong(id),
								ActivityPrivilegeStatus.RHIRD_PARTY.getStatus());
				bean.setVerified(false);
				bean.setPhone(phone);
				if (privilege != null) {
					bean.setVerified(ActivityPrivilegeStatus.CANNOTPUBLISHED.getStatus().toString()
							.equals(privilege.getPublishActivityPrivilege()) ? true: false);
				}
				bean.setId(id);
				bean.setNickname(username);
				bean.setFirstTime(1);
				re.setFlag(ReturnValue.FLAG_SUCCESS);
				re.setMeg("成功");
				re.setObject(bean);
			} else {
				ActivityPrivilege privilege =
						activityPrivilegeDao.getId(
								Long.parseLong(String.valueOf(ubsave.getId())),
								ActivityPrivilegeStatus.RHIRD_PARTY.getStatus());
				bean.setVerified(false);
				if (privilege != null) {
					bean.setVerified(ActivityPrivilegeStatus.CANNOTPUBLISHED.getStatus().toString()
							.equals(privilege.getPublishActivityPrivilege()) ? true: false);
				}
				bean.setNickname(ubsave.getUsername());
				bean.setId(String.valueOf(ubsave.getId()));
				bean.setFirstTime(0);
				bean.setPhone(ubsave.getMobile());
				re.setFlag(ReturnValue.FLAG_SUCCESS);
				re.setMeg("成功");
				re.setObject(bean);
			}
		} catch (Exception ex) {
			return SystemException.setResult(re, ex, logger);
		}
		return re;
	}

	/**
	 * 第三方解绑
	 * @param bean 接收相关参数
	 * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
	 */
	@Transactional
	public ReturnValue<FUserBindBean> unbindUser(FUserBindBean bean) {
		ReturnValue<FUserBindBean> re = new ReturnValue<>();
		try {
			UserBind ub = new UserBind();
			ub.setPlatform(bean.getPlatform());
			ub.setPlatform_uid(bean.getPlatform_uid());
			UserBind bind = ubd.updateStatus(ub, "1");
			if (bind == null) {
				re.setFlag(ReturnValue.FLAG_FAIL);
				re.setMeg("该账户并未绑定");
				return re;
			}
			bind.setPassword("");
			bean.setId(String.valueOf(bind.getId()));
			re.setFlag(ReturnValue.FLAG_SUCCESS);
			re.setMeg("解绑成功");
			re.setObject(bean);
		} catch (Exception ex) {
			return SystemException.setResult(re, ex, logger);
		}
		return re;
	}
	/**
	 *
	 * @param userid 带有平台信息
	 * @return
	 */
	@Transactional
	public ReturnValue<Map> getComment(String userid,int current)
	{
		ReturnValue<Map> rv=new ReturnValue<Map>();
		Page<Map> page=cd.getCommentByUser(userid, current);
		rv.setObject(PageMapUtil.getMap(page));
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		return rv;
	}
	/**
	 * userid 带有平台信息
	 * @param
	 * @return
	 */
	@Transactional
	public ReturnValue<Map> getLikes(String uid,int current)
	{
		ReturnValue<Map> rv=new ReturnValue<Map>();
		Page<Map> page=likesDao.getLikeByUser(uid, current);
		rv.setObject(PageMapUtil.getMap(page));
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		return rv;
	}
	/**
	 * userid 带有平台信息
	 * @param
	 * @return
	 */
	@Transactional
	public ReturnValue<Map> getCollect(String uid,int current)
	{
		ReturnValue<Map> rv=new ReturnValue<Map>();
		Page<Map> page=collectDao.getCollectByUser(uid, current);
		rv.setObject(PageMapUtil.getMap(page));
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		System.err.println();
		return rv;
	}
	@Transactional
	public ReturnValue<Map<String, Object>> getUserList(String nickname,String status,int current,int amount){
		ReturnValue<Map<String, Object>> rv = new ReturnValue<Map<String, Object>>();
		Page<Map<String,Object>> map =ud.getList(nickname, status, current, amount);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Map<String, Object> m:map.getPageContent()){
			m.put("createtime",sdf.format(m.get("createtime")));
			String platform=m.get("platform").toString();
			if(platform.contains("USER")){
				m.put("id", "00"+m.get("id"));
			}
			else if(platform.contains("WECHAT")){
				m.put("id", "01"+m.get("id"));
			}else if(platform.contains("WEIBO")){
				m.put("id", "02"+m.get("id"));
			}else if(platform.contains("QQ")){
				m.put("id", "03"+m.get("id"));
			}
		}
		rv.setObject(PageMapUtil.getMap(map));
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		return rv;
	}
	@Transactional
	public ReturnValue<String> updateStatus(String id,String status){
		ReturnValue<String> rv = new ReturnValue<String>();
		int ups= ud.updateStatus(id, status);
		rv.setFlag(ups>0?ReturnValue.FLAG_SUCCESS:ReturnValue.FLAG_FAIL);
		rv.setMeg(ups>0?"修改成功":"修改失败");
		return rv;
	}
	@Transactional
	public ReturnValue<Object> resetNickname(String id){
		ReturnValue<Object> rv = new ReturnValue<Object>();
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		rv.setMeg("重置成功");
		rv.setObject(ud.resetNickname(id));
		return rv;
	}
}

package cn.com.ql.wiseBeijing.user.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.serviceUtil.StringUtil;
import cn.com.ql.wiseBeijing.user.daoBean.User;
import cn.com.ql.wiseBeijing.user.daoBean.UserBind;
import cn.com.ql.wiseBeijing.user.frontBean.FNewUserBean;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDao extends BasicDao<User> {
	@Bean
	public UserDao userDao() {
		return new UserDao();
	}
	private final static Logger logger = LoggerFactory.getLogger(UserDao.class);
	@Resource(name = "userBindDao")
	private UserBindDao userBindDao;

	/**
	 * 根据平台信息取用户
	 * 
	 * @param userid
	 * @return
	 */
	public Map<String, Object> getUser(String userid) {
		String platform = userid.substring(0,2);
		String userid_r = userid.substring(2);
		Map<String, Object> map = new HashMap<String, Object>();
		int userid_real = Integer.parseInt(userid_r);
		if (platform.equals("00")) {
			User user = get(User.class, userid_real);
			if (user == null)
				return null;
			map.put("userid", user.getId());
			map.put("userName", user.getNickname());
			map.put("userHeadPic", user.getHeadpic());
			map.put("userType", platform);
			map.put("platform", platform);
		} else {
			UserBind ub = userBindDao.get(UserBind.class, userid_real);
			if (ub == null)
				return null;
			map.put("userid", ub.getId());
			map.put("platform", platform);
			map.put("userName", ub.getNickname());
			map.put("userHeadPic", ub.getHeadpic());
			map.put("userType", platform);
		}
		return map;
	}

	/**
	 * 根据用户名查询
	 * @param username 用户名
	 * @return cn.com.ql.wiseBeijing.user.daoBean.User
	 * @author liumengwei
	 * @Date 2017/8/5
	 */
	public User getUserByUserName(String username) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		return getUserByEq(map);
	}

	public User getUserByNickName(String nickname) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("nickname", nickname);
		return getUserByEq(map);
	}

	/**
	 * 查询手机号
	 * @param phone 手机号
	 * @return cn.com.ql.wiseBeijing.user.daoBean.User
	 */
	public User getUserByPhone(String phone) {
		Map<String, String> map = new HashMap<>();
		map.put("mobile", phone);
		//尚未冻结用户
		map.put("status", "1");
		return getUserByEq(map);
	}

	public User updatePwd(int userid, String pwd) {
		User user = (User) getSession().get(User.class, userid);
		if (user == null) {
			return null;
		}
		user.setPassword(pwd);
		getSession().update(user);
		return user;
	}

	public User updateNickName(int userid, String nickname) {
		User user = (User) getSession().get(User.class, userid);
		if (user == null) {
			return null;
		}
		user.setNickname(nickname);
		getSession().update(user);
		return user;
	}

	public User updateHeadpic(int userid, String headpic) {
		User user = (User) getSession().get(User.class, userid);
		if (user == null) {
			return null;
		}
		user.setHeadpic(headpic);
		getSession().update(user);
		return user;
	}

	/**
	 * 根据相关参数查询用户
	 * @param map 参数
	 * @return cn.com.ql.wiseBeijing.user.daoBean.User
	 */
	private User getUserByEq(Map map) {
		Criteria cri = getSession().createCriteria(User.class);
		cri.add(Restrictions.allEq(map));
		Object obj = cri.uniqueResult();
		if (obj == null)
			return null;
		return (User) obj;
	}

	/**
	 * 查询用户是否实名
	 * @param uid
	 * @return
	 */
	public List getVerified(Long uid) {
		Session session = getSession();
		String hql = "select " +
				"givename as givename, mobile as mobile, gender as gender, " +
				"IDNumber as IDNumber, headpic as headpic, type as type " +
				"from cn.com.ql.wiseBeijing.user.daoBean.User " +
				"where id = :id";
		List query = session.createQuery(hql)
				.setLong("id", uid)
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
				.list();
		for (int i = 0; i < query.size(); i++) {
			if (query.get(i) != null) {
				return query;
			}
		}
		return null;
	}

	/**
	 * 修改密码
	 * @param password 密码
	 * @param phone 手机号
	 * @return int
	 * @author liumengwei
	 */
	public int updatePassword(String password, String phone) {
		int update = -1;
		try {
			String hql = "update User set password = :password where mobile = :mobile where status = 1";
			Query newsQuery = getSession().createQuery(hql);
			newsQuery.setString("password", password);
			newsQuery.setString("mobile", phone);
			update = newsQuery.executeUpdate();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return update;
	}

	public int updateStatus(String id,String status)
	{
		String hql = "update User set status=:status where id=:id";
		int update=-1;
		String platform =id.substring(0,2);
		String ids = id.substring(2);
		if(platform.contains("00")){
			Query newsQuery = getSession().createQuery(hql);
			newsQuery.setString("id", ids);
			newsQuery.setString("status",status);
	   	    update =  newsQuery.executeUpdate();
		}else{
			String bHql = "update UserBind set status=:status where id=:id and platform=:platform";
			Query newsQuery1 = getSession().createQuery(bHql);
		   	newsQuery1.setString("id", ids);
		   	newsQuery1.setString("status",status);
		   	if(platform.contains("01")){
		   		newsQuery1.setString("platform", "WECHAT");
		   	}else if(platform.contains("02")){
		   		newsQuery1.setString("platform", "WEIBO");
		   	}else if(platform.contains("03")){
		   		newsQuery1.setString("platform", "QQ");
		   	}else{
		   		return -1;
		   	}
			update =  newsQuery1.executeUpdate();
		}
	    return update;
	}
	public Object resetNickname(String id){
		Object temp = initUser(id);
		if(temp==null) return null;
		if(temp instanceof User){
			User user = (User) temp;
			user.setNickname(StringUtil.getRandomString(8));
			getSession().update(user);
			return user;
		}else{
			UserBind user = (UserBind) temp;
			user.setNickname(StringUtil.getRandomString(8));
			getSession().update(user);
			return user;
		}
		
	}
	public Page<Map<String,Object>> getList(String nickname,String status,int current, int amount){
		if(status==null){
			status="1=1";
		}else{
			status = "status = "+status;
		}
		if (nickname == null) {
			nickname = "and 1=1";
		} else {
			nickname = "and (id like '%" + (nickname.length()<=2?nickname:nickname.substring(2)) + "%' or nickname like '%"
					+ nickname + "%') ";
		}
		final Session session = getSession();
		String userHql = "select id,username,mobile,email,nickname,createtime ,'USER' as platform,status from User where "+status+" "+nickname+" union all select id,username,mobile,email,nickname,createtime, platform,status from User_Bind where "+status+" "+nickname+" order by createtime desc ";
		Query query2  = session.createSQLQuery(userHql).setResultTransformer(
			     Transformers.ALIAS_TO_ENTITY_MAP);
		PageWaterfallAdapter adapter =new PageWaterfallAdapter(query2);
		Page<Map<String,Object>> page= adapter.execute(current, amount, new ResultTransfer<Map<String,Object>>() {

			@Override
			public List<Map<String,Object>> transfer(List list) {
				// TODO Auto-generated method stub
				return list;
			}
		});
		return page;
	}
	private Object initUser(String id){
		String platform =id.substring(0,2);
		try {
			Integer ids = Integer.parseInt(id.substring(2));
			if(platform.contains("00")){
				return getSession().get(User.class, ids);
		   	}else if(platform.contains("01")){
				return getSession().get(UserBind.class, ids);
		   	}else if(platform.contains("02")){
				return getSession().get(UserBind.class, ids);
		   	}else if(platform.contains("03")){
				return getSession().get(UserBind.class, ids);
		   	}
		} catch (Exception e) {
			return null;

		}
		return null;
	}

	/**
	 * 用户实名，更新用户信息
	 * @param u 接受认证参数
	 * @param uid 用户id
	 * @return int
	 * @author liumengwei
	 */
	public int updateVerified(FNewUserBean u, String uid) {
		String hql = "update User set givename = :givename, IDNumber = :IDNumber, type = :type, headPic = :headPic " +
				"where id = :id and status = 1";
		int updateResult = -1;
		try {
			String platform = uid.substring(0, 2);
			String ids = uid.substring(2);
			if (platform.contains("00")) {
				Query newsQuery = getSession().createQuery(hql);
				newsQuery.setString("givename", u.getGivename());
				newsQuery.setString("IDNumber", u.getIdNumber());
				newsQuery.setString("headPic", u.getHeadPic());
				newsQuery.setInteger("type", u.getType());
				newsQuery.setInteger("id", Integer.parseInt(ids));

				updateResult = newsQuery.executeUpdate();
			} else {
				String bHql =
						"update UserBind set givename = :givename, IDNumber = :IDNumber, type = :type " +
								"where id = :id ";
				Query newsQuery1 = getSession().createQuery(bHql);
				newsQuery1.setString("givename", u.getGivename());
				newsQuery1.setString("IDNumber", u.getIdNumber());
				newsQuery1.setInteger("type", u.getType());
				newsQuery1.setInteger("id", Integer.parseInt(ids));
				updateResult = newsQuery1.executeUpdate();
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return updateResult;
	}

	/**
	 * 用户实名，更新用户信息
	 * @param username 用户名
	 * @return java.lang.Boolean
	 * @author liumengwei
	 * @date 2017/12/15
	 */
	public Integer getUserName(String username) {
		Object result = 0;
		try {
			String hql = "select count(*) from user where username = :username";
			result = getSession().createSQLQuery(hql)
			.setString("username", username)
			.uniqueResult();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return Integer.parseInt(result.toString());
	}
}

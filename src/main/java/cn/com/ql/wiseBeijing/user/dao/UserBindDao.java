package cn.com.ql.wiseBeijing.user.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.user.daoBean.UserBind;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserBindDao extends BasicDao<UserBind>{
	@Bean
	public UserBindDao userBindDao()
	{
		return new UserBindDao();
	}
	private final static Logger logger = LoggerFactory.getLogger(UserBindDao.class);
	
	public UserBind getByPlatform(UserBind bind)
	{
		Map<String,String> map=new HashMap<String,String>();
		map.put("platform", bind.getPlatform());
		map.put("platform_uid", bind.getPlatform_uid());
		return getUserBindByEq(map);
	}
	
	public UserBind updateStatus(UserBind bind,String status)
	{
		UserBind ub=getByPlatform(bind);
		if(ub==null)
		{
			return null;
		}
		Session session=getSession();
		ub.setStatus(status);
		session.update(ub);
		return ub;
	}
	
	private UserBind getUserBindByEq(Map map)
	{
		Criteria  cri=getSession().createCriteria(UserBind.class);
		cri.add(Restrictions.allEq(map));
		Object obj=cri.uniqueResult();
		if(obj==null)
			return null;
		return (UserBind)obj;
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
			String hql = "select count(*) from user_bind where  username = :username";
			result = getSession().createSQLQuery(hql)
					.setString("username", username)
					.uniqueResult();

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return Integer.parseInt(result.toString());
	}

	/**
	 * 查询手机号是否存在
	 * @param phone 手机号
	 * @return java.lang.Boolean
	 * @author liumengwei
	 * @date 2017/12/15
	 */
	public UserBind getByPhone(String phone) {
		Object result = 0;
		try {
			String hql = "from UserBind where mobile = :phone";
			result = getSession().createQuery(hql)
					.setString("phone", phone)
					.uniqueResult();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return (UserBind) result;
	}
}

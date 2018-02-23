package cn.com.ql.wiseBeijing.session.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.com.ql.wiseBeijing.session.dao.MobileSessionDao;
import cn.com.ql.wiseBeijing.session.daoBean.MobileSession;
import cn.xxtui.support.util.DateUtil;

@Component
public class MobileSessionService {
	@Resource(name = "mobileSessionDao")
	private MobileSessionDao mobileSessionDao;

	@Bean
	public MobileSessionService mobileSessionService() {
		return new MobileSessionService();
	}

	/**
	 * 
	 * @param sessionid
	 *            : 通过session拿到
	 * @param uid
	 *            ：包括平台信息00,01,02,03
	 * @return
	 */
	@Transactional
	public ReturnValue<Map> saveSession(String rsid, String uid, String uname) {
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Map<String, String> map = new HashMap<String, String>();
		rv.setObject(map);
		MobileSession ms = new MobileSession();
		ms.setCreateTime(DateUtil.getCurrentDateForSql());
		ms.setLogoutTime(DateUtil.getCurrentDateForSql());
		ms.setSessionid(rsid);
		ms.setUid(uid);
		ms.setUname(uname);
		ms.setStatus("0");
		try {
			String id = mobileSessionDao.save(ms).toString();
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
			map.put("rsid", id);
			map.put("uid", uid);
		} catch (Exception ex) {
			rv.setFlag(ReturnValue.FLAG_FAIL);
			rv.setMeg(ex.getMessage() + "logon register failed");
		}
		return rv;
	}

	@Transactional
	public ReturnValue<Map> getSession(String rsid) {
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Map<String, String> map = new HashMap<String, String>();
		rv.setObject(map);
		if(rsid==null)
		{
			rv.setFlag(ReturnValue.FLAG_FAIL);
			rv.setMeg("logon infomation don't exist");
			return rv;
		}
		MobileSession session = mobileSessionDao.get(MobileSession.class, rsid);
		if (session == null) {
			rv.setFlag(ReturnValue.FLAG_FAIL);
			rv.setMeg("logon infomation don't exist");
		} else {
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
			map.put("rsid", session.getSessionid());
			map.put("uid", session.getUid());
			map.put("uname", session.getUname());
			map.put("createTime", session.getCreateTime());
			map.put("status", session.getStatus());
		}
		return rv;
	}

	@Transactional
	public ReturnValue<Map> logoutSession(String rsid)
	{
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Map<String, String> map = new HashMap<String, String>();
		rv.setObject(map);
		MobileSession session = mobileSessionDao.get(MobileSession.class, rsid);
		
		if (session == null) {
			rv.setFlag(ReturnValue.FLAG_FAIL);
			rv.setMeg("logon infomation don't exist");
		} else {
			session.setStatus("1");
			mobileSessionDao.getSession().update(session);
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
			map.put("rsid", session.getSessionid());
			map.put("uid", session.getUid());
			map.put("uname", session.getUname());
			map.put("createTime", session.getCreateTime());
			map.put("status", session.getStatus());
		}
		return rv;
	}
	@Transactional
	public ReturnValue<Map> updateLastTime(String rsid)
	{
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Map<String, String> map = new HashMap<String, String>();
		rv.setObject(map);
		MobileSession session = mobileSessionDao.get(MobileSession.class, rsid);
		
		if (session == null) {
			rv.setFlag(ReturnValue.FLAG_FAIL);
			rv.setMeg("logon infomation don't exist");
		} else {
			session.setStatus(DateUtil.getCurrentDateForSql());
			mobileSessionDao.getSession().update(session);
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
			map.put("rsid", session.getSessionid());
			map.put("uid", session.getUid());
			map.put("uname", session.getUname());
			map.put("createTime", session.getCreateTime());
			map.put("status", session.getStatus());
		}
		return rv;
	}
}

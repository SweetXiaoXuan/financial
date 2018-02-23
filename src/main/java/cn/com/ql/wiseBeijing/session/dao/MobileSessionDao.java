package cn.com.ql.wiseBeijing.session.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.session.daoBean.MobileSession;
@Component
public class MobileSessionDao extends BasicDao<MobileSession> {
	@Bean
	public MobileSessionDao mobileSessionDao()
	{
		return new MobileSessionDao();
	}
	
}

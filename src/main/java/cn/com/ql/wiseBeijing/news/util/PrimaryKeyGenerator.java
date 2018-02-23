package cn.com.ql.wiseBeijing.news.util;

import java.io.Serializable;
import java.util.Random;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import cn.xxtui.support.util.DateUtil;

public class PrimaryKeyGenerator implements IdentifierGenerator {

	public static final String NEWS_PRIMARY_KEY="cn.com.ql.wiseBeijing.news.util.PrimaryKeyGenerator";
	@Override
	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException {
		//MainNews mainNews=(MainNews)object;
		String str=DateUtil.getCurrentDateForKey()+new Random().nextInt(1000);
		return str;
	}
}

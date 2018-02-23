package cn.com.ql.wiseBeijing.dec.util;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import cn.com.ql.wiseBeijing.dec.daoBean.DecMainNews;
import cn.xxtui.support.util.DateUtil;

public class PrimaryKeyGenerator implements IdentifierGenerator {

	public static final String NEWS_PRIMARY_KEY="cn.com.ql.wiseBeijing.dec.util.PrimaryKeyGenerator";
	@Override
	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException {
		DecMainNews mainNews=(DecMainNews)object;
		String str=DateUtil.getCurrentDateForKey();
		return str;
	}

}

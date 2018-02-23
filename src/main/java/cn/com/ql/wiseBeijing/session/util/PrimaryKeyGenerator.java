package cn.com.ql.wiseBeijing.session.util;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import cn.com.ql.wiseBeijing.session.daoBean.MobileSession;

public class PrimaryKeyGenerator implements IdentifierGenerator {

	public static final String SESSION_PRIMARY_KEY="cn.com.ql.wiseBeijing.session.util.PrimaryKeyGenerator";
	@Override
	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException {
		MobileSession s=(MobileSession)object;
		String str=s.getSessionid();
		return str;
	}

}

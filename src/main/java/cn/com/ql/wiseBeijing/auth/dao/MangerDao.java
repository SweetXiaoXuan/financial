package cn.com.ql.wiseBeijing.auth.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.auth.daoBean.Manger;
import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;

@Component
public class MangerDao extends BasicDao<Manger> {

	public int addManger(Manger manger) {
		return (Integer) getSession().save(manger);
	}
	@SuppressWarnings("unchecked")
	public Page<Map> getList() {
		Query query=getSession().createQuery("from Manger ");
		PageWaterfallAdapter adapter = new PageWaterfallAdapter(query);
		Page<Map> page = adapter.execute(0, 100, new ResultTransfer<Map>() {

			@Override
			public List<Map> transfer(List list) {
				Iterator<Manger> it=list.iterator();
				List<Map> l=new ArrayList<Map>();
				while(it.hasNext())
				{
					Manger m=it.next();
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("manager", m);
					List lt=getUserAuthority(m.getId()).getPageContent();
					if(lt!=null&&!lt.isEmpty())
						map.put("authority", lt.get(0));
					else
						map.put("authority", "[]");
					l.add(map);
				}
				return l;
			}
		});
		return page;
	}

	public Page<Map> getUserAuthority(int uid) {
		Session session = getSession();
		String sql = "select r.*,a.auth_name from rel_u_auth as r left outer join authority as a on a.id=r.auth_id where r.user_id=:uid";
		Query query = session
				.createSQLQuery(sql)
				.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
				.setInteger("uid", uid);
		PageWaterfallAdapter adapter = new PageWaterfallAdapter(query);
		Page<Map> page = adapter.execute(0, 100, new ResultTransfer<Map>() {

			@Override
			public List<Map> transfer(List list) {

				return list;
			}
		});
		return page;
	}

	public boolean isRename(String username) {
		Query query = getSession().createQuery(
				"from Manger where username=:username");
		query.setString("username", username);
		return (query.list() == null || query.list().size() == 0) ? false
				: true;
	}
	public Manger login(String username, String pwd) {
		Query query = getSession().createQuery(
				"from Manger where username=:username and password =:password and status = 0");
		query.setString("username", username);
		query.setString("password", pwd);
		return ((query.list() == null || query.list().size() == 0) ? null
				: (Manger) query.list().get(0));
	}
	public int delete(int mid) {
		String hql = "update Manger set status = 1  where id = :mid";
	 	Query query= getSession().createSQLQuery(hql);
	 	query.setInteger("mid", mid);
	 	return query.executeUpdate();
	}
}

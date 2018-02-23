package cn.lv.jewelry.product.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.ActivityContentType;
import cn.lv.jewelry.product.daoBean.ProductActivity;
import cn.lv.jewelry.product.daoBean.ProductCategory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductActivityDao extends BasicDao<ProductActivity>{
	@Bean
	public ProductActivityDao productActivityDao(){
		return new ProductActivityDao();
	}

	public List<ProductActivity> getProductRelativeActivity(long pid) {
		Session session=getSession();
		String hql="from cn.lv.jewelry.product.daoBean.ProductActivity where pid=:pid";
		Query query=session.createQuery(hql);
		query.setLong("pid", pid);
		return query.list();
	}

	public List<ProductActivity> getActivityRelativeProduct(long aid) {
		Session session=getSession();
		String hql="from cn.lv.jewelry.product.daoBean.ProductActivity where aid=:aid";
		Query query=session.createQuery(hql);
		query.setLong("aid", aid);
		return query.list();
	}
}

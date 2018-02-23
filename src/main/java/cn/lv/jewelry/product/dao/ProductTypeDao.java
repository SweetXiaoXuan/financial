package cn.lv.jewelry.product.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.product.daoBean.ProductType;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductTypeDao extends BasicDao<ProductType>{
	@Bean
	public ProductTypeDao productTypeDao(){
		return new ProductTypeDao();
	}

	public List<ProductType> getProductType() {
		Session session=getSession();
		String hql="from cn.lv.jewelry.product.daoBean.ProductType";
		Query query=session.createQuery(hql);
		return query.list();
	}
}

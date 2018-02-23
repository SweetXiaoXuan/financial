package cn.lv.jewelry.product.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.product.daoBean.ProductOrigin;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductOriginDao extends BasicDao<ProductOrigin>{
	@Bean
	public ProductOriginDao productOriginDao(){
		return new ProductOriginDao();
	}

	public List<ProductOrigin> getOrigins(int pageNum) {
		Session session = getSession();
		String hql = "from cn.lv.jewelry.product.daoBean.ProductOrigin";
		Query query = session.createQuery(hql);
		PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
		Page<ProductOrigin> pageBean = pageWaterfallAdapter.execute(pageNum, 50, new ResultTransfer<ProductOrigin>() {
			@Override
			public List<ProductOrigin> transfer(List list) {
				return list;
			}
		});
		return pageBean.getPageContent();
	}
}

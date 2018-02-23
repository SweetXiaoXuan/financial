package cn.lv.jewelry.product.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.product.daoBean.ProductStyle;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductStyleDao extends BasicDao<ProductStyle>{
	@Bean
	public ProductStyleDao productStyleDao(){
		return new ProductStyleDao();
	}

	public List<ProductStyle> getStyles(int pageNum) {

		Session session = getSession();
		String hql = "from cn.lv.jewelry.product.daoBean.ProductStyle";
		Query query = session.createQuery(hql);
		PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
		Page<ProductStyle> pageBean = pageWaterfallAdapter.execute(pageNum, 50, new ResultTransfer<ProductStyle>() {
			@Override
			public List<ProductStyle> transfer(List list) {
				return list;
			}
		});
		return pageBean.getPageContent();
	}
}

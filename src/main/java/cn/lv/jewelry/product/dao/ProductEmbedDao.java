package cn.lv.jewelry.product.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.product.daoBean.ProductEmbed;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductEmbedDao extends BasicDao<ProductEmbed>{
	@Bean
	public ProductEmbedDao productEmbedDao(){
		return new ProductEmbedDao();
	}

	public List<ProductEmbed> getEmbeds(int pageNum) {

		Session session = getSession();
		String hql = "from cn.lv.jewelry.product.daoBean.ProductEmbed";
		Query query = session.createQuery(hql);
		PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
		Page<ProductEmbed> pageBean = pageWaterfallAdapter.execute(pageNum, 50, new ResultTransfer<ProductEmbed>() {
			@Override
			public List<ProductEmbed> transfer(List list) {
				return list;
			}
		});
		return pageBean.getPageContent();
	}
}

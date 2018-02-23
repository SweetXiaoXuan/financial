package cn.lv.jewelry.brand.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.brand.daoBean.Brand;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BrandDao extends BasicDao<Brand>{
	@Bean
	public BrandDao brandDao(){
		return new BrandDao();
	}

    // TODO 以下方法是之前的，用到了brand表，2018-2-9号修改表设计需注释掉该代码
//    public List<Brand> getBrand(int brandType, int brandPage) {
//        Session session = getSession();
//        String hql = "from cn.lv.jewelry.brand.daoBean.Brand b where b.type=:type";
//        Query query = session.createQuery(hql);
//        query.setInteger("type",brandType);
//        PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
//        Page<Brand> pageBean = pageWaterfallAdapter.execute(brandPage, 10, new ResultTransfer<Brand>() {
//            @Override
//            public List<Brand> transfer(List list) {
//                return list;
//            }
//        });
//        return pageBean.getPageContent();
//    }
}

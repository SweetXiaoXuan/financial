package cn.xxtui.support.page;

import java.util.List;

import org.hibernate.Query;

public  class PageWaterfallAdapter extends PageBase{
	
	private Query contentQuery;
	/**
	 * 
	 * @param clazz
	 * @param current：当前页面
	 * @param requst_count：请求页面容量
	 * @return
	 */
	
	private class BasicPage<T> extends Page<T>
	{
		
	}
	
	public <T> Page<T> execute(int current,int request_count,ResultTransfer<T> transfer)
	{
		Page<T> page=new BasicPage<T>();
		if(request_count<=0)
		{
			throw new RuntimeException("分页中请求页面容量不能小于0");
		} 
		try
		{
			int temp=request_count;
			request_count++;//每次多读一条，为了就是判断还有没有下一页
			page.setCount(request_count);
			page.setCurrent(current);
			int rowNumber=(current-1)*temp;
			contentQuery.setFirstResult(rowNumber);
			contentQuery.setMaxResults(request_count);
			List list=contentQuery.list();
			list=transfer.transfer(list);
			//TODO 如果结果集中没有内容，则探索最大的页数是多少，定位到那个位置
			if(list.size()<request_count)
			{
				page.setHasNextPage(false);
			}
			else
			{
				list.remove(request_count-1);//如果是正常多读一条则把最后条去掉，否则不要去掉
			}
			page.setList(list);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		PageWrap<T> newPage=new PageWrap<T>();
		newPage.setCount(page.getCount());
		newPage.setCurrent(page.getCurrent());
		newPage.setHasNextPage(page.isHasNextPage());
		newPage.setList(page.getPageContent());
		return newPage;
	}

	public PageWaterfallAdapter(Query contentQuey)
	{
		this.contentQuery=contentQuey;
	}
	public PageWaterfallAdapter()
	{
	}

	public static  <T> Page<T> pagination(Query query, Page<T> pageMap, Integer page, Integer count) {
		PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
		pageMap =
				pageWaterfallAdapter.execute(
						page, count, new ResultTransfer<T>() {
							@Override
							public List<T> transfer(List list) {
								return list;
							}
						}
				);
		return pageMap;
	}
	
}

package cn.xxtui.support.page;

import java.util.List;

//因为PageWaterfallAdapter会有一个Query存在，导致出错，我们需要包装成一个纯粹的Page
public class PageWrap<T> extends Page<T> {
	private int count;// 每页数据
	private int current;// 当前页
	private List<T> list;// 结果
	protected boolean hasNextPage = true;

	public PageWrap(){}
	public PageWrap(Page page){
		hasNextPage = page.isHasNextPage();
		list = page.getPageContent();
		count = page.getCount();
		current = page.getCurrent();
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		super.setList(list);
		this.list = list;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	
}

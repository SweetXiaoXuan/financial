package cn.xxtui.support.page;

import java.util.List;

/**
 * 进行分页
 * @author StarLee
 *
 */
public  abstract class Page<T> {
	private int count;//每页数据
	
	private int current;//当前页
	
	private List<T> list;//结果
	
	/**
	 * 说明分页是否还有下一次分页
	 */
	protected boolean hasNextPage=true;
	public boolean isHasNextPage() {
		return hasNextPage;
	}
	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	public int getCount() {
		return count;
	}
	protected void setList(List<T> list) {
		this.list = list;
	}
	protected void setCount(int count) {
		this.count = count;
	}
	public int getCurrent() {
		return current;
	}
	protected void setCurrent(int current) {
		this.current = current;
	}
	/**
	 * 如果没有内容，则返回null
	 * @return
	 */
	public List<T> getPageContent()
	{
		return this.list;
	}
}

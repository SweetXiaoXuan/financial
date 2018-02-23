package cn.xxtui.support.page;


public abstract class PageBase {
	public  abstract <T> Page<T> execute(int current,int request_count,ResultTransfer<T> transfer);
	public long totalPages(long records,int request_count)
	{
		long pages=((records-1)/request_count)+1;
		return pages;
	}
}

package cn.com.ql.wiseBeijing.serviceUtil;

public class ReturnValue<T>
{
	/**
	 * 0成功
	 * 1失败
	 * 2异常
	 */
	public final static int FLAG_SUCCESS=0;
	public final static int FLAG_FAIL=1;
	public final static int FLAG_EXCEPTION=2;
	private int flag;
	private String meg;
	private T object;
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getMeg() {
		return meg;
	}
	public void setMeg(String meg) {
		this.meg = meg;
	}
	public T getObject() {
		return object;
	}
	public void setObject(T object) {
		this.object = object;
	}
	
}

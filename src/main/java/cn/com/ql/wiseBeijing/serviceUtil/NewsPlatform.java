package cn.com.ql.wiseBeijing.serviceUtil;

public enum NewsPlatform {
	NEWS("news"),DEC("dec"),POLICY("policy");
	private String name;
	private NewsPlatform(String name)
	{
		this.name=name;
	}
	public String getName()
	{
		return this.name;
	}
}

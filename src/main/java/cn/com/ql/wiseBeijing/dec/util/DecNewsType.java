package cn.com.ql.wiseBeijing.dec.util;

public enum DecNewsType {
	REGULAR("0"),GROUP_IMAGES("1"),AUDIO("2"),TOPIC("3"),WEB("4");
	private String code;
	private DecNewsType(String str)
	{
		this.code=str;
	}
	public String get()
	{
		return this.code;
	}
	public static DecNewsType get(String str)
	{
		if("0".equals(str))
		{
			return REGULAR;
		}
		else if("1".equals(str))
		{
			return GROUP_IMAGES;
		}
		else if("2".equals(str))
		{
			return AUDIO;
		}
		else if("3".equals(str))
		{
			return TOPIC;
		}
		else if("4".equals(str))
		{
			return WEB;
		}
		else return REGULAR;
	}

}

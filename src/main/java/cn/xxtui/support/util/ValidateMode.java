package cn.xxtui.support.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateMode {
	/**
	 * 是否满足邮箱格式
	 * 
	 * @param inputStr
	 * @return
	 */
	public static boolean email(String inputStr) {
		// String regex =
		// "^([a-z0-9A-Z]+)([-|\\.|_]?[a-z0-9A-Z]+)*@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";//
		// 支持匹配star.lee@gmail.com
		String regex = "^[0-9a-zA-Z_][_.0-9a-zA-Z-]{0,31}@([0-9a-zA-Z][0-9a-zA-Z-]{0,30}\\.){1,4}[a-z]{2,4}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(inputStr);
		boolean result = matcher.matches();
		return result;
	}

	/**
	 * 只能为数字
	 * @param inputStr
	 * @return
	 */
	public static boolean digital(String inputStr) {
		// String regex =
		// "^([a-z0-9A-Z]+)([-|\\.|_]?[a-z0-9A-Z]+)*@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";//
		// 支持匹配star.lee@gmail.com
		if(inputStr==null)
			return false;
		String regex = "^[0-9]*$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(inputStr);
		boolean result = matcher.matches();
		return result;
	}

	/**
	 * Double格式校验
	 * @param inputStr
	 * @return
	 */
	public static boolean digitalDouble(String inputStr) {
		if(inputStr==null)
			return false;
		String regex = "^\\d{1,9}(\\.\\d{1,2})?$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(inputStr);
		boolean result = matcher.matches();
		return result;
	}
	
	/**
	 * 长度是否满足要求
	 * 
	 * @param inputStr
	 * @param length
	 * @param maxLen
	 *            为最大长度，0表示不作限制
	 * @return
	 */
	public static boolean length(String inputStr, int minLen, int maxLen) {
		if(inputStr==null)
			return false;
		// String regex = "^[a-z0-9A-Z]{6,}$";
		StringBuilder builder = new StringBuilder();
		builder.append("^[\\w~!@#$%^&*()_+{}:\"?\\-=\\[\\];\\',.\\/]{");
		builder.append(minLen);
		builder.append(",");
		if (maxLen != 0)
			builder.append(maxLen);
		builder.append("}$");
		String regex = builder.toString();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(inputStr);
		boolean result = matcher.matches();
		return result;
	}

	public static boolean length(String inputStr, int minLen) {
		return length(inputStr, minLen, 0);
	}



	private static boolean compare(int comparor,int begin,int last)
	{
		return comparor>=begin&&comparor<=last?true:false;
	}
	
	/**
	 * 用以匹配汉字等不要超过长度，汉字算两个字节
	 * @param inputStr
	 * @param minLen
	 * @param maxLen
	 * @return
	 */

	public static boolean lengthMixChinese(String inputStr,int minLen,int maxLen)
	{
		if(inputStr==null)
			return false;
		int length=0;
		int latChinese=0x9fff;
		int firstChinese=0x4e00;
		int alphaLittleCodeBegin="a".codePointAt(0);
		int alphaLittleCodeEnd="z".codePointAt(0);
		int alphaBigCodeBegin="A".codePointAt(0);
		int alphaBigCodeEnd="Z".codePointAt(0);
		int numberBegin="0".codePointAt(0);
		int numberEnd="9".codePointAt(0);
		for(int i=0;i<inputStr.length();i++)
		{
			int code=inputStr.codePointAt(i);
			if(!compare(code, firstChinese, latChinese)&&!compare(code, alphaBigCodeBegin, alphaBigCodeEnd)&&!compare(code,alphaLittleCodeBegin, alphaLittleCodeEnd)&&!compare(code, numberBegin, numberEnd))
			{
				return false;
			}
		
			if(code<latChinese&&code>firstChinese)
			{
				length+=2;
			}
			else
				length+=1;
		}
		boolean result =length>=minLen&&length<=maxLen?true:false;
		return result;
	}
	/**
	 * 不为空，包括空白
	 * 
	 * @param inputStr
	 * @return
	 */
	public static boolean isNull(String... str) {
		if (null == str||str.length == 0) {
			return true;
		}
		for (int i = 0; i < str.length; i++) {
			if (null == str[i] || "".equals(str[i].trim())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断字符串的gbk编码后字节 （汉字两个字节，英文一个字节）
	 * @param inputStr
	 * @param minLen
	 * @param maxLen
	 * @return
	 */
	public static boolean strLength(String inputStr,int minLen,int maxLen){
		if(!isNull(inputStr)){
			return false;
		}else{
			try {
				byte[] len = inputStr.getBytes("gbk");
				if(len.length>=minLen&&len.length<=maxLen)
					return true;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	
   
	public static void main(String[] args) throws UnsupportedEncodingException {
//		System.out.println(ValidateMode
//				.email("1tarleead.abc-bc.abc.tdr@gm-ail.cm"));
//		System.out.println(ValidateMode.length("00dd_", 4));
		String abc="123";
		System.out.println(ValidateMode.digital(abc));
//		System.out.println(abc.codePointAt(0));

//		System.out.println(formatDate("1970-02-29 21:44:00","1972-02-28 21:44:00"));

//		System.out.println(String.valueOf("\u9FA5"));
	
	}
}

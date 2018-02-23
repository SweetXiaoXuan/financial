package cn.xxtui.support.util;
public class EncodeUtils {
	public static String toEncodeString(String str){
		 String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };  
	        for (String key : fbsArr) {  
	            if (str.contains(key)) {  
	            	str = str.replace(key, "\\" + key);  
	            }  
	        }   	
		return str.replace("\"", "\\\"").replace("", "");
	}
}

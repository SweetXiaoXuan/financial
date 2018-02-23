package cn.xxtui.support.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SystemConf {
	private final static Properties properties=new Properties();
	private Logger log=LogManager.getLogger("SystemConf");
	private SystemConf() {
		try {
			if(log.isInfoEnabled())
			{
				log.info("loading the system configuration:config.conf");
			}
			properties.load(SystemConf.class.getClassLoader().getResourceAsStream("config.conf"));
			if(log.isInfoEnabled())
			{
				log.info(properties.toString());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(log.isInfoEnabled())
			{
				log.error("loading the system failed:config.conf");
			}
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static String get(String key)
	{
		return (String)properties.get(key);
	}
	
	public static SystemConf init() {
		return new SystemConf();
	}
}

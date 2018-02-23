package cn.xxtui.support.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonType {
	public JsonTypeEnum type() default JsonTypeEnum.STRING;
	public String name() default "";
	public Class clazz() default Object.class;
}

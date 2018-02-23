package cn.xxtui.support.session;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MobileResourceAccess {
	AccessType accessType() default AccessType.AUTHORIZE;//另外一种authorize
}
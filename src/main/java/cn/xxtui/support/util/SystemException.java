package cn.xxtui.support.util;

import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.xxtui.support.bean.ResultStruct;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;

/**
 * System exception
 * Created by Administrator on 2017/8/2 0002.
 */
public enum SystemException {
    SYSTEM_EXCEPTION(500, "System exception");

    SystemException(int value, String description) {
        this.value = value;
        this.description = description;
    }
    private int value;
    private String description;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Response setResult(ResultStruct resultStruct, Exception e, Logger logger) {
        e.printStackTrace();
        logger.error(e.getMessage(), e);
        resultStruct.setBody("");
        resultStruct.setMsg(e.getMessage());
        resultStruct.setStatus(String.valueOf(SYSTEM_EXCEPTION.getValue()));
        return Response.ok(resultStruct.toString()).build();
    }

    public static ReturnValue setResult(ReturnValue returnValue, Exception e, Logger logger) {
        e.printStackTrace();
        logger.error(e.getMessage(), e);
        returnValue.setObject("");
        returnValue.setMeg(e.getMessage());
        returnValue.setFlag(Integer.parseInt(String.valueOf(SYSTEM_EXCEPTION.getValue())));
        return returnValue;
    }

}

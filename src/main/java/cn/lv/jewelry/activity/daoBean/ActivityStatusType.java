package cn.lv.jewelry.activity.daoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixing on 16/3/23.
 */
public enum  ActivityStatusType {
    // 3未发布 2未举办 1举办中 0举办完成 4删除 5草稿
    COMPLETE(0), ONGOING(1), UPCOMING(2), UNPUBLISHED(3),DELETE(4), DRAFT(5);

    private int v;
    ActivityStatusType(int v)
    {
       this.v=v;
    }
    public int getV()
    {
        return this.v;
    }
    //状态顺序 未发布 -> 未举办 -> 举办中 -> 举办完成(要求新增状态时按照顺序添加到sortStatus)
    private static List<Integer> sortStatus = new ArrayList<Integer>(){{
        add(3);
        add(2);
        add(1);
        add(0);
        add(4);
        add(5);
    }};
    public static boolean isAvailStatus(int activityStatus, int updateStatus){
        boolean isAvail = false;
        int activityStatusIndex = sortStatus.indexOf(activityStatus);
        int updateStatusIndex = sortStatus.indexOf(updateStatus);
        if(activityStatusIndex == 5 || activityStatusIndex != -1 && updateStatusIndex != -1 && updateStatusIndex >= activityStatusIndex)
            isAvail = true;

        return isAvail;

    }
    public static ActivityStatusType type(int i)
    {
        switch (i)
        {
            case 0:return COMPLETE;
            case 1:return ONGOING;
            case 2:return UPCOMING;
            case 3:return UNPUBLISHED;
            case 4:return DELETE;
            case 5:return DRAFT;
            default:break;
        }
        return null;
    }

}

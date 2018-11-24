package xin.l024.blog.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataFormat {
    static SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String getData(Date date){
        Date d1 = null;
        Date d2 = null;

        try {
            d1 = sdf.parse(sdf.format(date));
            d2 = sdf.parse(sdf.format(new Date()));

            //毫秒ms
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000)%365;
            long year = diff/(24*60*60*1000)/365;

            System.out.print("两个时间相差："+diffDays + " 天, "+diffHours + " 小时, "+diffMinutes + " 分钟, "+diffSeconds + " 秒.");
            return year+"年"+diffDays+"天";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMiss(){
        try {
            Date date = sdf.parse(sdf.format(new Date()));
            return date.getTime()+"";
        }catch (Exception e){
            return "1544871414156";
        }
    }
}

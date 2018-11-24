package xin.l024.blog.util;

import org.springframework.beans.factory.annotation.Autowired;
import xin.l024.blog.service.SettingService;

public class SettingUtil {
    @Autowired
    private  SettingService settingService;
    public  String getNetName(){
       return settingService.getSetting().getTitle();
    }
}

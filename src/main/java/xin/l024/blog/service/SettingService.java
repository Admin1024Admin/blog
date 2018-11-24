package xin.l024.blog.service;

import xin.l024.blog.entity.Setting;

public interface SettingService {
    //获取
    public Setting getSetting();
    //设置更改
    public Setting updataSetting(Setting setting);
}

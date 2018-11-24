package xin.l024.blog.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.l024.blog.entity.Setting;
import xin.l024.blog.repository.SettingRepository;
import xin.l024.blog.service.SettingService;

@Service
public class SettingServiceImpl implements SettingService{
    @Autowired
    private SettingRepository settingRepository;
    @Override
    public Setting getSetting() {
        return settingRepository.findAll().get(0);
    }

    @Override
    public Setting updataSetting(Setting setting) {
        return settingRepository.save(setting);
    }
}

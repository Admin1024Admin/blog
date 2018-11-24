package xin.l024.blog.service;

import xin.l024.blog.entity.BlogType;
import xin.l024.blog.entity.User;

import java.util.List;

public interface BlogTypeService {
    //根据用户id查询类型
    public List<BlogType> fingByUserId(Long uid);
    //添加
    //保存分类
    public BlogType savaType(User user, String typeName);
}

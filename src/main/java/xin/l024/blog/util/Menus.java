package xin.l024.blog.util;

import xin.l024.blog.dto.Menu;

import java.util.ArrayList;
import java.util.List;

public class Menus {
    public static List<Menu> getMenus(){
        List<Menu> menus = new ArrayList<>();
        menus.add(new Menu("用户管理", "/admins/index",true));
        menus.add(new Menu("博客管理", "/admins/blogs",false));
        menus.add(new Menu("评论管理", "/admins/commits",false));
        return menus;
    }
}

package xin.l024.blog.dto;

import java.io.Serializable;

public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String url;
    private boolean active;

    public Menu(String name, String url,Boolean active) {
        this.name = name;
        this.url = url;
        this.active = active;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

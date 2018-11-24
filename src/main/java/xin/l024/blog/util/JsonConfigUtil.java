package xin.l024.blog.util;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonConfigUtil {
    public static JsonConfig getJsonConfig(){
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"handler","hibernateLazyInitializer"});
        jsonConfig.registerJsonValueProcessor(Date.class,
                new JsonValueProcessor() {

                    /**
                     * paramString -> 参数名 paramObject -> 参数值
                     */
                    @Override
                    public Object processObjectValue(String paramString,
                                                     Object paramObject, JsonConfig paramJsonConfig) {
                        if (paramObject == null) {
                            return null;
                        }
                        String ret = null;
                        try {
                            SimpleDateFormat format = new SimpleDateFormat(
                                    "yyyy-MM-dd"); //"yyyy-MM-dd HH:mm:ss"
                            ret = format.format((Date) paramObject);
                        } catch (Exception e) {
                            SimpleDateFormat format = new SimpleDateFormat(
                                    "yyyy-MM-dd");
                            ret = format.format((Date) paramObject);
                        }
                        return ret;
                    }

                    @Override
                    public Object processArrayValue(Object paramObject,
                                                    JsonConfig paramJsonConfig) {
                        return null;
                    }
                });
        return jsonConfig;
    }
}

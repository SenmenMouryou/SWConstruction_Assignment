package My_Chart_Main;

import java.io.*;
import java.util.Properties;

/**
 * @author SENMEN
 * 配置文件管理类
 */
public class Property_Manager {

    private static final String filename = "Resource/settings.property";

    /**
     * 向配置文件中插入字段
     * @param key  字段名
     * @param value 字段值
     * @throws IOException
     */
    public static void write_Property(String key, String value) throws IOException {

        //这种方式有键值的会修改，否则创建键值，修改后的配置文件查看target目录
        Properties properties = new Properties();

        OutputStream fos = new FileOutputStream(filename);

        properties.setProperty(key, value);

        //保存，并加入注释
        properties.store(fos, "");
        fos.close();
    }

    /**
     * 读取输入键值的字段值
     * @param key 键值
     * @return  键值对应的字段值
     * @throws IOException
     */
    public static String read_Property(String key) throws IOException {
        //读取配置
        InputStream inputStream = new FileInputStream(filename);
        Properties properties = new Properties();
        properties.load(inputStream);

        return properties.getProperty(key);
    }

}

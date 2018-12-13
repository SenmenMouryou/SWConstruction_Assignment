package My_Chart_Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SENMEN
 * 读取器类
 * 用于读取.bin文件中的数据
 */
public class Data_Reader {
    //日志类
    private Logger logger = Logger.getLogger(Data_Reader.class.getName());

    //将打开的文件名
    private String file_Name = null;
    public void setFile_Name(String file_Name) {
        this.file_Name = file_Name;
    }

    //存放数据的数组
    private byte[] data;
    public byte[] getData() {
        return data;
    }

    //读取的字节数
    private int bytes_To_Read = 0;

    //文件随机访问流
    private RandomAccessFile raf = null;

    /**
     * 构造器
     * @param file_Name     将读取的文件名
     */
    public Data_Reader(String file_Name){
        this.file_Name = file_Name;
    }

    /**
     * 启动读取器方法
     * @return 0：启动正常 -1：启动出错
     */
    public int run_Reader(){

        if(open_File()<0){
            logger.log(Level.SEVERE,"打开文件出错");
            return -1;
        }

        if(init_Data_Array()<0){
            logger.log(Level.SEVERE,"字节数组初始化出错");
            return -1;
        }

        if(read_From_File_To_Data()<0){
            logger.log(Level.SEVERE,"文件读取到数组时出错");
            return -1;
        }
        logger.log(Level.INFO,"文件读取完成");

        return 0;
    }


    /**
     * 打开文件，检测文件路径合法性
     * @return 0：文件路径合法 -1：不合法
     */
    private int open_File(){
        try {
            File file = new File(file_Name);

            //检测文件长度
            if(file.length()>Integer.parseInt(Property_Manager.read_Property("MAX_FILE_SIZE"))){
                logger.log(Level.WARNING,"文件过长");
                return -1;
            }
            //确定要读入的字节数
            bytes_To_Read = (int)file.length();

            //创建文件随机访问流
            raf = new RandomAccessFile(file,"r");

        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE,"文件无法访问：路径不存在",e);
            return -1;
        } catch (IOException e) {
            logger.log(Level.SEVERE,"配置文件访问失败",e);
        }

        logger.log(Level.INFO,"文件已打开");

        return 1;
    }

    /**
     * 初始化字节数组，长度与文件长度匹配
     * @return 数组长度 -1：读取长度获取失败
     */
    private int init_Data_Array(){
        if(bytes_To_Read<=0){
            logger.log(Level.SEVERE,"读取长度获取失败");
            return -1;
        }
        data = new byte[bytes_To_Read];

        return data.length;
    }

    /**
     * 从文件随机访问流中读取数据到字节数组
     * @return  读取到的字节数 -1：读取失败
     */
    private int read_From_File_To_Data(){
        try {
            raf.read(data);
        } catch (IOException e) {
            logger.log(Level.SEVERE,"文件读取出错",e);
            return -1;
        }
        return data.length;
    }


}

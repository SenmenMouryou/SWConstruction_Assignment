package Test_Drivers;

import My_Chart_Main.Data_Reader;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SENMEN
 * 数据读取器的测试用例
 */
public class Data_Reader_Tester {
    //日志类
    private static Logger logger = Logger.getLogger(Data_Reader_Tester.class.getName());

    private static final int DATA_LENGTH = 10000000;

    private byte[] data = new byte[DATA_LENGTH];

    private String filename = "Test/file_Reader_Test.bin";
    private String file_Path = "Test";

    //将被测试的读取器
    private Data_Reader data_Reader_To_Test;

    public Data_Reader_Tester(){
        init_Data();
        write_Data_To_File();
        assert check_Data_Written()==true;

        //初始化读取器
        data_Reader_To_Test = new Data_Reader(filename);
        logger.log(Level.INFO,"读取器已初始化");

    }

    /**
     * 开始测试用例函数
     * @return 当测试结果合法时返回true，不合法时返回false
     */
    public boolean test_Start(){

        logger.log(Level.INFO,"读取器测试开始");

        if(data_Reader_To_Test.run_Reader()<0){
            return false;
        }

        //检验读取结果的正确性
        logger.log(Level.INFO,"读取完成，开始检验");
        for(int i=0; i<data_Reader_To_Test.getData().length; i++){
            if(data_Reader_To_Test.getData()[i]!=data[i]){
                logger.log(Level.SEVERE,"检验出读取错误，在第"+i+"个字节处\n" +
                        "文件中的数据为："+data[i]+"\n"+
                        "读取后的数据为："+data_Reader_To_Test.getData()[i]);
                return false;
            }
        }
        logger.log(Level.INFO,"测试完毕，读取器组件运行正常");

        return true;

    }

    /**
     * 初始化将写入文件的byte数组
     */
    private void init_Data(){
        for( int i = 0; i < DATA_LENGTH; i++ ){
            byte newByte = (byte)(i%128);
            data[i] = newByte;
        }
    }

    /**
     * 将数组data写入.bin文件
     * @return true 写入文件成功; false 写入文件失败
     */
    private boolean write_Data_To_File(){
        try {
            //创建目录
            File file_path = new File(file_Path);
            if(file_path.mkdirs()){
                logger.log(Level.INFO,"测试文件存放目录已创建");
            }
            else{
                logger.log(Level.WARNING,"测试文件存放目录创建失败");
            }

            logger.log(Level.INFO,"开始写入测试文件");

            OutputStream out = new BufferedOutputStream(new FileOutputStream(filename));
            out.write(data);

            System.out.println();
            out.close();

            logger.log(Level.INFO,"测试文件写入完毕");
            return true;

        }catch (IOException e){
            logger.log(Level.SEVERE,"测试数据写入文件失败！",e);
            return false;
        }

    }

    /**
     * 重新读取写完的文件，检查其正确性
     * @return 文件正确为真，反之为假
     */
    private boolean check_Data_Written(){

        try{
            InputStream in = new FileInputStream(filename);

            byte[] data_Reread = new byte[DATA_LENGTH];

            int data_Read_Length = in.read(data_Reread);

            //检查文件长度
            if(data_Read_Length!=DATA_LENGTH){
                logger.log(Level.SEVERE,"测试文件长度出错！");
                return false;
            }

            //检查数据正确性

            for(int i=0;i<DATA_LENGTH;i++){
                if(data[i]!=data_Reread[i]){
                    logger.log(Level.SEVERE,"测试文件内容出错！");
                    return false;
                }
            }

        }catch (FileNotFoundException e){
            logger.log(Level.SEVERE,"测试文件创建失败！",e);
        }catch (IOException e){
            logger.log(Level.SEVERE,"测试文件字节数读取失败！",e);
        }

        return true;
    }

}

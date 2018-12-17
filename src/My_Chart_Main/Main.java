package My_Chart_Main;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    //日志类
    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Data_Driver dataDriver = new Data_Driver();

//        Test_Drivers.Data_Reader_Tester test = new Test_Drivers.Data_Reader_Tester();
//        test.test_Start();

//        Data_Processor_Tester test = new Data_Processor_Tester();
//        test.test_Start();

//        Data_Array_Transformer_Tester test = new Data_Array_Transformer_Tester();
//        test.test_Start();

//        test_IO();

    }

    private static void test_IO(){
        int FILE_LENGTH = 2000;

        String filename = "Test/Test_Data_Random_2000.bin";

        try {
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filename));

            logger.log(Level.INFO,"文件开始写入");

            for(int i = 0; i < FILE_LENGTH; i++){
                byte byte_To_Write = (byte)(Math.random()*(Byte.MAX_VALUE-Byte.MIN_VALUE));
                System.out.print(byte_To_Write+" ");
                outputStream.write(byte_To_Write);
            }

            logger.log(Level.INFO,"文件写入完毕");

            outputStream.close();

        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE,"文件写入流创建失败：文件路径不存在",e);
        } catch (IOException e){
            logger.log(Level.SEVERE,"文件写入出错",e);
        }

        try {
            RandomAccessFile raf = new RandomAccessFile(filename,"r");
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(filename));

            byte[] bytes = new byte[FILE_LENGTH];

            logger.log(Level.INFO,"文件开始读取");

//            buf.read(bytes, 0, FILE_LENGTH);
//            buf.close();

            raf.read(bytes);
            raf.close();

            logger.log(Level.INFO,"文件读取完毕");

            for(int i=0; i<5; i++){
                System.out.print(bytes[i]+" ");
                if(i%10==0){
                    System.out.println();
                }
            }


        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE,"文件读取流创建失败",e);
        } catch (IOException e) {
            logger.log(Level.SEVERE,"文件读取错误",e);
        }

    }
}

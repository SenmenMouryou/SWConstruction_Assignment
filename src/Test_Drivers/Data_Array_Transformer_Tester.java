package Test_Drivers;

import Default_Package.Data_Array_Magnify;
import Default_Package.Data_Array_Transformer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SENMEN
 * 数据数组变形器的测试类
 */
public class Data_Array_Transformer_Tester {
    //日志类
    private Logger logger = Logger.getLogger(Data_Array_Transformer_Tester.class.getName());

    private Data_Array_Transformer transformer = null;
    //原始数据长度
    private final int DATA_LENGTH= 1000;
    //原始数据
    private int[] source_Data_Tested = new int[DATA_LENGTH];


    public boolean test_Start(){
        init_Test_Data();

        return true;
    }


    /**
     * 初始化测试数组
     * @return  返回码 0：正确返回 -1：错误
     */
    private int init_Test_Data(){

        logger.log(Level.INFO,"开始生成测试用原始数据");

        int max_Byte = Byte.MAX_VALUE;
        int min_Byte = Byte.MIN_VALUE;

        for(int i=0; i<DATA_LENGTH; i++){
            int random = (int)(Math.random()*(max_Byte-min_Byte)+min_Byte);

            if(random>max_Byte || random<min_Byte){
                logger.log(Level.SEVERE,"测试数组生成错误");
                return -1;
            }

            source_Data_Tested[i] = random;
        }

        return 0;



    }

    /**
     * 测试放大变形器
     * @return 测试通过情况的布尔值
     */
    private boolean test_Magnify(){

        transformer = new Data_Array_Magnify();

        MARK

        return true;
    }
}

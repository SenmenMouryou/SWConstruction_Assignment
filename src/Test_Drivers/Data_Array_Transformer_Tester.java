package Test_Drivers;

import Default_Package.Data_Array_Magnify;
import Default_Package.Data_Array_Shrink;
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
    //放大率
    private int rate_Magnify = 1;
    //缩小率
    private int rate_Shrink = 1;
    //放大/缩小率最大值
    private final int MAXIMUM_RATE = 20;

    /**
     * 测试入口
     * @return 测试情况的布尔值
     */
    public boolean test_Start(){

        if(test_Magnify() && test_Shrink()){
            logger.log(Level.INFO,"变形器测试完毕，未检测出错误");
        }
        else{
            return false;
        }

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

        //初始化测试数据
        init_Test_Data();
        rate_Magnify = (int)(Math.random() * MAXIMUM_RATE);
        //初始化放大器
        transformer = new Data_Array_Magnify();
        //执行放大
        int[] data_Trans = transformer.data_Array_Trans(source_Data_Tested, rate_Magnify);

        //检测数据
        logger.log(Level.INFO, "开始匹配放大数据,放大倍率为"+rate_Magnify);
        for(int i = 0; i < source_Data_Tested.length; i++){
            //比较放大前后的数据
            if(source_Data_Tested[i] != data_Trans[i*rate_Magnify]){
                logger.log(Level.WARNING,"检测出错误：从原始数据第"+i+"位开始");
                return false;
            }
        }

        logger.log(Level.INFO,"放大器测试完毕，未出现错误");
        return true;
    }

    private boolean test_Shrink(){

        //初始化测试数据
        init_Test_Data();
        rate_Shrink = (int)(Math.random() * MAXIMUM_RATE);
        //初始化缩小器
        transformer = new Data_Array_Shrink();
        //执行缩小
        int[] data_Shrink = transformer.data_Array_Trans(source_Data_Tested, rate_Shrink);

        //检测数据
        logger.log(Level.INFO,"开始匹配缩小数据，缩小倍率为"+rate_Shrink);
        for(int i = 0; i < data_Shrink.length; i++){
            if(data_Shrink[i] != source_Data_Tested[i*rate_Shrink]){
                logger.log(Level.WARNING,"检测出错误：从原始数据第"+i*rate_Shrink+"位开始");
                return false;
            }
        }

        logger.log(Level.INFO,"缩小器测试完毕，未出现错误");
        return true;

    }
}

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SENMEN
 * 数据处理器的测试类
 */
public class Data_Processor_Test {
    //日志类·
    private Logger logger = Logger.getLogger(Data_Processor_Test.class.getName());

    private Data_Processor data_processor = null;
    private final int DATA_LENGTH = 1000;
    private byte[] source_Data_Tested = new byte[DATA_LENGTH];

    public boolean test_Start(){

        logger.log(Level.INFO,"数据处理器测试开始");

        boolean result = test_Diff() && test_Sec_Diff() && test_Intgr();

        if(result){
            logger.log(Level.INFO,"数据处理器测试完成，全部测试通过");
        }

        return result;
    }

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

            source_Data_Tested[i] = (byte)random;
        }

        return 0;
    }

    /**
     * 测试一阶微分
     * @return 测试结果通过情况的布尔值
     */
    private boolean test_Diff(){

        logger.log(Level.INFO,"一阶微分测试开始");

        //重新生成数据
        if(init_Test_Data()<0){
            return false;
        }

        data_processor = new Diff_Processor();
        int[] diff_Data_Test = data_processor.generate_Data(Byte_Array_To_Int.byte_Array_To_Int(source_Data_Tested));

        for(int i=0; i<DATA_LENGTH-1; i++){
            if(diff_Data_Test[i] != source_Data_Tested[i+1]- source_Data_Tested[i]){
                logger.log(Level.WARNING,"一阶微分测试出错:从第"+i+"个数据起结果错误");
                return false;
            }
        }

        logger.log(Level.INFO,"一阶微分测试通过");
        return true;
    }

    /**
     * 测试二阶微分
     * @return 测试结果通过情况的布尔值
     */
    private boolean test_Sec_Diff(){

        logger.log(Level.INFO,"二阶微分测试开始");

        //重新生成数据
        if(init_Test_Data()<0){
            return false;
        }

        data_processor = new Sec_Diff_Processor();
        int[] sec_Diff_Test = data_processor.generate_Data(Byte_Array_To_Int.byte_Array_To_Int(source_Data_Tested));

        for(int i=0; i<DATA_LENGTH-2; i++){
            if(sec_Diff_Test[i] != source_Data_Tested[i+2]+ source_Data_Tested[i]-2* source_Data_Tested[i+1]){
                logger.log(Level.WARNING,"二阶微分测试出错:从第"+i+"个数据起结果错误");
                return false;
            }
        }

        logger.log(Level.INFO,"二阶微分测试通过");
        return true;
    }

    /**
     * 测试一阶积分
     * @return 测试结果通过情况的布尔值
     */
    private boolean test_Intgr() {

        logger.log(Level.INFO,"二阶微分测试开始");

        //重新生成数据
        if(init_Test_Data()<0){
            return false;
        }

        data_processor = new Intgr_Processor();
        int[] intgr_Test = data_processor.generate_Data(Byte_Array_To_Int.byte_Array_To_Int(source_Data_Tested));

        for(int i=0; i<DATA_LENGTH-1; i++){
            if(intgr_Test[i] != source_Data_Tested[i]+source_Data_Tested[i+1]){
                logger.log(Level.WARNING,"一阶积分测试出错:从第"+i+"个数据起结果错误");
                return false;
            }
        }

        logger.log(Level.INFO,"一阶积分测试通过");
        return true;
    }


}

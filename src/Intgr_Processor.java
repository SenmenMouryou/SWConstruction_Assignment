import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SENMEN
 * 计算一阶积分的数据处理类
 */
public class Intgr_Processor implements Data_Processor{
    //日志类
    private Logger logger = Logger.getLogger(Intgr_Processor.class.getName());

    @Override
    public int[] generate_Data(int[] source_Data) {

        logger.log(Level.INFO,"开始计算一阶积分函数");

        int data_Length = source_Data.length;
        int[] intgr_Data = new int[data_Length];

        for(int i=0; i<data_Length-1; i++){
            intgr_Data[i] = source_Data[i]+source_Data[i+1];
        }
        //末位处理
        intgr_Data[data_Length-1] = intgr_Data[data_Length-2];

        logger.log(Level.INFO,"一阶积分函数计算完毕");

        return intgr_Data;
    }
}

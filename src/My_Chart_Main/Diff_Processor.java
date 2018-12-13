package My_Chart_Main;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SENMEN
 * 计算一阶微分的数据处理类
 */
public class Diff_Processor implements Data_Processor {
    //日志类
    private Logger logger = Logger.getLogger(Diff_Processor.class.getName());

    @Override
    public int[] generate_Data(int[] source_Data) {

        logger.log(Level.INFO,"开始计算一阶微分函数");

        int data_Length = source_Data.length;
        int[] diff_Data = new int[data_Length];

        for(int i=0; i<data_Length-1; i++){
            diff_Data[i] = source_Data[i+1]-source_Data[i];
        }
        //末位处理
        diff_Data[data_Length-1] = diff_Data[data_Length-2];

        logger.log(Level.INFO,"一阶微分函数计算完毕");

        return diff_Data;

    }
}

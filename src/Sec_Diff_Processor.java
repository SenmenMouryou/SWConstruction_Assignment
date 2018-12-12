import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SENMEN
 * 计算二阶微分的数据处理类
 */
public class Sec_Diff_Processor implements Data_Processor{
    //日志类
    private Logger logger = Logger.getLogger(Sec_Diff_Processor.class.getName());

    @Override
    public int[] generate_Data(int[] source_Data) {

        logger.log(Level.INFO,"开始计算二阶微分函数");

        int[] sec_diff = null;

        Diff_Processor diff_processor = new Diff_Processor();
        sec_diff = diff_processor.generate_Data(source_Data);
        sec_diff = diff_processor.generate_Data(sec_diff);

        logger.log(Level.INFO,"二阶微分函数计算完毕");

        return sec_diff;
    }
}

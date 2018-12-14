package My_Chart_Main;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SENMEN
 * 曲线生成的驱动类
 * 构造时读入读取器，调用数据处理器生成微分积分等数组
 */
public class Driver {

    //日志类
    private Logger logger = Logger.getLogger(Driver.class.getName());

    /*************************数据数组*************************/
    //原始数据
    private byte[] source_Data = null;

    //整型原始数据副本
    private int[] source_Data_Int = null;

    //一阶微分
    private int[] diff_Data = null;
    //二阶微分
    private int[] sec_Diff_Data = null;
    //一阶积分
    private int[] intgr_Data = null;

    /*************************窗口和面板************************/
    //程序主窗口
    private Main_Window main_Window = null;

    //通道面板数组
    private int CHANNEL_NUM = 4;
    private Channel_Panel[] channel_Panel_Array = new Channel_Panel[CHANNEL_NUM];

    /**
     * 构造器
     * 取得原始数据
     * @param data_reader 读取器
     */
    public Driver(Data_Reader data_reader){

        //获取原始数据
        data_reader.run_Reader();
        source_Data = data_reader.getData();
        logger.log(Level.INFO,"已获取原始数据");
        //获取原始数据整型副本
        source_Data_Int = Byte_Array_To_Int.byte_Array_To_Int(source_Data);

        //获取一阶微分
        Diff_Processor diff_Processor = new Diff_Processor();
        diff_Data = diff_Processor.generate_Data(source_Data_Int);
        logger.log(Level.INFO,"一阶微分已计算");

        //获取二阶微分
        Sec_Diff_Processor sec_Diff_Processor = new Sec_Diff_Processor();
        sec_Diff_Data = sec_Diff_Processor.generate_Data(source_Data_Int);
        logger.log(Level.INFO,"二阶微分已计算");

        //获取一阶积分
        Intgr_Processor intgr_Processor = new Intgr_Processor();
        intgr_Data = intgr_Processor.generate_Data(source_Data_Int);
        logger.log(Level.INFO,"一阶积分已计算");

        //获取窗口
        main_Window = new Main_Window();

    }
}

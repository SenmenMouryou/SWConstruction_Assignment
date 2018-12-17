package My_Chart_Main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SENMEN
 * 图表生成的数据驱动类
 * 构造时读入读取器，调用数据处理器生成微分积分等数组
 */
public class Data_Driver {

    //日志类
    private Logger logger = Logger.getLogger(Data_Driver.class.getName());

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

    /***************************图表***************************/
    private int CHART_ARRAY_LENGTH = 0;{
        try {
            //获取图表数 = 通道数
            CHART_ARRAY_LENGTH = Integer.parseInt(Property_Manager.read_Property("CHANNEL_NUM"));
        } catch (IOException e) {
            logger.log(Level.SEVERE,"配置文件读取失败",e);
        }
    }
    private Chart[] chart_Array = new Chart[CHART_ARRAY_LENGTH];

    /***************************窗口***************************/
    //程序主窗口
    private Main_Window main_Window = null;

    /**
     * 构造器
     */
    public Data_Driver(){

        //构造窗口
        main_Window = new Main_Window(this);
        //显示窗口
        main_Window.setVisible(true);
    }

    /**
     * 取得原始数据
     * @param data_reader 读取器
     */
    public void open_File(Data_Reader data_reader){

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

        //更新图表类
        chart_Array[0] = new Chart(source_Data_Int, 0);
        chart_Array[1] = new Chart(diff_Data, 1);
        chart_Array[2] = new Chart(sec_Diff_Data, 2);
        chart_Array[3] = new Chart(intgr_Data, 3);

        //更新窗口中各通道面板的显示
        Channel_Panel[] channel_Panels = main_Window.get_Channel_Panel_Array();
        //给各通道面板添加标签文本
        channel_Panels[0].add_Label_Name("原始数据");
        channel_Panels[1].add_Label_Name("一阶微分");
        channel_Panels[2].add_Label_Name("二阶微分");
        channel_Panels[3].add_Label_Name("一阶积分");
        //给各通道添加图表曲线
        for(int i = 0; i < CHART_ARRAY_LENGTH; i++){
            channel_Panels[i].renew_Chart_Panel_And_Repaint(chart_Array[i].repaint_Chart());
        }
        main_Window.repaint();

    }

    /**
     * 调整缩放比例
     * @param scale 新的缩放比例
     */
    public void modify_Scale(double scale){

        for(int i = 0; i < CHART_ARRAY_LENGTH; i++){
            Chart chart = chart_Array[i];
            if(chart == null){
                logger.log(Level.WARNING,"未定义图表数组内容");
                return;
            }
            chart.set_Print_Scale(scale);
            Channel_Panel channel_panel = main_Window.get_Channel_Panel_Array()[i];
            channel_panel.renew_Chart_Panel_And_Repaint(chart.repaint_Chart());

        }
    }

}

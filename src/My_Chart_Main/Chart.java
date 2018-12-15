package My_Chart_Main;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SENMEN
 * 图表类
 * 接收一个存放数据的int数组，将其转化为最大为窗口宽度的折线图显示在canvas类中
 * 并支持对该折线图的整数比例缩放与平移
 */
public class Chart{

    //日志类
    private Logger logger = Logger.getLogger(Chart.class.getName());

    //图表ID
    private int chart_ID = 0;
    public int get_Chart_ID() {
        return chart_ID;
    }

    /************************数据数组*************************/
    //原始数据数组
    private int[] data_Array_Origin = null;
    public void set_Data(int[] data) {
        int data_Length = data.length;
        this.data_Array_Origin = new int[data_Length];
        for(int i = 0; i < data_Length; i++){
            this.data_Array_Origin[i] = data[i];
        }
    }

    //默认的生成数组的长度
    private int DATA_GENERATE_LENGTH = 1000;
    //生成数组
    private int[] data_Array_Generate = new int[DATA_GENERATE_LENGTH];

    //生成数组起始对应原始数组的索引
    private int origin_Index_To_Generate_Start = 0;
    public void set_Origin_Index_To_Generate_Start(int origin_Index_To_Generate_Start) {
        if(origin_Index_To_Generate_Start < 0){
            logger.log(Level.WARNING,"生成数组起始索引设置失败：超出范围");
            return;
        }
        else if(origin_Index_To_Generate_Start >= data_Array_Origin.length && data_Array_Origin !=null){
            logger.log(Level.WARNING,"生成数组起始索引设置失败：超出范围");
            return;
        }
        else if(data_Array_Origin == null){
            logger.log(Level.WARNING,"尚未设置原始数组");
            return;
        }
        this.origin_Index_To_Generate_Start = origin_Index_To_Generate_Start;
    }

    /************************显示设置*************************/
    //窗口宽度
    private int WINDOW_WIDTH = 0;{
        try {
            WINDOW_WIDTH = Integer.parseInt(Property_Manager.read_Property("WINDOW_WIDTH"));
        } catch (IOException e) {
            logger.log(Level.SEVERE,"配置文件加载出错",e);
        }
    }


    //显示数据点的横向间距
    private int space_Between_Points = 5;{
        try {
            space_Between_Points = Integer.parseInt(Property_Manager.read_Property("SPACE_BETWEEN_POINTS"));
        } catch (IOException e) {
            logger.log(Level.SEVERE,"配置文件读取失败");
        }
    }

    //绘制缩放比例
    private double print_Scale = 1.0;
    private final double MAX_PRINT_SCALE = 3.0;
    public void set_Print_Scale(double print_Scale) {
        if(print_Scale > 0 && print_Scale < MAX_PRINT_SCALE){
            this.print_Scale = print_Scale;
        }
        else{
            logger.log(Level.WARNING,"缩放比例设置失败：超出范围");
        }
    }

    //生成数组绘制起始点
    private int generate_Index_Start_Paint = 0;
    public int get_Generate_Index_Start_Paint() {
        return generate_Index_Start_Paint;
    }

    /**
     * 构造器，读入初始的原始数据
     * @param data 原始数据
     */
    public Chart(@NotNull int[] data, int chart_ID){

        this.chart_ID = chart_ID;

        //取得原始数组
        this.data_Array_Origin = data;

        if(DATA_GENERATE_LENGTH > data.length){
            DATA_GENERATE_LENGTH = data.length;
        }

        //构造生成数组
        form_Data_Array_Generate();

        logger.log(Level.INFO,"图表类已构造");
    }

    /**
     * 重新绘制曲线，更新其缩放比例
     * @param print_Scale  新的绘制缩放比例
     */
    public Chart_Panel repaint_Chart(double print_Scale){

        //更新绘制缩放比例
        set_Print_Scale(print_Scale);

        //计算重绘的生成数组
        logger.log(Level.INFO,"计算重绘的生成数组");
        form_Data_Array_Generate();

        //构造绘制数组
        int print_Data_Length = WINDOW_WIDTH/space_Between_Points;
        int[] data_Print = new int[print_Data_Length];
        for(int i = 0; i < print_Data_Length; i++){
            data_Print[i] = data_Array_Generate[i];
        }

        logger.log(Level.INFO,"已生成重绘图表");
        //重绘图表面板
        return new Chart_Panel(data_Print, this);
    }

    /**
     * 重新绘制曲线，若超出生成数组范围则更新其显示起始点索引
     * @param generate_Index_Start_Paint 开始显示生成数组的索引
     */
    public Chart_Panel repaint_Chart(int generate_Index_Start_Paint){

        int print_Data_Length = WINDOW_WIDTH/space_Between_Points;
        if(print_Data_Length > DATA_GENERATE_LENGTH){
            print_Data_Length = DATA_GENERATE_LENGTH;
        }
        int[] data_Print = new int[print_Data_Length];

        //判断是否需要重新计算生成数组
        if(generate_Index_Start_Paint >= 0 &&
                generate_Index_Start_Paint + print_Data_Length <= data_Array_Generate.length) {
            if(generate_Index_Start_Paint + print_Data_Length > data_Array_Origin.length){
                logger.log(Level.WARNING,"超出原始数组范围，不移动显示区间");
            }
            else{
                //不需要重新计算生成数组：新的显示域仍在生成数组范围内
                logger.log(Level.INFO,"仅移动显示区间");
                //更新起始绘制点
                this.generate_Index_Start_Paint = generate_Index_Start_Paint;
            }
            for(int i = 0; i < print_Data_Length; i++){
                data_Print[i] = data_Array_Generate[i + this.generate_Index_Start_Paint];
            }
        }
        else{
            //需要重新计算生成数组
            logger.log(Level.INFO,"重新计算生成数组");
            //重置生成数组起始对应原始数组的索引
            set_Origin_Index_To_Generate_Start(generate_Index_Start_Paint + origin_Index_To_Generate_Start);
            //重新计算生成数组
            form_Data_Array_Generate();

            //构造绘制数组
            for(int i = 0; i < print_Data_Length; i++){
                data_Print[i] = data_Array_Generate[i];
                System.out.print(data_Print[i]+" ");
            }
            //更新起始绘制点
            this.generate_Index_Start_Paint = 0;
        }

        //重绘图表面板
        logger.log(Level.INFO,"已生成重绘图表");
        return new Chart_Panel(data_Print, this);
    }

    /**
     * 通过原始数据和显示设置计算得到生成数组
     */
    private void form_Data_Array_Generate() {

        //通过原始数据计算生成数组原型
        System.out.println(DATA_GENERATE_LENGTH);
        for(int i = 0; i < DATA_GENERATE_LENGTH; i++){
            if(origin_Index_To_Generate_Start +i < data_Array_Origin.length){
                data_Array_Generate[i] = data_Array_Origin[origin_Index_To_Generate_Start +i];
            }
            else{
                data_Array_Generate[i] = 0;
            }
        }

        //根据缩放比例缩放生成数组原型
        Data_Array_Transformer transformer = new Data_Array_Magnify();
        data_Array_Generate = transformer.data_Array_Trans(data_Array_Generate, (int)(print_Scale*10));
        transformer = new Data_Array_Shrink();
        data_Array_Generate = transformer.data_Array_Trans(data_Array_Generate, 10);

        logger.log(Level.INFO,"已重新计算绘制数组");

    }


}

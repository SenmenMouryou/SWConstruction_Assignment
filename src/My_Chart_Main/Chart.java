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
    private final int DEFAULT_DATA_GENERATE_LENGTH = 1000;
    //生成数组
    private int[] data_Array_Generate = new int[DEFAULT_DATA_GENERATE_LENGTH];

    //生成数组起始对应原始数组的索引
    private int origin_Index_To_Generate_Start = 0;
    public void set_Origin_Index_To_Generate_Start(int origin_Index_To_Generate_Start) {
        if(origin_Index_To_Generate_Start < 0){
            logger.log(Level.WARNING,"生成数组起始索引设置失败：超出范围");
            return;
        }
        else if(origin_Index_To_Generate_Start + WINDOW_WIDTH/space_Between_Points >=
                data_Array_Origin.length && data_Array_Origin !=null){
            logger.log(Level.WARNING,"生成数组起始索引设置失败：超出范围");
            return;
        }
        else if(data_Array_Origin == null){
            logger.log(Level.WARNING,"尚未设置原始数组");
            return;
        }
        this.origin_Index_To_Generate_Start = origin_Index_To_Generate_Start;
    }

    //绘制数组
    int[] data_Print = null;

    //生成数组绘制起始索引
    private int generate_Index_Start_Paint = 0;
    public int get_Generate_Index_Start_Paint() {
        return generate_Index_Start_Paint;
    }
    public void set_Generate_Index_Start_Paint(int generate_Index_Start_Paint) {
        //仅当向左移动 或 右移未超出生成数组显示范围时 允许重设生成数组显示起始点
        if(generate_Index_Start_Paint + WINDOW_WIDTH/space_Between_Points <= data_Array_Origin.length ||
                (generate_Index_Start_Paint < this.generate_Index_Start_Paint)){
            this.generate_Index_Start_Paint = generate_Index_Start_Paint;
        }else {
            logger.log(Level.WARNING,"超出原始数组范围，不移动显示起始点");
        }
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
    private int space_Between_Points = 3;
    private final int MAX_SPACE_BETWEEN_POINTS = 10;
    public int get_Space_Between_Points() {
        return space_Between_Points;
    }
    public void set_Space_Between_Points(int space_Between_Points) {
        if(space_Between_Points > 0 && space_Between_Points <= MAX_SPACE_BETWEEN_POINTS ){

            this.space_Between_Points = space_Between_Points;
        }
        else{
            logger.log(Level.WARNING,"间距修改失败:超出间距范围");
        }
    }

    //规定绘制缩放比例最值
    private double MAX_PRINT_SCALE = 3.0;
    private double MIN_PRINT_SCALE = 0.1;
    {
        //从配置文件获取缩放比例最值
        try {
            MAX_PRINT_SCALE = Double.parseDouble(Property_Manager.read_Property("MAX_PRINT_SCALE"));
            MIN_PRINT_SCALE = Double.parseDouble(Property_Manager.read_Property("MIN_PRINT_SCALE"));
        } catch (IOException e) {
            logger.log(Level.SEVERE,"配置文件读取失败",e);
        }
    }

    //绘制缩放比例
    private double print_Scale = 1.0;
    public double get_Print_Scale() {
        return print_Scale;
    }
    public void set_Print_Scale(double print_Scale) {
        if(print_Scale == this.print_Scale){
            logger.log(Level.WARNING,"缩放比例未改变");
            return;
        }
        else if(print_Scale >= MIN_PRINT_SCALE && print_Scale <= MAX_PRINT_SCALE){
            this.print_Scale = print_Scale;
            //重设缩放比后，应重新计算生成数组
            form_Data_Array_Generate();
        }
        else{
            logger.log(Level.WARNING,"缩放比例设置失败：超出范围");
        }
    }

    /**
     * 构造器，读入初始的原始数据
     * @param data 原始数据
     */
    public Chart(@NotNull int[] data, int chart_ID){

        this.chart_ID = chart_ID;

        //取得原始数组
        this.data_Array_Origin = data;

        //若原始数组长度短于默认生成数组长度
        if(DEFAULT_DATA_GENERATE_LENGTH > data.length){
            data_Array_Generate = new int[data.length];
        }

        //构造生成数组
        form_Data_Array_Generate();

        logger.log(Level.INFO,"图表类已构造");
    }

//    /**
//     * 重新绘制曲线，更新其缩放比例
//     * @param print_Scale  新的绘制缩放比例
//     */
//    public Chart_Panel repaint_Chart(double print_Scale){
//
//        //更新绘制缩放比例
//        set_Print_Scale(print_Scale);
//
//        //计算重绘的生成数组
//        logger.log(Level.INFO,"计算重绘的生成数组");
//        form_Data_Array_Generate();
//
//        //构造绘制数组
//        int print_Data_Length = WINDOW_WIDTH/space_Between_Points;
//        int[] data_Print = new int[print_Data_Length];
//        for(int i = 0; i < print_Data_Length; i++){
//            data_Print[i] = data_Array_Generate[i];
//        }
//
//        logger.log(Level.INFO,"已生成重绘图表");
//        //重绘图表面板
//        return new Chart_Panel(data_Print, this);
//    }

    /**
     * 重新绘制曲线
     */
    public Chart_Panel repaint_Chart(){

        //判断是否需要重新计算生成数组
        if(generate_Index_Start_Paint >= 0
                && generate_Index_Start_Paint + WINDOW_WIDTH/space_Between_Points <= data_Array_Generate.length) {
            //不需要重新计算生成数组：新的显示域仍在生成数组范围内
            logger.log(Level.INFO,"仅移动显示区间");

            //计算绘制数组
            form_Data_Array_Print();

            for(int i = 0; i < data_Print.length; i++){
                data_Print[i] = data_Array_Generate[i + generate_Index_Start_Paint];
            }
        }
        else{
            //需要重新计算生成数组
            logger.log(Level.INFO,"重新计算生成数组");
            //重置生成数组起始对应原始数组的索引
            set_Origin_Index_To_Generate_Start(generate_Index_Start_Paint + origin_Index_To_Generate_Start);
            //重新计算生成数组
            form_Data_Array_Generate();

            //计算绘制数组
            form_Data_Array_Print();

            for(int i = 0; i < data_Print.length; i++){
                data_Print[i] = data_Array_Generate[i];
            }
            //更新起始绘制点
            set_Generate_Index_Start_Paint(0);
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
        for(int i = 0; i < data_Array_Generate.length; i++){
            if(origin_Index_To_Generate_Start +i < data_Array_Origin.length){
                data_Array_Generate[i] = data_Array_Origin[origin_Index_To_Generate_Start + i];
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

    private void form_Data_Array_Print(){
        //计算显示数据的长度
        int print_Data_Length = 0;
        if( data_Array_Generate.length - generate_Index_Start_Paint < WINDOW_WIDTH/space_Between_Points){
            //若可显示的生成数组短于窗口宽度允许显示的范围
            print_Data_Length = data_Array_Generate.length - generate_Index_Start_Paint;
        }
        else{
            //若可显示的生成数组不短于窗口宽度允许显示的范围
            print_Data_Length = WINDOW_WIDTH/space_Between_Points;
        }
        //显示数据的数组
        data_Print = new int[print_Data_Length];
    }


}

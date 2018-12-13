package Default_Package;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SENMEN
 * 图表类
 * 接收一个存放数据的int数组，将其转化为最大为窗口宽度的折线图显示在canvas类中
 * 并支持对该折线图的整数比例缩放与平移
 */
public class Chart {
    //日志类
    private Logger logger = Logger.getLogger(Chart.class.getName());

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

    //生成数组的长度
    private int data_Generate_Length = 1000;
    //生成数组
    private int[] data_Array_Generate = new int[data_Generate_Length];

    /************************显示设置*************************/

    //生成数组显示的起始位置
    private int data_Index_Start_To_Print = 0;
    public void set_Data_Index_Start_To_Print(int data_Index_Start_To_Print) {
        if(data_Index_Start_To_Print>= data_Array_Origin.length && data_Array_Origin !=null){
            logger.log(Level.WARNING,"无效的起始绘制点");
            return;
        }
        else if(data_Array_Origin == null){
            logger.log(Level.WARNING,"尚未设置原始数组");
            return;
        }
        this.data_Index_Start_To_Print = data_Index_Start_To_Print;
    }

    //显示数据点的横向间距
    private int space_Between_Points = 5;

    //允许显示的数据长度
    private int print_Data_Length=0;{
        //预置允许显示的数据长度为窗口宽度/显示数据点的横向间距
        try {
            int window_width = Integer.parseInt(Property_Manager.read_Property("WINDOW_WIDTH"));
            print_Data_Length = window_width/space_Between_Points;
        } catch (IOException e) {
            logger.log(Level.SEVERE,"配置文件加载出错",e);
        }
    }

    //绘制缩放比例
    private double print_Scale = 1.0;
    public void set_Print_Scale(int print_Scale) {
        this.print_Scale = print_Scale;
    }

    /**
     * 重新绘制曲线
     * @param data_Index_Start_To_Print 新的绘制起始点
     * @param reduce_Scale  新的绘制缩放比例
     * @param canvas 需要重绘的画布
     */
    public void repaint(int data_Index_Start_To_Print, double reduce_Scale, Canvas canvas){

        //更新绘制起始点与缩放比例
        this.data_Index_Start_To_Print = data_Index_Start_To_Print;
        this.print_Scale = reduce_Scale;
        //生成数组的显示域
        int[] print_Frame = {data_Index_Start_To_Print, data_Index_Start_To_Print + print_Data_Length};

        //计算重绘的生成数组
        logger.log(Level.INFO,"计算重绘的生成数组");
        form_Data_Array_Generate();

//        MMMMM
        logger.log(Level.INFO,"重绘图表");

    }

    /**
     * 计算新的绘制数组
     */
    private void caculate_Data_Print(){

        //初始化生成数组
        form_Data_Array_Generate();


//        MMMMM

    }

    /**
     * 通过原始数据和显示设置计算得到生成数组
     */
    private void form_Data_Array_Generate() {

        //通过原始数据计算生成数组原型
        for(int i = 0; i < data_Generate_Length; i++){
            if(data_Index_Start_To_Print+i < data_Array_Origin.length){
                data_Array_Generate[i] = data_Array_Origin[data_Index_Start_To_Print+i];
            }
            else{
                data_Array_Generate[i] = 0;
            }
        }

        //根据缩放比例缩放生成数组原型
        //初始化变形器
        Data_Array_Transformer transformer = new Data_Array_Magnify();
        data_Array_Generate = transformer.data_Array_Trans(data_Array_Generate, (int)(print_Scale*10));
        transformer = new Data_Array_Shrink();
        data_Array_Generate = transformer.data_Array_Trans(data_Array_Generate, 10);

        logger.log(Level.INFO,"已重新计算绘制数组");

    }

}

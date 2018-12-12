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

    //显示数据点的横向间距
    private int space_Between_Points = 5;

    //允许显示的数据长度
    private int print_Data_Length=0;
    //预置允许显示的数据长度为窗口宽度/显示数据点的横向间距
    {
        try {
            int window_width = Integer.parseInt(Property_Manager.read_Property("WINDOW_WIDTH"));
            print_Data_Length = window_width/space_Between_Points;
        } catch (IOException e) {
            logger.log(Level.SEVERE,"配置文件加载出错",e);
        }
    }

    //显示数据点的数组
    private int[] data_Print = new int[print_Data_Length];

    //全部数据点的数组
    private int[] data = null;
    public void set_Data(int[] data) {
        int data_Length = data.length;
        this.data = new int[data_Length];
        for(int i=0; i<data_Length; i++){
            this.data[i] = data[i];
        }
    }


}

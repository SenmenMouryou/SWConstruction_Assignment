package My_Chart_Main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SENMEN
 * 曲线绘制面板类
 */

public class Chart_Panel extends JPanel {

    //日志类
    private Logger logger = Logger.getLogger(Chart_Panel.class.getName());

    //绘制点位置数组
    private int[] data = null;

    //绘制间距
    private int space_Between_Points; {
        try {
            space_Between_Points = Integer.parseInt(Property_Manager.read_Property("SPACE_BETWEEN_POINTS"));
        } catch (IOException e) {
            logger.log(Level.SEVERE,"配置文件读取失败");
        }
    }

    //纵向缩放比例
    private final int scale_Y = 2;

    /**
     * 图表绘制面板的构造器
     * @param data 需要绘制的生成数组
     */
    public Chart_Panel(int[] data){
        this.data = data;
        logger.log(Level.INFO,"图表绘制面板已构造");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for(int i = 0; i < data.length-1; i++){
            g.drawLine(i*space_Between_Points, data[i]/scale_Y + Byte.MAX_VALUE,
                    (i+1)*space_Between_Points, data[i+1]/scale_Y + Byte.MAX_VALUE);
        }

    }
}

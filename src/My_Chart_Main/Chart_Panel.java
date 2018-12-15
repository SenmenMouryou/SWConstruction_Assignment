package My_Chart_Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SENMEN
 * 曲线绘制面板类
 */

public class Chart_Panel extends JPanel{

    //日志类
    private Logger logger = Logger.getLogger(Chart_Panel.class.getName());

    //绘制点位置数组
    private int[] data = null;

    //绘制间距
    private int space_Between_Points = 0;

    //纵向缩放比例
    private double scale_Y = 2;

    //所属图表
    private Chart chart = null;
    public Chart get_Chart() {
        return chart;
    }

    //此图表是否被激活
    private boolean is_Active = false;

    /**
     * 图表绘制面板的构造器
     * @param data 需要绘制的生成数组
     */
    public Chart_Panel(int[] data, Chart chart){
        this.data = data;
        this.chart = chart;
        this.space_Between_Points = chart.get_Space_Between_Points();
        logger.log(Level.INFO,"图表绘制面板已构造");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for(int i = 0; i < data.length-1; i++){
            g.drawLine(i*space_Between_Points, (int)((data[i] + Byte.MAX_VALUE)/scale_Y),
                    (i+1)*space_Between_Points, (int)((data[i+1] + Byte.MAX_VALUE)/scale_Y) );
        }

    }

}

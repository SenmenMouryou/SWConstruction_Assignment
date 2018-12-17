package My_Chart_Main;

import javax.swing.*;
import java.awt.*;
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
    private double space_Y = 2;
    public void set_Space_Y(double space_Y) {
        this.space_Y = space_Y;
    }

    //所属图表
    private Chart chart = null;
    public Chart get_Chart() {
        return chart;
    }

    /**
     * 图表绘制面板的构造器
     * @param data 需要绘制的生成数组
     */
    public Chart_Panel(int[] data, Chart chart){
        setBackground(Color.WHITE);
        this.data = data;
        this.chart = chart;
        this.space_Between_Points = chart.get_Space_Between_Points();
        set_Space_Y(chart.get_Space_Y());
        logger.log(Level.INFO,"图表绘制面板已构造");
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);

        int halfHeight = (int)(0.5*getHeight());

        //边框
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(0, 0, getWidth(), getHeight());
        //垂直网格线
        for(int i = 0; i < getWidth(); i += 10*space_Between_Points){
            g.drawLine(i, 0, i, getHeight());
        }
        //水平网格线
        g.setFont( new Font("黑体",Font.PLAIN, 10));
        for(int i = 0; i < halfHeight; i += 20){
            g.drawLine(0, halfHeight+i, getWidth(), halfHeight+i);
            g.drawString(i+"", 0, getHeight()/2-i);
            g.drawLine(0, halfHeight-i, getWidth(), halfHeight-i);
            g.drawString(-i+"", 0, getHeight()/2+i);
        }
        //水平中线
        g.setColor(Color.BLUE);
        g.drawLine(0, halfHeight, getWidth(), halfHeight);

        g.setColor(Color.RED);
        for(int i = 0; i < data.length-1; i++){
            int x1 = i*space_Between_Points;
            int x2 = (i+1)*space_Between_Points;
            int y1 = (int)((-1)*((data[i])/ space_Y) + halfHeight);
            int y2 = (int)((-1)*((data[i+1])/ space_Y) + halfHeight);

            g.drawLine(x1, y1, x2, y2);

        }
    }

}

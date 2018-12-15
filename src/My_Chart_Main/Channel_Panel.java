package My_Chart_Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SENMEN
 * 一个通道的面板，包含图表面板和其对应的操作控件
 */

public class Channel_Panel extends JPanel implements MouseListener, MouseWheelListener{

    //日志类
    private Logger logger = Logger.getLogger(Channel_Panel.class.getName());

    //通道ID
    private int channel_ID = 0;
    public int get_Channel_ID() {
        return channel_ID;
    }

    //图表面板
    private Chart_Panel chart_Panel = null;
    public void set_Chart_Panel(Chart_Panel chart_Panel){
        this.chart_Panel = chart_Panel;
        this.add(this.chart_Panel, BorderLayout.CENTER);
    }

    //鼠标激活指示
    private boolean is_Active = false;

    /**
     * 构造器
     */
    public Channel_Panel(int channel_ID){
        this.channel_ID = channel_ID;
        this.setLayout(new BorderLayout(2,2));
        this.addMouseListener(this);
        logger.log(Level.INFO,"通道面板已构造");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //绘制图表
        if(this.chart_Panel != null){
            this.chart_Panel.paint(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        is_Active = true;
        logger.log(Level.INFO,"通道"+this.get_Channel_ID()+"已激活");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        is_Active = false;
        logger.log(Level.INFO,"通道"+this.get_Channel_ID()+"未激活");
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(is_Active){
        //滚轮向前
        if(e.getWheelRotation() == 1){
            //取得容器面板
            Container parent = this.getParent();
            //取得当前绘制起始点
            int pressent_Start_Index = this.chart.get_Generate_Index_Start_Paint();
            parent.remove(this);
            parent.add(this.chart.repaint_Chart(pressent_Start_Index+1),
                    BorderLayout.CENTER);
            parent.repaint();
        }
        //滚轮向后
        else if(e.getWheelRotation() == -1){
            //取得容器面板
            Container parent = this.getParent();
            //取得当前绘制起始点
            int pressent_Start_Index = this.chart.get_Generate_Index_Start_Paint();
            parent.remove(this);
            parent.add(this.chart.repaint_Chart(pressent_Start_Index-1),
                    BorderLayout.CENTER);
            parent.repaint();

        }

        }
    }
}

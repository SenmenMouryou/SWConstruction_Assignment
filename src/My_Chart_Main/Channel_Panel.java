package My_Chart_Main;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SENMEN
 * 一个通道的面板，包含图表面板和其对应的操作控件
 */

public class Channel_Panel extends JPanel {

    //日志类
    private Logger logger = Logger.getLogger(Channel_Panel.class.getName());

    //图表面板
    private Chart_Panel chart_Panel = null;
    public void set_Chart_Panel(Chart_Panel chart_Panel){
        this.chart_Panel = chart_Panel;
    }

    /**
     * 构造器
     */
    public Channel_Panel(){
        this.setLayout(new BorderLayout(2,2));
        logger.log(Level.INFO,"通道面板已构造");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //绘制图表
        if(this.chart_Panel != null){
            MMMM
            this.add(this.chart_Panel,"");
            this.chart_Panel.paint(g);
        }
    }
}

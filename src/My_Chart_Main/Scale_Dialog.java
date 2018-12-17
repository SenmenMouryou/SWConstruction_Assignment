package My_Chart_Main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Scale_Dialog extends JDialog {
    //日志类
    private Logger logger = Logger.getLogger(Scale_Dialog.class.getName());

    //缩放值最值
    private double MAX_SCALE = 0;
    private double MIN_SCALE = 0;
    {
        try {
            MAX_SCALE = Double.parseDouble(Property_Manager.read_Property("MAX_PRINT_SCALE"));
            MIN_SCALE = Double.parseDouble(Property_Manager.read_Property("MIN_PRINT_SCALE"));
        } catch (IOException e) {
            logger.log(Level.SEVERE,"配置文件读取失败");
        }
    }
    //缩放值
    private double scale = 1.0;
    public double get_Scale() {
        return scale;
    }
    public void set_Scale(double scale) {
        if(scale >= MIN_SCALE && scale <= MAX_SCALE){
            this.scale = scale;
        }
        else {
            logger.log(Level.WARNING,"缩放值未修改：超出范围");
        }
    }

    public Scale_Dialog(Data_Driver dataDriver){

        this.setTitle("曲线压缩设置");
        this.getContentPane().setLayout(new BorderLayout());

        //数字调节控件
        SpinnerModel model = new SpinnerNumberModel(scale, MIN_SCALE, MAX_SCALE, 0.1);
        JSpinner spinner = new JSpinner(model);
        this.add(spinner, BorderLayout.CENTER);

        JPanel button_Panel = new JPanel();
        this.add(button_Panel, BorderLayout.SOUTH);
        //确定按钮
        JButton button_OK = new JButton("确定");
        button_OK.addActionListener(event -> {
            scale = (Double) spinner.getValue();
            dataDriver.modify_Scale(scale);

            setVisible(false);
        });
        button_Panel.add(button_OK, BorderLayout.SOUTH);
        //取消按钮
        JButton button_Cancel = new JButton("取消");
        button_Cancel.addActionListener(event -> {
            setVisible(false);
        });
        button_Panel.add(button_Cancel, BorderLayout.SOUTH);

        pack();

        //初始位置居中
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int loc_X = (int)(toolkit.getScreenSize().getWidth()-this.getWidth())/2;
        int loc_Y = (int)(toolkit.getScreenSize().getHeight()-this.getHeight())/2;
        this.setLocation(loc_X, loc_Y);

    }
}

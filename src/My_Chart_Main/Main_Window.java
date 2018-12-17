package My_Chart_Main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SENMEN
 * 程序的主要窗口
 */
public class Main_Window extends JFrame {
    //日志类
    private Logger logger = Logger.getLogger(Main_Window.class.getName());

    //窗口的上层驱动器
    private Data_Driver dataDriver = null;

    //文件名
    private String filename = null;
    public String getFilename() {
        return filename;
    }

    //图表显示通道面板数组
    private int CHANNEL_PANEL_ARRAY_LENGTH = 0;{
        try {
            CHANNEL_PANEL_ARRAY_LENGTH = Integer.parseInt(Property_Manager.read_Property("CHANNEL_NUM"));
        } catch (IOException e) {
            logger.log(Level.SEVERE,"配置文件读取失败",e);
        }
    }
    private Channel_Panel[] channel_Panel_Array = new Channel_Panel[CHANNEL_PANEL_ARRAY_LENGTH];
    public Channel_Panel[] get_Channel_Panel_Array() {
        return channel_Panel_Array;
    }

    /***************************成员对话框*******************************/
    //缩放比例调节
    private Scale_Dialog Scale_Dialog = null;
    public Scale_Dialog get_Scale_Dialog() {
        return Scale_Dialog;
    }
    public void set_Scale_Dialog(Scale_Dialog scale_Dialog) {
        Scale_Dialog = scale_Dialog;
    }

    public Main_Window(Data_Driver dataDriver) throws HeadlessException {

        //获取上层驱动器
        this.dataDriver = dataDriver;

        this.setTitle("My My_Chart_Main.Chart");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //设置窗口大小
        try {
            int window_Width = Integer.parseInt(Property_Manager.read_Property("WINDOW_WIDTH"));
            int window_Height = Integer.parseInt(Property_Manager.read_Property("WINDOW_HEIGHT"));
            this.setSize(window_Width, window_Height);
        }catch (IOException e){
            logger.log(Level.SEVERE,"配置文件加载失败！");
            this.setSize(640,480);//默认窗口大小
        }
        //固定窗口大小
        this.setResizable(false);
        //窗口居中
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int loc_X = (int)(toolkit.getScreenSize().getWidth()-this.getWidth())/2;
        int loc_Y = (int)(toolkit.getScreenSize().getHeight()-this.getHeight())/2;
        this.setLocation(loc_X, loc_Y);

        //添加菜单栏
        Main_Menu_Bar menuBar = new Main_Menu_Bar();
        this.setJMenuBar(menuBar);

        //添加通道面板
        init_Channel_Panels();
        this.getContentPane().setLayout(new GridLayout(CHANNEL_PANEL_ARRAY_LENGTH,1));
        for(int i = 0; i < CHANNEL_PANEL_ARRAY_LENGTH; i++){
            this.getContentPane().add(this.channel_Panel_Array[i]);
        }
        logger.log(Level.INFO,"通道面板已添加至窗口");

        //添加监听器
        Listener listener = new Listener();
        this.addKeyListener(listener);
        this.addMouseWheelListener(listener);

        logger.log(Level.INFO,"窗口已初始化");
    }

    /**
     * 初始化通道面板数组
     */
    private void init_Channel_Panels() {
        for(int i = 0; i < CHANNEL_PANEL_ARRAY_LENGTH; i++){
            channel_Panel_Array[i] = new Channel_Panel(i);
        }
        logger.log(Level.INFO,"通道面板已初始化");
    }

    /**
     * 该窗口的菜单栏
     */
    private class Main_Menu_Bar extends JMenuBar{
        /**
         * 构造器
         */
        public Main_Menu_Bar(){
            /***************************“文件”菜单*******************************/
            JMenu menu_File = new JMenu("文件(F)");
            menu_File.setMnemonic('F');
            this.add(menu_File);

            //添加“文件”菜单的各菜单项
            //打开
            JMenuItem item_Open = new JMenuItem("打开(O)");
            item_Open.setMnemonic('O');
            item_Open.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    JFileChooser jFileChooser = new
                            JFileChooser("C:");
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(".bin","bin");
                    jFileChooser.setFileFilter(filter);
                    int return_Val = jFileChooser.showOpenDialog(null);
                    if(return_Val == jFileChooser.APPROVE_OPTION){
                        //取得文件路径
                        filename = jFileChooser.getSelectedFile().getAbsolutePath();
                        if(check_File_Length_Legal()){
                            //由将打开的文件构造读取器，上传给驱动器，使驱动器获取数据
                            dataDriver.open_File(new Data_Reader(filename));
                        }
                        else{
                            JOptionPane.showMessageDialog(Main_Window.this.getParent(),"读取的文件过大",
                                    "警告",JOptionPane.WARNING_MESSAGE);
                            filename = null;
                        }
                    }
                }
            });
            menu_File.add(item_Open);

            //重置
            JMenuItem item_Reset = new JMenuItem("重置(R)");
            item_Reset.setMnemonic('R');
            item_Reset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(filename != null){
                        dataDriver.open_File(new Data_Reader(filename));
                    }
                    else{
                        JOptionPane.showMessageDialog(Main_Window.this.getParent(),"尚未打开任何文件！",
                                "警告",JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            menu_File.add(item_Reset);

            //分割线
            menu_File.addSeparator();
            //退出
            JMenuItem item_Exit = new JMenuItem("退出");
            item_Exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            menu_File.add(item_Exit);

            /***************************“编辑”菜单*******************************/
            JMenu menu_Edit = new JMenu("编辑(E)");
            menu_File.setMnemonic('E');
            this.add(menu_Edit);

            //添加“编辑”菜单的各菜单项
            //缩放
            JMenuItem item_Scale = new JMenuItem("缩放(S)");
            item_Scale.setMnemonic('S');
            item_Scale.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(filename != null){
                        if(get_Scale_Dialog()!=null){
                        //调出缩放编辑对话框
                            get_Scale_Dialog().setVisible(true);
                        }
                        else {
                            set_Scale_Dialog(new Scale_Dialog(dataDriver));
                            get_Scale_Dialog().setVisible(true);
                        }
                    }
                    else{

                        JOptionPane.showMessageDialog(Main_Window.this.getParent(),"尚未打开任何文件！",
                                "警告",JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            menu_Edit.add(item_Scale);

        }

        /**
         * 检查文件名对应文件是否过长
         * @return true:长度合法 false:长度不合法
         */
        private boolean check_File_Length_Legal(){
            File file = new File(filename);
            long file_Length = file.length();

            //检查文件长度
            long illegal_Length = 0;
            try {
                illegal_Length = Long.parseLong(Property_Manager.read_Property("MAX_FILE_SIZE"));
            } catch (IOException e) {
                logger.log(Level.SEVERE,"配置文件读取失败",e);
                return false;
            }

            //文件过长
            if(file_Length > illegal_Length){
                return false;
            }

            return true;
        }
    }

    /**
     * Main_Window监听器私用类
     */
    private class Listener implements KeyListener, MouseWheelListener{

        private boolean key_Ctrl_Pressed = false;
        private boolean key_Space_Pressed = false;
        private boolean key_Shift_Pressed = false;

        /**
         * 鼠标滚轮事件:平移图表
         * @param channel_Panel 执行事件的通道面板
         * @param e             滚轮事件
         */
        private void channel_Mouse_Wheel_Event(Channel_Panel channel_Panel, MouseWheelEvent e){
            //取得图表类
            Chart chart = channel_Panel.get_Chart_Panel().get_Chart();

            //滚轮上滚/下滚
            int delta = 0;
            if(!key_Space_Pressed){
                delta = 9*e.getWheelRotation()/chart.get_Space_Between_Points();
            }
            else if(key_Space_Pressed){
                delta = 30*e.getWheelRotation()/chart.get_Space_Between_Points();
            }
            //更新图表面板
            chart.set_Generate_Index_Start_Paint(chart.get_Generate_Index_Start_Paint() + delta);
            channel_Panel.renew_Chart_Panel_And_Repaint(chart.repaint_Chart());

            logger.log(Level.INFO,"图表面板已更新");
        }

        /**
         * 按住Ctrl时鼠标滚轮事件：横向拉伸/缩小图表
         * @param channel_Panel 执行事件的通道面板
         * @param e             滚轮事件
         */
        private void channel_Mouse_Wheel_Event_Ctrl(Channel_Panel channel_Panel, MouseWheelEvent e){
            //滚轮上滚/下滚的变化值
            int delta = e.getWheelRotation();
            //取得图表类
            Chart chart = channel_Panel.get_Chart_Panel().get_Chart();
            //更新图表面板
            chart.set_Space_Between_Points(chart.get_Space_Between_Points() + delta);
            channel_Panel.renew_Chart_Panel_And_Repaint(chart.repaint_Chart());

            logger.log(Level.INFO,"图表面板已更新");
        }

        /**
         * 按住Alt时鼠标滚轮事件：纵向向拉伸/缩小图表
         * @param channel_Panel 执行事件的通道面板
         * @param e             滚轮事件
         */
        private void channel_Mouse_Wheel_Event_Shift(Channel_Panel channel_Panel, MouseWheelEvent e){
            //滚轮上滚/下滚的变化值
            int delta = e.getWheelRotation();
            //取得图表类
            Chart chart = channel_Panel.get_Chart_Panel().get_Chart();
            //更新图表面板
            chart.set_Space_Y(chart.get_Space_Y() + delta);
            channel_Panel.renew_Chart_Panel_And_Repaint(chart.repaint_Chart());

            System.out.print(":"+chart.get_Space_Y());
            logger.log(Level.INFO,"图表面板已更新");
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

            if(e.getKeyCode()==KeyEvent.VK_CONTROL){
                key_Ctrl_Pressed = true;
            }

            if(e.getKeyCode()==KeyEvent.VK_SPACE){
                key_Space_Pressed = true;
            }

            if(e.getKeyCode()==KeyEvent.VK_SHIFT){
                key_Shift_Pressed = true;
                System.out.println(true);
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {

            if(e.getKeyCode()==KeyEvent.VK_CONTROL){
                key_Ctrl_Pressed = false;
            }

            if(e.getKeyCode()==KeyEvent.VK_SPACE){
                key_Space_Pressed = false;
            }

            if(e.getKeyCode()==KeyEvent.VK_SHIFT){
                key_Shift_Pressed = false;
                System.out.println(false);
            }

        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if(key_Ctrl_Pressed) {
                for (int i = 0; i < channel_Panel_Array.length; i++) {
                    if (channel_Panel_Array[i].get_Chart_Panel() != null) {
                        channel_Mouse_Wheel_Event_Ctrl(channel_Panel_Array[i], e);
                    }
                }
            }
            else if(key_Shift_Pressed){
                for (int i = 0; i < channel_Panel_Array.length; i++) {
                    if (channel_Panel_Array[i].get_Chart_Panel() != null) {
                        channel_Mouse_Wheel_Event_Shift(channel_Panel_Array[i], e);
                    }
                }
            }
            else {
                for (int i = 0; i < channel_Panel_Array.length; i++) {
                    if (channel_Panel_Array[i].get_Chart_Panel() != null) {
                        channel_Mouse_Wheel_Event(channel_Panel_Array[i], e);
                    }
                }
            }
        }

    }//private class Listener

}//public class Main_Window

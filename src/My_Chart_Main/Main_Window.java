package My_Chart_Main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private Driver driver = null;

    //文件名
    private String filename = "";
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

    public Main_Window(Driver driver) throws HeadlessException {

        //获取上层驱动器
        this.driver = driver;

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

        logger.log(Level.INFO,"窗口已初始化");
    }

    /**
     * 初始化通道面板数组
     */
    private void init_Channel_Panels() {
        for(int i = 0; i < CHANNEL_PANEL_ARRAY_LENGTH; i++){
            channel_Panel_Array[i] = new Channel_Panel();
        }
        logger.log(Level.INFO,"通道面板已初始化");
    }

    /**
     * 该窗口的菜单栏
     */
    class Main_Menu_Bar extends JMenuBar{
        /**
         * 构造器
         */
        public Main_Menu_Bar(){
            //“文件”菜单
            JMenu menu_File = new JMenu("文件(F)");
            menu_File.setMnemonic('F');
            this.add(menu_File);

            //添加“文件”菜单的各菜单项
            //打开
            JMenuItem item_Open = new JMenuItem("打开");
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
                        if(check_File_Length_Legal()){

                            //取得文件路径
                            filename = jFileChooser.getSelectedFile().getAbsolutePath();

                            //由将打开的文件构造读取器，上传给驱动器，使驱动器获取数据
                            driver.open_File(new Data_Reader(filename));

                        }
                        else{
                            JOptionPane.showMessageDialog(Main_Window.this.getParent(),"读取的文件过大",
                                    "警告",JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            });
            menu_File.add(item_Open);
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
}

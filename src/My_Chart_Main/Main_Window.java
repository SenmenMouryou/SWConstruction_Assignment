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
 * 程序的主要窗口
 */

public class Main_Window extends JFrame {
    //日志类
    private Logger logger = Logger.getLogger(Main_Window.class.getName());

    //文件名
    private String filename = "";
    public String getFilename() {
        return filename;
    }

    public Main_Window() throws HeadlessException {
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

        test_Canvas();

        //显示窗口
//      this.pack();
        this.setVisible(true);
    }

    private void test_Canvas() {
        int[] data = new int[100];
        for(int i=0;i<data.length;i++){
            if(i%2==0){
                data[i] = i;
            }
            else {
                data[i] = -1*i;
            }
        }

        Chart_Panel panel1 = new Chart_Panel(data);
        Chart_Panel panel2 = new Chart_Panel(data);
        Chart_Panel panel3 = new Chart_Panel(data);
        Chart_Panel panel4 = new Chart_Panel(data);

        this.getContentPane().setLayout(new GridLayout(4,1));
        this.getContentPane().add(panel1);
        this.getContentPane().add(panel2);
        this.getContentPane().add(panel3);
        this.getContentPane().add(panel4);


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
                            filename = jFileChooser.getSelectedFile().getAbsolutePath();
                            //调用显示图表功能
//                        MARKMARKMARKMARKMARK
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

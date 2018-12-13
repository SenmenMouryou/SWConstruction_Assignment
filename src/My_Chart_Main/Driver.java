package My_Chart_Main;

/**
 * @author SENMEN
 * 曲线生成的驱动类
 * 构造时读入读取器，调用数据处理器生成衍生数组
 */
public class Driver {

    //原始数据
    private byte[] source_Data = null;
    //衍生数据
    //一阶微分
    private byte[] diff_Data = null;
    //二阶微分
    private byte[] sec_Diff_Data = null;
    //一阶积分
    private byte[] intgr_Data = null;


    /**
     * 构造器
     * 取得原始数据
     * @param data_reader 读取器
     */
    public Driver(Data_Reader data_reader){

        //获取原始数据
        data_reader.run_Reader();
        source_Data = data_reader.getData();

        //获取衍生数据
        //获取一阶微分
        //获取二阶微分
        //获取一阶积分
    }
}

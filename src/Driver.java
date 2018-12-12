/**
 * @author SENMEN
 * 曲线生成的驱动类
 * 构造时读入读取器，调用数据处理器生成衍生数组
 */
public class Driver {

    //原始数据
    private byte[] source_Data = null;
    //一阶微分
    //二阶微分
    //一阶积分


    /**
     * 构造器
     * 取得原始数据
     * @param data_reader 读取器
     */
    public Driver(Data_Reader data_reader){
        data_reader.run_Reader();
        source_Data = data_reader.getData();
    }
}

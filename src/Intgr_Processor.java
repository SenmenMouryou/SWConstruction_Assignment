/**
 * @author SENMEN
 * 计算一阶积分的数据处理类
 */
public class Intgr_Processor implements Data_Processor{
    @Override
    public byte[] generate_Data(byte[] source_Data) {
        int data_Length = source_Data.length;
        byte[] intgr_Data = new byte[data_Length];

        for(int i=0; i<data_Length-1; i++){
            intgr_Data[i] = (byte)((source_Data[i]+source_Data[i+1])/2);
        }
        //末位处理
        intgr_Data[data_Length-1] = intgr_Data[data_Length-2];

        return intgr_Data;
    }
}

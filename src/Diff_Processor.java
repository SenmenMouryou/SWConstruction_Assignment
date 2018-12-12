/**
 * @author SENMEN
 * 计算一阶微分的数据处理类
 */
public class Diff_Processor implements Data_Processor {
    @Override
    public byte[] generate_Data(byte[] source_Data) {
        int data_Length = source_Data.length;
        byte[] diff_Data = new byte[data_Length];

        for(int i=0; i<data_Length-1; i++){
            diff_Data[i] = (byte)((source_Data[i]-source_Data[i+1])/2);
        }
        //末位处理
        diff_Data[data_Length-1] = diff_Data[data_Length-2];

        return diff_Data;

    }
}

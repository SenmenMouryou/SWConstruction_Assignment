/**
 * @author SENMEN
 * 计算二阶微分的数据处理类
 */
public class Sec_Diff_Processor implements Data_Processor{

    @Override
    public byte[] generate_Data(byte[] source_Data) {
        int data_Length = source_Data.length;
        byte[] sec_diff = new byte[data_Length];

        Diff_Processor diff_processor = new Diff_Processor();
        sec_diff = diff_processor.generate_Data(source_Data);

        return sec_diff;
    }
}

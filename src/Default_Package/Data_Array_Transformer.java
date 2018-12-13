package Default_Package;

/**
 * @author SENMEN
 * 数据数组的变形器
 */
public interface Data_Array_Transformer {
    /**
     * 将原始数组安整数比变形为目标数组
     * @param source_Data   原始数组
     * @param rate          变形比例
     * @return              目标数组
     */
    public int[] data_Array_Trans(int[] source_Data, int rate);
}

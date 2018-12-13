package My_Chart_Main;

/**
 * @author SENMEN
 * 数据数组的缩小器
 */
public class Data_Array_Shrink implements Data_Array_Transformer{

    /**
     * 将原始数组安整数比缩小为目标数组
     * @param source_Data   原始数组
     * @param rate          缩小比例
     * @return              目标数组
     */
    @Override
    public int[] data_Array_Trans(int[] source_Data, int rate) {
        int dest_Data_Size = (source_Data.length-1)/rate+1;
        int[] dest_Data = new int[dest_Data_Size];

        for(int i = 0; i < dest_Data_Size; i++){
            //取原始数据的首个点作为目标数组的点
            dest_Data[i] = source_Data[i*rate];
        }

        return dest_Data;

    }
}

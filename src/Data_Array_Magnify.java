/**
 * @author SENMEN
 * 数据数组的放大器
 */
public class Data_Array_Magnify implements Data_Array_Transformer{
    /**
     * 将原始数组安整数比放大为目标数组
     * @param source_Data   原始数组
     * @param rate          放大比例
     * @return              目标数组
     */
    @Override
    public int[] data_Array_Trans(int[] source_Data, int rate) {

        int dest_Data_Size = source_Data.length*(rate-1)+1;
        int[] dest_Data = new int[dest_Data_Size];

        for(int i=0; i<source_Data.length-1; i++){
            for(int j=0; j<rate; j++){
                //线性补完原始数据间隔间的点
                dest_Data[i*rate+j] = source_Data[i]+j*(source_Data[i+1]-source_Data[i])/rate;
            }
        }
        //末尾点
        dest_Data[dest_Data_Size-1] = source_Data[source_Data.length-1];

        return dest_Data;
    }
}

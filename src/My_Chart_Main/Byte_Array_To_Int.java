package My_Chart_Main;

/**
 * @author SENMEN
 * 将byte类型数组转化为整型数组的工具类
 */
public class Byte_Array_To_Int {

    /**
     * 将输入的byte数组输出位整型数组
     * @param byte_Array byte类型数组
     * @return 等值的整型数组
     */
    public static int[] byte_Array_To_Int(byte[] byte_Array){
        int array_Length = byte_Array.length;
        int[] int_Array = new int[array_Length];

        for(int i=0; i<array_Length; i++){
            int_Array[i] = (int)byte_Array[i];
        }

        return int_Array;
    }
}

/**
 * @author SENMEN
 * 数据处理器接口
 * 提供将原始数据加工为衍生数据的方法
 */
public interface Data_Processor {

    public abstract int[] generate_Data(int[] source_Data);

}

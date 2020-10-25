package UMC.Data;

import java.io.OutputStream;
import java.io.Writer;

public interface IJSON {


    /** 序列化
     * @param writer
     */
    void write(Writer writer);
    /** 反序列化
     * @param key 属性名
     * @param value 属性值
     */
    void read(String key, Object value);
}

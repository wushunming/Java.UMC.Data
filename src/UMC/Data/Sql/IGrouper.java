package UMC.Data.Sql;

import java.util.List;
import java.util.Map;

public interface IGrouper<T> extends IScript {


    /** 分组的单行
     * @return
     */
    T single();

    /** 查询分组
     * @return
     */
    List<Map> query();

    /** 查询分组
     * @param reader
     */
    void query(IDataReader<T> reader);

    /** 排序
     * @return
     */
    IGroupOrder order();

    /**求个数
     * @param asName as后的字段
     * @return
     */
    IGrouper<T> count(String asName);

    /**  求记录数,对应的字段为"G"+(i+1)，i为统计次数,例如：e.groupBy("field'}).count(),则Count的列名为"G1"
     * @param field 实体
     * @return
     */
    IGrouper<T> count(T field);

    /**  求记录数,对应的字段为"G"+(i+1)，i为统计次数,例如：e.groupBy("field'}).count(),则Count的列名为"G1"
     * @return
     */
    IGrouper<T> count();

    /** 求和,对应的字段为"G"+(i+1)，i为统计次数,例如：e.groupBy("field'}).sum(),则sum的列名为"G1"
     * @param field 字段
     * @return
     */
    IGrouper<T> sum(String field);

    /** 求和,对应的字段为"G"+(i+1)，i为统计次数,例如：e.groupBy("field'}).sum(),则sum的列名为"G1"
     * @param field 字段
     * @param asName as转变的名称
     * @return
     */
    IGrouper<T> sum(String field, String asName);

    /**  求平均,对应的字段为"G"+(i+1)，i为统计次数,例如：e.groupBy("field'}).avg(field),则avg的列名为"G1"
     * @param field
     * @return
     */
    IGrouper<T> avg(String field);
    IGrouper<T> avg(String field, String asName);

    /** 求最大值,对应的字段为"G"+(i+1)，i为统计次数,例如：e.groupBy("field'}).max(field),则max的列名为"G1"
     * @param field
     * @return
     */
    IGrouper<T> max(String field);
    IGrouper<T> max(String field, String asName);
    /** 求最小值,,对应的字段为"G"+(i+1)，i为统计次数,例如：e.groupBy("field'}).min(field),则min的列名为"G1"
     * @param field 字段
     * @return
     */
    IGrouper<T> min(String field);
    IGrouper<T> min(String field, String asName);

    /** 求和,对应的字段值为实体非空字段
     * @param field 字段
     * @return
     */
    IGrouper<T> sum(T field);
    /** 求平均,对应的字段值为实体非空字段
     * @param field 字段
     * @return
     */
    IGrouper<T> avg(T field);

    /** 求最大值,对应的字段值为实体非空字段
     * @param field 字段
     * @return
     */
    IGrouper<T> max(T field);

    /** 求最小值,对应的字段值为实体非空字段
     * @param field 字段
     * @return
     */
    IGrouper<T> min(T field);
}


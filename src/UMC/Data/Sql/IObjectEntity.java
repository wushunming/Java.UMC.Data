package UMC.Data.Sql;

import javax.swing.*;
import java.util.Map;
import java.util.function.Predicate;

public interface IObjectEntity<T> extends IScript {


    /**创建查询脚本
     * @param field
     * @return
     */
    Script script(T field);


    /**创建查询脚本
     * @param field
     * @return
     */
    Script script(String field);


    /**分组查询
     * @param fields
     * @return
     */
    IGrouper<T> groupBy(String... fields);


    /**分组查询
     * @param field
     * @return
     */
    IGrouper<T> groupBy(T field);


    /**插入
     * @param items
     * @return
     */
    int insert(T... items);

    /** 更新实体，如果字段“fields”的长度为0，则采用非空属性值规则更新对应的字段，否则更新指定的的字段
     * @param item  实体
     * @param fields 更新的字段
     * @return
     */
    int update(T item, String... fields);


    /**  更新实体，如果字段“fields”的长度为0，则采用非空属性值规则更新对应的字段，否则更新指定的的字段
     * @param format 更新值格式：其中{0}表示字段，{1}表示参数值
     * @param item 实体
     * @param fields 更新的字段
     * @return 返回受影响的行数
     */
    int update(String format, T item, String... fields);

    /**采用字典更新实体
     * @param fieldValues 字段字典对
     * @return
     */
    int update(Map fieldValues);

    /**采用字典更新实体
     * @param format 更新值格式：其中{0}表示字段，{1}表示参数值
     * @param fieldValues 字段字典对
     * @return
     */
    int update(String format, Map fieldValues);


    /** 删除
     * @return
     */
    int delete();

    /** 排序
     * @return
     */
    IOrder<T> order();


    /** 查询一个字段，如果是“*”，必返回DataRow[]数据
     * @param field 字段实例
     */
    Object[] query(String field);

    /**自定义处理一个字段查询的只读结果集
     * @param field 字段实例
     * @param dr
     */
    void query(String field, IResultReader dr);

    /**查询
     * @param dr
     */
    void query(IDataReader<T> dr);


    /**查询
     * @param field 字段实例
     * @param dr
     */
    void query(T field, IDataReader<T> dr);

    /** 查询实段实例
     * @return
     */
    T[] query();

    /** 查询实段实例
     * @param field 字段实例
     * @return
     */
    T[] query(T field);
    /**查询实体集分页
     * @param start 开始记录
     * @param limit 记录数
     * @param dr
     */
    void query(int start, int limit, IDataReader<T> dr);

    /**查询实体集分页
     * @param start 开始记录
     * @param limit 记录数
     */
    T[] query(int start, int limit);

    /**查询实体集分页
     * @param field 字段实例
     * @param start 开始记录
     * @param limit 记录数
     */
    T[] query(T field, int start, int limit);

    /**查询实体集分页
     * @param field 字段实例
     * @param start 开始记录
     * @param limit 记录数
     * @param dr
     */
    void query(T field, int start, int limit, IDataReader<T> dr);



    /**查询头一个实体
     * @return
     */
    T single();

    /**查询头一个实体
     * @param field 字段实例
     * @return
     */
    T single(T field);


    /**查询一个字段，如果是“*”则返回一行记录的字典对
     * @param field
     * @return
     */
    Object single(String field);

    /**查询一个字段，返回一行记录的字典对
     * @param field
     * @return
     */
    Map single(String... field);


    /**求记录的个数
     * @return
     */
    int count();


    /**求和
     * @param field
     * @return
     */
    Object sum(String field);


    /**求和
     * @param field
     * @return
     */
    T sum(T field);


    /**求平均
     * @param field
     * @return
     */
    Object avg(String field);

    /**求平均
     * @param field
     * @return
     */
    T avg(T field);

    /**求最大值
     * @param field
     * @return
     */
    Object max(String field);

    /**求最大值
     * @param field
     * @return
     */
    T max(T field);

    /**求最小值
     * @param field
     * @return
     */
    Object min(String field);

    /** 求最小值
     * @param field
     * @return
     */
    T min(T field);

    /**查询条件
     * @return
     */
    IWhere<T> where();


    /** 如果where返回值，则运行@true
     * @param where
     * @param True
     * @return
     */
    IObjectEntity<T> iff(Predicate<IObjectEntity<T>> where,  IAction<IObjectEntity<T>> True);



    /**如果where返回值，则运行@true，否则的运行@false
     * @param where
     * @param True
     * @param False
     * @return
     */
    IObjectEntity<T> iff(Predicate<IObjectEntity<T>> where, IAction<IObjectEntity<T>> True, IAction<IObjectEntity<T>> False);

}

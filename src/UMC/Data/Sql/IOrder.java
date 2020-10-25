package UMC.Data.Sql;

public interface IOrder<T> {

    /** desc
     * @param fieldName 排序字段
     * @return
     */
    IOrder<T> desc(String fieldName);

    /** asc
     * @param fieldName 排序字段
     * @return
     */
    IOrder<T> asc(String fieldName);

    /** 清空排序设置
     * @return
     */
    IOrder<T> clear();

    /**采用实体非空正序
     * @param field
     * @return
     */
    IOrder<T> asc(T field);

    /** 采用实体非空正序
     * @param field 实体
     * @return
     */
    IOrder<T> desc(T field);

    /** 关联的查询实体
     * @return
     */
    IObjectEntity<T> entities();
}

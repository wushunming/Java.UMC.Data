package UMC.Data.Sql;

public interface IGroupOrder<T> {
    IGroupOrder<T> desc(String fieldName);

    /** asc
     * @param fieldName fieldName
     * @return
     */
    IGroupOrder<T> asc(String fieldName);
    /**清空排序设置
     * @return
     */
    IGroupOrder<T> clear();
    IGroupOrder<T> asc(T field);
    IGroupOrder<T> desc(T field);
    IGrouper<T> entities();

}

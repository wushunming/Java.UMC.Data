package UMC.Data.Sql;

public interface IOperator<T> {




    /** 实体不等于 &gt;&gt;
     * @param field 非空属性实体
     * @return
     */
    IWhere<T> unEqual(T field);


    /** 实体等于 =
     * @param field 非空属性实体
     * @return
     */
    IWhere<T> equal(T field);


    /** 实体大于 &gt;
     * @param field 非空属性实体
     * @return
     */
    IWhere<T> greater(T field);


    /** 实体小于&lt;
     * @param field 非空属性实体
     * @return
     */
    IWhere<T> smaller(T field);


    /** 实体大于等于 &gt;=
     * @param field 非空属性实体
     * @return
     */
    IWhere<T> greaterEqual(T field);

    /// <summary>
    /// 实体小于等于 &lt;=
    /// </summary>
    /// <param name="field">非空属性实体</param>

    /** 实体小于等于 &lt;=
     * @param field 非空属性实体
     * @return
     */
    IWhere<T> smallerEqual(T field);

    /**不等于&lt;&gt;
     * @param field
     * @param value
     * @return
     */
    IWhere<T> unEqual(String field, Object value);

    /** 等于 =
     * @param field
     * @param value
     * @return
     */
    IWhere<T> equal(String field, Object value);


    /**大于 &gt;
     * @param field
     * @param value
     * @return
     */
    IWhere<T> greater(String field, Object value);



    /**小于&lt;
     * @param field
     * @param value
     * @return
     */
    IWhere<T> smaller(String field, Object value);

    /**大于等于 &gt;=
     * @param field
     * @param value
     * @return
     */
    IWhere<T> greaterEqual(String field, Object value);



    /**小于等于 &lt;=
     * @param field
     * @param value
     * @return
     */
    IWhere<T> smallerEqual(String field, Object value);

    /**不等于
     * @param field
     * @param value
     * @return
     */
    IWhere<T> notLike(String field, String value);



    /**like
     * @param field
     * @param value
     * @return
     */
    IWhere<T> like(String field, String value);

    /**Like 查询
     * @param field
     * @param schar 是否加追加%
     * @return
     */
    IWhere<T> like(T field, boolean schar);

    /**
     * @param field 非空字段的的做做为Like
     * @return
     */
    IWhere<T> like(T field);

    /** in查询条件
     * @param field  字段
     * @param values in的值
     * @return
     */
    IWhere<T> in(String field, Object... values);

    /** in查询条件
     * @param field 只能一个非空字段的值
     * @param values in的值
     * @return
     */
    IWhere<T> in(T field, Object... values);


    /**
     * @param field
     * @param script
     * @return
     */
    IWhere<T> in(String field, Script script);

    /** notIn
     * @param field 字段
     * @param values Not in的值
     * @return
     */
    IWhere<T> notIn(String field, Object... values);

    /** Not in
     * @param field 只能一个非空字段的值
     * @param values Not in的值
     * @return
     */
    IWhere<T> notIn(T field, Object... values);

    /**Not in
     * @param field
     * @param script
     * @return
     */
    IWhere<T> notIn(String field, Script script);

    /**
     * 创建带小括号 SQL WHERE条件，例如 ：(field1=1)
     * @return
     */
    IWhere<T> contains();

}

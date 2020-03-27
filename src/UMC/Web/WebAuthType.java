package UMC.Web;

/**
 * 请求权限验证精确度
 */
public enum WebAuthType {

    /**
     * 所有人能访问
     */
    all,
    /**
     * 登录后可访问
     */
    guest,
    /**
     * 后台人员可访问
     */
    user,
    /**
     * 管理员可访问
     */
    admin,
    /**
     * 检测配置权限
     */
    check,
    /**
     * 必须先后台人员，再检测权限
     */
    userCheck

}

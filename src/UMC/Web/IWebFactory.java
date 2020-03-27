package UMC.Web;


/**
 * UMC 模板指令路由工厂类处理接口
 */
public interface IWebFactory {

    /**
     * 工厂类初始化
     * @param context 处理上下文
     */
    void init(WebContext context);


    /**用获取有效的处理模块
     * @param model 模块名称
     * @return
     */
    WebFlow flowHandler(String model);
}

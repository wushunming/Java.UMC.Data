package UMC.Web;

public abstract class WebActivity extends WebHandler {


    /**
     * 终止流程的活动
     */
    public static WebActivity Empty = new WebActivity() {
        @Override
        public void processActivity(WebRequest request, WebResponse response) {

        }
    };


    /** 活动Id
     * @return
     */
    public String Id() {

        return this.getClass().getName();

    }



    /**活动节点处理方法
     * @param request 请求
     * @param response 响应
     */
    public abstract void processActivity(WebRequest request, WebResponse response);
}

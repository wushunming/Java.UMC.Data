package UMC.Web;

import UMC.Data.Utility;
import UMC.Security.Identity;
import UMC.Security.Membership;

import java.net.URI;
import java.net.URL;
import java.util.Map;

public class WebRequest {
    private WebClient client;
    private boolean isMaster, isWeiXin;

    protected void init(Map header, WebClient client) {
        this.client = client;
        if (!Utility.isEmpty(client.UserAgent)) {
            isWeiXin = client.UserAgent.indexOf("MicroMessenger") > 10;
        }
        isMaster = Identity.current().isInRole(Membership.AdminRole);

        _model = (String) header.get("POS-MODEL");
        _cmd = (String) header.get("POS-COMMAND");
        header.remove("POS-MODEL");
        header.remove("POS-COMMAND");

        WebMeta he = new WebMeta(header);

        this._Headers = he;
        this.Arguments = this._Headers.meta(KEY_HEADER_ARGUMENTS);
        if (this.Arguments == null) {
            this.Arguments = new WebMeta();
        }


        this._items = this.Arguments.meta(KEY_ARGUMENTS_ITEMS);

        if (this._items == null) {
            this._items = new WebMeta();
        }

        this.Arguments.remove(KEY_ARGUMENTS_ITEMS);
    }

    String _model, _cmd;

    /**
     * 请求的模块
     *
     * @return
     */
    public String model() {
        return _model;
    }

    /**
     * 提交的值
     *
     * @return
     */
    public String sendValue() {
        return this._Headers.get(this._cmd);

    }

    /**
     * 提交的值
     *
     * @return
     */
    public WebMeta sendValues() {

        return _Headers.meta(this._model);

    }

    public String userHostAddress() {
        return this.client.UserHostAddress;
    }

    WebMeta Arguments = new WebMeta();

    /**
     * 当前已经得到的对话参数
     *
     * @return
     */
    public WebMeta arguments() {
        return Arguments;
    }

    WebMeta _Headers;

    public WebMeta headers() {
        return _Headers;
    }

    WebMeta _items;

    public WebMeta items() {
        return _items;
    }


    public String cmd() {
        return _cmd;
    }

    public boolean isCashier() {
        return this.client.isCashier;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public boolean isWeiXin() {
        return isWeiXin;
    }

    public String userAgent() {
        return client.UserAgent;
    }

    public boolean isApp() {
        return this.client.isApp;
    }

    public URL uri() {
        return client.Uri;
    }

    public URL referrer() {
        return client.UrlReferrer;
    }

    public final static String KEY_HEADER_ARGUMENTS = "Arguments",
            KEY_ARGUMENTS_ITEMS = "KEY_ARGUMENTS_ITEMS";
}

package UMC.Web.UI;

import UMC.Data.Utility;
import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.UIEventText;
import UMC.Web.WebMeta;

import java.util.LinkedList;
import java.util.List;

public class UITextItems extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "TextItems";
    }


    private WebMeta data;


    private List<Object> items = new LinkedList<>();

    /**
     * 获取项内容的请求配置
     *
     * @param model
     * @param cmd
     */
    public UITextItems(String model, String cmd) {
        this();
        this.data.put("model", model, "cmd", cmd);
    }

    /**
     * 获取项内容的请求配置
     *
     * @param model
     * @param cmd
     * @param value
     */
    public UITextItems(String model, String cmd, String value) {
        this();
        this.data.put("model", model, "cmd", cmd, "search", value);

    }

    /**
     * 获取项内容的请求配置
     *
     * @param model
     * @param cmd
     * @param value
     */
    public UITextItems(String model, String cmd, WebMeta value) {
        this();
        this.data.put("model", model, "cmd", cmd).put("search", value);

    }

    /** 绑定客户端事件
     * @param key
     * @return
     */
    public UITextItems event(String key)
    {
        this.data.put("event", key);
        return this;
    }


    public UITextItems() {
        this.data = new WebMeta().put("data", this.items);
    }


    /**
     * 添加空值提示
     *
     * @param text
     * @return
     */
    public UITextItems msg(String text) {
        data.put("msg", text);
        return this;

    }

    public UITextItems add(UIEventText... items) {
        for (UIEventText i : items) {
            this.items.add(i);
        }
        return this;
    }

    public UITextItems add(UIEventText eventText) {
        items.add(eventText);
        return this;
    }


    public int size() {

        return this.items.size();

    }
}
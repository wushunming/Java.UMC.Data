package UMC.Web;

import UMC.Data.Utility;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;

public class UIClick implements UMC.Data.IJSON {

    public UIClick() {
    }

    public UIClick(String send) {
        this._send = send;
    }

    public UIClick(String... keys) {
        this._send = new WebMeta().put(keys);
    }

    public UIClick(WebMeta send) {
        this._send = send;
    }

    public UIClick send(WebMeta send) {
        this._send = send;
        return this;

    }

    public UIClick send(String send) {
        this._send = send;
        return this;

    }

    public UIClick send(String model, String cmd) {
        this._model = model;
        this._cmd = cmd;
        return this;
    }

    public UIClick key(String key) {
        this._key = key;
        return this;
    }

    public UIClick text(String text) {
        this._text = text;
        return this;
    }

    public Object send() {

        return _send;
    }

    private String _key;
    private Object _send;
    private String _model;
    private String _cmd;

    private String _text;
    private String _value;

    public UIClick value(String value) {
        this._value = value;
        return this;
    }

    public String cmd() {
        return _cmd;
    }

    public String model() {
        return _model;
    }

    public String value() {
        return this._value;
    }

    @Override
    public void write(Writer writer) {
        try {
            writer.write("{");

            if (Utility.isEmpty(this._key) == false) {
                UMC.Data.JSON.serialize("key", writer);
                writer.write(":");
                UMC.Data.JSON.serialize(this._key, writer);
                if (this._send != null) {

                    writer.write(",");
                    UMC.Data.JSON.serialize("send", writer);
                    writer.write(":");
                    UMC.Data.JSON.serialize(this._send, writer);


                }
            } else {

                UMC.Data.JSON.serialize("model", writer);
                writer.write(":");
                UMC.Data.JSON.serialize(this._model, writer);
                writer.write(",");
                UMC.Data.JSON.serialize("cmd", writer);
                writer.write(":");
                UMC.Data.JSON.serialize(this._cmd, writer);

                if (this._send != null) {

                    writer.write(",");
                    UMC.Data.JSON.serialize("send", writer);
                    writer.write(":");
                    UMC.Data.JSON.serialize(this._send, writer);


                }
            }

            if (Utility.isEmpty(_text) == false) {
                writer.write(",");
                UMC.Data.JSON.serialize("text", writer);
                writer.write(":");
                UMC.Data.JSON.serialize(this._text, writer);


            }
            if (Utility.isEmpty(_value) == false) {
                writer.write(",");
                UMC.Data.JSON.serialize("value", writer);
                writer.write(":");
                UMC.Data.JSON.serialize(this._value, writer);


            }
            writer.write("}");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void read(String key, Object value) {
        switch (key) {
            case "key":
                this._key = String.valueOf(value);//value as string;
                break;
            case "send":
                this._send = value;
                break;
            case "model":
                this._model = String.valueOf(value);//as string;
                break;
            case "cmd":
                this._cmd = String.valueOf(value);
                break;
        }

    }


    public static UIClick search() {
        UIClick cl = new UIClick();
        cl._key = "Search";
        return cl;
    }

    /** 打开搜索界面，接提供搜索界面请求指令
     * @param model 检索的模块
     * @param cmd 检索的指令
     * @return
     */
    public static UIClick search(String model, String cmd) {

        UIClick cl = new UIClick("model", model, "cmd", cmd);
        cl._key = "Search";
        return cl;
    }


    /** 创建打开界面的点击方式
     * @param model 界面的model
     * @param cmd 界面的cmd
     * @param search 界面的参数
     * @return
     */
    public static UIClick pager(String model, String cmd, WebMeta search) {
        WebMeta key = new WebMeta().put("model", model, "cmd", cmd).put("search", search);

        UIClick cl = new UIClick(key);
        cl._key = "Pager";
        return cl;
    }

    /** 提交参数查询，在UISesction界面有效
     * @param query 查询参数
     * @param cache 是否启用缓存
     * @return
     */
    public static UIClick query( WebMeta query,boolean cache) {

        return cache?new UIClick(new WebMeta(query).put("cache", cache)).key("Query"):new UIClick(new WebMeta(query)).key("Query");
    }
    /** 提交参数查询，在UISection界面有效，部分刷新方式
     * @param key 把结果从此section中开始加载
     * @param query 查询参数
     * @return
     */
    public static UIClick query(String key, WebMeta query) {
        return new UIClick(new WebMeta().put("key", key).put("send", query)).key("Query");
    }
    /** 提交参数查询，在UISesction界面有效
     * @param query 查询参数
     * @return
     */
    public static UIClick query(WebMeta query) {
        return new UIClick(query).key("Query");
    }

    public static UIClick pager(String model, String cmd, WebMeta search, boolean isCache) {

        WebMeta key = new WebMeta().put("model", model, "cmd", cmd).put("search", search);
        if (isCache) {
            key.put("Cache", isCache);
        }
        UIClick cl = new UIClick(key);
        cl._key = "Pager";
        return cl;
    }

    public static UIClick pager(String model, String cmd) {
        WebMeta key = new WebMeta().put("model", model, "cmd", cmd);

        UIClick cl = new UIClick(key);
        cl._key = "Pager";
        return cl;
    }

    public static UIClick pager(String model, String cmd, boolean isCache, String... closeEvent) {
        WebMeta key = new WebMeta().put("model", model, "cmd", cmd);
        key.put("ColseEvent", closeEvent);
        if (isCache) {
            key.put("Cache", isCache);
        }

        UIClick cl = new UIClick(key);
        cl._key = "Pager";
        return cl;
    }

    public static UIClick pager(String model, String cmd, boolean isCache) {
        WebMeta key = new WebMeta().put("model", model, "cmd", cmd);
        if (isCache) {
            key.put("Cache", isCache);
        }
        UIClick cl = new UIClick(key);
        cl._key = "Pager";
        return cl;
    }



    /** 创建打开界面的点击方式
     * @param model 界面的model
     * @param cmd 界面的cmd
     * @param refreshEvent 刷新的数据事件
     * @return
     */
    public static UIClick pager(String model, String cmd, String... refreshEvent) {
        WebMeta key = new WebMeta().put("model", model, "cmd", cmd);
        if (refreshEvent.length > 0) {
            key.put("RefreshEvent", String.join(",", refreshEvent));
        }

        UIClick cl = new UIClick(key);
        cl._key = "Pager";
        return cl;
    }

    /** 创建打开界面的点击方式
     * @param model 界面的model
     * @param cmd 界面的cmd
     * @param search 界面的参数
     * @param refreshEvent 刷新的数据事件
     * @return
     */
    public static UIClick pager(String model, String cmd, WebMeta search, String... refreshEvent) {
        WebMeta key = new WebMeta().put("model", model, "cmd", cmd).put("search", search);
        if (refreshEvent.length > 0) {
            key.put("RefreshEvent", String.join(",", refreshEvent));
        }
        UIClick cl = new UIClick(key);
        cl._key = "Pager";
        return cl;
    }

    public static UIClick url(URI url) {
        UIClick cl = new UIClick(url.toString());
        cl._key = "Url";
        return cl;
    }

    public static UIClick tel(String tel) {
        UIClick cl = new UIClick(tel);
        cl._key = "Tel";
        return cl;

    }

    public static UIClick scanning() {
        UIClick cl = new UIClick();
        cl._key = "Scanning";
        return cl;
    }

    /** 扫码，就把扫码结果就click提交到后端，提交方式，如果click没有send，则结果作为send,如果有，则替换Value值换成扫码结果
     * @param click
     * @return
     */
    public static UIClick scanning(UIClick click) {

        UIClick cl = new UIClick();
        cl._key = "Scanning";
        cl._send = click;
        return cl;
    }


    public static UIClick map(String location, String address, WebMeta... items) {
        UIClick click = new UIClick(new WebMeta().put("location", location, "address", address).put("items", items));
        click._key = "Map";
        return click;
    }

    public static UIClick map(String address, WebMeta... items) {
        if (items.length > 0) {
            UIClick click = new UIClick(new WebMeta().put("address", address).put("items", items));
            click._key = "Map";
            return click;

        } else {
            UIClick click = new UIClick(address);
            click._key = "Map";
            return click;
        }
    }
    /** 创建追加界面和单元行参数的点击事件
     * @param click 点击事件
     * @return
     */
    public static UIClick click(UIClick click) {
        UIClick c = new UIClick();

        c._key = "Click";

        c._send = click;
        return c;
    }

}

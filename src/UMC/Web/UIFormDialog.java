package UMC.Web;

import UMC.Data.Utility;

import java.net.URI;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class UIFormDialog extends UIDialog {
    private List<WebMeta> dataSrouce = new LinkedList<>();

    @Override
    protected String type() {
        return "Form";
    }

    private WebMeta meta(String type, String name, String title) {
        WebMeta v = new WebMeta();
        v.put("Type", type).put("Name", name)
                .put("Title", title);
        this.dataSrouce.add(v);
        return v;
    }


    private WebMeta meta(String type, String name, String title, String defaultValue) {


        return meta(type, name, title).put("DefaultValue", defaultValue);
    }


    public void menu(String text, String model, String cmd, String value) {
        this.menu(this.createMenu(text, model, cmd, value));
    }

    public void menu(WebMeta... menus) {
        this.config.put("menu", menus);
    }


    public void menu(String text, String model, String cmd, WebMeta param) {
        this.menu(createMenu(text, model, cmd, param));
    }


    public void add(UICell cell) {
        this.dataSrouce.add(new WebMeta().put("Type", cell.type()).put("value", cell.data()).put("format", cell.format()).put("style", cell.style()));

    }


    public void addSlider(String title, String name, int defaultValue, int min, int max) {

        meta("FieldSlider", name, title).put("Max", max).put("Min", min).put("DefaultValue", defaultValue);
    }

    public void addSlider(String title, String name, int defaultValue) {
        addSlider(title, name, defaultValue, 0, 100);
    }


    /**
     * 地址输入框，可获取本地地址
     *
     * @param title
     * @param name
     * @param defaultValue
     */
    public void addAddress(String title, String name, String defaultValue) {

        meta("Address", name, title).put("DefaultValue", defaultValue);
    }

    public void addPhone(String title, String name, String defaultValue) {

        meta("Number", name, title, defaultValue).put("Vtype", "Phone");
    }

    public WebMeta addNumber(String title, String name, Integer defaultValue) {

        return meta("Number", name, title, defaultValue + "");//.put("Vtype", "Phone");
    }

    public WebMeta add(String type, String name, String title, String defaultValue) {

        return meta(type, name, title, defaultValue);

    }

    public void addNumber(String title, String name, Float defaultValue) {
        meta("Number", name, title, defaultValue + "");
    }


    public void addConfirm(String caption) {
        this.addConfirm(caption, "CONFIRM_NAME", "YES");
    }

    public void addConfirm(String caption, String name, String defaultValue) {
        meta("Confirm", name, null, defaultValue).put("Text", caption);
    }

    public void addPrompt(String caption) {
        meta("Prompt", Math.random() + "", null).put("Text", caption);
    }

    public WebMeta addBarCode(String title, String name, String defaultValue) {

        return meta("BarCode", name, title, defaultValue);
    }

    public WebMeta addOption(String title, String name, String value, String text) {
        return meta("Option", name, title, value).put("Text", text);
    }

    public WebMeta addFile(String title, String name, String defaultValue) {

        return meta("File", name, title, defaultValue);//.put("Text", text);
    }

    public WebMeta addFiles(String title, String name) {
        return meta("Files", name, title);// defaultValue);/
    }

    public WebMeta addTextarea(String title, String name, String defaultValue) {

        return meta("Textarea", name, title, defaultValue);
    }

    public WebMeta addDate(String title, String name, Date date) {

        WebMeta v = meta("Date", name, title);
        if (date != null) {
            java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
            v.put("DefaultValue", format.format(date));
        }
        return v;
    }

    public WebMeta addText(String title, String name, String defaultValue) {

        return meta("Text", name, title, defaultValue);
    }

    public WebMeta addTextValue(String title, ListItemCollection items) {
        WebMeta v = new WebMeta();
        if (Utility.isEmpty(title) == false)
            v.put("Title", title);

        v.put("DataSource", items);
        v.put("Type", "TextValue");

        this.dataSrouce.add(v);
        return v;
    }

    public WebMeta addTextValue(ListItemCollection items) {
        return this.addTextValue("", items);
    }

    public WebMeta addTextNameValue(ListItemCollection items) {
        return this.addTextNameValue("", items);
    }

    public WebMeta addTextNameValue(String title, ListItemCollection items) {

        WebMeta v = new WebMeta();
        if (Utility.isEmpty(title) == false)
            v.put("Title", title);

        v.put("DataSource", items);
        v.put("Type", "TextNameValue");

        this.dataSrouce.add(v);
        return v;
    }

    public ListItemCollection addTextNameValue(String title) {
        ListItemCollection t = new ListItemCollection();
        this.addTextNameValue(title, t);
        return t;
    }

    public ListItemCollection addTextNameValue() {
        ListItemCollection t = new ListItemCollection();
        this.addTextNameValue(t);
        return t;
    }

    public ListItemCollection addTextValue(String title) {
        ListItemCollection t = new ListItemCollection();
        this.addTextValue(title, t);
        return t;
    }

    public ListItemCollection addTextValue() {
        ListItemCollection t = new ListItemCollection();
        this.addTextValue(t);
        return t;
    }

    public void addImage(URI src) {
        WebMeta v = new WebMeta();
        v.put("Type", "Image");
        v.put("Src", src.toString());
        v.put("Name", "_image_" + this.dataSrouce.size());

        this.dataSrouce.add(v);
    }


    /**
     * 增加密码输入选择框
     *
     * @param title
     */
    public WebMeta addPassword(String title, String name, boolean IsDisabledMD5) {
        WebMeta v = meta("Password", name, title);

        if (IsDisabledMD5) {
            v.put("IsDisabledMD5", "true");

        } else {
            v.put("Time", System.currentTimeMillis() / 1000);
        }
        return v;
    }

    /**
     * 增加密码输入选择框，在APP上默认采用md5(Time+密码)
     *
     * @param title
     */
    public WebMeta addPassword(String title, String name, String defaultValue) {
        WebMeta v = meta("Password", name, title, defaultValue);

        v.put("Time", System.currentTimeMillis() / 1000);

        return v;
    }

    public WebMeta addUI(String title, String name, String desc) {
        WebMeta v = meta("UI", name, title, desc);

        return v;

    }

    public WebMeta addUI(String title, String desc) {
        return addUI(title, "UI" + this.dataSrouce.size(), desc);
    }

    public WebMeta addUIIcon(char icon, String title) {
        return addUIIcon(icon, title, "", 0);
    }

    public WebMeta addUIIcon(char icon, String title, int color) {
        return addUIIcon(icon, title, "", color);
    }

    public WebMeta addUIIcon(char icon, String title, String desc, int color) {
        WebMeta v = meta("UI", "icon" + this.dataSrouce.size(), title, desc);
        v.put("Icon", icon);

        if (color != 0) {
            v.put("Color", UIStyle.intParseColor(color));
        }
        return v;
    }


    /**
     * 增加时间选择框
     *
     * @param title
     * @param name
     */

    public WebMeta addTime(String title, String name, int hour, int minute) {
        WebMeta v = meta("Time", name, title, hour + ":" + minute);
        return v;

    }

    /**
     * 增加时间选择框
     *
     * @param title
     * @param name
     */

    public WebMeta addTime(String title, String name, Date defaultValue) {
        WebMeta v = meta("Time", name, title);
        if (defaultValue != null) {
            java.text.DateFormat format = new java.text.SimpleDateFormat("HH:mm");
            v.put("DefaultValue", format.format(defaultValue));
        }
        return v;
    }

    /**
     * 增加列表框
     *
     * @param title
     * @param name
     */
    public ListItemCollection addSelect(String title, String name) {
        ListItemCollection t = new ListItemCollection();
        addSelect(title, name, t);
        return t;
    }

    /**
     * 增加列表框
     *
     * @param title
     * @param name
     * @param items
     */
    public void addSelect(String title, String name, ListItemCollection items) {
        WebMeta v = meta("Select", name, title);

        v.put("DataSource", items);
        this.dataSrouce.add(v);
    }


    /**
     * 增加多选框
     *
     * @param title
     * @param name
     * @param defaultValue
     */
    public ListItemCollection addCheckBox(String title, String name, String defaultValue) {
        ListItemCollection t = new ListItemCollection();
        addCheckBox(title, name, t, defaultValue);
        return t;
    }

    /**
     * 增加多选框
     *
     * @param title
     * @param name
     */
    public ListItemCollection addCheckBox(String title, String name) {
        ListItemCollection t = new ListItemCollection();
        addCheckBox(title, name, t);
        return t;
    }

    /**
     * 增加多选框
     *
     * @param title
     * @param name
     * @param items
     */
    public void addCheckBox(String title, String name, ListItemCollection items, String defaultValue) {

        WebMeta v = meta("CheckboxGroup", name, title, defaultValue);

        v.put("DataSource", items);
    }

    /**
     * 增加多选框
     *
     * @param title
     * @param name
     * @param items
     */
    public void addCheckBox(String title, String name, ListItemCollection items) {
        addCheckBox(title, name, items, null);
    }

    public ListItemCollection addRadio(String title, String name) {
        ListItemCollection t = new ListItemCollection();
        addRadio(title, name, t);
        return t;
    }

    /**
     * 增加单选框
     *
     * @param title
     * @param name
     * @param items
     */
    public void addRadio(String title, String name, ListItemCollection items) {
        WebMeta v = meta("RadioGroup", name, title);
        v.put("DataSource", items);

    }

    /**
     * @param btnName 修改提交按钮的名称
     */
    public void submit(String btnName) {
        this.config.put("submit", btnName);
        this.dataSrouce.get(this.dataSrouce.size() - 1).put("Submit", "YES");
    }

    /**
     * 改变默认提交的model与cmd
     *
     * @param btnName 提交按钮名称
     * @param model   提交的model
     * @param cmd     提交的cmd
     */
    public void submit(String btnName, String model, String cmd, WebMeta param) {
        WebMeta p = new WebMeta();
        if (param != null && param.size() > 0) {
            p.put("send", param);
        }
        p.put("model", model, "cmd", cmd);


        if (Utility.isEmpty(btnName) == false) {
            p.put("text", btnName);
        }

        this.config.put("submit", p);
        this.dataSrouce.get(this.dataSrouce.size() - 1).put("Submit", "YES");

    }

    private WebMeta submit;

    /**
     * 采用事件模式提交
     *
     * @param btnName    提交按钮名称
     * @param model      提交的model
     * @param cmd        提交的cmd
     * @param colseEvent 关闭事件
     */
    public void submit(String btnName, String model, String cmd, String... colseEvent) {
        WebMeta p = new WebMeta();

        p.put("model", model, "cmd", cmd);
        if (Utility.isEmpty(btnName) == false) {
            p.put("text", btnName);
        }
        if (colseEvent.length > 0) {
            this.config.put("CloseEvent", String.join(",", colseEvent));
        }
        this.config.put("submit", p);

    }

    /**
     * 隐藏提交按钮
     */
    public void hideSubmit() {
        this.config.put("submit", false);
    }

    /**
     * 设置提交按钮在当前位置
     */
    public void submit() {
        this.dataSrouce.get(this.dataSrouce.size() - 1).put("Submit", "YES");
    }

    public WebMeta addVerify(String title, String name, String placeholder) {


        WebMeta v = meta("Verify", name, title).put("placeholder", placeholder);
        return v;

    }

    /**
     * 采用事件方式提交
     *
     * @param btnName    提交按钮名称
     * @param request    当前请求
     * @param colseEvent 关闭事件
     */
    public void submit(String btnName, WebRequest request, String... colseEvent) {
        if (colseEvent.length > 0) {
            this.config.put("CloseEvent", String.join(",", colseEvent));
        }
        WebMeta pa = new WebMeta(request.arguments());

        submit = new WebMeta().put("model", request.model(), "cmd", request.cmd(), "text", btnName).put("send", pa);
        submit(btnName);
    }

    protected void initialization(WebContext context) {
        if (submit != null) {
            WebMeta send = submit.meta("send");
            send.put(UIDialog.KEY_DIALOG_ID, this.asyncId());
            //submit._send !=
            this.config.put("submit", submit);
        }
        this.config.put("DataSource", dataSrouce);
    }


}
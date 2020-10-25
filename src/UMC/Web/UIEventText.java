package UMC.Web;


import java.io.Writer;

public class UIEventText implements UMC.Data.IJSON {
    private WebMeta meta = new WebMeta();

    public UIEventText key(String key) {
        meta.put("key", key);
        return this;
    }

    public UIEventText() {
    }

    private UIStyle _style = new UIStyle();

    public UIEventText style(UIStyle style) {
        _style.copy(style);
        return this;

    }

    /**
     * 在UISection的UIFooter，宽度flex占比
     *
     * @param flex
     * @return
     */
    public UIEventText flex(float flex) {

        meta.put("flex", flex);
        return this;
    }

    public UIEventText(String text) {
        meta.put("text", text);
        meta.put("format", "{text}");
    }

    /**
     * 面对Icons组件的图片图标
     *
     * @param src
     * @return
     */
    public UIEventText src(String src) {
        meta.put("src", src);
        return this;

    }

    /**
     * 图标
     *
     * @param icon  图标
     * @param color
     * @return
     */
    public UIEventText icon(char icon, String color) {
        meta.put("icon", icon);
        _style.name("icon").name("color", color);
        return this;

    }

    public UIEventText icon(String icon, String color) {
        meta.put("icon", icon);
        _style.name("icon").name("color", color);
        return this;

    }

    public UIEventText icon(char icon, int color) {
        meta.put("icon", icon);
        _style.name("icon").color(color);


        return this;

    }

    public UIEventText icon(char icon) {
        meta.put("icon", icon);
        meta.put("format", "{icon}");
        this.style(new UIStyle().name("icon", new UIStyle().font("wdk").size(20)));
        return this;
    }

    public UIEventText(char icon, String text) {
        meta.put("icon", icon);
        meta.put("text", text);
        meta.put("format", "{icon}\n{text}");
        this.style(new UIStyle().size(8).name("icon", new UIStyle().font("wdk").size(20)).color(0x666));
    }

    public UIEventText init(UIClick init) {
        meta.put("init", init);
        return this;
    }

    public UIEventText format(String format) {
        meta.put("format", format);
        return this;

    }


    public UIEventText click(UIClick click) {
        meta.put("click", click);
        return this;
    }

    public UIEventText click(String click) {
        meta.put("click", click);
        return this;
    }

    public UIEventText badge(String badge) {
        meta.put("badge", badge);
        return this;
    }

    @Override
    public void write(Writer writer) {
        if (_style.length() > 0) {
            meta.put("style", _style);
        }
        UMC.Data.JSON.serialize(this.meta, writer);
    }

    @Override
    public void read(String key, Object value) {

    }
}
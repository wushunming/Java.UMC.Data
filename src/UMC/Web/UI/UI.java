package UMC.Web.UI;


import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.WebMeta;

public class UI extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "UI";
    }


    WebMeta data;

    public UI(WebMeta data) {
        this.data = data;
    }

    public UI(String text, String value) {
        this.data = new WebMeta().put("text", text, "value", value);
    }

    public UI text(String text) {
        this.format("text", text);
        return this;
    }

    public UI value(String value) {
        this.format("value", value);
        return this;
    }

    public UI click(UIClick click) {
        data.put("click", click);
        return this;

    }

    public UI icon(char icon) {
        data.put("Icon", icon);
        return this;

    }

    public UI icon(char icon, int color) {
        data.put("Icon", icon);
        this.style().name("Icon").color(color);
        return this;

    }
}
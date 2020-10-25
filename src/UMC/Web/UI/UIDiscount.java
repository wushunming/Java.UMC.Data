package UMC.Web.UI;


import UMC.Web.WebMeta;
import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.UIStyle;

public class UIDiscount extends UICell {

    WebMeta data;

    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "Discount";
    }

    private UIDiscount(WebMeta data) {
        this.data = data;
    }

    public UIDiscount () {
        this.data = new WebMeta();
    }

    public UIDiscount (UIClick click) {
        this.data = new WebMeta();
        this.data.put("click", click);

    }

    public UIDiscount click(UIClick click) {

        data.put("click", click);
        return this;
    }

    public UIDiscount gradient(int startColor, int endColor) {

        this.style().name("endColor", UIStyle.intParseColor(endColor));

        this.style().name("startColor", UIStyle.intParseColor(startColor));

        return this;
    }

    public UIDiscount desc(String desc) {
        this.format("desc", desc);
        return this;
    }

    public UIDiscount title(String title) {
        this.format("title", title);
        return this;

    }
    public UIDiscount time(String time) {
        this.format("time", time);
        return this;

    }
    public UIDiscount value(String value) {
        this.format("value", value);
        return this;

    }


    public UIDiscount end(String end) {
        data.put("end", end);
        return this;

    }

    public UIDiscount start(String start) {
        data.put("start", start);
        return this;

    }


    public UIDiscount state(String state) {
        this.data.put("state", state);
        return this;

    }
}
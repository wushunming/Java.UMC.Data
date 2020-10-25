package UMC.Web.UI;

import UMC.Web.WebMeta;
import UMC.Web.UICell;
import UMC.Web.UIClick;

public class UIImageTextValue extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "ImageTextValue";
    }


    public UIImageTextValue(WebMeta data) {
        this.data = data;
    }

    public UIImageTextValue(String src, String text, String value) {
        // UIImageTextValue t = new UIImageTextValue(new WebMeta());
        this.data = new WebMeta();
        this.data.put("src", src);
        this.data.put("text", text).put("value", value);
//        return t;

    }

    public UIImageTextValue text(String text) {
        this.format("text", text);
        return this;
    }

    public UIImageTextValue value(String value) {
        this.format("value", value);
        return this;
    }

//    public UIImageTextValue put(String name, String value) {
//        this.format(name, value);
//        return this;
//
//    }

    public UIImageTextValue click(UIClick click) {
        data.put("click", click);
        return this;

    }

    private WebMeta data;

}

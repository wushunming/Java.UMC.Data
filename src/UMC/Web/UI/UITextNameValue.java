package UMC.Web.UI;


import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.WebMeta;

public class UITextNameValue extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "TextNameValue";
    }


    WebMeta data;

    public UITextNameValue(String name, String text, String value) {
        this.data = new WebMeta().put("name", name, "text", text, "value", value);
    }

    public UITextNameValue click(UIClick click) {

        this.data.put("click", click);//name
        return this;
    }

    public UITextNameValue(WebMeta desc) {
        this.data = desc;

    }

    public UITextNameValue name(String name) {
        this.format("name", name);
        return this;
    }

    public UITextNameValue text(String text) {
        this.format("text", text);
        return this;
    }

    public UITextNameValue value(String value) {
        this.format("value", value);
        return this;
    }
}
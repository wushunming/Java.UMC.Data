package UMC.Web.UI;


import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.WebMeta;

public class UIItemText extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "ItemText";
    }


    WebMeta data;

    public UIItemText(WebMeta data) {


        this.data = data;

    }

    public UIItemText(String text, String desc) {
        this.data = new WebMeta().put("text", text, "desc", desc);
    }

    public UIItemText text(String text) {
        this.format("text", text);
        return this;
    }

    public UIItemText desc(String desc) {
        this.format("desc", desc);
        return this;
    }

    public UIItemText click(UIClick click) {
        data.put("click", click);
        return this;

    }


}
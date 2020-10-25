package UMC.Web.UI;


import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.WebMeta;

public class UITextDesc extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "TextDesc";
    }


    WebMeta data;

    public UITextDesc(String title, String desc, String tag) {
        this.data = new WebMeta().put("desc", desc, "title", title, "tag", tag);
        // this.Type = "TextDesc";
    }

    public UITextDesc click(UIClick click) {

        this.data.put("click", click);
        return this;
    }

    public UITextDesc(WebMeta desc) {
        this.data = desc;// new POSMeta().Put("desc", desc);
        // this.Type = "TextDesc";

    }

    public UITextDesc desc(String desc) {
        this.format("desc", desc);
        return this;
    }

    public UITextDesc tag(String tag) {
        this.format("tag", tag);
        return this;
    }

    public UITextDesc title(String title) {
        this.format("title", title);
        return this;
    }
}
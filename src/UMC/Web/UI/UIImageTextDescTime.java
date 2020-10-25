package UMC.Web.UI;

import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.WebMeta;

public class UIImageTextDescTime extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "ImageTextDescTime";
    }


    WebMeta data;
    public UIImageTextDescTime(String src, String text, String desc, String time)
    {
        this.data = new WebMeta().put("text", text, "desc", desc, "time", time).put("src", src);
//        this.Type = "ImageTextDescTime";
    }
    public UIImageTextDescTime click(UIClick click)
    {

        this.data.put("click", click);
        return this;
    }
    public UIImageTextDescTime(WebMeta desc)
    {
        this.data = desc; 

    }
    public UIImageTextDescTime text(String text)
    {
        this.format("text", text);
        return this;
    }
    public UIImageTextDescTime value(String value)
    {
        this.format("value", value);
        return this;
    }
    public UIImageTextDescTime tag(String tag)
    {
        this.format("tag", tag);
        return this;
    }
}
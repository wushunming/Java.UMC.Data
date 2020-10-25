package UMC.Web.UI;

import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.UIStyle;
import UMC.Web.WebMeta;

import java.io.Writer;

public class UIImageTextDesc extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "ImageTextDesc";
    }


    WebMeta data;
 
    public UIImageTextDesc(String src, String title, String desc, String tag)
    {
        this.data = new WebMeta().put("desc", desc, "title", title, "tag", tag, "src", src);
//        this.Type = "ImageTextDesc";
    }
    public UIImageTextDesc click(UIClick click)
    {

        this.data.put("click", click);
        return this;
    }
    public UIImageTextDesc(WebMeta desc)
    {
        this.data = desc; 

    }
    public UIImageTextDesc desc(String desc)
    {
        this.format("desc", desc);
        return this;
    }
    public UIImageTextDesc tag(String tag)
    {
        this.format("tag", tag);
        return this;
    }
    public UIImageTextDesc title(String title)
    {
        this.format("title", title);
        return this;
    }
}
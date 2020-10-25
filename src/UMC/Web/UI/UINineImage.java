package UMC.Web.UI;

import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.UIStyle;
import UMC.Web.WebMeta;

import java.util.LinkedList;
import java.util.List;

public class UINineImage extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "NineImage";
    }


    WebMeta data;// = new WebMeta();


    List<WebMeta> items;//= new List<WebMeta>();

    public UINineImage() {
        this.items = new LinkedList<>();
        this.data = new WebMeta().put("images", this.items);
    }

    public UINineImage add(String src) {
        this.items.add(new WebMeta().put("src", src));
        return this;
    }

    public UINineImage add(UIClick click, String src) {
        this.items.add(new WebMeta().put("click", click).put("src", src));
        return this;
    }

    public UINineImage click(UIClick click) {
        this.data.put("click", click);//.Put("src", src));
        return this;
    }
    public int size() {

        return this.items.size();

    }
}
package UMC.Web.UI;

import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.UIEventText;
import UMC.Web.WebMeta;

import java.util.LinkedList;
import java.util.List;

public class UIImages extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "UIImages";
    }


    WebMeta data;


    List<WebMeta> items;//= new List<WebMeta>();

    public UIImages() {
        this.items = new LinkedList<>();
        this.data = new WebMeta().put("data", this.items);
    }

    public UIImages add(String src) {
        this.items.add(new WebMeta().put("src", src));
        return this;
    }

    public UIImages add(UIClick click, String src) {
        this.items.add(new WebMeta().put("click", click).put("src", src));
        return this;
    }

    public int size() {

        return this.items.size();

    }
}
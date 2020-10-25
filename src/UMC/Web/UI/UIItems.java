package UMC.Web.UI;

import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.UIStyle;
import UMC.Web.WebMeta;

import java.util.LinkedList;
import java.util.List;

public class UIItems extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "UIItems";
    }


    WebMeta data;// = new WebMeta();


    List<WebMeta> items;//= new List<WebMeta>();

    public UIItems() {
        this.items = new LinkedList<>();
        this.data = new WebMeta().put("items", this.items);
    }

    public UIItems add(String src) {
        this.items.add(new WebMeta().put("src", src));
        return this;
    }

    public UIItems add(UIClick click, String src) {
        this.items.add(new WebMeta().put("click", click).put("src", src));
        return this;
    }

    public UIItems add(String src, String title, String desc) {
        this.items.add(new WebMeta().put("title", title, "desc", desc).put("src", src));
        return this;
    }

    public UIItems add(WebMeta item) {
        this.items.add(item);// new WebMeta().put("title", title, "desc", desc).put("src", src));
        return this;
    }

    public UIItems add(String src, String title, String desc, int startColor, int endColor) {
        this.items.add(new WebMeta().put("title", title, "desc", desc).put("src", src)
                .put("style", new UIStyle()
                        .name("startColor", UIStyle.intParseColor(startColor)).name("endColor", UIStyle.intParseColor(endColor))));
        return this;
    }

    public UIItems add(UIClick click, String src, String title, String desc) {
        this.items.add(new WebMeta().put("click", click).put("title", title, "desc", desc).put("src", src));
        return this;
    }

    public UIItems add(UIClick click, String src, String title, String desc, int startColor, int endColor) {
        this.items.add(new WebMeta().put("click", click).put("src", src).put("title", title, "desc", desc)
                .put("style", new UIStyle()
                        .name("startColor", UIStyle.intParseColor(startColor)).name("endColor", UIStyle.intParseColor(endColor))));
        return this;
    }

    public int size() {

        return this.items.size();

    }
}
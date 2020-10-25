package UMC.Web.UI;


import UMC.Data.Utility;
import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.WebMeta;

import java.util.LinkedList;
import java.util.List;

public class UIItemsTitleDesc extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "ItemsTitleDesc";
    }


    WebMeta data;

    List<WebMeta> items;//= new List<WebMeta>();

    public UIItemsTitleDesc() {
        this.items = new LinkedList<>();
        this.data = new WebMeta().put("items", this.items);
        ;
    }

    public UIItemsTitleDesc add(String src, String title, String desc) {
        this.items.add(new WebMeta().put("title", title, "desc", desc).put("src", src));
        this.hide(4 | 8);
        return this;
    }

    public UIItemsTitleDesc hideTitle() {
        return hide(1);
    }

    public UIItemsTitleDesc hideDesc() {
        return hide(2);
    }

    public UIItemsTitleDesc hideLeft() {
        return hide(4);
    }

    public UIItemsTitleDesc hideRight() {
        return hide(8);
    }

    public UIItemsTitleDesc hide(int hide) {
        int show = Utility.parse(this.data.get("show"), 0);
        this.data.put("show", String.valueOf(hide | show));
        return this;
    }

    public UIItemsTitleDesc add(String src, String title) {
        this.items.add(new WebMeta().put("title", title).put("src", src));
        this.hide(2 | 4 | 8);
        return this;
    }

    public UIItemsTitleDesc add(UIClick click, String src, String title) {
        this.items.add(new WebMeta().put("click", click).put("title", title).put("src", src));
        this.hide(2 | 4 | 8);
        return this;
    }

    public UIItemsTitleDesc add(String src, String title, String desc, String left, String right) {
        this.items.add(new WebMeta().put("title", title, "desc", desc).put("src", src).put("left", left).put("right", right));
        return this;
    }

    public UIItemsTitleDesc add(UIClick click, String src, String title, String desc) {
        this.hide(4 | 8);
        this.items.add(new WebMeta().put("click", click).put("title", title, "desc", desc).put("src", src));
        return this;
    }

    public UIItemsTitleDesc add(UIClick click, String src, String title, String desc, String left, String right) {
        this.items.add(new WebMeta().put("click", click).put("src", src).put("title", title, "desc", desc).put("left", left).put("right", right));
        ;//.Put("startColor", UIStyle.ToColor(startColor)).Put("endColor", UIStyle.ToColor(endColor)));
        return this;
    }

    public int size() {

        return this.items.size();

    }
}
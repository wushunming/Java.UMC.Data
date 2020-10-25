package UMC.Web.UI;

import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.UIStyle;
import UMC.Web.WebMeta;

import java.util.LinkedList;
import java.util.List;

public class UIProduct extends UICell {

    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "Products";
    }


    WebMeta data;// = new WebMeta();


    List<WebMeta> items;//= new List<WebMeta>();

    public UIProduct() {
        this.items = new LinkedList<>();
        this.data = new WebMeta().put("data", this.items);
    }


    public UIProduct add(String src, String id, String name, Double price, Double origin) {
        this.items.add(new WebMeta().put("src", src, "name", name).put("id", id).put("price", price).put("origin", origin));
        return this;
    }

    public UIProduct add(String src, UIClick click, String name, Double price, Double origin) {
        this.items.add(new WebMeta().put("src", src, "name", name).put("click", click).put("price", price).put("origin", origin));
        return this;
    }

    public UIProduct add(String src, UIClick click, String name, Double price) {
        this.items.add(new WebMeta().put("src", src, "name", name).put("click", click).put("price", price));
        return this;
    }

    public UIProduct add(String src, String id, String name, Double price) {
        this.items.add(new WebMeta().put("src", src, "name", name).put("id", id).put("price", price));
        return this;
    }

    public UIProduct add(WebMeta item) {
        this.items.add(item);// new WebMeta().put("title", title, "desc", desc).put("src", src));
        return this;
    }

    public int size() {

        return this.items.size();

    }
}
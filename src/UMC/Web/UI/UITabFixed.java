package UMC.Web.UI;

import UMC.Data.Utility;
import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.WebMeta;

import java.util.LinkedList;
import java.util.List;

public class UITabFixed extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "TabFixed";
    }


    private WebMeta data;


    List<Object> items = new LinkedList<>();


    public UITabFixed() {
        this.data = new WebMeta().put("items", this.items);
    }

    public int getSelectIndex() {

        return Utility.parse(this.data.get("selectIndex"), 0);
    }

    public void setSelectIndex(int selectIndex) {
        this.data.put("selectIndex", String.valueOf(selectIndex));
    }

    public WebMeta getSelectValue() {

        int s = this.getSelectIndex();
        if (s < this.items.size()) {
            Object v = this.items.get(s);//[s];
            if (v instanceof WebMeta) {
                return (WebMeta) v;
            }
        }
        return null;
    }


    public UITabFixed add(String text, String search) {
        items.add(new WebMeta().put("text", text).put("search", search));//.Put("Key", key));
        return this;

    }

    public UITabFixed add(String text, String search, String key) {
        items.add(new WebMeta().put("text", text).put("search", search).put("Key", key));
        return this;
    }

    public UITabFixed add(String text, WebMeta search) {
        items.add(new WebMeta().put("text", text).put("search", search));
        return this;
    }

    public UITabFixed add(String text, WebMeta search, String key) {
        items.add(new WebMeta().put("text", text).put("search", search).put("Key", key));
        return this;
    }

    public UITabFixed add(UIClick click) {
        items.add(click);
        return this;
    }

    public int size() {

        return this.items.size();

    }
}
package UMC.Web.UI;


import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.WebMeta;

public class UIProductPrice extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "ProBuy";
    }


    WebMeta data;

    public UIProductPrice(WebMeta data) {


        this.data = data;
    }

    public UIProductPrice() {


        this.data = new WebMeta();
    }

    public UIProductPrice title(String text) {
        this.format("text", text);
        return this;
    }

    public UIProductPrice value(String value) {
        this.format("value", value);
        return this;
    }

    public UIProductPrice price(String price) {
        this.format("price", price);
        return this;

    }

    public UIProductPrice caption(String caption) {
        this.format("caption", caption);
        return this;

    }

    public UIProductPrice tag(String tag) {
        this.format("tag", tag);
        return this;

    }
}
package UMC.Web.UI;


import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.WebMeta;

public class UIImageTextDescQuantity extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "ImageTextDescQuantity";
    }


    WebMeta data;

    public UIImageTextDescQuantity(WebMeta data) {


        this.data = data;
    }

    public UIImageTextDescQuantity(String src, String title, String desc) {
        this.data = new WebMeta().put("title", title, "desc", desc).put("src", src);
    }

    public UIImageTextDescQuantity title(String title) {
        this.format("title", title);
        return this;
    }

    public UIImageTextDescQuantity desc(String desc) {
        this.format("desc", desc);
        return this;
    }

    public UIImageTextDescQuantity src(String src) {
        data.put("src", src);
        return this;

    }

    public UIImageTextDescQuantity quantity(int quantity) {
        data.put("Quantity", quantity);
        return this;

    }

    /**
     * 点击图片的事件
     *
     * @param click
     * @return
     */
    public UIImageTextDescQuantity imageClick(UIClick click) {
        data.put("image-click", click);
        return this;

    }

    /**
     * 点击减少的事件
     *
     * @param click
     * @return
     */
    public UIImageTextDescQuantity decreaseClick(UIClick click) {
        data.put("decrease-click", click);
        return this;

    }

    /**
     * 点击增加的事件
     *
     * @param click
     * @return
     */
    public UIImageTextDescQuantity increaseClick(UIClick click) {
        data.put("increase-click", click);
        return this;

    }

}
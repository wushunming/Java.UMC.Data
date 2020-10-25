package UMC.Web.UI;

import UMC.Web.*;

public class UIButton extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "UIButton";
    }


    WebMeta data;


    public UIButton() {
        this.data = new WebMeta();
    }

    public UIButton(WebMeta data) {
        this.data = data;
    }

    public UIButton title(String title) {
        this.format("title", title);
        return this;
    }


    public void button(UIEventText... btns) {
        data.put("buttons", btns);
    }
}
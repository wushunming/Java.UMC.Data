package UMC.Web.UI;

import UMC.Web.*;

import java.util.LinkedList;
import java.util.List;

public class UIIcon extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "Icons";
    }


    WebMeta data;// = new WebMeta();


    List<UIEventText> items;//= new List<WebMeta>();

    public UIIcon() {
        this.items = new LinkedList<>();
        this.data = new WebMeta().put("icons", this.items);
    }

    public UIIcon add(UIEventText... eventTexts) {
        for (UIEventText eventText : eventTexts) {
            this.items.add(eventText);
        }
        return this;
    }


    public int size() {

        return this.items.size();

    }
}
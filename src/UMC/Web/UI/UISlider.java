package UMC.Web.UI;

import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.WebMeta;

import java.util.LinkedList;
import java.util.List;

public class UISlider extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "Slider";
    }


    private WebMeta data;


    private List<WebMeta> items = new LinkedList<>();


    public UISlider() {
        this.data = new WebMeta().put("data", this.items);

    }

    public UISlider(boolean auto) {

        this.data = new WebMeta().put("data", this.items);

        if (auto) {
            this.data.put("auto", true);
        }
    }

    public UISlider add(String src) {
        this.items.add(new WebMeta().put("src", src));
        return this;
    }
    public UISlider add(String video, String src)
    {
        this.items.add(new WebMeta().put("src", src).put("video-src", video));
        return this;
    }
    public UISlider add(UIClick click) {
        this.items.add(new WebMeta().put("click", click));//.Put("src", src));
        return this;

    }

    public UISlider add(UIClick click, String src) {
        this.items.add(new WebMeta().put("click", click).put("src", src));
        return this;
    }


    public UISlider row() {
        this.data.put("type", "Row");
        return this;
    }

    public UISlider small() {
        this.data.put("type", "Small");
        return this;
    }

    public UISlider square() {
        this.data.put("type", "Square");
        return this;
    }

    public int size() {

        return this.items.size();

    }
}
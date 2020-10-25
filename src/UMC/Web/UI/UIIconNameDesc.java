package UMC.Web.UI;

import UMC.Web.*;

import java.io.Writer;

public class UIIconNameDesc extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "IconNameDesc";
    }


    WebMeta data;

    public static class Item implements UMC.Data.IJSON {
        WebMeta meta = new WebMeta();

        @Override
        public void write(Writer writer) {
            UMC.Data.JSON.serialize(this.meta, writer);

        }

        @Override
        public void read(String key, Object value) {
            this.meta.put(key, value);
        }

        public Item Click(UIClick click) {

            meta.put("click", click);
            return this;
        }

        public Item(char icon, String name, String desc) {
            meta.put("icon", icon);
            meta.put("name", name);
            meta.put("desc", desc);
        }

        public Item(String src, String name, String desc) {
            meta.put("src", src);
            meta.put("name", name);
            meta.put("desc", desc);
        }

        public Item(String name, String desc) {
            meta.put("name", name);
            meta.put("desc", desc);
        }

        public Item color(int color) {


            meta.put("color", UIStyle.intParseColor(color));

            return this;
        }
    }

    public UIIconNameDesc put(String name, Object v) {
        data.put(name, v);
        return this;
    }

    public UIIconNameDesc(Item... ns) {
        this.data = new WebMeta();
        if (ns.length > 0)
            this.data.put("items", ns);
    }

    public UIIconNameDesc button(String name, UIClick click, int color) {

        this.data.put("button-click", click);
        this.data.put("button", name);

        data.put("button-color", UIStyle.intParseColor(color));
        this.style().name("button").bgColor(color);

        return this;
    }
}
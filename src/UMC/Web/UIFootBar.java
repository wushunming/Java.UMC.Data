package UMC.Web;


import java.io.Writer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class UIFootBar implements UMC.Data.IJSON {

    WebMeta meta = new WebMeta();
    List<Object> _icons = new LinkedList<>();
    List<Object> _btons = new LinkedList<>();

    public UIFootBar cart() {
        _icons.add("-");
        if (this.meta.containsKey("icons") == false) {
            this.meta.put("icons", _icons);
        }
        return this;
    }

    public UIFootBar text(UIEventText... text) {
        _btons.addAll(Arrays.asList(text));
        if (this.meta.containsKey("buttons") == false) {
            this.meta.put("buttons", _btons);
        }
        return this;

    }

    public UIFootBar icon(UIEventText... icons) {
        _icons.addAll(Arrays.asList(icons));
        if (this.meta.containsKey("icons") == false) {
            this.meta.put("icons", _icons);
        }
        return this;

    }

    public boolean fixed() {


        return this.meta.containsKey("fixed");
    }

    public UIFootBar fixed(boolean value) {
        if (value) {
            this.meta.put("fixed", true);
        } else {
            this.meta.remove("fixed");

        }
        return this;
    }

    @Override
    public void write(Writer writer) {
        UMC.Data.JSON.serialize(this.meta, writer);

    }

    @Override
    public void read(String key, Object value) {

    }
}
package UMC.Web;

import UMC.Data.Utility;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

public class UIView implements UMC.Data.IJSON {

    String _type;
    protected List<Object> items = new LinkedList<>();

    public UIView addSrc(String src, UIStyle style) {
        items.add(new WebMeta().put("src", src).put("style", style));
        return this;

    }

    public UIView(String cellName) {
        if (cellName.startsWith("UMC_") == false) {
            throw new RuntimeException("cellName需要以“UMC_”为开头");
        }
        this._type = cellName;
    }

    public UIView() {

    }

    public UIView addSrc(String key, String src, UIStyle style) {
        items.add(new WebMeta().put("src", src).put("style", style).put("key", key));
        return this;

    }
    public UIView addSrc(UIClick click, String src, UIStyle style) {
        items.add(new WebMeta().put("src", src).put("style", style).put("click", click));
        return this;

    }
    public UIView add(UIClick click, String text, UIStyle style) {
        items.add(new WebMeta().put("format", text).put("style", style).put("click", click));
        return this;
    }


    public UIView add(String key, String text, UIStyle style) {
        items.add(new WebMeta().put("format", text).put("style", style).put("key", key));
        return this;
    }

    public UIView add(String text, UIStyle style) {
        items.add(new WebMeta().put("format", text).put("style", style));
        return this;

    }

    String _src;

    public void setSrc(String src) {
        _src = src;
    }

    public String getSrc() {
        return _src;

    }


    String _key;

    public String getKey() {
        return _key;

    }

    public void setKey(String key) {
        _key = key;

    }


    private UIStyle _style = new UIStyle();


    public UIStyle style()

    {

        return _style;


    }

    @Override
    public void write(Writer writer) {
        try {
            if (Utility.isEmpty(this._type)) {
                WebMeta webm = new WebMeta();
                if (this._src != null) {
                    webm.put("style", _style).put("items", this.items).put("src", this._src).put("key", this._key);//
                } else {
                    webm.put("style", _style).put("items", this.items);//

                }
                UMC.Data.JSON.serialize(webm, writer);


            } else {


                writer.write("{");

                UMC.Data.JSON.serialize("key", writer);
                writer.write(":");
                UMC.Data.JSON.serialize(this._type, writer);


                writer.write(",");
                UMC.Data.JSON.serialize("data", writer);
                writer.write(":");
                WebMeta webm = new WebMeta();
                if (this._src != null) {
                    webm.put("style", _style).put("items", this.items).put("src", this._src).put("key", this._key);//
                } else {
                    webm.put("style", _style).put("items", this.items);//
                }

                UMC.Data.JSON.serialize(webm, writer);


                writer.write("}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void read(String key, Object value) {

    }
}

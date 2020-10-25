package UMC.Web;


import UMC.Data.Utility;

import java.io.IOException;
import java.io.Writer;

public abstract class UICell implements UMC.Data.IJSON {

    private static class UICeller extends UICell {
        String _Type;
        Object _data;

        @Override
        public Object data() {
            return _data;
        }

        @Override
        public String type() {
            return _Type;
        }
    }

    public abstract Object data();


    public static UICell create(String type, WebMeta data) {
        UICeller celler = new UICeller();
        celler._data = data;
        celler._Type = type;
        return celler;
    }

    public abstract String type();

    private WebMeta _format = new WebMeta();

    public WebMeta format() {
        return _format;

    }

    public UICell format(String name, String value) {
        _format.put(name, value);
        return this;
    }

    private UIStyle _style = new UIStyle();


    public UIStyle style() {
        return _style;

    }

    @Override
    public void write(Writer writer) {

        if (Utility.isEmpty(this.type())) {
            throw new RuntimeException("Cell Type is empty");
        }
        try {
            writer.write("{\"_CellName\":");

            UMC.Data.JSON.serialize(this.type(), writer);
            writer.write(",");
            UMC.Data.JSON.serialize("value", writer);
            writer.write(":");
            UMC.Data.JSON.serialize(this.data(), writer);
            if (this._format.size() > 0) {

                writer.write(",");
                UMC.Data.JSON.serialize("format", writer);
                writer.write(":");
                UMC.Data.JSON.serialize(_format, writer);
            }
            if (this._style.length() > 0) {

                writer.write(",");
                UMC.Data.JSON.serialize("style", writer);
                writer.write(":");
                UMC.Data.JSON.serialize(_style, writer);
            }

            writer.write("}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void read(String key, Object value) {

    }
}
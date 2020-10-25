package UMC.Web;


import UMC.Data.Utility;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class UISection implements UMC.Data.IJSON {

    public static class Editer {
        private UMC.Web.WebMeta webMeta = new WebMeta();

        /**
         * @param section UISection 中的section
         * @param row     UISection 中的row
         */
        public Editer(int section, int row) {

            webMeta.put("section", section).put("row", row);

        }


        /**
         * 行更新，
         *
         * @param value       更新的组件
         * @param reloadSinge 是否是单行更新，还是整体更新
         * @return
         */
        public Editer put(UICell value, boolean reloadSinge) {
            if (reloadSinge) {
                webMeta.put("value", new WebMeta().cell(value)).put("method", "PUT").put("reloadSinle", true);
            } else {
                webMeta.put("value", new WebMeta().cell(value)).put("method", "PUT");

            }
            return this;
        }


        /**
         * 行删
         *
         * @return
         */
        public Editer delete() {
            webMeta.put("method", "DEL");
            return this;
        }

        /**
         * 行追加
         *
         * @param value 追加的组件
         * @return
         */
        public Editer append(UICell value) {
            webMeta.put("value", new WebMeta().cell(value)).put("method", "APPEND");
            return this;
        }

        /**
         * 行插入
         *
         * @param value 插入的组件
         * @return
         */
        public Editer insert(UICell value) {
            webMeta.put("value", new WebMeta().cell(value)).put("method", "INSERT");
            return this;
        }

        /**
         * 发送到客户端
         *
         * @param context     UMC上下文
         * @param ui          界面的名
         * @param endResponse 是否立刻返回客户端
         */
        public void builder(WebContext context, String ui, boolean endResponse) {
            context.send(new UMC.Web.WebMeta().event("UI.Edit", ui, webMeta), endResponse);

        }
    }

    private WebMeta _header = new WebMeta();

    private UISection() {

    }

    public WebMeta header() {
        return _header;

    }

    private UIHeader _uiheaders;
    private UIHeader _uifooter;
    private UITitle _title;
    private UIFootBar _footBar;

    public UIHeader uiHeader() {
        return Utility.isNull(this.parent, this)._uiheaders;
    }

    public UISection title(UITitle title) {
        Utility.isNull(this.parent, this)._title = title;
        return this;
    }

    public UITitle title() {
        return Utility.isNull(this.parent, this)._title;
    }

    public UISection uiFootBar(UIFootBar footer) {
        Utility.isNull(this.parent, this)._footBar = footer;
        return this;
    }

    public UIFootBar uiFootBar() {
        return Utility.isNull(this.parent, this)._footBar;
    }


    public UISection uiFooter(UIHeader footer) {
        Utility.isNull(this.parent, this)._uifooter = footer;
        return this;
    }

    public UIHeader uiFooter() {
        return Utility.isNull(this.parent, this)._uifooter;
    }

    public UISection uiheader(UIHeader header) {
        Utility.isNull(this.parent, this)._uiheaders = header;
        return this;
    }

    public static UISection create(UIHeader header, UIFootBar footer) {
        UISection t = new UISection();
        t.Sections = new LinkedList<>();
        t._componens = new LinkedList<>();
        t.Sections.add(t);
        t._uiheaders = header;
        t._footBar = footer;
        return t;
    }


    public static UISection create(UIHeader header, UIFootBar footer, UITitle title) {
        UISection t = new UISection();
        t.Sections = new LinkedList<>();
        t._componens = new LinkedList<>();
        t.Sections.add(t);
        t._uiheaders = header;
        t._footBar = footer;
        t._title = title;
        return t;
    }

    public static UISection create(UITitle title, UIFootBar footer) {
        UISection t = new UISection();
        t.Sections = new LinkedList<>();
        t._componens = new LinkedList<>();
        t.Sections.add(t);
        t._footBar = footer;
        t._title = title;
        return t;
    }

    public static UISection create(UITitle title) {
        UISection t = new UISection();
        t.Sections = new LinkedList<>();
        t._componens = new LinkedList<>();
        t.Sections.add(t);
        t._title = title;
        return t;


    }

    public static UISection create(UIHeader header, UITitle title) {
        UISection t = new UISection();
        t.Sections = new LinkedList<>();
        t._componens = new LinkedList<>();
        t.Sections.add(t);
        t._title = title;
        t._uiheaders = header;
        return t;


    }

    private List<UIView> _componens;

    public List<UIView> componen() {
        return Utility.isNull(this.parent, this)._componens;

    }
//    public

    private int Total;

    public int total() {
        return Utility.isNull(this.parent, this).Total;
    }

    public UISection total(int total) {
        Utility.isNull(this.parent, this).Total = total;
        return this;
    }

    public static UISection create() {
        UISection t = new UISection();
        t._componens = new LinkedList<>();
        t.Sections = new LinkedList<>();
        t.Sections.add(t);
        return t;

    }

    public String key() {
        return this.Key;
    }

    public UISection key(String key) {
        this.Key = key;
        return this;
    }

    private String Key;

    private boolean IsEditer;

    public boolean editer() {
        return this.IsEditer;
    }

    public UISection editer(boolean key) {
        this.IsEditer = key;
        return this;
    }

    public UISection disableSeparatorLine() {
        disableSeparatorLine = true;
        return this;
    }

    private boolean disableSeparatorLine;


    private List<UISection> Sections;
    //    private Object _data;
    private List<Object> data = new LinkedList<>();
    private UISection parent;

    public UISection newSection() {
        UISection t = new UISection();
        t.Sections = this.Sections;
        t._componens = this._componens;
        t.parent = Utility.isNull(this.parent, this);

        this.Sections.add(t);
        return t;
    }

    public int size()

    {

        return this.Sections.size();

    }

    public UISection newSection(Collection data) {
        UISection t = new UISection();
        t.Sections = this.Sections;
        t._componens = this._componens;
        this.Sections.add(t);
        t.data.addAll(data);
        t.parent = Utility.isNull(this.parent, this);
        return t;
    }

    public UISection putCells(WebMeta... data) {

        this.data.addAll(Arrays.asList(data));
        return this;
    }

    public UISection putCells(UICell... cells) {
        for (int c = 0; c < cells.length; c++)//var sec in this.Sections)
        {
            this.put(cells[c]);
        }
        return this;
    }

    public int length()

    {
        return data.size();

    }

    public UISection putCell(String text, String value, UIClick click) {
        return this.put(UICell.create("Cell", new WebMeta().put("value", value, "text", text).put("click", click)));

    }

    public UISection putCell(String text, String value, String click) {
        return this.put(UICell.create("Cell", new WebMeta().put("value", value, "text", text).put("click", click)));

    }

    public UISection putCell(String text, UIClick click) {
        return this.put(UICell.create("Cell", new WebMeta().put("text", text).put("click", click)));

    }

    public UISection putCell(String text, String value) {
        return this.put(UICell.create("Cell", new WebMeta().put("value", value, "text", text)));

    }

    public UISection putCell(char icon, String text, String value) {
        return this.put(UICell.create("UI", new WebMeta().put("value", value, "text", text).put("Icon", icon)));

    }

    public UISection putCell(char icon, String text, String value, UIClick click) {
        return this.put(UICell.create("UI", new WebMeta().put("value", value, "text", text).put("Icon", icon).put("click", click)));

    }

    /**
     * 添加支持左滑删除的组件
     *
     * @param cell      行组件
     * @param eventText 删除后请求的事件
     * @return
     */
    public UISection delete(UICell cell, UIEventText eventText) {

        data.add(new WebMeta().put("del", eventText).put("_CellName", cell.type()).put("value", cell.data()).put("format", cell.format()).put("style", cell.style()));
        return this;
    }

    public UISection put(UICell cell) {

        data.add(cell);//new WebMeta().put("_CellName", cell.type()).put("value", cell.data()).put("format", cell.format()).put("style", cell.style()));
        return this;
    }

    public UISection put(String type, WebMeta value, WebMeta format, UIStyle style) {
        data.add(new WebMeta().put("_CellName", type).put("value", value).put("format", format).put("style", style));
        return this;
    }

    public UISection put(String type, WebMeta value) {
        data.add(new WebMeta().put("_CellName", type).put("value", value));
        return this;
    }


    private Boolean IsNext;

    /**
     * 在设置有没有下一页，我们采用了两种方式，一种是直接设置有没有下一页，还有一种是通过total和limit来计算没有没有下一页
     *
     * @return
     */
    public UISection next(boolean IsNext) {
        Utility.isNull(this.parent, this).IsNext = IsNext;
        return this;
    }

    public Boolean next() {
        return Utility.isNull(this.parent, this).IsNext;
    }

    private Integer StartIndex;

    public UISection start(int start) {
        Utility.isNull(this.parent, this).StartIndex = start;
        return this;
    }

    public Integer start() {
        return Utility.isNull(this.parent, this).StartIndex;
    }


    @Override
    public void read(String key, Object value) {

    }

    private boolean isOutPageKey;

    public void builder(WebContext context) {
        Utility.isNull(this.parent, this).isOutPageKey = true;
        context.response().ClientEvent |= WebEvent.DATAEVENT;
        context.response().headers().put("DataEvent", this);
        context.end();

    }

    @Override
    public void write(Writer writer) {
        UISection me = Utility.isNull(this.parent, this);
        try {
            writer.write("{");
            if (me.isOutPageKey) {
                UMC.Data.JSON.serialize("type", writer);
                writer.write(":");
                UMC.Data.JSON.serialize("Pager", writer);
                writer.write(",");

            }
            if (me._componens.size() > 0) {
                UMC.Data.JSON.serialize("Componen", writer);
                writer.write(":");
                UMC.Data.JSON.serialize(me._componens, writer);
                writer.write(",");

            }

            if (me._uiheaders != null) {
                UMC.Data.JSON.serialize("Header", writer);
                writer.write(":");
                UMC.Data.JSON.serialize(me._uiheaders, writer);
                writer.write(",");
            }
            if (me._footBar != null) {

                UMC.Data.JSON.serialize("FootBar ", writer);
                writer.write(":");
                UMC.Data.JSON.serialize(me._footBar, writer);
                writer.write(",");
            }
            if (me._title != null) {
                UMC.Data.JSON.serialize("Title", writer);
                writer.write(":");
                UMC.Data.JSON.serialize(me._title, writer);

                writer.write(",");

            }
            if (me._uifooter != null) {
                UMC.Data.JSON.serialize("Footer", writer);
                writer.write(":");
                UMC.Data.JSON.serialize(me._uifooter, writer);

                writer.write(",");
            }
            if (me.Total > 0) {
                UMC.Data.JSON.serialize("total", writer);
                writer.write(":");
                UMC.Data.JSON.serialize(me.Total, writer);
                writer.write(",");

            }
            if (me.StartIndex != null && me.StartIndex > -1) {
                UMC.Data.JSON.serialize("start", writer);
                writer.write(":");
                UMC.Data.JSON.serialize(me.StartIndex, writer);
                writer.write(",");

            }
            if (me.IsNext != null) {

                UMC.Data.JSON.serialize("next", writer);
                writer.write(":");
                UMC.Data.JSON.serialize(me.IsNext, writer);
                writer.write(",");
            }
            UMC.Data.JSON.serialize("DataSource", writer);
            writer.write(":[");
            boolean b = false;
            for (int c = 0; c < this.Sections.size(); c++)//var sec in this.Sections)
            {
                UISection sec = this.Sections.get(c);
                if (b) {
                    writer.write(",");
                } else {
                    b = true;
                }
                writer.write("{");
                if (!Utility.isEmpty(sec.Key)) {
                    UMC.Data.JSON.serialize("key", writer);
                    writer.write(":");
                    UMC.Data.JSON.serialize(sec.Key, writer);
                    writer.write(",");
                }
                if (sec.IsEditer) {
                    UMC.Data.JSON.serialize("isEditer", writer);
                    writer.write(":");
                    UMC.Data.JSON.serialize(sec.IsEditer, writer);
                    writer.write(",");

                }
                UMC.Data.JSON.serialize("data", writer);
                writer.write(":");

                UMC.Data.JSON.serialize(sec.data, writer);

                if (sec._header.size() > 0) {
                    writer.write(",");
                    UMC.Data.JSON.serialize("header", writer);
                    writer.write(":");
                    UMC.Data.JSON.serialize(sec._header, writer);
                }
                if (sec.disableSeparatorLine) {
                    writer.write(",");
                    UMC.Data.JSON.serialize("separatorLine", writer);
                    writer.write(":false");
                }
                writer.write("}");

            }

            writer.write("]}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
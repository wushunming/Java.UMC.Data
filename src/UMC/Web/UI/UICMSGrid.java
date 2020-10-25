package UMC.Web.UI;


import UMC.Data.JSON;
import UMC.Data.Utility;
import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.UIStyle;
import UMC.Web.WebMeta;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

public class UICMSGrid extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "CMSGrid";
    }


    public static class Cell implements UMC.Data.IJSON {
        private WebMeta Data;
        private String Format;
        private UIStyle Style;
        public String Text;

        public Cell setFormat(String format) {
            this.Format = format;
            return this;
        }

        public Cell setStyle(UIStyle style) {
            this.Style = style;
            return this;
        }

        public Cell setData(WebMeta data) {
            this.Data = data;
            return this;
        }


        @Override
        public void write(Writer writer) {

            try {
                writer.write("{");
                if (this.Data == null) {
                    writer.write("\"text\":");
                    JSON.serialize(this.Text, writer);

                } else {
                    writer.write("\"data\":");
                    JSON.serialize(UMC.Data.Utility.isNull(this.Data, new WebMeta()), writer);

                }

                if (Utility.isEmpty(this.Format) == false) {
                    writer.write(',');
                    writer.write("\"format\":");
                    JSON.serialize(this.Format, writer);
                }
                if (this.Style != null) {

                    writer.write(',');
                    writer.write("\"style\":");
                    JSON.serialize(this.Style, writer);
                }
                writer.write('}');
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void read(String key, Object value) {

        }
    }

    private List<Cell[]> cells = new LinkedList<>();

    private WebMeta data;

    public UICMSGrid() {


        this.data = new WebMeta().put("grid", this.cells);
    }

    /** 添加一行杜桥表格
     * @param cells
     * @return
     */
    public UICMSGrid putRow(Cell... cells) {
        this.cells.add(cells);

        return this;
    }

    /** 添加一行文本表格
     * @param cells
     * @return
     */
    public UICMSGrid putRow(String... cells) {
        List<Cell> cls = new LinkedList<>();
        for (String s : cells) {
            Cell cell = new Cell();
            cell.Text = s;
            cls.add(cell);
        }
        this.cells.add(cls.toArray(new Cell[0]));

        return this;
    }

    public int size() {
        return this.cells.size();
    }
}
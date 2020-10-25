package UMC.Web.UI;


import UMC.Web.WebMeta;
import UMC.Web.UIEventText;
import UMC.Web.UIStyle;
import UMC.Web.UICell;

import java.io.Writer;

public class UIComment extends UICell {

    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "Comment";
    }

    public static class Image {
        public String max;
        public String src;
    }

    public static class Reply implements UMC.Data.IJSON {
        public String title;

        public String content;
        public UIStyle style;

        public WebMeta data;

        @Override
        public void write(Writer writer) {
            UMC.Data.JSON.serialize(new WebMeta().put("format", new WebMeta().put("content", this.content, "title", this.title)).put("value", this.data).put("style", this.style), writer);

        }

        @Override
        public void read(String key, Object value) {

        }

    }

    private WebMeta data;

    /**
     * @param headerSrc 头像地址
     */
    public UIComment(String headerSrc) {
        this.data = new WebMeta().put("src", headerSrc);
    }

    public UIComment name(String name, String value) {

        this.data.put(name, value);
        return this;
    }

    public UIComment name(String name) {
        this.format("name", name);
        return this;
    }

    public UIComment time(String title) {
        this.format("time", title);
        return this;
    }

    public UIComment content(String content) {
        this.format("content", content);
        return this;
    }

    public UIComment images(Image... images) {
        this.data.put("image", images);
        return this;

    }

    public UIComment tag(UIEventText eventText) {
        this.data.put("tag", eventText);
        return this;
    }

    public String Id;

    public UIComment replys(Reply... replys) {
        this.data.put("replys", replys);
        return this;

    }

    public UIComment button(UIEventText... btns) {
        this.data.put("buttons", btns);
        return this;

    }
}
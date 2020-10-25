package UMC.Web.UI;


import UMC.Web.UICell;
import UMC.Web.WebMeta;

public class UICMSText extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return Type;
    }

    private String Type;
    private WebMeta data;

    public UICMSText(WebMeta data) {
        this.data = data;
        this.Type = "CMSText";
    }

    public UICMSText(String text) {
        this.data = new WebMeta().put("text", text);//, "src", src);
        this.Type = "CMSText";
    }

    /*
     * 引用格式
     *
     * */
    public UICMSText rel() {
        this.Type = "CMSRel";
        return this;
    }

    /*
     * 代码格式
     *
     * */
    public UICMSText code() {
        this.Type = "CMSCode";
        return this;
    }
}
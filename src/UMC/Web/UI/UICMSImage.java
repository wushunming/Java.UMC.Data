package UMC.Web.UI;


import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.WebMeta;

public class UICMSImage extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "CMSImage";
    }


    private WebMeta data;

    public UICMSImage(WebMeta data) {


        this.data = data;
    }

    public UICMSImage(String video, String src) {
        this.data = new WebMeta().put("video-src", video, "src", src);

    }


    public UICMSImage(String src) {
        this.data = new WebMeta().put("src", src);
    }
}
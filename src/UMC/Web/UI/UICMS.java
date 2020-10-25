package UMC.Web.UI;


import UMC.Web.WebMeta;
import UMC.Web.UICell;
import UMC.Web.UIClick;


public class UICMS extends UICell {
    @Override
    public Object data() {
        return data;
    }

    private String Type;

    @Override
    public String type() {
        return Type;
    }

    private WebMeta data;

    /**
     * 创建单图资讯组件
     *
     * @param click
     * @param data
     */
    public UICMS(UIClick click, WebMeta data) {

        this.data = data.put("click", click);
        this.Type = "CMSMax";

    }


    /**
     * 创建单图或者大图资讯组件
     *
     * @param click
     * @param data
     * @param src
     * @param max   是否是大图
     */
    public UICMS(UIClick click, WebMeta data, String src, boolean max) {
        this.data = data.put("click", click).put("src", src);
        this.Type = max ? "CMSMax" : "CMSOne";

    }

    /**
     * 创建大图资讯组件，并支持视频
     *
     * @param data
     * @param click
     * @param videoSrc 视频Url
     * @param src      视频预览图片
     */
    public UICMS(WebMeta data, UIClick click, String videoSrc, String src) {
        this.data = data.put("click", click).put("src", src).put("video-src", videoSrc);
        this.Type = "CMSMax";
    }


    /**
     * 创建视频资讯组件
     *
     * @param data
     * @param videoSrc 视频Url
     * @param src      视频预览图片
     */
    public UICMS(WebMeta data, String videoSrc, String src) {
        this.data = data.put("src", src).put("video-src", videoSrc);
        this.Type = "CMSMax";
    }

    /**
     * 创建单图资讯组件
     *
     * @param click
     * @param data
     * @param src
     */
    public UICMS(UIClick click, WebMeta data, String src) {

        this.data = data.put("click", click).put("src", src);
        this.Type = "CMSOne";


    }

    /**
     * 创建三张图的资讯组件
     *
     * @param click
     * @param data
     * @param src1
     * @param src2
     * @param src3
     */
    public UICMS(UIClick click, WebMeta data, String src1, String src2, String src3) {

        this.data = data.put("click", click).put("images", new String[]{src1, src2, src3});
        this.Type = "CMSThree";


    }

    public UICMS desc(String desc) {
        this.format("desc", desc);
        return this;
    }

    public UICMS title(String title) {
        this.format("title", title);
        return this;

    }

    public UICMS right(String right) {
        this.format("right", right);
        return this;

    }

    public UICMS left(String left) {
        this.format("left", left);
        return this;

    }
}
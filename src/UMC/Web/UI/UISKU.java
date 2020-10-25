package UMC.Web.UI;

import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.WebMeta;

import java.util.LinkedList;
import java.util.List;

public class UISKU extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "UISKU";
    }


    WebMeta data;// = new WebMeta();


    public UISKU(String title) {
        this.data = new WebMeta().put("title", title);
    }

    public UISKU() {

        this.data = new WebMeta();//.Put("title", title);
    }


}
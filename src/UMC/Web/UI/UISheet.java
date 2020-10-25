package UMC.Web.UI;

import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.WebMeta;

import java.util.LinkedList;
import java.util.List;

public class UISheet extends UICell {
    @Override
    public Object data() {
        return data;
    }

    @Override
    public String type() {
        return "UISheet";
    }


    WebMeta data;// = new WebMeta();


    List<WebMeta> items;//= new List<WebMeta>();

    public UISheet(String title)
    {
        this.items=new LinkedList<>();
        this.data = new WebMeta().put("title", title);
        this.data.put("items", items);
       // this.Type = "UISheet";
        this.style().name("icon").font("wdk").color(0x999);
        this.style().name("info").font("wdk").color(0xef4f4f);
    }
    public UISheet addItem(String text, String desc, boolean isInfo)
    {
        WebMeta b = new WebMeta().put("text", text, "desc", desc);
        if (isInfo)
        {
            b.put("icon", '\uF05A');
        }
        else
        {

            b.put("info", '\uF05D');
        }
        this.items.add(b);
        return this;
    }
    public UISheet addItem(String text, String desc)
    {
        this.addItem(text, desc, false);
        return this;
    }

    public int size() {

        return this.items.size();

    }
}
package UMC.Data.Entities;

import java.util.Date;
import java.util.UUID;

public class Menu {


    public UUID Id;
    public String Icon;
    public String Caption;
    public String Url;
    public Integer Seq;
    public UUID ParentId ;

    public Boolean IsDisable;// { get; set; }

    public Menu Id(UUID id) {
        Id = id;
        return this;
    }

    public Menu Icon(String icon) {
        Icon = icon;
        return this;
    }

    public Menu Caption(String caption) {
        Caption = caption;
        return this;
    }

    public Menu Url(String url) {
        Url = url;
        return this;
    }

    public Menu Seq(Integer seq) {
        Seq = seq;
        return this;
    }

    public Menu ParentId(UUID parentId) {
        ParentId = parentId;
        return this;
    }

    public Menu Disable(Boolean disable) {
        IsDisable = disable;
        return this;
    }
}

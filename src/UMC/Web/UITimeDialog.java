package UMC.Web;

import java.util.Date;

public class UITimeDialog extends UIDialog {
    public UITimeDialog(int hour, int minute) {
        super();
        this.config.put("DefaultValue", String.format("%d:%d", hour, minute));

    }

    public UITimeDialog() {
    }

    @Override
    protected String type() {
        return "Date";
    }
}

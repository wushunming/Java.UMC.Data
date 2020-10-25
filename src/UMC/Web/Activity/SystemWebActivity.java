package UMC.Web.Activity;

import UMC.Data.ProviderConfiguration;
import UMC.Data.Utility;
import UMC.Data.WebResource;
import UMC.Web.*;

import java.io.File;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SystemWebActivity extends WebActivity {

    public void processActivity(WebRequest request, WebResponse response) {
        if (request.isMaster() == false) {
            this.prompt("需要管理员权限");
        }

        String svalue = this.asyncDialog("Key", d ->
        {
            return this.dialogValue("New");
        });
        switch (svalue) {
            case "RESETAPPSECRET":
                this.prompt("AppSecret", "AppSecret：" + WebResource.Instance().AppSecret(true));
                break;
            case "APPSECRET":
                this.prompt("AppSecret", "AppSecret：" + WebResource.Instance().AppSecret(false));
                break;
        }

        ProviderConfiguration cfg = ProviderConfiguration.configuration("UMC");
        if (cfg == null) {
            cfg = new ProviderConfiguration();

        }
        final UMC.Data.Provider n = Utility.isNull(cfg.get(svalue), UMC.Data.Provider.create("", ""));
        WebMeta Settings = this.asyncDialog(g ->
        {
            UIFormDialog fm = new UIFormDialog();
            fm.title("配置云模块");

            if (Utility.isEmpty(n.name()))
                fm.addText("云模块名", "name", n.name());
            else {
                fm.addTextValue().put("云模块名", n.name());
            }
            fm.addText("指令通配符", "type", n.type());
            fm.addText("描述", "desc", n.get("desc"));
            fm.addText("服务网址", "src", n.get("src"));
            fm.addText("AppSecret", "secret", n.get("secret")).notRequired();
            if (Utility.isEmpty(n.name()) == false) {
                fm.addCheckBox("", "Status", "NO").put("移除", "DEL");
            }
            fm.submit("确认", request, "Config");
            return fm;
        }, "Settings");
        String status = Utility.isNull(Settings.get("Status"), "");
        if (status.contains("DEL")) {
            cfg.remove(svalue);
        } else {
            URI src = URI.create(Settings.get("src"));
            UMC.Data.Provider p = UMC.Data.Provider.create(Utility.isNull(Settings.get("name"), svalue), Settings.get("type"));

            p.attributes().put("src", src.toString());
            p.attributes().put("desc", Settings.get("desc"));
            if (Utility.isEmpty(Settings.get("secret")) == false)
                p.attributes().put("secret", Settings.get("secret"));
            cfg.set(p);
        }

        try {
            File file = new File(Utility.mapPath("App_Data/UMC/UMC.xml"));
            cfg.WriteTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<WebMeta> data2 = new LinkedList<>();


        for (int i = 0; i < cfg.size(); i++) {
            UMC.Data.Provider p = cfg.get(i);
            String cmd = p.type();

            if (Utility.isEmpty(cmd)) {
                cmd = "*";
            }
            data2.add(new WebMeta("model", p.name(), "cmd", cmd, "text", p.get("desc"), "src", p.get("src")));


        }

        this.context().send("Config", new WebMeta().put("data", data2), false);

        this.prompt("配置成功");

    }
}

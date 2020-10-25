package UMC.Web.Activity;

import UMC.Data.DataProvider;
import UMC.Data.Provider;
import UMC.Data.ProviderConfiguration;
import UMC.Data.Utility;
import UMC.Web.*;

import java.io.File;
import java.util.Map;

public class SystemConfigActivity extends WebActivity {
    @Override
    public void processActivity(WebRequest request, WebResponse response) {
        if (request.isMaster() == false) {
            this.prompt("只有管理员才能配置");
        }

        String configKey = this.asyncDialog("Key", g ->
        {
            WebMeta form = Utility.isNull(request.sendValues(), new UMC.Web.WebMeta());
            if (form.containsKey("limit") == false) {
                this.context().send(new UISectionBuilder(request.model(), request.cmd(), new WebMeta(request.arguments()))
                        .refreshEvent("ProviderConfiguration")
                        .builder(), true);
            }
            String key = this.asyncDialog("Type", "FILES");

            UISection ui = UISection.create(new UITitle("配置文件"));
            if (key.equals("FILES")) {
                File file = new File(Utility.mapPath("~App_Data/UMC"));
                String[] files = file.list((d, n) -> n.toLowerCase().endsWith(".xml"));
                for (String f : files) {
                    String name = f.substring(f.lastIndexOf(File.separator) + 1);
                    name = name.substring(0, name.indexOf('.'));
                    String d = "";

                    ui.title().right(new UIEventText("新建").click(new UIClick("Key", "NEW").send(request.model(), request.cmd())));

                    switch (name.toLowerCase()) {
                        case "assembly":
                            d = "处理类配置";
                            break;
                        case "database":
                            d = "数据库配置";
                            break;
                        case "umc":
                            d = "云模块配置";
                            break;
                        case "parser":
                            d = "转码配置";
                            break;
                        case "payment":
                            d = "支付配置";
                            break;
                    }
                    ui.putCell(name, d, UIClick.query(new WebMeta("Type", name)));
                }
            } else {
                String[] keys = key.split("\\$");
                ProviderConfiguration cfg = ProviderConfiguration.configuration(keys[0]);//, "*.xml");

                if (keys.length == 1) {
                    ui.title().right(new UIEventText("新建").click(new UIClick("Key", keys[0] + "$NEW").send(request.model(), request.cmd())));
                    ui.putCell('\uf112', "上一层", keys[0], UIClick.query(new WebMeta("Type", "FILES")));
                    UISection ui2 = ui.newSection();
                    for (int i = 0; i < cfg.size(); i++) {
                        Provider p = cfg.get(i);
                        ui2.putCell(p.name(), "", UIClick.query(new WebMeta("Type", String.format("%s$%s", keys[0], p.name()))));
                    }
                } else {
                    Provider p = cfg.get(keys[1]);
                    ui.title().right(new UIEventText("新建").click(new UIClick("Key", keys[0] + "$" + p.name() + "$NEW").send(request.model(), request.cmd())));
                    ui.putCell('\uf112', "上一层", p.name(), UIClick.query(new WebMeta("Type", keys[0])));
                    ui.putCell("类型类型", p.type());
                    UISection ui2 = ui.newSection();
                    for (Map.Entry<String, String> k : p.attributes().entrySet()) {
                        ui2.putCell(k.getKey(), new UIClick(g, String.format("%s$%s$%s", keys[0], p.name(), k.getKey())).send(request.model(), request.cmd()));

                    }
                }
            }

            response.redirect(ui);
            return this.dialogValue("none");
        });
        switch (configKey) {
            case "NEW":
                WebMeta fName = this.asyncDialog(g ->
                {
                    UIFormDialog fm = new UIFormDialog();
                    fm.title("新建文件配置");
                    fm.addText("新建文件名", "Name", "");
                    fm.submit("确认", request, "ProviderConfiguration");
                    return fm;
                }, "Setting");
                File pf = new File(Utility.mapPath("~App_Data/UMC/" + fName.get("Name") + ".xml"));

                if (pf.exists() == false) {
                    try {
                        new ProviderConfiguration().WriteTo(pf);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.context().send("ProviderConfiguration", true);
                } else {
                    this.prompt("此文件已经存在");
                }
                break;
            default:
                String[] ckeys = configKey.split("\\$");
                String cfgFile = Utility.mapPath("~App_Data/UMC/" + ckeys[0] + ".xml");
                ProviderConfiguration cfg = ProviderConfiguration.configuration(ckeys[0]);

                if (ckeys.length == 3) {
                    Provider pro = cfg.get(ckeys[1]);
                    WebMeta ps = this.asyncDialog(g ->
                    {
                        UIFormDialog fm = new UIFormDialog();
                        fm.title(ckeys[1] + "配置");

                        if (ckeys[2] == "NEW") {
                            fm.addText("新建属性名", "Name", "");
                            fm.addText("新建属性值", "Value", "");
                        } else {

                            fm.addTextValue().put("属性名", ckeys[2]);
                            fm.addText("属性值", "Value", pro.get(ckeys[2]));
                        }
                        fm.submit("确认", request, "ProviderConfiguration");
                        return fm;
                    }, "Setting");
                    String value = ps.get("Value");
                    if (value.equals("none")) {
                        pro.attributes().remove(ckeys[2]);
                    } else {

                        pro.attributes().put(Utility.isNull(ps.get("Name"), ckeys[2]), ps.get("Value"));
                    }
                    try {
                        cfg.WriteTo(new File(cfgFile));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.context().send("ProviderConfiguration", true);

                } else if (ckeys.length == 2) {
                    Provider pro = Utility.isNull(cfg.get(ckeys[1]), Provider.create("", ""));
                    WebMeta ps = this.asyncDialog(g ->
                    {
                        UIFormDialog fm = new UIFormDialog();
                        fm.title(ckeys[0] + "节点");

                        if (ckeys[1] == "NEW") {
                            fm.addText("节点名", "Name", "");
                            fm.addText("类型值", "Value", "");
                        } else {

                            fm.addText("节点名", "Name", pro.name());
                            fm.addText("类型值", "Value", pro.type());
                        }
                        fm.submit("确认", request, "ProviderConfiguration");
                        return fm;
                    }, "Setting");

                    Provider pro2 = Provider.create(ps.get("Name"), ps.get("Value"));
                    pro2.attributes().putAll(pro.attributes());
                    cfg.set(pro2);
                    try {
                        cfg.WriteTo(new File(cfgFile));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.context().send("ProviderConfiguration", true);
                }
                break;
        }
    }
}

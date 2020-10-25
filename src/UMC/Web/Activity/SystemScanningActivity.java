package UMC.Web.Activity;

import UMC.Data.Utility;
import UMC.Data.WebResource;
import UMC.Security.Identity;
import UMC.Web.*;
import UMC.Web.UI.UIDesc;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Mapping(model = "System", cmd = "Scanning", auth = WebAuthType.all, desc = "移动扫描处理")
public class SystemScanningActivity extends WebActivity {

    protected void Scanning(URL url) {
        String domain = null;
        try {
            domain = new URL(url, WebResource.Instance().WebDomain()).toString();
        } catch (MalformedURLException e) {
            return;
        }
        if (domain.contains(url.getHost())) {
            List<String> paths = new LinkedList<>();

            paths.addAll(Arrays.asList(Utility.trim(url.getPath(), '/').split("/")));

            if (paths.size() == 0) {
                return;

            }
            switch (paths.get(0)) {
                case "download":
                case "app":
                    if (Utility.isEmpty(url.getQuery())) {
                        this.OpenUrl(url);
                    }
                    String query = url.getQuery();//.substring(1);
                    Identity user = UMC.Security.Identity.current();
                    if (user.isAuthenticated() == false) {
                        this.context().response().redirect("Account", "Login");
                    }
                    UUID dever = Utility.uuid(query);
                    if (dever != null) {
                        WebResource webr = WebResource.Instance();
                        this.asyncDialog("Device", g ->
                        {
                            webr.Push(new UUID[]{dever}, new WebMeta().put("msg", "扫码成功").put("src", webr.ImageResolve(user.id(), "1", 4)));

                            UIFormDialog fm = new UIFormDialog();
                            fm.title("扫码登录");
                            UIDesc desc = new UIDesc(new UMC.Web.WebMeta().put("desc", "正在进行扫码登录").put("icon", "\uF108"));
                            desc.style().align(1)
                                    .color(0xaaa).padding(40, 20).bgColor(0xfff).name("icon", new UIStyle().font("wdk").size(160));
                            desc.desc("{icon}\n{desc}");
                            fm.add(desc);

                            fm.submit("确认登录", this.context().request(), "PC.Login");
                            return fm;
                        });

                        UMC.Security.AccessToken.login(user, dever, "PC");
                        webr.Push(new UUID[]{dever}, new WebMeta().put("msg", "OK", "type", "SignIn").put("root", Utility.getRoot(this.context().request().uri())));
                        this.context().send("PC.Login", true);
                    } else {
                        this.OpenUrl(url);
                    }
                    break;
                case "Click":
                case "Pager":
                    String p = paths.get(0);
                    paths.remove(0);

                    WebMeta data = new UMC.Web.WebMeta();
                    String model = "Corp";
                    String cmd = "Scanning";
                    if (paths.size() > 1) {
                        model = paths.get(0);
                        cmd = paths.get(0);
                        paths.remove(0);
                        paths.remove(0);

                    }
                    String SValue = "";// String.Empty;
                    if (paths.size() == 1) {
                        SValue = paths.get(0);
                        data.put("Id", SValue);
                    } else {
                        while (paths.size() > 0) {
                            if (paths.size() > 1) {
                                data.put(paths.get(0), paths.get(1));
                            }
                            paths.remove(0);
                            paths.remove(0);
                        }

                    }
                    if (p == "Click") {
                        if (data.size() > 0) {
                            if (Utility.isEmpty(SValue)) {

                                this.context().response().redirect(model, cmd, data, true);
                            } else {
                                this.context().response().redirect(model, cmd, SValue, true);
                            }
                        } else {
                            this.context().response().redirect(model, cmd, true);
                        }
                    } else {
                        if (data.size() > 0) {
                            this.context().send("Pager", new UMC.Web.WebMeta().put("model", model, "cmd", cmd).put("search", data), true);
                        } else {
                            this.context().send("Pager", new UMC.Web.WebMeta().put("model", model, "cmd", cmd), true);
                        }

                    }
                    break;
            }
        }
    }

    void OpenUrl(URL url) {

        this.context().send("OpenUrl", new WebMeta().put("value", url.toString()), true);
    }

    @Override
    public void processActivity(WebRequest request, WebResponse response) {


        String svalue = this.asyncDialog("Url", d ->
        {
            this.context().send("Scanning", true);
            return this.dialogValue("none");
        });

        if (svalue.startsWith("http://") || svalue.startsWith("https://")) {
            URL d = null;
            try {
                d = new URL(svalue);
                this.Scanning(d);
                this.OpenUrl(d);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        this.prompt("此扫码未处理");
    }
}

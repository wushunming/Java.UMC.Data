package UMC.Web.Activity;

import UMC.Data.ProviderConfiguration;
import UMC.Data.Sql.Initializer;
import UMC.Data.Utility;
import UMC.Web.*;

import java.util.LinkedList;
import java.util.List;

@Mapping(model = "System", auth = WebAuthType.all, desc = "UMC基础组件")
public class SystemFlow extends WebFlow {
    @Override
    public WebActivity firstActivity() {
        switch (this.context().request().cmd()) {
            case "Config":
                return new SystemConfigActivity();
            case "TimeSpan":
                this.context().response().redirect(new WebMeta().put("time", System.currentTimeMillis() / 1000));
                break;
            case "Start":
                this.context().send("Setup", true);
                break;
            case "Mapping": {
                Initializer[] initializers = Initializer.Initializers();
                ProviderConfiguration database = Utility.isNull(ProviderConfiguration.configuration("database"), new ProviderConfiguration());//?? new Configuration.ProviderConfiguration();

                List<WebMeta> data = new LinkedList<>();
                for (Initializer n : initializers) {
                    data.add(new WebMeta().put("name", n.name()).put("text", n.caption()).put("setup", database.get(n.providerName()) != null));

                }

                ProviderConfiguration cfg = ProviderConfiguration.configuration("UMC");
                List<WebMeta> data2 = new LinkedList<>();

                if (cfg != null) {
                    for (int i = 0; i < cfg.size(); i++) {
                        UMC.Data.Provider p = cfg.get(i);
                        String cmd = p.type();

                        if (Utility.isEmpty(cmd)) {
                            cmd = "*";
                        }
                        data2.add(new WebMeta("model", p.name(), "cmd", cmd, "text", p.get("desc"), "src", p.get("src")));


                    }
                }
                context().response().redirect(new WebMeta().put("component", data).put("data", WebServlet.mapping()).put("webs", data2));

            }
            break;
            case "Menu":
                return new SystemMenuActivity();
            case "Icon":
                return new SystemIconActivity();
            case "Setup":
                return new SystemSetupActivity();
            case "Web":
                return new SystemWebActivity();
            case "Upgrade":
                return new SystemUpgradeActivity();
        }
        return WebActivity.Empty;
    }
}

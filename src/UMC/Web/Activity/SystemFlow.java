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
            case "TimeSpan":
                this.context().response().redirect(new WebMeta().put("time", System.currentTimeMillis() / 1000));
                break;
            case "Start":
                this.context().send("Setup", true);
                break;
            case "Mapping": {
                Initializer[] Initializers = Initializer.Initializers();
                ProviderConfiguration database = Utility.isNull(ProviderConfiguration.configuration("Database"), new ProviderConfiguration());//?? new Configuration.ProviderConfiguration();

                List<WebMeta> data = new LinkedList<>();
                for (Initializer n : Initializers) {
                    data.add(new WebMeta().put("name", n.name()).put("text", n.caption()).put("setup", database.get(n.providerName()) != null));
//                    data.Rows.Add(n.Name, n.Caption, database.Providers.ContainsKey(n.ProviderName));
                }
                context().response().redirect(new WebMeta().put("component", data).put("data", WebServlet.mapping()));

            }
            break;
            case "Icon":
                return new SystemIconActivity();
            case "Setup":
                return new SystemSetupActivity();
//            case "Access":
//                return new SystemAccessActivity();
        }
        return WebActivity.Empty;
    }
}

package UMC.Web.Activity;

import UMC.Data.Provider;
import UMC.Data.ProviderConfiguration;
import UMC.Data.Sql.DbProvider;
import UMC.Data.Sql.Initializer;
import UMC.Data.Utility;
import UMC.Web.WebActivity;
import UMC.Web.WebRequest;
import UMC.Web.WebResponse;

import java.util.HashMap;

public class SystemUpgradeActivity extends WebActivity {
    @Override
    public void processActivity(WebRequest request, WebResponse response) {

        if (request.isMaster() == false) {
            this.prompt("只有管理员才能升级");
        }
        ProviderConfiguration database = UMC.Data.ProviderConfiguration.configuration("database");
        if (database == null) {
            database = new ProviderConfiguration();
        }
        Initializer[] Initializers = Initializer.Initializers();
        for (Initializer initer : Initializers) {
            Provider provider1 = database.get(initer.providerName());
            if (provider1 != null) {

                initer.Check((DbProvider) Utility.createInstance(provider1));

            }
        }
        this.prompt("提示", "检测升级已完成");
    }
}

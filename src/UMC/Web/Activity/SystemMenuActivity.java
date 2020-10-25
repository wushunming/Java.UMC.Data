package UMC.Web.Activity;

import UMC.Data.Database;
import UMC.Data.Entities.Menu;
import UMC.Data.Provider;
import UMC.Data.ProviderConfiguration;
import UMC.Data.Sql.DbProvider;
import UMC.Data.Sql.Initializer;
import UMC.Data.Utility;
import UMC.Web.*;

import java.util.HashMap;

public class SystemMenuActivity extends WebActivity {

    public void processActivity(WebRequest request, WebResponse response) {

        if (request.isMaster() == false) {
            this.prompt("只有管理员才能重置菜单");
        }
        this.asyncDialog("Confirm", g -> new UIConfirmDialog("将会清空现有菜单，同时清除菜单权限"));

//        DbProvider p = Database.instance().provider();


        Database factory = Database.instance();
        factory.objectEntity(Menu.class).delete();


        Initializer[] Initializers = Initializer.Initializers();


        ProviderConfiguration database = UMC.Data.ProviderConfiguration.configuration("Database");
        if (database == null) {
            database = new ProviderConfiguration();
        }
        for (Initializer initer : Initializers) {
            Provider provider1 = database.get(initer.providerName());
            if (provider1 != null) {
                if (Utility.isEmpty(provider1.attributes().get("setup")) == false) {
                    initer.Menu(new HashMap(), factory);

                }

            }
        }

        this.prompt("检测菜单已完成");


    }
}

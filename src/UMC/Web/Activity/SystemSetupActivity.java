package UMC.Web.Activity;

import UMC.Data.Database;
import UMC.Data.Provider;
import UMC.Data.ProviderConfiguration;
import UMC.Data.Sql.DbProvider;
import UMC.Data.Sql.Initializer;
import UMC.Data.Utility;
import UMC.Web.*;

import java.io.File;
import java.util.HashMap;

public class SystemSetupActivity extends WebActivity {
    @Override
    public void processActivity(WebRequest request, WebResponse response) {

        String type = this.asyncDialog("type", g ->
        {
            UISheetDialog fm = new UISheetDialog();
            fm.title("安装数据库");

//            fm.options().add(new UIClick("Oracle").text("Oracle数据库").send(request.model(), request.cmd()));
//            fm.options().add(new UIClick("MySql").text("MySql数据库").send(request.model(), request.cmd()));
//            fm.options().add(new UIClick("MSSQL").text("SQL Server数据库").send(request.model(), request.cmd()));
            fm.options().add(new UIClick("SQLite").text("SQLite数据库").send(request.model(), request.cmd()));
            return fm;
        });
        WebMeta Settings = this.asyncDialog(g ->
        {
            UIFormDialog fm = new UIFormDialog();
            fm.title("安装数据库");

            fm.addText("服务地址", "Server", "");
            fm.addText("用户名", "User", "");
            fm.addText("密码", "Password", "");
            fm.addText("数据库名", "Database", "");
            switch (type) {
                case "SQLite":
                    return this.dialogValue(new WebMeta().put("File", "UMC.java"));
//                    break;
                case "Oracle":
                    fm.addText("端口", "Port", "1521");
                    fm.addText("表前缀", "Prefix", "").put("tip", "分表设置");
                    fm.title("Oracle连接配置");
                    break;
                case "MySql":
                    fm.addText("端口", "Port", "3306");
                    fm.addText("表前缀", "Prefix", "").put("tip", "分表设置");
                    fm.title("MySql连接配置");
                    break;
                case "MSSQL":
                    fm.addText("端口", "Port", "1433");
                    fm.addText("表前缀", "Prefix", "").put("tip", "分表设置");
                    fm.addText("拆表符", "Delimiter", "_");

                    fm.title("SQL Server连接配置");
                    break;
                default:
                    this.prompt("数据类型错误");
                    break;
            }
            fm.submit("确认安装", request, "Initializer");
            return fm;
        }, "Settings");

        UMC.Data.Provider provder = null;


        switch (type) {
            case "SQLite":
                provder = UMC.Data.Provider.create("Database", UMC.Data.Sql.SQLIteDBProvider.class.getName());
                String filename = Settings.get("File") + ".sqlite";

                File file = new File(Utility.mapPath("~/App_Data/" + filename));

                if (file.exists()) {
                    // File.
                    int i = 1;
                    File m = new File(String.format("%s.%d.bak", file.toString(), i));
                    while (m.exists()) {
                        i++;
                        m = new File(String.format("%s.%d.bak", file.toString(), i));
                    }
                    file.renameTo(m);
                } else if (file.getParentFile().exists() == false) {
                    file.getParentFile().mkdirs();
                }
                provder.attributes().put("db", filename);// Settings["File"];
                break;
            case "Oracle":
                provder = UMC.Data.Provider.create("Database", UMC.Data.Sql.OracleDbProvider.class.getName());
//                provder.Attributes["conString"] = Oracle(Settings);

                provder.attributes().put("conString", String.format("jdbc:oracle:thin:@%s:%s:%s", Settings.get("Server"), Settings.get("Port"), Settings.get("Database")));

                provder.attributes().put("user", Settings.get("User"));
                provder.attributes().put("password", Settings.get("Password"));
                break;
            case "MySql":
                provder = UMC.Data.Provider.create("Database", UMC.Data.Sql.MysqlDbProvider.class.getName());
                provder.attributes().put("conString", String.format("jdbc:mysql://%s:%s/%s", Settings.get("Server"), Settings.get("Port"), Settings.get("Database")));
                provder.attributes().put("user", Settings.get("User"));
                provder.attributes().put("password", Settings.get("Password"));
                break;
            case "MSSQL":
                provder = UMC.Data.Provider.create("Database", UMC.Data.Sql.SqlDbProvider.class.getName());
                // provder.Attributes["conString"] = MSSQL(Settings);
                provder.attributes().put("conString", String.format("jdbc:sqlserver://%s:%s; DatabaseName=%s", Settings.get("Server"), Settings.get("Port"), Settings.get("Database")));
                provder.attributes().put("user", Settings.get("User"));
                provder.attributes().put("password", Settings.get("Password"));
                break;
            default:
                this.prompt("数据类型错误");
                break;
        }
        if (Utility.isEmpty(Settings.get("Prefix")) == false) {
            provder.attributes().put("delimiter", Utility.isNull(Settings.get("Delimiter"), "_"));
            provder.attributes().put("prefix", Settings.get("Prefix"));
        }
        DbProvider provider = (DbProvider) Utility.createInstance(provder);

        Database factory = Database.instance(provider);
        try {
            factory.open();
            factory.close();

        } catch (Exception ex) {
            this.prompt(ex.getMessage());
        }


        Initializer[] Initializers = Initializer.Initializers();


        ProviderConfiguration database = UMC.Data.ProviderConfiguration.configuration("database");
        if (database == null) {
            database = new ProviderConfiguration();
        }

        boolean count = false;
        for (Initializer initer : Initializers) {
            Provider provider1 = database.get(initer.providerName());
            if (provider1 == null) {
                count = true;
                initer.Setup(new HashMap(), provider);

                Provider de = UMC.Data.Provider.create(initer.providerName(), provder.type());
                de.attributes().putAll(provder.attributes());//.Add(provder.Attributes);
                database.set(de);
                initer.Menu(new HashMap(), factory);
            } else {

                if (Utility.isEmpty(provider1.attributes().get("setup")) == false) {
                    DbProvider dbProvider = (DbProvider) Utility.createInstance(provider1);
                    count = true;
                    initer.Setup(new HashMap(), dbProvider);
                    initer.Menu(new HashMap(), Database.instance(dbProvider));
                }

            }
        }

        UMC.Data.ProviderConfiguration.clear();
        if (count == false) {
            this.prompt("对应组件已经安装");
        } else {
            File file = new File(Utility.mapPath("App_Data\\UMC\\database.xml"));
//            if (file.exists()) {
//                // File.
//                int i = 1;
//                File m = new File(Utility.mapPath(String.format("App_Data\\UMC\\Database.xml.%d.bak", i)));
//                while (m.exists()) {
//                    i++;
//                    m = new File(Utility.mapPath(String.format("App_Data\\UMC\\Database.xml.%d.bak", i)));
//                }
//                file.renameTo(m);
//            }
            try {
                database.WriteTo(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.prompt("安装完成", "默认账户为admin,密码admin,请刷新页面", false);

        }

        this.context().send("Initializer", false);
    }
}

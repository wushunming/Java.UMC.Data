package UMC.Data.Entities;

import UMC.Data.Database;
import UMC.Data.Sql.IObjectEntity;
import UMC.Data.Utility;
import UMC.Net.NetContext;
import UMC.Security.Membership;

import java.io.IOException;
import java.util.*;

public class Initializer extends UMC.Data.Sql.Initializer {
    public Initializer() {
        this.Setup(new Account().User_id(Utility.uuidEmpty).Type(0), new Account().ConfigData(""));
        this.Setup(new Session().SessionKey(""), new Session().Content(""));
        this.Setup(new User().Username(""), "Password");
        this.Setup(new Wildcard().WildcardKey(""), new Wildcard().Authorizes(""));
        this.Setup(new UserToRole().Role_id(Utility.uuidEmpty).User_id(Utility.uuidEmpty));
        this.Setup(new Role().Id(Utility.uuidEmpty));
        this.Setup(new Menu().Id(Utility.uuidEmpty));
        this.Setup(new SearchKeyword().User_id(Utility.uuidEmpty).Keyword(""));
        this.Setup(new Location().Id(0));
    }

    @Override
    public String name() {
        return "UMC";
    }

    @Override
    public String caption() {
        return "UMC基础";
    }

    @Override
    public String providerName() {
        return "defaultDbProvider";
    }

    @Override
    public boolean Resource(NetContext context) {
        String path = context.getUrl().getPath();
        if (path.endsWith("/UMC.js") || path.endsWith("/Page.js")) {

            try {
                context.getOutput().write("UMC.UI.Config({'posurl': '/UMC/' + (UMC.cookie('device') || UMC.cookie('device', UMC.uuid()))}); ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.Resource(context);
    }

    @Override
    public void Menu(Map hash, Database factory) {

        factory.objectEntity(Menu.class)
                .insert(new Menu().Icon("\uf188")
                                .Caption("开发调试").Disable(false).ParentId(Utility.uuidEmpty).Seq(93)
                                .Url("#debug").Id(UUID.randomUUID()), new Menu().Icon("\uf0ae")
                                .Caption("菜单管理").Disable(false).ParentId(Utility.uuidEmpty).Seq(94)
                                .Url("#menu").Id(UUID.randomUUID())
                        , new Menu().Icon("\uf0c0")
                                .Caption("用户管理").Disable(false).ParentId(Utility.uuidEmpty).Seq(95)
                                .Url("#user").Id(UUID.randomUUID()));


    }

    @Override
    protected void Setup(Map hash, Database factory) {
        IObjectEntity<User> iObjectEntity = factory.objectEntity(User.class);
        if (iObjectEntity.count() == 0) {
            Role adminRole = new Role().Id(UUID.randomUUID()).Rolename(Membership.AdminRole)
                    .Explain("管理员");

            factory.objectEntity(Role.class).insert(adminRole, new Role().Id(UUID.randomUUID())
                            .Rolename(UMC.Security.Membership.UserRole).Explain("员工账户")
                    , new Role().Id(UUID.randomUUID())
                            .Rolename(UMC.Security.Membership.GuestRole).Explain("来客"));


            IObjectEntity<User> userEntiy = factory.objectEntity(User.class);

            UUID sn = UUID.randomUUID();

            userEntiy.insert(new User().Username("admin").Alias("管理员").RegistrTime(new Date())
                    .Member(false).Id(sn).Flags(UMC.Security.Membership.UserFlagsNormal));

            Map map = new HashMap();
            map.put("Password",
                    Base64.getEncoder().encodeToString(Utility.des("admin", sn)));

            userEntiy.update(map);

            factory.objectEntity(UserToRole.class)
                    .insert(new UserToRole().Role_id(adminRole.Id).User_id(sn));


        }


    }
}

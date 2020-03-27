package UMC.Data.Entities;

import java.util.UUID;

public class Role {
    public UUID Id;
    public String Rolename;
    public String Explain;

    public Role Id(UUID id) {
        Id = id;
        return this;
    }

    public Role Rolename(String rolename) {
        Rolename = rolename;
        return this;
    }

    public Role Explain(String explain) {
        Explain = explain;
        return this;
    }
}

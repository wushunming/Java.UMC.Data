package UMC.Data.Entities;

import java.util.UUID;

public class Account {

    public UUID user_id;


    public String Name;

    public Integer Type;

    public Integer Flags;

    public String ForId;

    public String ConfigData;

    public Account User_id(UUID user_id) {
        this.user_id = user_id;
        return this;
    }

    public Account Name(String name) {
        Name = name;
        return this;
    }

    public Account Type(Integer type) {
        Type = type;
        return this;
    }

    public Account Flags(Integer flags) {
        Flags = flags;
        return this;
    }

    public Account ForId(String forId) {
        ForId = forId;
        return this;
    }

    public Account ConfigData(String configData) {
        ConfigData = configData;
        return this;
    }
}
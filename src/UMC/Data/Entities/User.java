package UMC.Data.Entities;

import java.util.Date;
import java.util.UUID;

public class User {
    public String Username;
    public String Alias;
    public Integer Flags;
    public Date RegistrTime;
    public Date ActiveTime;
    public Integer VerifyTimes;
    public UUID SessionKey;
    public UUID Id;
    public Boolean IsMember;

    public User Username(String username) {
        Username = username;
        return this;
    }

    public User Alias(String alias) {
        Alias = alias;
        return this;
    }

    public User Flags(Integer flags) {
        Flags = flags;
        return this;
    }

    public User RegistrTime(Date registrTime) {
        RegistrTime = registrTime;
        return this;
    }

    public User ActiveTime(Date activeTime) {
        ActiveTime = activeTime;
        return this;
    }

    public User VerifyTimes(Integer verifyTimes) {
        VerifyTimes = verifyTimes;
        return this;
    }

    public User SessionKey(UUID sessionKey) {
        SessionKey = sessionKey;
        return this;
    }

    public User Id(UUID id) {
        Id = id;
        return this;
    }

    public User Member(Boolean member) {
        IsMember = member;
        return this;
    }
}

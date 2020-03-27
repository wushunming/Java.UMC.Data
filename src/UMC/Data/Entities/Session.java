package UMC.Data.Entities;

import java.util.Date;
import java.util.UUID;

public class Session {
    public String SessionKey;



    public UUID user_id;


    public String Content;


    public String ContentType;


    public Date UpdateTime;


    public String DeviceToken;

    public Session SessionKey(String sessionKey) {
        SessionKey = sessionKey;
        return this;
    }

    public Session User_id(UUID user_id) {
        this.user_id = user_id;
        return this;
    }

    public Session Content(String content) {
        Content = content;
        return this;
    }

    public Session ContentType(String contentType) {
        ContentType = contentType;
        return this;
    }

    public Session UpdateTime(Date updateTime) {
        UpdateTime = updateTime;
        return this;
    }

    public Session DeviceToken(String deviceToken) {
        DeviceToken = deviceToken;
        return this;
    }
}

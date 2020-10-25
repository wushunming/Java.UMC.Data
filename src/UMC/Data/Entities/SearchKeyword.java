package UMC.Data.Entities;

import java.util.UUID;

public class SearchKeyword {

    public String Keyword;
    public UUID user_id;
    public Integer Time;

    public SearchKeyword Keyword(String keyword) {
        Keyword = keyword;
        return this;
    }

    public SearchKeyword User_id(UUID user_id) {
        this.user_id = user_id;
        return this;
    }

    public SearchKeyword Time(Integer time) {
        Time = time;
        return this;
    }
}

package UMC.Data.Entities;

public class Wildcard {
    public String WildcardKey; 
    public String GroupBy;
    public String Description;
    public String Authorizes;

    public Wildcard WildcardKey(String wildcardKey) {
        WildcardKey = wildcardKey;
        return this;
    }

    public Wildcard GroupBy(String groupBy) {
        GroupBy = groupBy;
        return this;
    }

    public Wildcard Description(String description) {
        Description = description;
        return this;
    }

    public Wildcard Authorizes(String authorizes) {
        Authorizes = authorizes;
        return this;
    }
}

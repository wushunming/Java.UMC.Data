package UMC.Data.Entities;

public class Location {

    public Integer Id;
    public Integer Type;
    public String ZipCode;
    public String Name;
    public Integer ParentId;

    public Integer getId() {
        return Id;
    }

    public Location Id(Integer id) {
        Id = id;
        return this;
    }

    public Integer getType() {
        return Type;
    }

    public Location Type(Integer type) {
        Type = type;
        return this;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public Location ZipCode(String zipCode) {
        ZipCode = zipCode;
        return this;
    }

    public String getName() {
        return Name;
    }

    public Location Name(String name) {
        Name = name;
        return this;
    }

    public Integer getParentId() {
        return ParentId;
    }

    public Location ParentId(Integer parentId) {
        ParentId = parentId;
        return this;
    }
}

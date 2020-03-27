package UMC.Data.Sql;

public abstract class DbBuilder {

    public abstract String PrimaryKey(String name,  String... fields);

    public  String Schema(String Prefixion)
    {
        return "";
    }
    public  String Column(String field)
    {
        return field;// String.Empty;
    }
    public abstract String Integer();
    public abstract String Boolean();
    public abstract String String();
    public abstract String Text();

    public abstract String Number();
    public abstract String Date();
    public abstract String Guid();
    public abstract String Float();

    public abstract String AddColumn(String name, String field, String type);

    public abstract String DropColumn(String name, String field);
}

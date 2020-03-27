package UMC.Data.Sql;

public class SqlDbProvider extends DbProvider {
    static {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    class SqlBuilder extends DbBuilder
    {
        public  String AddColumn(String name, String field, String type)
        {
            return String.format("ALTER TABLE %s ADD %s %s", name, field, type);
        }

        public  String Boolean()
        {
            return "BIT";
        }

        public  String Date()
        {
            return "DATETIME";
        }

        public  String DropColumn(String name, String field)
        {
            return String.format("ALTER TABLE %s DROP COLUMN %s", name, field);
        }

        public  String Float()
        {
            return "FLOAT";
        }

        public  String Guid()
        {
            return "UNIQUEIDENTIFIER";
        }

        public  String Integer()
        {
            return "INTEGER";
        }

        public  String Number()
        {
            return "DECIMAL(16,2)";
        }

        public  String PrimaryKey(String name,  String... fields)
        {
            StringBuilder sb = new  StringBuilder();
            sb.append("ALTER TABLE ");
            sb.append(name.toUpperCase());
            sb.append(" ADD PRIMARY KEY (");// name);// id);")
            for (String s : fields)
            {
                sb.append( s);
                sb.append(',');

            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
            return sb.toString();

        }

        public  String String()
        {
            return "NVARCHAR(255)";
        }

        public  String Text()
        {
            return "NVARCHAR(MAX)";
        }
        public  String Schema(String Prefixion)
        {
            return String.format("CREATE SCHEMA %s AUTHORIZATION [dbo]", Prefixion);
        }
    }

    @Override
    public DbBuilder builder() {
        return new SqlBuilder();
    }

    @Override
    public String delimiter() {
        return super.delimiter();
    }

    @Override
    public String quotePrefix() {
        return "[";
    }

    @Override
    public String quoteSuffix() {
        return "]";
    }

    @Override
    public String paginationText(int start, int limit, String selectText) {
        StringBuilder sb = new StringBuilder(selectText);
        int topIndex = selectText.toLowerCase().indexOf("select") + 6;

        if (start > 0) {
            ;

            sb.insert(topIndex, String.format(" TOP %d ", start + limit));
            sb.insert(0, "SELECT IDENTITY(INT,0,1) AS __WDK_Page_ID , WebADNukePagge.* INTO #__WebADNukePagges FROM(");

            sb.append(") AS WebADNukePagge");
            sb.append("\n");
            sb.append("SELECT *FROM  #__WebADNukePagges  WHERE __WDK_Page_ID >=");
            sb.append(start);
            sb.append("\n");

            sb.append("DROP TABLE #__WebADNukePagges");

        } else {
            sb.insert(topIndex, String.format(" TOP %d", limit));
        }
        return sb.toString();

    }
}

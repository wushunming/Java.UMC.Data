package UMC.Data.Sql;

public class OracleDbProvider extends DbProvider {
    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

  static   class OracleBuilder  extends DbBuilder
    {
        public  String AddColumn(String name, String field, String type)
        {
            return String.format("ALTER TABLE %s ADD(%s %s)", name, field, type);
        }

        public  String Boolean()
        {
            return "TINYINT";
        }

        public  String Date()
        {
            return "DATE";
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
            return "CHAR(36)";
        }

        public  String Integer()
        {
            return "INTEGER";
        }
        public  String Column(String field)
        {
            return field.toUpperCase();
        }

        public  String Number()
        {
            return "NUMBER(16,2)";
        }

        public  String PrimaryKey(String name,  String... fields)
        {
            StringBuilder sb = new  StringBuilder();
            sb.append("ALTER TABLE ");
            sb.append(name.toUpperCase());
            sb.append(" ADD PRIMARY KEY (");// name);// id);")
            for (String s : fields)
            {
                sb.append( s.toUpperCase());
                sb.append(',');

            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
            return sb.toString();

        }

        public  String String()
        {
            return "NVARCHAR2(255)";
        }

        public  String Text()
        {
            return "VARCHAR2(4000)";
        }
    }

    @Override
    public DbBuilder builder() {
        return new OracleBuilder();
    }

    @Override
    public String quotePrefix() {
        return "\"";
    }

    @Override
    public String quoteSuffix() {
        return "\"";
    }

    @Override
    public String paginationText(int start, int limit, String selectText) {
        return String.format("SELECT * FROM (SELECT A.*, ROWNUM R__ FROM (%s) A WHERE ROWNUM <= %d)WHERE R__ > %d ", selectText, start + limit, start);

    }
}

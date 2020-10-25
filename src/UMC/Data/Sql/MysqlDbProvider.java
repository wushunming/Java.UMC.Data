package UMC.Data.Sql;

public class MysqlDbProvider extends DbProvider {
    static {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Throwable e) {
           System.out.println("not mysql jdbc.Driver");
        }
    }

    static class MySqlBuilder extends DbBuilder {
        public String PrimaryKey(String name, String... fields) {
            StringBuilder sb = new StringBuilder();
            sb.append("ALTER TABLE ");
            sb.append(name);
            sb.append(" DROP PRIMARY KEY,ADD PRIMARY KEY (");// name);// id);")
            for (String s : fields) {
                sb.append(s);
                sb.append(',');

            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
            return sb.toString();
        }


        public String AddColumn(String name, String field, String type) {
            return String.format("ALTER TABLE %s ADD %s %s ", name, field, type);
        }


        public String Boolean() {
            return "TINYINT";
        }

        public String Date() {
            return "DateTime";
        }

        public String DropColumn(String name, String field) {
            return String.format("ALTER TABLE %s DROP %s", name, field);
        }

        public String Float() {
            return "FLOAT";
        }

        @Override
        public Boolean Check(String name, ISqler sqler) {


            int m = (int)(sqler.executeScalar("select count(*) from information_schema.tables where table_name = {0}", name));
            return m > 0;
        }

        public Boolean Check(String name, String field, ISqler sqler) {

            int m = (int) (sqler.executeScalar("select count(*) from information_schema.columns where table_name = {0} and column_name = {1}", name, field));
            return m > 0;
        }


        public String Guid() {
            return "CHAR(36)";
        }

        public String Integer() {
            return "INTEGER";
        }

        public String Number() {
            return "decimal(16,2)";
        }


        public String String() {
            return "VARCHAR(255)";
        }

        public String Text() {
            return "TEXT";
        }
    }

    @Override
    public DbBuilder builder() {
        return new MySqlBuilder();
    }

    @Override
    public String quotePrefix() {
        return "`";
    }

    @Override
    public String quoteSuffix() {
        return "`";
    }

    @Override
    public String paginationText(int start, int limit, String selectText) {
        return String.format("%s limit %d,%d", selectText, start, limit);

    }
}

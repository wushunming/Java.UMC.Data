package UMC.Data.Sql;

import UMC.Data.Utility;

import java.io.File;
import java.io.IOException;

public class SQLIteDBProvider extends DbProvider {
    static {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static class SQLiteBuilder extends DbBuilder {
        public String AddColumn(String name, String field, String type) {
            return "";// String.Format("ALTER TABLE {0} ADD {1} {2} ", name, field, type);
        }

        public String Boolean() {
            return "INTEGER";
        }

        public String Date() {
            return "DATETIME";
        }

        public String DropColumn(String name, String field) {
            return String.format("ALTER TABLE %s DROP %s", name, field);
        }

        public String Float() {
            return "REAL";
        }

        public String Guid() {
            return "CHAR(36)";
        }

        public String Integer() {
            return "INTEGER";
        }

        public String Number() {
            return "NUMERIC";
        }

        public String PrimaryKey(String name, String... fields) {
            return "";
        }

        public String String() {
            return "CHAR(255)";
        }

        public String Text() {
            return "CHAR";
        }
    }

    @Override
    public DbBuilder builder() {
        return new SQLiteBuilder();
    }

    @Override
    public String conntionString() {
        String name = Utility.isNull(this.provider.get("db"), "umc.db");
        File file = new File(Utility.mapPath("~/App_Data/" + name));
//        if (file.exists() == false) {
//            if (file.getParentFile().exists() == false) {
//                file.getParentFile().mkdirs();
//            }
//        }

        return "jdbc:sqlite:" + file.toPath().toString();

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
        return String.format("%s limit %d,%d", selectText, start, limit);

    }
}

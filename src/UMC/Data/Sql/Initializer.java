package UMC.Data.Sql;

import UMC.Data.Database;
import UMC.Data.Utility;
import UMC.Net.NetContext;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

public abstract class Initializer {


    public static List<Class> __initializers = new LinkedList<>();

    public static Initializer[] Initializers() {

        List<Initializer> its = new LinkedList<>();
        its.add(new UMC.Data.Entities.Initializer());

        for (Class type : __initializers) {
            Initializer t = null;
            try {
                t = (Initializer) type.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (t instanceof UMC.Data.Entities.Initializer) {
                its.remove(0);
                its.add(0, t);
            } else {
                its.add(t);
            }

        }
        return its.toArray(new Initializer[0]);
    }

    public int pageIndex() {
        return 100;
    }

    public abstract String name();

    public abstract String caption();

    public abstract String providerName();

    public boolean Resource(NetContext context) {
        return false;
    }

    public String resourceJS() {
        return "";
    }

    public abstract void Menu(Map hash, Database factory);

    protected abstract void Setup(Map hash, Database factory);

    private Map<Object, Object> dictionary = new HashMap<>();

    protected <T> void Setup(T key)

    {
        dictionary.put(key, null);
    }

    protected <T> void Setup(T key, T text)

    {
        dictionary.put(key, text);

    }

    protected <T> void Setup(T key, String... fields)

    {
        dictionary.put(key, fields);

    }

    public void Check(DbProvider provider) {

        DbBuilder builder = provider.builder();
        if (builder == null) {
            return;
        }
        Database factory = Database.instance(provider);
        ISqler sqler = factory.sqler(false);


        for (Map.Entry<Object, Object> value : dictionary.entrySet()) {

            CheckTable(sqler, provider, value.getKey(), value.getValue());
        }

    }

    void CheckTable(ISqler sqler, DbProvider provider, Object key, Object value) {


        String tabName = key.getClass().getSimpleName();

        Map<String, Object> keys = SqlUtils.fieldMap(key);
        Map<String, Object> textKeys = new HashMap<>();
        if (value != null) {
            if (value instanceof String) {
                value = new String[]{(String) value};
            } else if (value.getClass().isArray() == false) {
                textKeys = SqlUtils.fieldMap(value);
            }

        }

        String Delimiter = provider.delimiter();
        //var provider = sqler.DbProvider;
        DbBuilder builder = provider.builder();

        if (Utility.isEmpty(Delimiter)) {
            tabName = String.format("%s%s%s", provider.quotePrefix(), tabName, provider.quoteSuffix());
        } else {
            if (Utility.isEmpty(provider.prefixion())) {
                tabName = String.format("%s%s%s", provider.quotePrefix(), tabName, provider.quoteSuffix());
            } else {
                switch (Delimiter) {
                    case ".":
                        tabName = String.format("%s%s%s.%s%s%s", provider.quotePrefix(), provider.prefixion(), provider.quoteSuffix(), provider.quotePrefix(), tabName, provider.quoteSuffix());
                        break;
                    default:
                        tabName = String.format("%s%s%s%s%s", provider.quotePrefix(), provider.prefixion(), Delimiter, tabName, provider.quoteSuffix());
                        break;
                }
            }

        }


//        tabName = GetName(provider, tabName);
        if (builder.Check(tabName.replace(provider.quotePrefix(), "").replace(provider.quoteSuffix(), ""), sqler) == false) {
            CreateTable(sqler, provider, key, value);
        } else {
            Field[] ps = key.getClass().getFields();
            for (Field property : ps) {
                String filed = String.format("%s%s%s ", provider.quotePrefix(), property.getName(), provider.quoteSuffix());

                //   String filed = String.Format("{0}{2}{1} ", provider.QuotePrefix, provider.QuoteSuffix, property.Name);
                String cfiled = builder.Column(filed);
                if (builder.Check(tabName.replace(provider.quotePrefix(), "").replace(provider.quoteSuffix(), "").trim(), cfiled.replace(provider.quoteSuffix(), "").replace(provider.quotePrefix(), "").trim(), sqler) == false) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(builder.Column(filed));
                    Class type = property.getType();

                    if (type.equals(Byte.class) || type.equals(Short.class) || type.equals(Integer.class)) {

                        sb.append(builder.Integer());
                    } else if (type.equals(Double.class) || type.equals(Long.class)) {
                        sb.append(builder.Number());
                    } else if (type.equals(Float.class)) {
                        sb.append(builder.Float());
                    } else if (type.equals(Boolean.class)) {
                        sb.append(builder.Boolean());
                    } else if (type.equals(Date.class)) {
                        sb.append(builder.Date());
                    } else if (type.equals(UUID.class)) {
                        sb.append(builder.Guid());
//                Decimal

                    } else {
                        if (textKeys.containsKey(property.getName())) {

                            sb.append(builder.Text());
                        } else {

                            sb.append(builder.String());
                        }
                    }

                    if (sb.length() > 0) {
                        sqler.executeNonQuery(sb.toString());
                    }
                }
            }
            if (value != null) {
                if (SqlParamer.isArray(value)) {

                }
                if (value instanceof Array) {

                    int length = Array.getLength(value);

                    for (int i = 0; i < length; i++) {
                        String f = (String) Array.get(value, i);

                        String filed = builder.Column(String.format("%s%s%s ", provider.quotePrefix(), provider.quoteSuffix(), f));
                        if (builder.Check(tabName.replace(provider.quotePrefix(), "").replace(provider.quotePrefix(), "").trim(), filed.replace(provider.quotePrefix(), "").replace(provider.quotePrefix(), "").trim(), sqler) == false) {
//                            log.Info("追加表字段", tabName + '.' + filed);
                            sqler.executeNonQuery(builder.AddColumn(tabName, filed, builder.String()));
                        }
                    }
                }
            }
        }

    }

    void CreateTable(ISqler sqler, DbProvider provider, Object key, Object value) {


        String tabName = key.getClass().getSimpleName();
        //   log.Info("创建表", tabName);

        Map<String, Object> keys = SqlUtils.fieldMap(key);
        Map<String, Object> textKeys = new HashMap<>();
        if (value != null) {
            if (value instanceof String) {
                value = new String[]{(String) value};
            } else if (value.getClass().isArray() == false) {
                textKeys = SqlUtils.fieldMap(value);
            }

        }
        String Delimiter = provider.delimiter();
        //var provider = sqler.DbProvider;
        DbBuilder builder = provider.builder();

        if (Utility.isEmpty(Delimiter)) {
            tabName = String.format("%s%s%s", provider.quotePrefix(), tabName, provider.quoteSuffix());
        } else {
            if (Utility.isEmpty(provider.prefixion())) {
                tabName = String.format("%s%s%s", provider.quotePrefix(), tabName, provider.quoteSuffix());
            } else {
                switch (Delimiter) {
                    case ".":
                        tabName = String.format("%s%s%s.%s%s%s", provider.quotePrefix(), provider.prefixion(), provider.quoteSuffix(), provider.quotePrefix(), tabName, provider.quoteSuffix());
                        break;
                    default:
                        tabName = String.format("%s%s%s%s%s", provider.quotePrefix(), provider.prefixion(), Delimiter, tabName, provider.quoteSuffix());
                        break;
                }
            }

        }
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(tabName);
        sb.append("(");
        Field[] ps = key.getClass().getFields();
        for (Field property : ps) {
            String filed = String.format("%s%s%s ", provider.quotePrefix(), property.getName(), provider.quoteSuffix());

            sb.append(builder.Column(filed));
            Class type = property.getType();

            if (type.equals(Byte.class) || type.equals(Short.class) || type.equals(Integer.class)) {

                sb.append(builder.Integer());
            } else if (type.equals(Double.class) || type.equals(Long.class)) {
                sb.append(builder.Number());
            } else if (type.equals(Float.class)) {
                sb.append(builder.Float());
            } else if (type.equals(Boolean.class)) {
                sb.append(builder.Boolean());
            } else if (type.equals(Date.class)) {
                sb.append(builder.Date());
            } else if (type.equals(UUID.class)) {
                sb.append(builder.Guid());
//                Decimal

            } else {
                if (textKeys.containsKey(property.getName())) {

                    sb.append(builder.Text());
                } else {

                    sb.append(builder.String());
                }
            }

            if (keys.containsKey(property.getName())) {
                sb.append(" NOT NULL");
            }
            sb.append(",");
        }
        if (value != null) {
            if (value.getClass().isArray()) {
                String[] fs = (String[]) value;
                for (String f : fs) {

                    String filed = String.format("%s%s%s ", provider.quotePrefix(), f, provider.quoteSuffix());
                    sb.append(builder.Column(filed));
                    sb.append(builder.String());
                    sb.append(",");
                }
            }
        }
        sb.deleteCharAt(sb.length() - 1);

        sb.append(")");
        try {
            sqler.executeNonQuery(sb.toString());
        } catch (Exception ex) {
            // log.Error(ex.Message);
        }
        if (keys.size() > 0) {
            List<String> ids = new LinkedList<>();
            for (Map.Entry<String, Object> set : keys.entrySet()) {

                String filed = String.format("%s%s%s", provider.quotePrefix(), set.getKey(), provider.quoteSuffix());
                ids.add(filed);
            }
            String sql = builder.PrimaryKey(tabName, ids.toArray(new String[0]));
            if (Utility.isEmpty(sql) == false) {
                try {
                    sqler.executeNonQuery(sql);
                } catch (Exception ex) {
                    //log.Error("创建主键" + String.Join(",", ids.ToArray()), ex.Message);
                }
            }
        }


    }

    public void Setup(Map args, DbProvider provider) {
        DbBuilder builder = provider.builder();
        if (builder == null) {

            return;
        }
        Database factory = Database.instance(provider);
        ISqler sqler = factory.sqler(false);


        String Delimiter = provider.delimiter();

        if (".".equals(Delimiter) && Utility.isEmpty(provider.prefixion()) == false) {
            String prefixion = String.format("%s%s%s", provider.quoteSuffix(), provider.prefixion(), provider.quoteSuffix());
            String schemaSQL = builder.Schema(prefixion);
            if (Utility.isEmpty(schemaSQL) == false) {
                try {
                    sqler.executeNonQuery(schemaSQL);
                } catch (Exception ex) {
                    //  log.Error(ex.Message);
                }
            }
        }
        for (Map.Entry<Object, Object> value : dictionary.entrySet()) {

            CreateTable(sqler, provider, value.getKey(), value.getValue());
        }
        this.Setup(args, factory);

    }


}

package UMC.Data.Sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IResultReader {
    void  reader(ResultSet resultSet) throws SQLException;
}
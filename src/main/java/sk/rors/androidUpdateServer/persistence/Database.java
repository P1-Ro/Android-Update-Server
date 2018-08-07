package sk.rors.androidUpdateServer.persistence;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;
import org.reflections.Reflections;
import java.sql.SQLException;
import java.util.Set;

public class Database {

    private ConnectionSource connection;
    private static Database instance;

    private Database() {
        String USERNAME =  System.getenv().get("DB_USERNAME");
        String PASSWORD = System.getenv().get("DB_PASSWORD");
        String URL = System.getenv().get("DATABASE_URL");

        try {
            connection = new JdbcConnectionSource(URL, USERNAME, PASSWORD);
            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    public  <D extends Dao<T, String>, T> D getDao(Class<T> clazz) throws SQLException {
        return DaoManager.createDao(connection, clazz);
    }

    private void createTables() throws SQLException {
        Reflections reflections = new Reflections("sk.rors.androidUpdateServer.model");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(DatabaseTable.class);

        for (Class<?> clazz : classes) {
            TableUtils.createTableIfNotExists(connection, clazz);
        }
    }

}

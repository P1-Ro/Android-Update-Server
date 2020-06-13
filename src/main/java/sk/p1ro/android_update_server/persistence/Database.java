package sk.p1ro.android_update_server.persistence;


public class Database {

    private static Database instance;

    private Database() {
        String USERNAME =  System.getenv().get("DB_USERNAME");
        String PASSWORD = System.getenv().get("DB_PASSWORD");
        String tmpUrl = System.getenv().get("DATABASE_URL");

        String URL = "jdbc:postgresql://" + tmpUrl.substring(tmpUrl.indexOf("@") + 1) + "?max_allowed_packet=268435456";
        System.out.println(URL);
    }
}

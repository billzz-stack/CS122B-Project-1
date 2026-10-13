import java.sql.*;
public final class Db {
    private Db() {}
    public static Connection connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/moviedb";
        String user = System.getenv().getOrDefault("MOVIEDB_USER", "root");
        String password = System.getenv("MOVIEDB_PASSWORD");
        if (password == null) throw new SQLException("Set MOVIEDB_PASSWORD in the Tomcat run configuration environment variables");
        return DriverManager.getConnection(url, user, password);
    }
    public static String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }
    public static String url(String value) {
        return java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8);
    }
    public static void start(java.io.PrintWriter out, String title) {
        out.println("<!doctype html><html><head><meta charset='UTF-8'><title>" + escape(title) + "</title><style>body{font-family:Arial,sans-serif;max-width:1100px;margin:35px auto;padding:0 20px;line-height:1.5}table{border-collapse:collapse;width:100%}th,td{border:1px solid #ddd;padding:10px;text-align:left;vertical-align:top}th{background:#eee}a{color:#1657a5}li{margin:5px 0}</style></head><body><p><a href='movies'>Movie List</a></p>");
    }
}

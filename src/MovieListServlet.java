import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

@WebServlet("/movies")
public class MovieListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = res.getWriter()) {
            Db.start(out, "Top 20 Movies");
            out.println("<h1>Top 20 Rated Movies</h1><table><tr><th>Title</th><th>Year</th><th>Director</th><th>First 3 genres</th><th>First 3 stars</th><th>Rating</th></tr>");
            String sql = "SELECT m.id,m.title,m.year,m.director,r.rating FROM movies m JOIN ratings r ON r.movie_id=m.id ORDER BY r.rating DESC,m.title ASC,m.id ASC LIMIT 20";
            try (Connection c = Db.connect(); PreparedStatement movies = c.prepareStatement(sql);
                 PreparedStatement genres = c.prepareStatement("SELECT g.name FROM genres g JOIN genres_in_movies gm ON gm.genre_id=g.id WHERE gm.movie_id=? ORDER BY g.name LIMIT 3");
                 PreparedStatement stars = c.prepareStatement("SELECT s.id,s.name FROM stars s JOIN stars_in_movies sm ON sm.star_id=s.id WHERE sm.movie_id=? ORDER BY s.name,s.id LIMIT 3");
                 ResultSet rs = movies.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id");
                    out.println("<tr><td><a href='single-movie?id="+Db.url(id)+"'>"+Db.escape(rs.getString("title"))+"</a></td><td>"+rs.getInt("year")+"</td><td>"+Db.escape(rs.getString("director"))+"</td>");
                    genres.setString(1,id);
                    out.print("<td>");
                    try(ResultSet gr=genres.executeQuery()) { boolean first=true; while(gr.next()){if(!first)out.print(", ");out.print(Db.escape(gr.getString(1)));first=false;} }
                    out.print("</td><td>");
                    stars.setString(1,id);
                    try(ResultSet sr=stars.executeQuery()) { boolean first=true; while(sr.next()){if(!first)out.print(", ");out.print("<a href='single-star?id="+Db.url(sr.getString("id"))+"'>"+Db.escape(sr.getString("name"))+"</a>");first=false;} }
                    out.println("</td><td>"+rs.getFloat("rating")+"</td></tr>");
                }
            } catch (SQLException e) {
            e.printStackTrace();
            getServletContext().log("Movie list query failed", e);
            out.println("</table><p>Database error. Check Tomcat logs and database credentials.</p>");
        }
        }
    }
}

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
@WebServlet("/single-movie")
public class SingleMovieServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException {
        res.setContentType("text/html;charset=UTF-8");
        String id=req.getParameter("id");
        if(id==null||id.isEmpty()){res.sendError(400,"Movie ID required");return;}
        try(PrintWriter out=res.getWriter()){
            Db.start(out,"Movie Details");
            try(Connection c=Db.connect();
                PreparedStatement movie=c.prepareStatement("SELECT m.title,m.year,m.director,r.rating FROM movies m LEFT JOIN ratings r ON r.movie_id=m.id WHERE m.id=?");
                PreparedStatement genres=c.prepareStatement("SELECT g.name FROM genres g JOIN genres_in_movies gm ON gm.genre_id=g.id WHERE gm.movie_id=? ORDER BY g.name");
                PreparedStatement stars=c.prepareStatement("SELECT s.id,s.name FROM stars s JOIN stars_in_movies sm ON sm.star_id=s.id WHERE sm.movie_id=? ORDER BY s.name,s.id")){
                movie.setString(1,id);
                try(ResultSet rs=movie.executeQuery()){
                    if(!rs.next()){res.setStatus(404);out.println("<h1>Movie not found</h1>");return;}
                    out.println("<h1>"+Db.escape(rs.getString("title"))+"</h1><p><strong>Year:</strong> "+rs.getInt("year")+"</p><p><strong>Director:</strong> "+Db.escape(rs.getString("director"))+"</p><p><strong>Rating:</strong> "+(rs.getObject("rating")==null?"N/A":rs.getFloat("rating"))+"</p>");
                }
                out.println("<h2>Genres</h2><ul>");genres.setString(1,id);
                try(ResultSet rs=genres.executeQuery()){while(rs.next())out.println("<li>"+Db.escape(rs.getString(1))+"</li>");}
                out.println("</ul><h2>Stars</h2><ul>");stars.setString(1,id);
                try(ResultSet rs=stars.executeQuery()){while(rs.next())out.println("<li><a href='single-star?id="+Db.url(rs.getString("id"))+"'>"+Db.escape(rs.getString("name"))+"</a></li>");}
                out.println("</ul>");
            }catch(SQLException e){getServletContext().log("Movie details query failed",e);out.println("<p>Database error. Check Tomcat logs.</p>");}
            out.println("</body></html>");
        }
    }
}

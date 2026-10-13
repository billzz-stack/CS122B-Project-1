import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
@WebServlet("/single-star")
public class SingleStarServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException {
        res.setContentType("text/html;charset=UTF-8");
        String id=req.getParameter("id");
        if(id==null||id.isEmpty()){res.sendError(400,"Star ID required");return;}
        try(PrintWriter out=res.getWriter()){
            Db.start(out,"Star Details");
            try(Connection c=Db.connect();
                PreparedStatement star=c.prepareStatement("SELECT name,birth_year FROM stars WHERE id=?");
                PreparedStatement movies=c.prepareStatement("SELECT m.id,m.title FROM movies m JOIN stars_in_movies sm ON sm.movie_id=m.id WHERE sm.star_id=? ORDER BY m.title,m.id")){
                star.setString(1,id);
                try(ResultSet rs=star.executeQuery()){
                    if(!rs.next()){res.setStatus(404);out.println("<h1>Star not found</h1>");return;}
                    out.println("<h1>"+Db.escape(rs.getString("name"))+"</h1><p><strong>Birth year:</strong> "+(rs.getObject("birth_year")==null?"N/A":rs.getInt("birth_year"))+"</p>");
                }
                out.println("<h2>Movies</h2><ul>");movies.setString(1,id);
                try(ResultSet rs=movies.executeQuery()){while(rs.next())out.println("<li><a href='single-movie?id="+Db.url(rs.getString("id"))+"'>"+Db.escape(rs.getString("title"))+"</a></li>");}
                out.println("</ul>");
            }catch(SQLException e){getServletContext().log("Star details query failed",e);out.println("<p>Database error. Check Tomcat logs.</p>");}
            out.println("</body></html>");
        }
    }
}



import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Main
 */
@WebServlet("/Main")
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Main() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		 PrintWriter out = response.getWriter();
		 out.println("<HTML>" +"<HEAD><TITLE>" +"Main Page" +
                "</TITLE></HEAD>\n<BODY>" +
                "<P>Main page reached</P>" );
		 out.println("<form action=/Fabflix/MovieList method='get'>");
		 out.println("<input type=\"hidden\" name=\"by\" value=\"search\" />");
		 out.println("<input type=\"text\" name=\"arg\"   Click here to search a movie...\"  maxlength=\"64\" />");
		 out.println("<input type=\"hidden\" name=\"order\" value=\"asc\" />");
	
		 out.println("<button class=\"btn\" title=\"Submit Search\">Search</button>");
		 out.println("</form><Br>");
		String loginUser = "root";
	    String loginPasswd = "decodder";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        response.setContentType("text/html");    // Response mime type

					        // Output stream to STDOUT
	   try
	   {
					              //Class.forName("org.gjt.mm.mysql.Driver");
           Class.forName("com.mysql.jdbc.Driver").newInstance();
           Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
					              // Declare our statement
		  Statement statement = dbcon.createStatement();
		  
	   String genre_query="Select distinct name,id from moviedb.genres join genres_in_movies on genres_in_movies.genre_id=moviedb.genres.id order by name asc";
	   ResultSet rs = statement.executeQuery(genre_query);
	   out.println("<Br>");
	   while(rs.next()){
		   
		   String genre=rs.getString("name");
		   int id = rs.getInt("id");
		   out.println("<a href=/Fabflix/MovieList?by=genre&arg="+id+">"+genre+"</a>");
	   }
	  out.println("<Br><Br>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=A>A</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=B>B</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=C>C</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=D>D</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=E>E</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=F>F</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=G>G</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=H>H</a>");
	   
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=I>I</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=J>J</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=K>K</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=L>L</a>");
	   
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=N>N</a>");
	   
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=M>M</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=O>O</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=P>P</a>");
	   
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=Q>Q</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=R>R</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=S>S</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=T>T</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=U>U</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=V>V</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=W>W</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=X>X</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=Y>Y</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=Z>Z</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=1>1</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=2>2</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=3>3</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=4>4</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=5>5</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=6>6</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=7>7</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=8>8</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=9>9</a>");
	   out.println("<a href=/Fabflix/MovieList?by=title&arg=0>0</a>");
	   
	   }
		  catch (SQLException ex) {
              while (ex != null) {
                    System.out.println ("SQL Exception:  " + ex.getMessage ());
                    ex = ex.getNextException ();
                }  // end while
            }  // end catch SQLException

        catch(java.lang.Exception ex)
            {
                out.println("<HTML>" +
                            "<HEAD><TITLE>" +
                            "MovieDB: Error" +
                            "</TITLE></HEAD>\n<BODY>" +
                            "<P>SQL error in doGet: " +
                            ex.getMessage() + "</P></BODY></HTML>");
                return;
            }
		
	
      
		
		out.print("</P></BODY></HTML>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

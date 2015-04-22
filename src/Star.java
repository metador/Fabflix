

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Stack;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class Star
 */
@WebServlet("/Star")
public class Star extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource dataSource;
    private Connection connection;
	headerFooter base = new headerFooter();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Star() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		try {
            // Get DataSource
            Context initContext  = new InitialContext();
            dataSource = (DataSource) initContext.lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			print(response, request);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void print(HttpServletResponse response, HttpServletRequest request) throws SQLException, IOException
	{
		connection = (Connection) dataSource.getConnection();
		int star_id = Integer.parseInt(request.getParameter("StarID"));
		String query = "Select * from stars where id like '" + star_id + "'";
		PreparedStatement ps_star = (PreparedStatement) connection.prepareStatement(query);
		ResultSet star = ps_star.executeQuery();
		PrintWriter out = response.getWriter();
		
		out.println(base.header());
		out.println("<HEAD><TITLE>Star Info</TITLE></HEAD>");
		out.println(base.banner());
		
		String message = request.getParameter("message");
		if (star.next())
		{
			do
			{
				String movie_query = "Select distinct(a.title), a.year, a.id from movies a "
						+ "where a.id in (select distinct(b.movie_id) from stars_in_movies b "
						+ "where b.star_id in (select distinct(c.id)  from stars c where "
						+ "c.first_name = '" + star.getString("first_name") + "' and c.last_name = '" + star.getString("last_name") + "'));";
				PreparedStatement ps_movies = (PreparedStatement) connection.prepareStatement(movie_query);
				ResultSet movies = ps_movies.executeQuery();
				
				
				
				out.println("<tr><td><br><br>" + 
						"<div>" + 
						"<table  id='movie_search' style='margin-left:20%;margin-right:20%;'><tr><td width=\"20%;\"><div id=\"mov_list\">" + 
						"<img  style=\"position:absolute;z-index:1;margin-top:30px;margin-left:75px;\" src=\"" + star.getString("photo_url") + "\"  alt=\"" + star.getString("first_name") + " " + star.getString("last_name") + "'s Profile Photo\" height='188' width='120'>" + 
						"<img style=\"z-index:2;\" src=\"http://gateway.hopto.org:9000/fabflix/images/short-case.png\" height='250' width='255'></div></td>" + 
						"<td width=\"40%;\"><div id=\"mov_det\">" + 
						"<span style=\"font-weight: bold;\">Actor : " + star.getString("first_name") + " " + star.getString("last_name") + "</span><br>" +
						"<br><span>Date of Birth : " + star.getString("dob") + "</span><br>"
						+ "<br><div style=\"float:left;width:20%;\"><span style=\"font-style: italic;\">Starred In : </span></div><div style=\"float:right;width:80%;\"><span>");
				String movie = "Dummy Name";
				while (movies.next())
				{
					if (!movie.equals(movies.getString("title") + movies.getString("year")))
						out.println("<a class=\"ag_links\" href=\"/Fabflix/Movie?MovieID=" + movies.getString("id") + "\">"
							+ movies.getString("title") + " (" + movies.getString("year") + ")" + "</a>");
					movie = movies.getString("title") + movies.getString("year");
				} 
	         out.println("</span></div></div></td></tr></table>" + 
						"</div>" + 
						"<br></td></tr>"); 
				
				
				

				
				/*out.println("<div id=\"container\"><div id=\"image\">");
				out.println("<img style=\"width:110;height;160;\"src=\"" + star.getString("photo_url")
						+ "\" alt=\"" + star.getString("first_name") + " " + star.getString("last_name") + " Photo\"></div>");
				out.println("<div id=\"details\"><span class=\"title\">Star Name = " + star.getString("first_name") + " " + star.getString("last_name")  + "</span><br>");
				out.println("<span class=\"title\">Date of Birth = " + star.getString("dob") + "</span><br>");
				out.println("<span class=\"title\">Starred In = ");
				String movie_list = "";
				while (movies.next())
				{
					movie_list += (movies.getString(1) + " " + movies.getString(2) + "<br>");
				}
				movie_list = movie_list.substring(0, movie_list.length()-4);
				out.println(movie_list + "</span><br>");*/
			} while (star.next());
		}
		else
		{
			String mess="Username or password incorrect";
			response.sendRedirect("/Fabflix/index.html?message="+mess);  
			out.println("<tr>" + "<td>" + message+ "</td>" +"</tr>");
		}
		
		out.println("<br><br><br>" + base.footer());
	}
	

	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

import java.sql.*;
import java.util.Stack;
import java.io.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class MovieList
 */
@WebServlet("/MovieList")
public class MovieList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource dataSource;
    private Connection connection;
       
    /**
     * @param connection 
     * @see HttpServlet#HttpServlet()
     */ 
    public void init() throws ServletException
    {
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		// TODO Auto-generated method stub;
		PrintWriter out = response.getWriter();
		String sort_by = request.getParameter("by");
		String query=null;
		String query_count="0";
	String spage_id= request.getParameter("page_id");
	String sipp= request.getParameter("ipp");
	if(spage_id == null)
		spage_id ="1";
	String orderby= request.getParameter("order");
		if(orderby == null)
			orderby ="ASC";
	if(sipp==null)
		sipp="5";
	int page_id=Integer.parseInt(spage_id)-1;
	int ipp=Integer.parseInt(sipp);
	
		switch(sort_by){
		
		case "genre" :
			String genre_id=request.getParameter("arg");
			query="select * from movies "+
			      "join  genres_in_movies on movies.id=genres_in_movies.movie_id "+
				  "where genres_in_movies.genre_id="+genre_id+" order by title "+orderby+" LIMIT "+ipp+" OFFSET "+ipp*page_id; 
			query_count="select count(*) from movies "+
				      "join  genres_in_movies on movies.id=genres_in_movies.movie_id "+
					  "where genres_in_movies.genre_id="+genre_id; 
			
			break;
		case "title" :
			String title=request.getParameter("arg");
			query="Select * from movies where title like '"+title+"%'"+" order by title "+orderby+"  LIMIT "+ipp+" OFFSET "+ipp*page_id ;
			query_count="Select count(*) from movies where title like '"+title+"%'";
			
			break;
			
		case "search" :
			String search_term=request.getParameter("arg");
			query="Select * from movies where title like '%"+search_term.replace("'", "''")+"%'"+" order by title "+orderby+"  LIMIT "+ipp+" OFFSET "+ipp*page_id;
			query_count="Select count(*) from movies where title like '% "+search_term.replace("'", "''")+"%'";
			//out.println(query);
			break;
		default:
			break;
		}
		out.println("<a href=/Fabflix/MovieList?by="+sort_by+"&arg="+request.getParameter("arg")+"&page_id="+(page_id+1)+"&ipp="+ipp+"&order=asc > Asec</a>");
		out.println("<a href=/Fabflix/MovieList?by="+sort_by+"&arg="+request.getParameter("arg")+"&page_id="+(page_id+1)+"&ipp="+ipp+"&order=desc > Dsec</a>");
		
		out.println("<HTML><style>"
				+ "#container {"
				+ "padding:10%"
		 		+ "height:250px;"
				+ "margin:20%;}"
		 		+ "#details {"
		 		+ "text-align:left;"
		 		+ "padding:5px;"
		 		+ "width:60%;"
		 		+ "color:white;"
		 		+ "background-color:black;"
		 		+ "height:220px;"
		 		+ "float:right;"
		 		+ "}"
		 		+ "#image {"
		 		+ "width:35%;"
		 		+ "float:left;"
		 		+ "background-color:black;"
		 		+ "height:220px;"
		 		+ "padding:5px;"
		 		+ "}"
		 		+ "</style>");
		out.println("<HEAD><TITLE>login</TITLE></HEAD>");
		out.println("<BODY><H1 ALIGN=\"CENTER\">Movie Details</H1></CENTER>");
		
		try {
			connection = (Connection) dataSource.getConnection();
			PreparedStatement ps_movies = (PreparedStatement) connection.prepareStatement(query);
			ResultSet movies = ps_movies.executeQuery();
			print(movies, response, request);
			PreparedStatement ps_movies_count = (PreparedStatement) connection.prepareStatement(query_count);
			ResultSet countrs=ps_movies_count.executeQuery();
			countrs.next();
			Integer count = countrs.getInt(1);
			int i=page_id-2;
			if(i<=0)i=1;
			
			for(;i<=page_id+5 && i<=count/ipp;i++)
				out.println("<a href=/Fabflix/MovieList?by="+sort_by+"&arg="+request.getParameter("arg")+"&page_id="+(i)+"&ipp="+ipp+"&order="+orderby+" >"+i+"</a>");
			out.print("<br>");
			
			for(int j=5;j<=25;j=j+5)
				out.println("<right><a href=/Fabflix/MovieList?by="+sort_by+"&arg="+request.getParameter("arg")+"&page_id="+(1)+"&ipp="+j+"&order="+orderby+">"+j+"</a></right>");	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void print(ResultSet result, HttpServletResponse response, HttpServletRequest request) throws SQLException, IOException
	{
		//connection = (Connection) dataSource.getConnection();
		//String query = "Select * from movies where title like 'I%'";
		//PreparedStatement ps_movies = (PreparedStatement) connection.prepareStatement(query);
		ResultSet movies = result;
		PrintWriter out = response.getWriter();
		
	
		out.println("<h4 align=\"right\"><a href=\"/Fabflix/Cart?MovieID=0\">My Cart</a></h4><br>");
		if (movies.next())
		{
			do
			{
				String star_query = "Select distinct(a.first_name), a.last_name, a.id from stars a "
						+ "where a.id in (select distinct(b.star_id) from stars_in_movies b "
						+ "where b.movie_id in (select distinct(c.id)  from movies c where c.title = '" + movies.getString("title").replace("'", "''") +"'));";
				PreparedStatement ps_stars = (PreparedStatement) connection.prepareStatement(star_query);
				ResultSet stars = ps_stars.executeQuery();
				
				String genre_query = "Select distinct(a.name) from genres a "
						+ "where a.id in (select distinct(b.genre_id) from genres_in_movies b where b.movie_id in "
						+ "(select distinct(c.id)  from movies c where c.title = '" + movies.getString("title").replace("'", "''") +"'));";
				PreparedStatement ps_genres = (PreparedStatement) connection.prepareStatement(genre_query);
				ResultSet genres = ps_genres.executeQuery();
				
				out.println("<div id=\"container\"><div id=\"image\">");
				out.println("<img style=\"width:110;height;160;\"src=\"" + movies.getString("banner_url")
						+ "\" alt=\"" + movies.getString("title") + " DVD Cover\"><br><br>");
				out.println("<button type=\"button\" style=\"padding:10px;background-color:blue;color:white;\""
						+ "onclick=\"window.location.href='/Fabflix/Cart?MovieID=" + movies.getString("id") + "&qty=1&req=add';\">Add to Cart</button></div>");
				out.println("<span class=\"title\">Movie = </span>"
						+ "<a class=\"title\" href=\"/Fabflix/Movie?MovieID=" + movies.getString("id") + "\">" + movies.getString("title") + "</a><br>");
				out.println("<span class=\"title\">Year = " + movies.getString("year") + "</span><br>");
				out.println("<span class=\"title\">Director = " + movies.getString("director") + "</span><br>");
				
				out.println("<span class=\"title\">Actors = ");
				int size = 0;
				while (stars.next())
					size++;
				stars.first();
				do
				{
					out.println("<a href=\"/Fabflix/Star?StarID=" + stars.getString("id") + "\">"
							+ stars.getString("first_name") + " " + stars.getString("last_name") + "</a>");
					if (size != 1)
						out.println(", ");
					size--;
				} while (stars.next());
				out.println("</span><br>");
				
				out.println("<span class=\"title\">Genre = ");
				String genre_list = "";
				while (genres.next())
				{
					genre_list += (genres.getString("name") + ", ");
				}
				genre_list = genre_list.substring(0, genre_list.length()-2);
				out.println(genre_list + "</span><br>");
				
				out.println("<a href=\"" + movies.getString("trailer_url") + "\">Watch Trailer</a></div></div><br><br><br><br>");
			} while (movies.next());
			
			
		}
		else
		{
			//String mess="Username or password incorrect";
			//response.sendRedirect("/Fabflix/index.html?message="+mess);
		}
		out.println("</BODY></HTML>");
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

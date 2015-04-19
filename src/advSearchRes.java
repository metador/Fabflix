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
 * Servlet implementation class advSearchRes
 */
@WebServlet("/advSearchRes")
public class advSearchRes extends HttpServlet {
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
		String order_accord="title";
		String orderedStatus="t_asc";
			if(orderby == null)
				orderby ="ASC";
			else{
				orderedStatus=orderby;
				switch(orderby){
				case "t_asc":
					orderby ="ASC";
					break;
					case "t_desc":
						orderby ="desc";
						break;
					case "d_asc":
						orderby ="ASC";
						order_accord="year";
						break;
					case "d_desc":
						orderby ="desc";
						order_accord="year";
						break;
				}
			}
	if(sipp==null)
		sipp="5";
	int page_id=Integer.parseInt(spage_id)-1;
	int ipp=Integer.parseInt(sipp);
	
	/*	switch(sort_by){
		
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
			
		case "advsearch" :*/
	
			//String search_term=request.getParameter("arg");
			query="select  distinct title, movies.id ,  director,movies.year, banner_url, trailer_url  from movies join stars_in_movies on stars_in_movies.movie_id=movies.id join stars on stars.id=stars_in_movies.star_id";
			//query_count="Select count(*) from movies where title like '% "+search_term.replace("'", "''")+"%'";
			//out.println(query); 
		
			 
			String title = request.getParameter("title");
			String year = request.getParameter("year");
			String director = request.getParameter("director");
			String s_first = request.getParameter("s_first");
			String s_last = request.getParameter("s_last");
			if(!title.equals("")){
				query=query+" and where movies.title like '% "+title.replaceAll("'", "''")+"%' ";
			}
			
			if(!year.equals("")){
				try{
					int year_int=Integer.parseInt(year);
					query=query+" and where movies.year='"+year_int+"'";
				}
				catch(NumberFormatException i){
				}
			}
			if(!director.equals("")){
				query=query+" and where director like '"+director.replaceAll("'", "''")+"%' ";
			}
			if(!s_first.equals("")){
				query=query+" and where stars.first_name='"+s_first.replaceAll("'", "''")+"'";
			}
			if(!s_last.equals("")){
				query=query+" and where stars.last_name='"+s_last.replaceAll("'", "''")+"'";
			}
			query=query.replaceFirst("and", "");
			query_count=query.replace("distinct title", "count(distinct title)");
			query=query + " order by "+order_accord+" "+orderby+" LIMIT "+ipp+" OFFSET "+ipp*page_id;
		/*	brea
		default:
			break;
		}*/
		out.println("<a href=/Fabflix/advSearchRes?by="+sort_by+"&title="+title+"&year="+year+"&director="+director+"&s_first="+s_first+"&s_last="+s_last+"&page_id="+(page_id+1)+"&ipp="+ipp+"&order=t_asc >Title-> Asec</a>");
		out.println("<a href=/Fabflix/advSearchRes?by="+sort_by+"&title="+title+"&year="+year+"&director="+director+"&s_first="+s_first+"&s_last="+s_last+"&page_id="+(page_id+1)+"&ipp="+ipp+"&order=t_desc >Title-> Dsec</a>");
		out.println("<a href=/Fabflix/advSearchRes?by="+sort_by+"&title="+title+"&year="+year+"&director="+director+"&s_first="+s_first+"&s_last="+s_last+"&page_id="+(page_id+1)+"&ipp="+ipp+"&order=d_asc >Year-> Asec</a>");
		out.println("<a href=/Fabflix/advSearchRes?by="+sort_by+"&title="+title+"&year="+year+"&director="+director+"&s_first="+s_first+"&s_last="+s_last+"&page_id="+(page_id+1)+"&ipp="+ipp+"&order=d_desc >Year-> Asec</a>");

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
			int displayed_count=0;
			for(;i<=page_id+5 ;i++)
				if(displayed_count<=count)
				{
					displayed_count=displayed_count+ipp;
					out.println("<a href=/Fabflix/advSearchRes?by="+sort_by+"&title="+title+"&year="+year+"&director="+director+"&s_first="+s_first+"&s_last="+s_last+"&page_id="+(i)+"&ipp="+ipp+"&order="+orderedStatus+" >"+i+"</a>");
				}
			out.print("<br>");
			
			for(int j=5;j<=25;j=j+5)
				out.println("<right><a href=/Fabflix/advSearchRes?by="+sort_by+"&title="+title+"&year="+year+"&director="+director+"&s_first="+s_first+"&s_last="+s_last+"&page_id="+(1)+"&ipp="+j+"&order="+orderedStatus+">"+j+"</a></right>");	
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
			out.println("<h3>No results found</h3>");
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

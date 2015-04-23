

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;


/**
 * Servlet implementation class index
 */
@WebServlet("/index.html")
public class index extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource dataSource;
    private Connection connection;
    private static String User = null;
    private static String Pass = null;
    private static String Page = null;
    /**
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 PrintWriter out = response.getWriter();
		 String message = request.getParameter("message");
		 if (message != null)
			 request.getSession().setAttribute("User", null);
		 else
			 request.getSession().setAttribute("User", User);
		 headerFooter base = new headerFooter(request.getSession());
		 out.println(base.header());
		 out.println(base.banner());
			out.println("<HEAD><TITLE>Login Page</TITLE></HEAD>");
			

		 out.println("<div id='login'><div style='float:left;width:35%'>"
		 		+ "<center><img src='http://png-2.findicons.com/files/icons/768/precious_metal/512/movie.png' height='192' width='192'></center>"
				 + "<br><center><span style=\"font-family: 'Pacifico', cursive;font-size:50px;\">Fabflix</span></center></div>"
		 		+ "<div style='float:right;width:55%'><H2 ALIGN=\"CENTER\">Login</H2><FORM =\"/Fabflix/\" METHOD=\"POST\">"
		 		+ "<center> Username: <INPUT id='login_field' TYPE='TEXT' NAME=\"Username\"><BR><Br> Password: <INPUT id='login_field' TYPE=\"PASSWORD\" NAME=\"password\"></center><BR><BR><BR>"
		 		+ " <CENTER><button class='login_btn' type='submit' style='font-size:20px;width:60%;'>"
			+ "<img src='http://goo.gl/wwTkAq?gdriveurl' height='24' width='24'>Confirm</button></center></div></div>");

		 if(message !=null)
			 out.println("<br><div><center><span style=\"color:red;fonts-size:50px;font-weight:bold;\">" + message + "</span></center></div>");
		
		    // Output stream to STDOUT
		   try
		   {
			connection = (Connection) dataSource.getConnection();
			Statement statement = connection.createStatement();
			User =request.getParameter("Username");
			Pass =request.getParameter("password");
			if(User !=null && Pass !=null)
			{
				String query = "SELECT * from customers where first_name='"+ User +"' AND password='"+ Pass +"';";
		
				// Perform the query
				 ResultSet rs = statement.executeQuery(query);
		
		          // Iterate through each row of rs
		      //    if(message !=null)
				 if (rs.next())
				 {
					HttpSession session = request.getSession();
				    synchronized(session) 
				    {
				         session.setAttribute("User", User);
				         session.setAttribute("Pass", Pass);
				         String temp = "";
				         temp=(String) session.getAttribute("Page");
				         if (temp.isEmpty())
				        	 Page = "/Fabflix/Main";
				         else
				         {
				        	 if (request.getQueryString() == null)
				        		 Page=request.getRequestURI();
				        	 else
				        		 Page=request.getRequestURI()+"?"+request.getQueryString();
				         }
				         
				    }	
				    if (Page.equals("/Fabflix/index.html") || Page.equals("/Fabflix/index.html?message=Username%20or%20password%20incorrect.%20Please%20try%20Again!"))
				    	Page = "/Fabflix/Main";
					 response.sendRedirect(Page);
					 
				 }else{
				String mess="Username or password incorrect. Please try Again!";
		        response.sendRedirect("/Fabflix/index.html?message="+mess);  
		          }
		          rs.close();
		          statement.close();
		          connection.close();
		        }
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
		                        "<P>Java Lang Exception in doGet: " +
		                        ex.getMessage() + "</P></BODY></HTML>");
						                return;
						            }
		   
						         out.close();

	
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	*/
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub  
		doPost(request,response);
	} 

}

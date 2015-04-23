

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
		 request.getSession().setAttribute("Dummy", User);
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
			 out.println("<br>"+message+"</br>");
		
		response.setContentType("text/html");    // Response mime type
		
		    // Output stream to STDOUT
		   try
		   {
			connection = (Connection) dataSource.getConnection();
			Statement statement = connection.createStatement();
			String username=request.getParameter("Username");
			String password=request.getParameter("password");
			if(username !=null && password !=null)
			{
				String query = "SELECT * from customers where first_name='"+username+"' AND password='"+ password+"';";
		
				// Perform the query
				 ResultSet rs = statement.executeQuery(query);
				 out.println("<TABLE border>");
		
		          // Iterate through each row of rs
		      //    if(message !=null)
				 if (rs.next())
				 {
					HttpSession session = request.getSession();
				    synchronized(session) 
				    {
				         session.setAttribute("User", username);
				         session.setAttribute("Pass", password);
				         String temp = "";
				        temp=(String) session.getAttribute("Page");
				         if (temp.isEmpty())
				        	 Page = "/Fabflix/Main";
				         else
				        	 Page = temp;
				    }	
				    
				    String mess = "Succesfully logged in";			
					 response.sendRedirect(Page);
					 
				 }else{
				String mess="Username or password incorrect";
		 response.sendRedirect("/Fabllix/index.html?message="+mess);  
		 out.println("<tr>" + "<td>" + message+ "</td>" +"</tr>");
		          }
		
		          out.println("</TABLE>");
		
		          rs.close();
		          statement.close();
		          connection.close();
		        }
		          out.println("</TABLE>");
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



import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Servlet implementation class Confirm
 */
@WebServlet("/Confirm")
public class Confirm extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String User = null;
    private static String Pass = null;
    private static String Page = "/Fabflix/Confirm";
	private DataSource dataSource;
    private Connection connection;
    headerFooter base = new headerFooter();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Confirm() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException 
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			connection = (Connection) dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpSession session = request.getSession();
	    synchronized(session) 
	    {
	         User = (String) session.getAttribute("User");
	         Pass = (String) session.getAttribute("Pass");
	         session.setAttribute("Page", Page);
        }
        if (User.isEmpty() || Pass.isEmpty())
       	 response.sendRedirect("/Fabflix/index.html");
	    try {
			print(response, request);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

	public void print(HttpServletResponse response, HttpServletRequest request) throws SQLException, IOException
    {
		PrintWriter out = response.getWriter();
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String address = request.getParameter("address");
		String email = request.getParameter("email");
		String ccid = request.getParameter("ccid");
		String password = request.getParameter("password");
		
		String custid = "Select id from customers where first_name = '" + fname + "' and password = '" + password + "';";
		PreparedStatement ps_custid = (PreparedStatement) connection.prepareStatement(custid);
		ResultSet id = ps_custid.executeQuery();
		id.next();
		int customer_id = Integer.parseInt(id.getString("id"));
		
		String custupdate = "update customers set first_name = '" + fname + "'"
				+ ", email = '" + email + "'"
				+ ", last_name = '" + lname + "'"
				+ ", address = '" + address + "'"
				+ ", cc_id = '" + ccid + "'"
				+ " where id = '" + customer_id + "';";
		PreparedStatement ps_custupdate = (PreparedStatement) connection.prepareStatement(custupdate);
		ps_custupdate.executeUpdate();
		
		String custinfo = "Select * from customers where id = '" + customer_id + "';";
		PreparedStatement ps_custinfo = (PreparedStatement) connection.prepareStatement(custinfo);
		ResultSet customer = ps_custinfo.executeQuery();
		
		out.println(base.header());
		out.println("<HEAD><TITLE>Customer Details</TITLE></HEAD>");
		out.println(base.banner());
		
		out.println("<form action='/Fabflix/Confirm' method='post'><div id=\"cust_info\">"
				+ "<span style='font-size:35px'>My Info</span>"
				+ "<button class='cart_btn' name='req' type='submit' value='Confirm' style='float:right;font-size:20px;'>"
				+ "<img src='http://goo.gl/iCLKUa?gdriveurl' height='20' width='20'>Check Out</button><br><br>");
		
		if(customer.next())
		{
			out.println("<div>");
			out.println("<span style='padding-left:50px'>First Name : </span><span style='padding-right:100px'>" + customer.getString("first_name") + "</span><br>");
			out.println("<span style='padding-left:50px'>Last Name : </span><span style='padding-right:100px'>" + customer.getString("last_name") + "</span><br>");
			out.println("<span style='padding-left:50px'>Address : </span><span style='padding-right:100px'>" + customer.getString("address") + "</span><br>");
			out.println("<span style='padding-left:50px'>Email : </span><span style='padding-right:100px'>" + customer.getString("email") + "</span><br></div></div></form>");
		}
		
		String query = "Select * from cart where customer_id like '" + customer_id + "'";
		PreparedStatement ps_cart = (PreparedStatement) connection.prepareStatement(query);
		ResultSet cart = ps_cart.executeQuery();
		
		out.println("<table border>"
				+ "<tr><th>Movie Title</th>"
				+ "<th>Price</th>"
				+ "<th>Quantity</th>"
				+ "<th>Update</th>"
				+ "<th>Remove</th></tr>");

		while(cart.next())
		{
			out.println("<tr><td>" + cart.getString("title") + "</td>");
			out.println("<td>" + cart.getString("price") + "</td>");
			out.println("<td>" + cart.getString("quantity") + "</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		
		out.println("</BODY></HTML>");
	    	
	}
}



import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
    Calendar cal1 = new GregorianCalendar();
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
	         Page="/Fabflix/Main";
	         session.setAttribute("Page", Page);
	         if (User == null || Pass == null)
	             response.sendRedirect("/Fabflix/index.html");
        }
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
		
		out.println("<div id='cust_info' style='height:200px;'>"
				+ "<span style='font-size:35px'>My Info</span><br><br>");
		
		if(customer.next())
		{
			out.println("<div>");
			out.println("<div  style='padding-left:50px;float:left;width:30%;'><span>Name : </span><br><br><span>Address : </span><br><br><span>Email : </span></div>"
					+ "<div style='float:right;width:60%;'><span>" + customer.getString("first_name") + " " + customer.getString("last_name") + "</span><br>");
			out.println("<br><span>" + customer.getString("address") + "</span><br>");
			out.println("<br><span>" + customer.getString("email") + "</span></div></div></div>");
		}
		
		String query = "Select * from cart where customer_id like '" + customer_id + "'";
		PreparedStatement ps_cart = (PreparedStatement) connection.prepareStatement(query);
		ResultSet cart = ps_cart.executeQuery();
		
		out.println("<br><br><br><div id=\"cart_title\"><span>Purchased Items: </span></div>");
		out.println("<table  cellpadding=\"10\" id=\"cart_res\"><tr style=\"background-color:#00CCFF;\" align='left'>"
				+ "<th>Movie Title</th>"
				+ "<th>Price</th>"
				+ "<th>Quantity</th></tr>");

		while(cart.next())
		{
			out.println("<tr style='color:green;'><td>" + cart.getString("title") + "</td>");
			out.println("<td>" + cart.getString("price") + "</td>");
			out.println("<td>" + cart.getString("quantity") + "</td></tr>");
		}
		out.println("</table><br><br><br>");
		
		String sale_q = "select * from movies where id in (select distinct(movie_id) from sales where customer_id like '" + customer_id + "');";
		PreparedStatement ps_sale_query = (PreparedStatement) connection.prepareStatement(sale_q);
		ResultSet sale_query = ps_sale_query.executeQuery();
		
		out.println("<div id=\"cart_title\"><span>Recent Purchase History: </span></div>");
		out.println("<table  cellpadding=\"10\" id=\"cart_res\"><tr style=\"background-color:#00CCFF;\" align='left'>"
				+ "<th>Movie Title</th>"
				+ "<th>Year</th>"
				+ "<th>Price</th></tr>");

		while(sale_query.next())
		{
			out.println("<tr style='color:orange;'><td>" + sale_query.getString("title") + "</td>");
			out.println("<td>" + sale_query.getString("Year") + "</td>"
					+ "<td>$12.35</td></tr>");
		}
		out.println("</table><br><br><br><br>");

		cart.first();
		do
		{
			String sale = "Select * from sales where customer_id like '" + customer_id + "' and movie_id = '" + cart.getString("movie_id") + "';";
			PreparedStatement ps_sales = (PreparedStatement) connection.prepareStatement(sale);
			ResultSet sales = ps_sales.executeQuery();
			if (!sales.next())
			{
				String date = "'" + cal1.get(Calendar.YEAR) + "-" + cal1.get(Calendar.MONTH) + "-" + cal1.get(Calendar.DAY_OF_MONTH) + "'";
				String sale_ins = "INSERT INTO `moviedb`.`sales` (`customer_id`, `movie_id`, `sale_date`) "
						+ "VALUES ('" + customer_id + "', '" + cart.getString("movie_id") + "', " + date + ");";
				PreparedStatement ps_sales_insert = (PreparedStatement) connection.prepareStatement(sale_ins);
				ps_sales_insert.executeUpdate();
			}			
		} while(cart.next()); 
		
		String delete = "delete from cart where customer_id like '" + customer_id + "'";
		PreparedStatement ps_delete = (PreparedStatement) connection.prepareStatement(delete);
		ps_delete.executeUpdate();
		
		out.println(base.footer());
	    	
	}
}

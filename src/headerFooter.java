import javax.servlet.http.HttpSession;


public class headerFooter {
	
	public boolean loggedin = false;
	String User = null;
	
	public headerFooter(HttpSession session)
	{
		User = (String) session.getAttribute("User");
		if (!(User == null))
			loggedin = true;
	}
	
	public String header()
	{
		String head = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + 
				"<html xmlns=\"http://www.w3.org/1999/xhtml\">" + 
				"<head>" + 
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" + 
				"<link href='http://fonts.googleapis.com/css?family=Roboto+Condensed' rel='stylesheet' type='text/css'>" + 
				"<link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>"
				+ "<link href='http://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>"
				+ "<link rel=\"stylesheet\" href=\"design.css\" type=\"text/css\" />";
		return head;
	}
	
	public String banner()
	{
		String banner = "<body id=\"set_font\" bgcolor=\"#EFFFFF\"><div id=\"banner\">" + 
				"<div>" + 
				"<button class=\"home\" onclick=\"location.href='/Fabflix/Cart?MovieID=0&qty=0&req=View'\"><img style=\"float:left;\"src=\"http://goo.gl/xuA1xS?gdriveurl\" height=\"34\" width=\"34\">My Cart</button>"; 
				if (loggedin)
					banner += "<button class=\"home\" onclick=\"location.href='/Fabflix/index.html'\"><img style=\"float:left;\"src=\"http://goo.gl/wwTkAq?gdriveurl\" height=\"34\" width=\"34\">Sign Out</button>"; 
				else
				{	banner += "<button class=\"home\" onclick=\"location.href='/Fabflix/index.html'\"><img style=\"float:left;\"src=\"http://goo.gl/wwTkAq?gdriveurl\" height=\"34\" width=\"34\">Sign In</button>"; }
				banner += "<button class=\"home\" onclick=\"location.href='/Fabflix/advSearch'\"><img style=\"float:left;\"src=\"http://goo.gl/sAojdW?gdriveurl\" height=\"34\" width=\"34\">Advanced Search</button>" + 
				"<button class=\"home\" onclick=\"location='/Fabflix/Main'\"><img style=\"float:left;\"src=\"http://goo.gl/RdfXu3?gdriveurl\" height=\"34\" width=\"34\">&nbspHome</button>" + 

				"<form action=/Fabflix/MovieList method='get'>" + 
				"<input type=\"hidden\" name=\"by\" value=\"search\">" + 
				"<button class=\"btn_srch\"><img src=\"http://goo.gl/vdKFWI?gdriveurl\" height=\"34\" width=\"34\"></button>" + 
				"<div style=\"float:right;\"><input id=\"srch_box\" type=\"text\" name=\"arg\"   Click here to search a movie...\"  maxlength=\"64\"/></div>" + 
				"<input type=\"hidden\" name=\"order\" value=\"t_asc\"/>" + 
				"</form>" + 
				"<img style=\"margin-right:20px;float:left;\" src=\"http://png-2.findicons.com/files/icons/768/precious_metal/512/movie.png\" height=\"72\" width=\"72\">" + 
				"<span style=\"font-family: 'Pacifico', cursive;font-size:47px;padding:0;\">Fabflix</span>"; 
				if (loggedin)
					banner += "<center><span style=\"font-family: 'Calibri', cursive;font-size:15px;margin-left:5px;\">Welcome " + User + "!</span></center>";
				banner += "</div></div><br><br>" + 
				"";
		return banner;
	}
	
	public String footer()
	{
		String footer = "<div id=\"footline\">" + 
				"<div id=\"footlinks\"><a class=\"ft_links\" href=\"www.google.com\">Home</a>" + 
				"<a class=\"ft_links\" href=\"Main\">About Us</a>" + 
				"<a class=\"ft_links\" href=\"Main\">Regsiter an Account</a>" + 
				"<a class=\"ft_links\" href=\"Main\">Privacy Policy</a>" + 
				"<a class=\"ft_links\" href=\"Main\">Checkout</a></div><br>" + 
				"<div id=\"footlinks\"><p>Copyright � 2009 by Mohammed & Melwin James. All rights reserved (R).</p></div>" + 
				"</div>" + 
				"</body>" + 
				"</html>" + 
				"";
		return footer;
	}

}

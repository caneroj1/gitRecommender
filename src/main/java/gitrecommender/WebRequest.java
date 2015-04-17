package gitrecommender;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class WebRequest extends HttpServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
	public String returnHeader() {
		StringWriter header = new StringWriter();
		
		header.write("<!DOCTYPE html>\n");
		header.write("<html lang=\"en\">\n");
        header.write("<head><title>Git Recommender</title>\n");
        header.write("<meta charset=\"utf-8\">\n");
        header.write("<link rel=\"stylesheet\" href=\"https://raw.githubusercontent.com/thomaspark/bootswatch/gh-pages/paper/bootstrap.min.css\"></head>\n");
        header.write("<body>\n");
        
        return header.toString();
	}
	
	public String returnFooter() {
		StringWriter footer = new StringWriter();
		
		footer.write("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js\"></script>");
		footer.write("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js\"></script>");
		footer.write("</body></html>");
		
		return footer.toString();
	}
	
	public HashMap<String, String> getQueryVariables(String queryString) {
		HashMap<String, String> urlData = new HashMap<String, String>();
		String[] urlVariables = queryString.split("&");
		String[] varSplit = new String[2];
		
		for(String variable : urlVariables) {
			varSplit = variable.split("=");
			urlData.put(varSplit[0], varSplit[1]);
		}
		
		return urlData;
	}
}

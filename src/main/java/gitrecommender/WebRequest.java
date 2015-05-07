package gitrecommender;

import org.json.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class WebRequest extends HttpServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// implement in inherited classes
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// implement in inherited classes
	}
	
	public JSONObject parseJSONFile(String filepath) throws FileNotFoundException {
//		InputStream stream = new FileInputStream(filepath);
		InputStream stream = getClass().getResourceAsStream(filepath);
		return new JSONObject(new JSONTokener(stream));
	}
	
	public String dataSectionForDonutChart(JSONObject languageColors, HashMap<String, Double> languages) {
		String data = "var data = [\n";
		
		String[] langNames = languages.keySet().toArray(new String[0]);
		String key, htmlColor;
		System.out.println("Inside 2");
		System.out.println(languages.toString());
		for(int i = 0; i < languages.size(); i++) {
			data += "\t{\n";
			key = langNames[i];
			htmlColor = languageColors.getString(key);
			data += ("\t\tvalue: " + languages.get(key).doubleValue()); 
			data += (",\n\t\tcolor: \"" + htmlColor + "\"");
			data += (",\n\t\thighlight: \"" + htmlColor + "\"");
			data += (",\n\t\tlabel: \"" + key + "\"");
			if(i != languages.size() - 1) { data += ("\n\t},\n"); }
			else { data += ("\n\t}\n"); }
		}
		data += "];\n";
		return data;
	}
	
	public String makeDonutChart(HashMap<String, Double> languages) throws FileNotFoundException {
		System.out.println("Inside 1");
		System.out.println(languages.toString());
		JSONObject languageColors = parseJSONFile("/colors.json");
		String html = "<script>\nvar ctx = document.getElementById(\"languageChart\").getContext(\"2d\");\n";
		html += dataSectionForDonutChart(languageColors, languages);
		html += "var myLanguageChart = new Chart(ctx).Doughnut(data);";
		
		html += "</script>";
		return html;
	}
	
	public String returnHeader() {
		StringWriter header = new StringWriter();
		header.write("<!DOCTYPE html>\n");
		header.write("<html lang=\"en\">\n");
        header.write("<head><title>Git Recommender</title>\n");
        header.write("<meta charset=\"utf-8\">\n");
        header.write("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.2/Chart.min.js\"></script>");
        header.write("<link rel=\"stylesheet\" href=\"http://maxcdn.bootstrapcdn.com/bootswatch/3.3.4/paper/bootstrap.min.css\"></head>\n");
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
	
	public String createLink(String linkName, String linkUrl) {
		return("<a class='text-center' href='" + linkUrl + "'>" + linkName + "</a>");
	}
	
	public String returnFormFieldWithLabel(String formIdentifier, String formDisplayName, String placeholder) {
		String html = "<div class='form-group'>";
		
		html += ("<label for='" + formIdentifier + "' class='col-sm-2 control-label'>" + formDisplayName + "</label>");
		html += "<div class='col-sm-10'>";
		html += ("<input type='text' class='form-control' id='" + formIdentifier + "' name='" + formIdentifier + "' placeholder='" + placeholder + "'>");
		html += "</div>";
		html += "</div>";
		return html;
	}
	
	// this handles a form submission from the main page and strings all of the request parameters into a query string.
	public String processFormSubmit(HttpServletRequest request) {
		String queryString = "";
		queryString += ("?githubName=" + request.getParameter("githubName"));
		queryString += ("&keyword1=" + request.getParameter("keyword1"));
		queryString += ("&keyword2=" + request.getParameter("keyword2"));
		queryString += ("&keyword3=" + request.getParameter("keyword3"));
		queryString += ("&keyword4=" + request.getParameter("keyword4"));
		queryString += ("&keyword5=" + request.getParameter("keyword5"));
		return queryString;
	}
	
	public boolean blankKeywords(String[] keywords) {
		for(int i = 0; i < keywords.length; i++) { if(keywords[i].equals("")) return true; }
		return false;
	}
	
	public HashMap<String, String> getQueryVariables(String queryString) {
		HashMap<String, String> urlData = new HashMap<String, String>();

		if(queryString != null) {
			String[] urlVariables = queryString.split("&");
			String[] varSplit = new String[2];
			
			for(String variable : urlVariables) {
				varSplit = variable.split("=");
				if(varSplit.length == 1) { urlData.put(varSplit[0], ""); }
				else { urlData.put(varSplit[0], varSplit[1]); }
			}
		}
		
		return urlData;
	}
}

package gitrecommender;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javalite.activejdbc.*;
import org.kohsuke.github.*;

@SuppressWarnings("serial")
@WebServlet("/")
public class Driver extends WebRequest {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		GitHub gh = GitHub.connectUsingOAuth("dbecfb2322c76d3fb4f59e0154a02335c51ba020");
		
		try {
			Class.forName("org.postgresql.Driver");
//			Base.open("org.postgresql.Driver", "jdbc:postgresql://45.56.104.253/gitrecommender", "root", "T5jvjabU");
			Base.open("org.postgresql.Driver", "jdbc:postgresql://localhost/gitrecommender", "joecanero", "");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
		}
		
		PrintWriter page = response.getWriter();
		page.print(returnHeader());
		page.print("<div class='container'>");
		page.print("<div class='row-fluid'>");
		page.print("<div class='col-md-12'>");
		page.print("<h1 class='text-center'>Git Recommender</h1>");
		page.print(processRecommendation(request.getQueryString()));
		
		page.print("<div class='col-md-10 col-md-offset-1'>");
		page.print("<form method='post' action='/gitrecommender-1.93/' name='keyword-form' id='keyword-form' class='text-center form-horizontal'>");
		page.print(returnFormFieldWithLabel("githubName", "GitHub Username", "Please enter your username"));
		page.print("<br/>");
		page.print(returnFormFieldWithLabel("keyword1", "Top Keyword", "This is your most desirable keyword"));
		page.print(returnFormFieldWithLabel("keyword2", "Next Keyword", ""));
		page.print(returnFormFieldWithLabel("keyword3", "Next Keyword", ""));
		page.print(returnFormFieldWithLabel("keyword4", "Next Keyword", ""));
		page.print(returnFormFieldWithLabel("keyword5", "Bottom Keyword", "This is your least desirable keyword"));
		page.print("<button type='submit' class='btn btn-primary btn-large text-center'>Compare</button>");
		
		page.print("</form>");
		page.print("</div>");
		
		page.print("</div>");
		page.print("</div>");
		page.print("</div>");
		page.print(returnFooter());
	}
	
	// this post request handles the form submission from the page. it basically takes all of the form parameters
	// and throws them into a query string to be passed to the get request for this page.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("PROCESSING POST REQUEST");
		
		String redirectUrl = "/gitrecommender-1.93/" + processFormSubmit(request);
		
		response.setStatus(HttpServletResponse.SC_SEE_OTHER);
		response.setHeader("Location", redirectUrl);
	}
	
	// this does all of the heavy lifting after a successful post request. it accepts a query string and parses variables
	// from the query string and does all of the recommendation stuff.
	private String processRecommendation(String queryString) throws IOException {
		GitHub gh = GitHub.connectUsingOAuth("dbecfb2322c76d3fb4f59e0154a02335c51ba020");
		
		HashMap<String, String> queryVars = getQueryVariables(queryString);
		String html = "";
		
		if(!queryVars.isEmpty()) {
			String[] treeKeywords = { queryVars.get("keyword1"), queryVars.get("keyword2"), queryVars.get("keyword3"), queryVars.get("keyword4"), queryVars.get("keyword5") };
			GHUser me = gh.getUser(queryVars.get("githubName"));
			
			HashMap<String, Double> myRank = Recommender.computeUserAverageLanguageRank(me, me.getPublicRepoCount());
			TreeMap<Integer, ArrayList<String>> processedRepositories = new TreeMap<Integer, ArrayList<String>>();
			
			int distance;
			for(Repository repository : Recommender.getRandomRepositories(50).toArray(new Repository[0])) {
				String repoName = repository.getName();
				distance = Recommender.distanceBetween(repository, myRank, treeKeywords);

				if(processedRepositories.containsKey(distance)) {
					processedRepositories.get(distance).add(repoName);
				}
				else {
					ArrayList<String> tmpList = new ArrayList<String>();
					tmpList.add(repoName);
					processedRepositories.put(distance, tmpList);
				}
			}
			
			html += "<div class='col-md-10 col-md-offset-1'>";
			html += "<h3 class='text-center'>Recommendations for " + queryVars.get("githubName") + "</h3>";
			int nearestNeighbors = 0;
			while(nearestNeighbors < 5) {
				int key = processedRepositories.firstKey();
				for(String repoName : processedRepositories.remove(key)) {
					html += ("<p class='text-center'>" + (nearestNeighbors + 1) + ": " + createLink(repoName, "https://github.com/" + repoName) + "</p>");
					nearestNeighbors += 1;
				}
			}
			
			html += "</div>";
		}
		
		return html;
	}
}

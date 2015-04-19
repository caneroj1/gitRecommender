package gitrecommender;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kohsuke.github.*;

@SuppressWarnings("serial")
@WebServlet("/")
public class Driver extends WebRequest {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter page = response.getWriter();
		
		page.print(returnHeader());
		page.print("<div class='container'>");
		page.print("<div class='row-fluid'>");
		page.print("<div class='col-md-12'>");
		page.print("<h1 class='text-center'>Git Recommender</h1>");
		page.print("<a href=\"#\" class=\"btn btn-primary btn-large text-center\"><i class=\"icon-white icon-ok-sign\"></i> It Works!</a>");
		GitHub gh = GitHub.connectUsingOAuth("PUT YOUR OAUTH TOKEN HERE");
		
		String uname = gh.getUser("kbohinski").toString();
		int pubRepoCount = gh.getUser("kbohinski").getPublicRepoCount();		
		page.print("</br><h2 class='text-center'> Hello! " + uname + ", it appears that you have " + pubRepoCount + " public repositories on github!</h2>");
		
		HashMap<String, Double> myRank = Recommender.computeUserAverageLanguageRank(gh.getUser("kbohinski"), pubRepoCount);
		HashMap<String, Double> targetRank = Recommender.computeLanguageRank(gh.getRepository("kbohinski/brokenlights-requests"));
		
		int distance = Recommender.computeLanguageDistance(myRank, targetRank);
		page.print("<p>Language rank distance: " + distance);
		
		GHRepository myRepo = gh.getRepository("caneroj1/dojob");
		Recommender.analyzeReadme(myRepo);
		distance = Recommender.computeLanguageDistance(myRank, Recommender.computeLanguageRank(myRepo));
		page.print("<p>Language rank distance: " + distance);
		
		distance = Recommender.computeLanguageDistance(myRank, Recommender.computeLanguageRank(gh.getRepository("caneroj1/jammerr")));
		page.print("<p>Language rank distance: " + distance);
		
		distance = Recommender.computeLanguageDistance(myRank, Recommender.computeLanguageRank(gh.getRepository("emberjs/ember-rails")));
		page.print("<p>Language rank distance: " + distance);
		
		distance = Recommender.computeLanguageDistance(myRank, Recommender.computeLanguageRank(gh.getRepository("ReturnInfinity/BareMetal-OS")));
		page.print("<p>Language rank distance: " + distance);
		
		distance = Recommender.computeLanguageDistance(myRank, Recommender.computeLanguageRank(gh.getRepository("mozilla/metrics-graphics")));
		page.print("<p>Language rank distance: " + distance);
		
		distance = Recommender.computeLanguageDistance(myRank, Recommender.computeLanguageRank(gh.getRepository("matplotlib/matplotlib")));
		page.print("<p>Language rank distance: " + distance);
		
		distance = Recommender.computeLanguageDistance(myRank, Recommender.computeLanguageRank(gh.getRepository("eyedol/kasahorow-Keyboard-For-Android")));
		page.print("<p>Language rank distance: " + distance);
		page.print("</div>");
		page.print("</div>");
		page.print("</div>");
		page.print(returnFooter());
	}
}

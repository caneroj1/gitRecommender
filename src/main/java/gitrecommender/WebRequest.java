package gitrecommender;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONTokener;

@SuppressWarnings("serial")
public class WebRequest extends HttpServlet {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* implement in inherited classes */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* implement in inherited classes */
	}

	/**
	 * @param filepath
	 * @return
	 * @throws FileNotFoundException
	 */
	public JSONObject parseJSONFile(String filepath)
			throws FileNotFoundException {
		InputStream stream = getClass().getResourceAsStream(filepath);
		return new JSONObject(new JSONTokener(stream));
	}

	/**
	 * @param languageColors
	 * @param languages
	 * @return
	 */
	public String dataSectionForDonutChart(JSONObject languageColors,
			HashMap<String, Double> languages) {
		String data = "var data = [\n";

		DecimalFormat formatter = new DecimalFormat("0.00");

		String[] langNames = languages.keySet().toArray(new String[0]);
		String key, htmlColor;
		System.out.println("Inside 2");
		System.out.println(languages.toString());
		for (int i = 0; i < languages.size(); i++) {
			data += "\t{\n";
			key = langNames[i];
			try {
				htmlColor = languageColors.getString(key);
			} catch (Exception e) {
				htmlColor = "#ccc";
			}
			data += ("\t\tvalue: " + formatter.format(languages.get(key)
					.doubleValue()));
			data += (",\n\t\tcolor: \"" + htmlColor + "\"");
			data += (",\n\t\thighlight: \"" + htmlColor + "\"");
			data += (",\n\t\tlabel: \"" + key + "\"");
			if (i != languages.size() - 1) {
				data += ("\n\t},\n");
			} else {
				data += ("\n\t}\n");
			}
		}
		data += "];\n";
		return data;
	}

	/**
	 * @param recommendations
	 * @param keywords
	 * @return
	 */
	public String dataSectionForBarChart(Repository[] recommendations,
			String[] keywords) {
		/* Total the heuristic */
		String data = "var data2 = {\n";
		data += "\tlabels: [";
		data += ("\"" + keywords[0] + "\",");
		data += ("\"" + keywords[1] + "\",");
		data += ("\"" + keywords[2] + "\",");
		data += ("\"" + keywords[3] + "\",");
		data += ("\"" + keywords[4] + "\"");
		data += ("],\n\tdatasets: [\n");
		data += ("\t\t{\n");

		int[] totals = new int[5];
		boolean[] keywordsMatched;

		for (int i = 0; i < 5; i++) {
			keywordsMatched = recommendations[i].getKeywordsMatched();
			totals[0] += keywordsMatched[0] ? 1 : 0;
			totals[1] += keywordsMatched[1] ? 1 : 0;
			totals[2] += keywordsMatched[2] ? 1 : 0;
			totals[3] += keywordsMatched[3] ? 1 : 0;
			totals[4] += keywordsMatched[4] ? 1 : 0;
		}

		data += "\t\t\tlabel: \"Keywords\",\n";
		data += "\t\t\tfillColor: \"rgba(151,187,205,0.5)\",\n";
		data += "\t\t\tstrokeColor: \"rgba(151,187,205,0.5)\",\n";
		data += "\t\t\thighlightFill: \"rgba(151,187,205,0.5)\",\n";
		data += "\t\t\thighlightStroke: \"rgba(151,187,205,1)\",\n";
		data += "\t\t\tdata: [";
		for (int i = 0; i < 4; i++) {
			data += (totals[i] + ",");
		}
		data += (totals[4]) + "]\n\t\t}\n\t]\n};";
		return data;
	}

	/**
	 * @param languages
	 * @param id
	 * @return
	 * @throws FileNotFoundException
	 */
	public String makeDonutChart(HashMap<String, Double> languages, String id)
			throws FileNotFoundException {
		JSONObject languageColors = parseJSONFile("/colors.json");
		String html = "<script>\nvar ctx = document.getElementById(\"" + id
				+ "\").getContext(\"2d\");\n";
		html += dataSectionForDonutChart(languageColors, languages);
		html += "\nvar " + id + "Chart = new Chart(ctx).Doughnut(data);";

		html += "</script>";
		return html;
	}

	/**
	 * @param recommendations
	 * @param keywords
	 * @return
	 */
	public String makeBarChart(Repository[] recommendations, String[] keywords) {
		String html = "<script>\nvar ctx2 = document.getElementById(\"keywordsChart\").getContext(\"2d\");\n";
		html += dataSectionForBarChart(recommendations, keywords);
		html += "\nvar myKeywordsChart = new Chart(ctx2).Bar(data2);";

		html += "</script>";
		return html;
	}

	/**
	 * @return
	 */
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

	/**
	 * @return
	 */
	public String returnFooter() {
		StringWriter footer = new StringWriter();

		footer.write("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js\"></script>");
		footer.write("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js\"></script>");
		footer.write("</body></html>");

		return footer.toString();
	}

	/**
	 * @param repository
	 * @param id
	 * @param keywords
	 * @param decision
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String createDropdown(Repository repository, String id,
			String[] keywords, String decision) throws FileNotFoundException,
			IOException {
		String html = "";
		html += "<div class=\"panel panel-default\">";
		html += "<div class=\"panel-heading\" role=\"tab\" id=\"heading" + id
				+ "\">";
		html += "<h4 class=\"panel-title\">";
		html += "<a data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"#"
				+ id
				+ "\" aria-expanded=\"true\" aria-controls=\""
				+ id
				+ "\">" + repository.getName() + "</a>";
		html += "</h4>";
		html += "</div>";
		html += "<div id=\""
				+ id
				+ "\" class=\"panel-collapse collapse in\" role=\"tabpanel\" aria-labelledby=\"heading"
				+ id + "\">";
		html += "<div class=\"panel-body\">";
		html += "<div class='col-md-12'>";

		/* Chart */
		html += "<div class='col-md-6'>";
		html += "<h5 class='text-center'>Language Breakdown</h5>";
		html += "\n<canvas id=\"" + id
				+ "Chart\" width='220' height='220'></canvas>\n";
		html += makeDonutChart(Recommender.computeLanguageRank(repository), id
				+ "Chart");
		html += "</div>";

		/* Other */
		html += "<div class='col-md-6'>";
		html += "<h4>Score: " + repository.getRecommenderScore() + "</h4>";

		if (decision.equals("true")) {
			html += "<p style='padding:10px;' class='bg-success text-success'>Our decision tree learning algorithm predicts you will actually like this repository on GitHub.</p>";
		} else {
			html += "<p style='padding:10px;' class='bg-warning text-warning'>Our decision tree learning algorithm predicts you will not actually like this repository on GitHub.</p>";
		}

		boolean[] matched = repository.getKeywordsMatched();
		for (int i = 0; i < 5; i++) {
			if (matched[i]) {
				html += "<p>"
						+ keywords[i]
						+ "<span style='margin-left: 5%; color: green;' class ='glyphicon glyphicon-ok'></span></p>";
			} else {
				html += "<p>"
						+ keywords[i]
						+ "<span style='margin-left: 5%; color: red;' class ='glyphicon glyphicon-remove'></span></p>";
			}
		}
		html += "<p>View <a href='https://github.com/" + repository.getName()
				+ "'>" + repository.getName() + "</a> on GitHub.</p>";
		html += "</div>";
		html += "</div>";
		html += "</div>";
		html += "</div>";
		html += "</div>";
		return html;
	}

	/**
	 * @param linkName
	 * @param linkUrl
	 * @param priority
	 * @return
	 */
	public String createLink(String linkName, String linkUrl, int priority) {
		return ("<div class=\"item\"><a href='"
				+ linkUrl
				+ "'><button type=\"button\" class=\"btn btn-success btn-lg btn-block dropdown-toggle\" type=\"button\" id=\"menu1\" data-toggle=\"dropdown\">"
				+ linkName
				+ "<span class=\"caret\"></span></button>"
				+ "</a><ul class=\"dropdown-menu\" role=\"menu\" aria-labelledby=\"menu1\"><li role=\"presentation\">"
				+ "<a role=\"menuitem\" tabindex=\"-1\" href=\"#\">LOLAI</a></li>"
				+ "<li role=\"presentation\" class=\"divider\"></li>"
				+ "<li role=\"presentation\"><a role=\"menuitem\" tabindex=\"-1\" href=\""
				+ linkUrl + "\">View on GitHub</a></li></div>");
	}

	/**
	 * @param formIdentifier
	 * @param formDisplayName
	 * @param placeholder
	 * @return
	 */
	public String returnFormFieldWithLabel(String formIdentifier,
			String formDisplayName, String placeholder) {
		String html = "<div class='form-group'>";

		html += ("<label for='" + formIdentifier
				+ "' class='col-sm-2 control-label'>" + formDisplayName + "</label>");
		html += "<div class='col-sm-10'>";
		html += ("<input type='text' class='form-control' id='"
				+ formIdentifier + "' name='" + formIdentifier
				+ "' placeholder='" + placeholder + "'>");
		html += "</div>";
		html += "</div>";
		return html;
	}

	/**
	 * this handles a form submission from the main page and strings all of the
	 * request parameters into a query string.
	 * 
	 * @param request
	 * @return
	 */
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

	/**
	 * @param keywords
	 * @return
	 */
	public boolean blankKeywords(String[] keywords) {
		for (int i = 0; i < keywords.length; i++) {
			if (keywords[i].equals(""))
				return true;
		}
		return false;
	}

	/**
	 * @param queryString
	 * @return
	 */
	public HashMap<String, String> getQueryVariables(String queryString) {
		HashMap<String, String> urlData = new HashMap<String, String>();

		if (queryString != null) {
			String[] urlVariables = queryString.split("&");
			String[] varSplit = new String[2];

			for (String variable : urlVariables) {
				varSplit = variable.split("=");
				if (varSplit.length == 1) {
					urlData.put(varSplit[0], "");
				} else {
					urlData.put(varSplit[0], varSplit[1]);
				}
			}
		}

		return urlData;
	}

}

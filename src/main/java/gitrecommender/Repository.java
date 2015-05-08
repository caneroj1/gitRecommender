package gitrecommender;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;

import org.javalite.activejdbc.Model;

/* This class models the database table called "repositories".
 * Each Repository object has a set of attributes that will allow our recommendation
 * system to process each repository efficiently, instead of querying GitHub for the information.
 * 
 * ATTRIBUTES
 * ----------
 * languages: 		a dictionary of <String, String> where the key is the name of the language and the 
 * 					value is the # of bytes for that language
 * id: 				unique identifier of the repository object
 * readme_url: 		a url to the raw readme on github
 * watchers: 		the number of GitHub users who starred (favorited) the repository.
 * pushed_at: 		timestamp of the last commit to the main branch of the repository, defaults to master
 * name: 			the name of the repository
 * 
 * CLASS VARIABLES
 * ----------
 * keywordsScore: 		the numerical value assigned to the repository that ranks how its readme's 
 * 						content matches up to the user's specified keywords
 * recommenderScore: 	the score of this repository from the nearest neighbor algorithm.
 * keywordsMatched: 	for each keyword, a boolean value of whether that keyword was found in the readme or not
*/
public class Repository extends Model {
	private int keywordsScore = 0;
	private int recommenderScore = 0;
	private boolean[] keywordsMatched;

	public HashMap<String, String> getLanguages() {
		return ((HashMap<String, String>) this.get("languages"));
	}

	public String getId() {
		return (String) this.get("id");
	}

	public String getReadmeUrl() {
		return (String) this.get("readme_url");
	}

	public Object getWatchers() {
		return this.get("watchers");
	}

	public Date getPushedAt() {
		return (Date) this.get("pushed_at");
	}

	public String getName() {
		return (String) this.get("name");
	}

	public void setKeywordsScore(int score) {
		keywordsScore = score;
	}

	public int getKeywordsScore() {
		return keywordsScore;
	}
	
	public boolean[] getKeywordsMatched() {
		return keywordsMatched;
	}

	public void setKeywordsMatched(boolean[] keywordsMatched) {
		this.keywordsMatched = keywordsMatched;
	}
	
	public String getRecommenderScore() {
		DecimalFormat formatter = new DecimalFormat("0.00");
		return (formatter.format(recommenderScore/400.0));
	}

	public void setRecommenderScore(int recommenderScore) {
		this.recommenderScore = recommenderScore;
	}
}

package gitrecommender;

import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.kohsuke.github.*;

/* The Recommender class is a wrapper for the processing we do on each repository. Our k-nearest-neighbors implementation
 * calculates the desirability of each repository using the methods contained in here.
 * The methods are basically all static, and do not require an instantiation of this class.
 */
public class Recommender {
	// PUBLIC METHODS ********************************************************************************
	/* Calls all of the recommender functionality for a specified GitHub user and a GitHub repo
	 * You need to pass in the keywords, and the language rank for the GitHub user.
	 */
	public static int distanceBetween(Repository repository, HashMap<String, Double> userLanguageRank, String[] keywords) throws IOException {
		int languageDistance = computeLanguageDistance(userLanguageRank, computeLanguageRank(repository));
		int readmeDistance = analyzeReadme(repository, keywords);
		int activityDistance = activity(repository);
		int watchersDistance = mapWatchers(repository);
		
		return overallDistance(readmeDistance, languageDistance, activityDistance, watchersDistance);
	}
	
	/* This is just the function where we compute the total distance that a repository is from the user.
	 * In the future, we plan on changing it from an unweighted sum to a weighted one, depending on user feedback.
	 */
	public static int overallDistance(int readmeDistance, int languageDistance, int timeDifference, int watchersRank) {
		return readmeDistance + languageDistance + timeDifference + watchersRank;
	}
	
	/* reads the readme for the specified repository.
	 * for each line it reads, it regexes out all non-word characters, leaving only a-zA-Z0-9 while replacing all instances
	 * of unwanted characters with spaces. next, it splits the string by space, and adds all the resulting words to the suffix tree.
	 * next, the algorithm uses the keywords passed in to determine the percentage of keywords that a repository has and ranks it.
	 * the keywords array should be in order of preference, with the presence of each keyword mapping to a score of:
	 * 1st - 36
	 * 2nd - 28
	 * 3rd - 20
	 * 4th - 12
	 * 5th - 4
	 * the total of these scores - 100 is the rank of the repository in terms of keyword matches
	 */
	public static int analyzeReadme(Repository repository, String[] keywords) throws IOException {
		String readmeURL = repository.getReadmeUrl();
		if(readmeURL == null || readmeURL.equals("")) {
			return 100;
		}
		
		SuffixTree suffixTree = new SuffixTree();
		URL readmeUrl = new URL(readmeURL);
		URLConnection readmeConnection = readmeUrl.openConnection();
		BufferedReader readmeReader = new BufferedReader(new InputStreamReader(readmeConnection.getInputStream()));
		
		String inputLine;
		while ((inputLine = readmeReader.readLine()) != null) {
			for(String token : inputLine.replaceAll("\\W", " ").split(" ")) {
				suffixTree.addWord(token);
			}
		}
		readmeReader.close();
		
		int[] scores = { 36, 28, 20, 12, 4 };
		int repositoryScore = 0;
		for(int i = 0; i < 5; i++) {
			if(suffixTree.findWord(keywords[i])) {
				repositoryScore += scores[i];
			}
		}
		
		int score = 100 - repositoryScore;
		repository.setKeywordsScore(score);
		return score;
	}
	
	/* computes the language rank of a given repo.
	 * The language rank of a repo is basically what percentage of the total bytes written for a repo
	 * each language constitutes. For example, if there were 20 bytes written total for a repo, and ruby makes up 10,
	 * javascript makes up 5, and html makes up 5, the language rank of the repo is:
	 * { ruby = 0.5, javascript= 0.25, html = 0.25 }	
	 */
	public static HashMap<String, Double> computeLanguageRank(Repository repository) throws IOException {
		System.out.println("wrong method");
		HashMap<String, String> languages = repository.getLanguages();
		HashMap<String, Double> languageRank = new HashMap<String, Double>();
		if(languages.isEmpty()) {
			return new HashMap<String, Double>();
		}
		
		Iterator<String> iter = languages.values().iterator();
		
		// total up the number of bytes written for the given repo
		long totalBytes = 0;
		while(iter.hasNext()) {
			Long num = Long.parseLong(iter.next());
			totalBytes += num.longValue();
		}
		
		// for each language, determine the percentage
		for(String language : languages.keySet().toArray(new String[0])) {
			Long temp = Long.parseLong(languages.get(language));
			languageRank.put(language, (temp.doubleValue() / totalBytes));
		}
	
		return languageRank;
	}
	
	/* Basically the same as the method given above, but this computes the language rank of a GitHub repository
	 * that is retrieved from the api, instead of frm our database.
	 */
	public static HashMap<String, Double> computeLanguageRankFromApi(GHRepository repository) throws IOException {
		System.out.println("Right here");
		Map<String, Long> languages = repository.listLanguages();
		HashMap<String, Double> languageRank = new HashMap<String, Double>();
		System.out.println(languages);
		
		if(languages.isEmpty()) {
			return new HashMap<String, Double>();
		}
		Iterator<Long> iter = languages.values().iterator();
		
		// total up the number of bytes written for the given repo
		long totalBytes = 0;
		while(iter.hasNext()) {
			Number temp = iter.next();
			totalBytes += temp.longValue();
		}
		
		// for each language, determine the percentage
		for(String language : languages.keySet().toArray(new String[0])) {
			Number temp = languages.get(language);
			languageRank.put(language, (temp.doubleValue() / totalBytes));
		}
	
		return languageRank;
	}
	
	/* computes the average language rank of a given user.
	 * The average language rank for a user is kind of similar to the language rank for a repository. What this method will do
	 * is total the percentage number of bytes written in each language across all of the user's public repos and then divide each
	 * running total by the number of repos.
	 * For example, suppose our user has two repos with the following ranks:
	 * { ruby = 0.5, javascript= 0.25, html = 0.25 }	
	 * { ruby = 0.2, javascript= 0.3, html = 0.5 }
	 * 
	 * We would then need to divide the totals of each language by 2, so the average language rank would be
	 * { ruby = 0.35, javascript= .275, html = 0.325 }	
	 */
	public static HashMap<String, Double> computeUserAverageLanguageRank(GHUser user, int publicRepoCount) throws IOException {
		HashMap<String, Double> averageLanguageRank = new HashMap<String, Double>();
	
		Iterator<GHRepository> iter = user.getRepositories().values().iterator();
		while(iter.hasNext()) {
			averageLanguageRank = mergeHashMaps(averageLanguageRank, computeLanguageRankFromApi(iter.next()));
		}
		
		for(String language : averageLanguageRank.keySet().toArray(new String[0])) {
			Number languageNumber = averageLanguageRank.remove(language);
			averageLanguageRank.put(language, languageNumber.doubleValue() / publicRepoCount);
		}
		
		return averageLanguageRank;
	}
	
	/* this method will compute the distance between the user's language rank and the language rank of a target repo. 
	 * the distance will be defined as the manhattan distance between languages. So basically, we just check the two hashes
	 * and if the key exists in both hashes then we get the distance for that language as |userNumber - targetNumber|. 
	 * if the key exists in one hash but not the other, then we just add the rank for that language.
	 * Finally, we multiply the sum of all the ranks by 50 to get it between 0 and 100.
	 */
	public static int computeLanguageDistance(HashMap<String, Double> userRankToCopy, HashMap<String, Double> targetRank) {
		Double runningSum = 0.0;
		
		HashMap<String, Double> userRank = new HashMap<String, Double>(userRankToCopy);
		for(String language : userRank.keySet().toArray(new String[0])) {
			Number userNumber = userRank.remove(language);

			if(targetRank.containsKey(language)) {
				Number targetNumber = targetRank.remove(language);
				runningSum += Math.abs(userNumber.doubleValue() - targetNumber.doubleValue());
			}
			else {
				runningSum += userNumber.doubleValue();
			}
		}
		
		if(!targetRank.isEmpty()) {
			for(String language : targetRank.keySet().toArray(new String[0])) {
				Number targetNumber = targetRank.remove(language);
				runningSum += targetNumber.doubleValue();
			}
		}
		
		runningSum *= 50;
		return runningSum.intValue();
	}
	
	
	// rank the repository's most recent activity in terms of desirability
	public static int activity(Repository repository) {
		long daysSinceLastPush = timeDifference(repository.getPushedAt());
		double normalized = normalize(daysSinceLastPush);
		int activityRank = (int) (100 * normalized);
		
		return activityRank;
	}
	
	// function to map a number of watchers for a repo to a value between 0 and 100
	public static int mapWatchers(Repository repository) {
		int watchers = (Integer)repository.getWatchers();
		if(watchers <= 1) return 100;
		return(int)( 100 / Math.log(watchers));
	}
	
	// returns a number of random repositories from the db
	public static List<Repository> getRandomRepositories(int limit) {
		Long count = Repository.count();
		List<Repository> randomRepos = Repository.findBySQL("SELECT * FROM repositories OFFSET random() * " + count.intValue() + " LIMIT " + limit + ";");
		return randomRepos;
	}
	
	// PRIVATE METHODS ********************************************************************************
	// compute the time difference in days between the last push to the repository and the current time.
	private static long timeDifference(Date pushTime) {
		Date currentTime = new Date();
		long difference = currentTime.getTime();
		long secondsToDaysFactor = 86400000;
		difference -= pushTime.getTime();
		
		return difference / secondsToDaysFactor;
	}
	
	// function normalize some value between 0 and 1
	private static double normalize(long value) {
		if(value < 1) return 0;
		return(1 - Math.exp(-Math.log10(value)));
	}
	
	// merges two hash maps. it adds values from the two hashes if both hashes have the corresponding key, otherwise we just
	// union the new value in.
	private static HashMap<String, Double> mergeHashMaps(HashMap<String, Double> previous, HashMap<String, Double> next) {
		HashMap<String, Double> newMap = new HashMap<String, Double>();
		
		Double newNumber = 0.0;
		for(String language : previous.keySet().toArray(new String[0])) {
			Number previousNumber = previous.remove(language);
			newNumber = previousNumber.doubleValue();
			
			if(next.containsKey(language)) {
				Number nextNumber = next.remove(language);
				newNumber += nextNumber.doubleValue();
			}
			
			newMap.put(language, newNumber);
		}
		
		// if there is anything contained in the next hash that is not in the previous hash, we need to add it in to the 
		// new map now because it would not have been added in the previous section
		for(String language : next.keySet().toArray(new String[0])) {
			Number languageNumber = next.remove(language);
			newMap.put(language, languageNumber.doubleValue());
		}
		
		return newMap;
	}
}

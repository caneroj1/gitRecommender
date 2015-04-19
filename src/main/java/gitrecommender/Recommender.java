package gitrecommender;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import org.kohsuke.github.*;

public class Recommender {
	/* reads the readme for the specified repository.
	 	right now it pretty much just prints out the readme.
	 	for the future, i want to regex out anything that isn't a letter or a number, so those can
	 	be put in the suffix tree.
	 */
	public static void analyzeReadme(GHRepository repository) throws IOException {
		GHContent readme = repository.getReadme();
		SuffixTree suffixTree = new SuffixTree();
		String readmeURL = readme.getDownloadUrl();
		URL readmeUrl = new URL(readmeURL);
		URLConnection readmeConnection = readmeUrl.openConnection();
		BufferedReader readmeReader = new BufferedReader(new InputStreamReader(readmeConnection.getInputStream()));
		
		String inputLine;
		while ((inputLine = readmeReader.readLine()) != null) {
			for(String token : inputLine.replaceAll("\\W", " ").split(" ")) {
				System.out.println("Adding " + token);
				suffixTree.addWord(token);
			}
			System.out.println(inputLine);
		}
		System.out.println("Looking for 'lawncare'.");
		System.out.println(suffixTree.findWord("lawncare"));
		System.out.println("Looking for 'tutoring'.");
		System.out.println(suffixTree.findWord("tutoring"));
		System.out.println("Looking for 'tutoringnot'.");
		System.out.println(suffixTree.findWord("tutoringnot"));
		readmeReader.close();
	}
	
	/* computes the language rank of a given repo.
		The language rank of a repo is basically what percentage of the total bytes written for a repo
		each language constitutes. For example, if there were 20 bytes written total for a repo, and ruby makes up 10,
		javascript makes up 5, and html makes up 5, the language rank of the repo is:
		{ ruby = 0.5, javascript= 0.25, html = 0.25 }	
	*/
	public static HashMap<String, Double> computeLanguageRank(GHRepository repository) throws IOException {
		Map<String, Long> languages = repository.listLanguages();
		HashMap<String, Double> languageRank = new HashMap<String, Double>();
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
		The average language rank for a user is kind of similar to the language rank for a repository. What this method will do
		is total the percentage number of bytes written in each language across all of the user's public repos and then divide each
		running total by the number of repos.
		For example, suppose our user has two repos with the following ranks:
		{ ruby = 0.5, javascript= 0.25, html = 0.25 }	
		{ ruby = 0.2, javascript= 0.3, html = 0.5 }
	
		We would then need to divide the totals of each language by 2, so the average language rank would be
		{ ruby = 0.35, javascript= .275, html = 0.325 }	
*/
	public static HashMap<String, Double> computeUserAverageLanguageRank(GHUser user, int publicRepoCount) throws IOException {
		HashMap<String, Double> averageLanguageRank = new HashMap<String, Double>();
		
		Iterator<GHRepository> iter = user.getRepositories().values().iterator();
		while(iter.hasNext()) {
			averageLanguageRank = mergeHashMaps(averageLanguageRank, computeLanguageRank(iter.next()));
		}
		
		for(String language : averageLanguageRank.keySet().toArray(new String[0])) {
			Number languageNumber = averageLanguageRank.remove(language);
			averageLanguageRank.put(language, languageNumber.doubleValue() / publicRepoCount);
		}
		
		return averageLanguageRank;
	}
	
	/* this method will compute the distance between the user's language rank and the language rank of a target repo. 
	 	the distance will be defined as the manhattan distance between languages. So basically, we just check the two hashes
	 	and if the key exists in both hashes then we get the distance for that language as |userNumber - targetNumber|. 
	 	if the key exists in one hash but not the other, then we just add the rank for that language.
	 	Finally, we multiply the sum of all the ranks by 50 to get it between 0 and 100.
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
		
		for(String language : targetRank.keySet().toArray(new String[0])) {
			Number targetNumber = targetRank.remove(language);
			runningSum += targetNumber.doubleValue();
		}
		
		runningSum *= 50;
		return runningSum.intValue();
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

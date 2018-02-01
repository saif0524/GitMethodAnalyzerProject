package gmap.changeCollector;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MethodFinder {

	private String commitHistory;

	public String getLogFileName() {
		return commitHistory;
	}

	public void setLogFileName(String commitHistory) {
		this.commitHistory = commitHistory;
	}

	public void getParamChangedMethods(String commitHistory) {
		List<String> methodSignatureList = new ArrayList<>();
		methodSignatureList = getMethodSignatureChangeLog(commitHistory);
		for (int i = 0; i < methodSignatureList.size()-1; i++) {
			// old and new signatures are consequent in the commit log and so 
			//i-->old and
			// i+1-->new
			String oldSignature = extractMethodSignature(methodSignatureList.get(i));
//			System.out.println(methodSignatureList.get(i));
			
				String newSignature = extractMethodSignature(methodSignatureList.get(++i));

				String bareSpaceOldSignature = removeWhiteSpace(oldSignature);
				String bareSpaceNewSignature = removeWhiteSpace(newSignature);

				if (compareOldAndNewSignatures(bareSpaceOldSignature, bareSpaceNewSignature)) {
					System.out.println("Old: " + oldSignature + "\nNew: " + newSignature);
				}
		}
	}

	/**
	 * helper for get a list of methods (in pairs) that have changes in signatures
	 **/

	public List<String> getMethodSignatureChangeLog(String commitLog) {
		// Used a regex from
		// https://stackoverflow.com/questions/68633/regex-that-will-match-a-java-method-declaration;
		String pattern = "[\\-\\+][ \\s+]*(?:(?:public|protected|private)\\s+)?"
				+ "(?:(static|final|native|synchronized|abstract|threadsafe|transient|"
				+ "(?:<[?\\w\\[\\] ,&]+>)|(?:<[^<]*<[?\\w\\[\\] ,&]+>[^>]*>)|(?:<[^<]*<["
				+ "^<]*<[?\\w\\[\\] ,&]+>[^>]*>[^>]*>))\\s+){0,}(?!return)\\b([\\w.]+)\\b"
				+ "(?:|(?:<[?\\w\\[\\] ,&]+>)|(?:<[^<]*<[?\\w\\[\\] ,&]+>[^>]*>)|(?:<[^<]*"
				+ "<[^<]*<[?\\w\\[\\] ,&]+>[^>]*>[^>]*>))((?:\\[\\]){0,})\\s+\\b\\w+"
				+ "\\b\\s*\\(\\s*(?:\\b([\\w.]+)\\b(?:|(?:<[?\\w\\[\\] ,&]+>)|(?:<[^<]*<[?\\w\\[\\] ,&]+"
				+ ">[^>]*>)|(?:<[^<]*<[^<]*<[?\\w\\[\\] ,&]+>[^>]*>[^>]*>))((?:\\[\\]){0,})(\\.\\.\\.)?\\s+"
				+ "(\\w+)\\b(?![>\\[])\\s*(?:,\\s+\\b([\\w.]+)\\b(?:|(?:<[?\\w\\[\\] ,&]+>)|(?:<[^<]*<[?\\w\\[\\] ,&]+"
				+ ">[^>]*>)|(?:<[^<]*<[^<]*<[?\\w\\[\\] ,&]+>[^>]*>[^>]*>))((?:\\[\\]){0,})(\\.\\.\\.)?\\s+"
				+ "(\\w+)\\b(?![>\\[])\\s*){0,})?\\s*\\)(?:\\s*throws [\\w.]+(\\s*,\\s*[\\w.]+))?\\s*(?:\\{|;)[ \\t]*$";

		List<String> methodSignatureList = new ArrayList<String>();

		try {
			
			BufferedReader reader = new BufferedReader(new StringReader(commitLog));
			String line = "";
			while ((line = reader.readLine()) != null) {
				
				if (line.startsWith("+") || line.startsWith("-")) {
					if (line.length() == 1 || line.toCharArray()[1] == '+' || line.toCharArray()[1] == '-') {
						continue;
					}
					if (line.matches(pattern)) {
						// System.out.println(line);
//						System.out.println(line);
						methodSignatureList.add(line);
					}
					
				}

			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return methodSignatureList;
	}// end of getMethodSignatureChangeLog

	/** helper method for extracting Method Signatures from a given line **/
	private String extractMethodSignature(String methodSignature) {
		return methodSignature.substring(2, methodSignature.length() - 1);
	}

	/** helper method for removing white spaces from the signature line **/
	private String removeWhiteSpace(String methodSignature) {
		// RegularExpression for white space
		return methodSignature.replaceAll("\\s+", "");
	}

	/** helper for checking if the old and new signatures are same of not **/
	private boolean compareOldAndNewSignatures(String bareSpaceOldSignature, String bareSpaceNewSignature) {
		StringTokenizer tokensOfOldSignature = new StringTokenizer(bareSpaceOldSignature, "(");
		StringTokenizer tokensOfNewSignature = new StringTokenizer(bareSpaceNewSignature, "(");

		return tokensOfOldSignature.nextToken().equals(tokensOfNewSignature.nextToken());
	}

	/***
	 * Constructor for Testing (otherwise some methods need to be static to be
	 * called by the main method)
	 **/
	public MethodFinder() {
		this.commitHistory = "";

	}

	/*** Main method for testing 
	 * @throws IOException ***/
	public static void main(String args[]) throws IOException {
		MethodFinder mf = new MethodFinder();
		
		StringBuilder sBuilder = new StringBuilder();
		
		BufferedReader reader = new BufferedReader(new FileReader("./externaltestfiles/newLogFile.log"));
		String line = "";
		while ((line = reader.readLine()) != null) {
			sBuilder.append(line+"\n");
		}
		
		
//		mf.setLogFileName(); // input your log file
		mf.getParamChangedMethods(sBuilder.toString());
	}

}

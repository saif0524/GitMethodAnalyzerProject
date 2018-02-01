package gmap.changeCollector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DiffAnalyzer {

	private String shaLog;
	private String gitPath;	
	private String commitInfo;
	
	public List<String> getShalist(String shaLog) {
	
		List<String> shaList = new ArrayList<String>();

		BufferedReader reader = new BufferedReader(new StringReader(shaLog));

		String line = "";
		try {
			while ((line = reader.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, " ");

				shaList.add(token.nextToken());

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return shaList;
	}
	
	
	public  List<String> getCommitHistoryFullList(String gitPath, List<String> shaList) {

		List<String> commitHistoryFullList = new ArrayList<String>();

		for (String sha : shaList) {
			// String commitInfo = getCommitInfo(gitPath, sha); 
			/*executeCommand("git --git-dir " + gitPath + " show " + sha);*/
			commitHistoryFullList.add(getCommitInfo(gitPath, sha));
		}
		return commitHistoryFullList;
	}
	
	
	public String getCommitInfo(String gitPath, String sha) {
		ConsoleCommunicator communicator = new ConsoleCommunicator();
//		gitPath = "C:/Users/saif-pc/Documents/dp-assignment-decorator-pattern/.git";
		String command  ="git --git-dir " + gitPath + " show " + sha;
		String getCommitInfo = communicator.executeCommand(command);
		return getCommitInfo;
	}
	
	public String getShaLog() {
    	return shaLog;
    }
    
    
    public void setShaLog(String shaLog) {
    	this.shaLog = shaLog;
    }
    
    
    public String getGitPath() {
    	return gitPath;
    }
    
    
    public void setGitPath(String gitPath) {
    	this.gitPath = gitPath;
    }
    
    
    public String getCommitInfo() {
    	return commitInfo;
    }
    
    
    public void setCommitInfo(String commitInfo) {
    	this.commitInfo = commitInfo;
    }

    public static void main(String[] args) {
//    	git --git-dir C:/Users/saif-pc/Documents/dp-assignment-decorator-pattern/.git show 18c61ba
//    	git --git-dir /foo/bar/.git log
//		 String s = executeCommand("git log --oneline");
    	
    	ConsoleCommunicator communicator = new ConsoleCommunicator();
		String gitPath = "C:\\Users\\saif-pc\\Documents\\repo\\argouml\\.git";
		String command  ="git --git-dir " + gitPath + " log --oneline";   	
		String shaLog = communicator.executeCommand(command);
				
		// String s = executeCommand("git show 1a170c4
		// src/javaapplication5/JavaApplication5.java");
		
		DiffAnalyzer diffAn = new DiffAnalyzer();

		List<String> shaList = new ArrayList<>();
		shaList = diffAn.getShalist(shaLog);
		
		List<String> chFullList = diffAn.getCommitHistoryFullList(gitPath, shaList);
		
		MethodFinder mf = new MethodFinder();
		for(String commitInfo: chFullList) {
			mf.getParamChangedMethods(commitInfo);
		}
    }
    
    


}
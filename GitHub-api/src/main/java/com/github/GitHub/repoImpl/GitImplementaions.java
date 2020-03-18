package com.github.GitHub.repoImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.stereotype.Service;

import com.github.GitHub.bean.AuthMsg;
import com.github.GitHub.bean.FileBean;
import com.github.GitHub.repo.GitOperations;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
@Service
public class GitImplementaions implements GitOperations {
 
	@Override
	public String createBranch() throws IOException, GitAPIException{
		String gitUrl="https://github.com/winfo-analytics/WDAS";
    	CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("durgapraveenchittem", "Durgapraveen@123");
           File file = new File("C:\\Users\\winfo84\\Pictures\\OK4");
		// clone repository. Like to 'git clone -n repository'
		Git git = Git.cloneRepository().setURI(gitUrl).setDirectory(file)
		  .setCredentialsProvider(credentialsProvider)
		  .setBranchesToClone(Arrays.asList("refs/heads/CGBackup"))
		  .setBranch("refs/heads/CGBackup")
		  
		  .call();

		// create branch locally
		//git.checkout().setCreateBranch(true).setName("myBrach_9_3").call();
		 git.branchCreate() 
	       .setName("myBrach_9_3")
	       .setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
	       .setForce(true)
	       .call(); 
		
		// push created branch to remote repository
		// This matches to 'git push targetBranch:targetBranch'
		RefSpec refSpec = new RefSpec().setSourceDestination("myBrach_9_3", "myBrach_9_3");   
		git.push().setRefSpecs(refSpec).setCredentialsProvider(credentialsProvider).call();
//
//		// push created branch to remote repository
//		// This matches to 'git push targetBranch:targetBranch'
//		RefSpec refSpec = new RefSpec().setSourceDestination(targetBranch, targetBranch);   
//		git.push().setRefSpecs(refSpec).setCredentialsProvider(credentials).call();
//		
	   // git.checkout().setCreateBranch(true).setName("myBranch9_3_2020").call();
		return "created branch";
	}

	@Override
	public List<String> listBranches() throws IOException, GitAPIException{
		System.out.println("listing all branches method....");
			String gitUrl="https://github.com/winfo-analytics/WDAS";
            Collection<Ref> refs;
            List<String> branches = new ArrayList<String>();
            try {
            	CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("durgapraveenchittem", "Durgapraveen@123");
                refs = Git.lsRemoteRepository()
                        .setHeads(true)
                        .setRemote(gitUrl).setCredentialsProvider(credentialsProvider)
                        .call();
                for (Ref ref : refs) {
                    branches.add(ref.getName().substring(ref.getName().lastIndexOf("/")+1, ref.getName().length()));
                }
                Collections.sort(branches);
            } catch (InvalidRemoteException e) {
                System.out.println("InvalidRemoteException occured in fetchGitBranches"+e);
                e.printStackTrace();
            } catch (TransportException e) {
                System.out.println(" TransportException occurred in fetchGitBranches"+e);
            } catch (GitAPIException e) {
                System.out.println(" GitAPIException occurred in fetchGitBranches"+e);
            }
        
		return branches;
	}

	@SuppressWarnings("resource")
	@Override
	public void deleteBranch(String branch) {
		
		String gitUrl="https://github.com/winfo-analytics/WDAS";
		String refBranch = "refs/heads/"+branch;
        Collection<Ref> refs;
        try {
        	File src = new File("C:\\Users\\winfo84\\Documents\\GitHub\\WDAS");
        	org.eclipse.jgit.lib.Repository repo = new FileRepositoryBuilder().readEnvironment().findGitDir(src).build();
        	Git git =new Git(repo);
        	CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("durgapraveenchittem", "Durgapraveen@123");
            refs = Git.lsRemoteRepository()
                    .setHeads(true)
                    .setRemote(gitUrl).setCredentialsProvider(credentialsProvider)
                    .call();
            for(Ref ref : refs) {
                System.out.println("Had branch: " + ref.getName());
                if(ref.getName().equals(refBranch)) {
                    System.out.println("Removing branch before");
                    git.branchDelete()
                    .setBranchNames(branch)
                    .setForce(true)
                    .call();
                    break;
                }
            }
          //delete branch 'branchToDelete' on remote 'origin'
            RefSpec refSpec = new RefSpec()
                    .setSource(null)
                    .setDestination(refBranch);
           // git.push().setRefSpecs(refSpec).setRemote("origin").call();
            git.push().setRefSpecs(refSpec).setRemote("origin").setCredentialsProvider(credentialsProvider).call();
        }
        catch (Exception e) {
			System.out.println(e);
		}
        
	}

	@Override
	public Collection<AuthMsg> getRepos(String name) {
		
		String url = "https://api.github.com/users/"+name+"/repos";
		
		String data = getJSON(url);
		System.out.println(data);
		
		Type collectionType = new TypeToken<Collection<AuthMsg>>(){}.getType();
		Collection<AuthMsg> enums = new Gson().fromJson(data, collectionType);
	
		return enums;
		
	}
	
	// demo
	
	public String getJSON(String url) {
	    HttpURLConnection c = null;
	    try {
	        URL u = new URL(url);
	        c = (HttpURLConnection) u.openConnection();
	        c.setRequestMethod("GET");
	        c.setRequestProperty("Content-length", "0");
	        c.setUseCaches(false);
	        c.setAllowUserInteraction(false);
	        c.connect();
	        int status = c.getResponseCode();

	        switch (status) {                                      
	            case 200:
	            case 201:
	                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
	                StringBuilder sb = new StringBuilder();
	                String line;
	                while ((line = br.readLine()) != null) {
	                    sb.append(line+"\n");
	                }
	                br.close();
	                return sb.toString();
	        }

	    } catch (MalformedURLException ex) {
	        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
	    } catch (IOException ex) {
	        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
	    } finally {
	       if (c != null) {
	          try {
	              c.disconnect();
	          } catch (Exception ex) {
	             Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
	          }
	       }
	    }
	    return null;
	}

	@Override
	public List getFiles() {
		ArrayList list = new ArrayList();
		FileBean bean = new FileBean();
		
        File src = new File("C:\\Users\\winfo84\\Documents\\GitHub\\WDAS");
 
        try {
        	org.eclipse.jgit.lib.Repository repository = new FileRepositoryBuilder().readEnvironment().findGitDir(src).build();
			//listRepositoryContents(repo);
			
			Ref head = repository.exactRef("HEAD");

	        // a RevWalk allows to walk over commits based on some filtering that is defined
	        RevWalk walk = new RevWalk(repository);

	        RevCommit commit = walk.parseCommit(head.getObjectId());
	        RevTree tree = commit.getTree();
	        System.out.println("Having tree: " + tree);

	        // now use a TreeWalk to iterate over all files in the Tree recursively
	        // you can set Filters to narrow down the results if needed
	        TreeWalk treeWalk = new TreeWalk(repository);
	        treeWalk.addTree(tree);
	        treeWalk.setRecursive(false);
	        while (treeWalk.next()) {
	            if (treeWalk.isSubtree()) {
	            	
	                System.out.println("dir: " + treeWalk.getPathString());
	                bean.setFolder(treeWalk.getPathString());
	                String a = "dir: " + treeWalk.getPathString();
	                list.add(a);
	                treeWalk.enterSubtree();
	            } else {
	            	
	                System.out.println("file: " + treeWalk.getPathString());
	                bean.setFile(treeWalk.getPathString());
	                String b = "file: " + treeWalk.getPathString();
	                list.add(b);
	            }
	        }
	        
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return list;
	}
	
	 private static void listRepositoryContents(Repository repository) throws IOException {
		 
		    ArrayList list = null;
	        Ref head = repository.exactRef("HEAD");

	        // a RevWalk allows to walk over commits based on some filtering that is defined
	        RevWalk walk = new RevWalk(repository);

	        RevCommit commit = walk.parseCommit(head.getObjectId());
	        RevTree tree = commit.getTree();
	        System.out.println("Having tree: " + tree);

	        // now use a TreeWalk to iterate over all files in the Tree recursively
	        // you can set Filters to narrow down the results if needed
	        TreeWalk treeWalk = new TreeWalk(repository);
	        treeWalk.addTree(tree);
	        treeWalk.setRecursive(false);
	        while (treeWalk.next()) {
	            if (treeWalk.isSubtree()) {
	            	list.add(treeWalk.getPathString());
	                System.out.println("dir: " + treeWalk.getPathString());
	                treeWalk.enterSubtree();
	            } else {
	            	list.add(treeWalk.getPathString());
	                System.out.println("file: " + treeWalk.getPathString());
	            }
	        }
	    }
	

}

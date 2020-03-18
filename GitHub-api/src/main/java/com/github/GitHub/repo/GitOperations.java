package com.github.GitHub.repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;

import com.github.GitHub.bean.AuthMsg;

public interface GitOperations {
	public String createBranch() throws IOException, GitAPIException;
	public List<String> listBranches() throws IOException, GitAPIException;
	public void deleteBranch(String branch);
	public Collection<AuthMsg> getRepos(String name);
	public List getFiles();
}

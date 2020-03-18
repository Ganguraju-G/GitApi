package com.github.GitHub.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.GitHub.bean.AuthMsg;
import com.github.GitHub.repo.GitOperations;

@RestController
public class GitRest {
		@Autowired
		private GitOperations gitOperations;
		@GetMapping("/createBranch")
		public String createBranch() throws IOException, GitAPIException {
			System.out.println("rest call method for create branch");
			return gitOperations.createBranch();
		}
		@GetMapping("/branches")
		public List<String> listAllBranches() throws IOException, GitAPIException {
			System.out.println("rest call of listing branches...");
			return gitOperations.listBranches();
		}
		@DeleteMapping("/branch/{branch}")
		public String deleteBranch(@PathVariable String branch) {
			gitOperations.deleteBranch(branch);
			return null;
		}
		
		@GetMapping("/repos/{name}")
		public Collection<AuthMsg> getRepos(@PathVariable String name) {
			return gitOperations.getRepos(name);
		}
		@GetMapping("/files")
		public List getFiles(){
			return gitOperations.getFiles();
		}
}

package com.thiruacademy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.internal.storage.dfs.DfsRepositoryDescription;
import org.eclipse.jgit.internal.storage.dfs.InMemoryRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

public class GetDataFromGit {

	public static void main(String[] args) throws InvalidRemoteException, TransportException, GitAPIException, IOException {
		String token = "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";
        Path tempDir = Files.createTempDirectory("mytempdir");
        // create a file in the temporary directory
        File tempFile = new File(tempDir.toString(), "");
        if (tempFile.createNewFile()) {
            System.out.println("Temp file created: " + tempFile.toString());
        }
		Git git = Git.cloneRepository()
        .setURI("https://github.com/thirupathammakonca/gitFileUpload.git")
		.setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, ""))
        .setDirectory(tempFile) 
        .call();
		Repository repository = git.getRepository();
        
        String fileName = "fileUpload.txt";
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
        ObjectId lastcommitId = repository.resolve("main");
		try(RevWalk revWalk = new RevWalk(repository)) {
			RevCommit commit = revWalk.parseCommit(lastcommitId);
			RevTree tree = commit.getTree();
			try(TreeWalk treeWalk = new TreeWalk(repository)){
				treeWalk.addTree(tree);
                treeWalk.setRecursive(true);
                treeWalk.setFilter(PathFilter.create(fileName));
                if (!treeWalk.next()) {
                    throw new IllegalStateException("Did not find expected file :: "+fileName);
				}
				ObjectId objectId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(objectId);
				loader.copyTo(stream);
				BufferedReader bufferedReader = new BufferedReader(new StringReader(new String(stream.toByteArray())));
				for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				       System.out.println(line);
				    }
			}
		}
		
	}

}

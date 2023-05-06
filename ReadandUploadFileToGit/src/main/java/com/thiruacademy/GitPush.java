package com.thiruacademy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class GitPush {

	public static void main(String[] args) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		Iterable<PushResult> result = null;
		String response = "";
		Path tempDir = Files.createTempDirectory("mytempdir");
        
        File tempFile = new File(tempDir.toString(), "");
	
		String token = "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";
		Git git = Git.cloneRepository()
				.setURI("https://github.com/thirupathammakonca/gitFileUpload.git")
				.setDirectory(tempFile)
				.setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, ""))
				.setBranch("main")
				.call();
		Repository repository = git.getRepository();
		File myFile = new File(repository.getDirectory().getParent()+"/"+"gitpush.txt");
		FileUtils.writeStringToFile(myFile, "Test data to update in git gitpush.txt file", "UTF-8");
		git.add().addFilepattern("gitpush.txt").call();
		git.commit().setMessage("Initial commit of Git push").call();
		RefSpec spec = new RefSpec("main"+":"+"main");
		result = git.push().setRemote("origin")
				.setRefSpecs(spec).setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, ""))
				.call();
		if(result != null) {
			for(final PushResult pushResult : result) {
				for(RemoteRefUpdate refUpdate : pushResult.getRemoteUpdates()) {
					if(refUpdate.getStatus() != RemoteRefUpdate.Status.OK) {
						System.out.println("Git push status for :: "+refUpdate.getStatus());
					}else {
						response = "SUCCESS";
						System.out.println("Successfully updated in GIT ");
					}
				}
			}
		}
	}

}

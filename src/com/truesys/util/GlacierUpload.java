package com.truesys.util;

import java.io.File;
import java.util.Date;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;

public class GlacierUpload {
	
	public static AmazonGlacierClient client;
	
	public static void Do(String accessKey, String secretKey, String vaultName, String archiveName){
		// execute upload to vault
	    
				AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
				client = new AmazonGlacierClient(credentials);
				client.setEndpoint("https://glacier.us-east-1.amazonaws.com/");

				try {
					ArchiveTransferManager atm = new ArchiveTransferManager(client,
							credentials);

					UploadResult result = atm.upload(vaultName, archiveName
							+ (new Date()), new File(archiveName));
					System.out.println("Archive ID: " + result.getArchiveId());

				} catch (Exception e) {
					System.err.println(e);
				}

		
	}
}

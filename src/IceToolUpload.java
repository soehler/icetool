import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;

public class IceToolUpload {

	public static String vaultName = "";
	public static String archiveName = "";
	public static String accessKey = "";
	public static String secretKey =""; 
	                                 
	public static AmazonGlacierClient client;

	public static void main(String[] args) throws IOException {

		System.out.println("True Systems (c) 2012");
		System.out.println("AWS Glacier command line upload tool");
		System.out.println("");
		
		//create argument options
		Option vaultname = OptionBuilder.withArgName("vaultname").hasArg()
				.withDescription("vault to store archive").create("vaultname");

		Option archivename = OptionBuilder.withArgName("archivename").hasArg()
				.withDescription("archive to be stored in vault")
				.create("archivename");

		Option accesskey = OptionBuilder.withArgName("accesskey").hasArg()
				.withDescription("AWS accress key")
				.create("accesskey");
		
		Option secretkey = OptionBuilder.withArgName("secretkey").hasArg()
				.withDescription("AWS secret key")
				.create("secretkey");
		
		
		//create  options
		Options options = new Options();
		options.addOption( vaultname );
		options.addOption( archivename );
		options.addOption( accesskey );
		options.addOption( secretkey );

				
		// create the parser
	    CommandLineParser parser = new GnuParser();
	    try {
	    	
	    	// automatically generate the help statement
			  HelpFormatter formatter = new HelpFormatter();
			    
	    	
	    	// parse the command line arguments
	        CommandLine line = parser.parse( options, args );
	    
	        // has the  argument been passed?
		    if( line.hasOption( "vaultname" ) ) {
		        vaultName = line.getOptionValue( "vaultname" ).toString().trim();
		    }else{
		    	formatter.printHelp( "icetool", options );
		    	System.exit(10);
		    }
		    
		    if( line.hasOption( "archivename" ) ) {
		        archiveName = line.getOptionValue( "archivename" ).toString().trim();
		    }else{
		    	formatter.printHelp( "icetool", options );
		    	System.exit(10);
		    }
		    
		    if( line.hasOption( "vaultname" ) ) {
		        accessKey = line.getOptionValue( "accesskey" ).toString().trim();
		    }else{
		    	formatter.printHelp( "icetool", options );
		    	System.exit(10);
		    }
		    
		    if( line.hasOption( "vaultname" ) ) {
		        secretKey = line.getOptionValue( "secretley" ).toString().trim();
		    }else{
		    	formatter.printHelp( "icetool", options );
		    	System.exit(10);
		    }

	    }
	    catch( ParseException exp ) {
	        // oops, something went wrong
	        System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
	        System.exit(10);
	    }
		
		
		
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
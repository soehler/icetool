package com.truesys.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class IceTool {

	public static String vaultName = "";
	public static String archiveName = "";
	public static String accessKey = "";
	public static String secretKey =""; 
	public static String upload = "";

	
	public static Options options;

	

	public static void main(String[] args) throws IOException {

		System.out.println("True Systems (c) 2012");
		System.out.println("Sergio Oehler");
		System.out.println("AWS Glacier command line tool");
		System.out.println("");
		
		options = new Options();
		
		readCachedPropertiesFile();
		
		createOptions(); 
		
		parseCommandLine(args);
				
		writeCachedPropertiesFile();
				
		GlacierUpload.Do(accessKey,secretKey,vaultName,archiveName);
		
		
		
	}
	
	private static void createOptions(){
	
		        //create argument options
				@SuppressWarnings("static-access")
				Option vaultname = OptionBuilder.withArgName("vaultname").hasArg()
						.withDescription("vault to store archive").create("vaultname");

				@SuppressWarnings("static-access")
				Option archivename = OptionBuilder.withArgName("archivename").hasArg()
						.withDescription("archive to be stored in vault")
						.create("archivename");
				@SuppressWarnings("static-access")
						Option accesskey = OptionBuilder.withArgName("accesskey").hasArg()
						.withDescription("AWS accress key")
						.create("accesskey");
				@SuppressWarnings("static-access")
				Option secretkey = OptionBuilder.withArgName("secretkey").hasArg()
						.withDescription("AWS secret key")
						.create("secretkey");
				
				
				//create  options
				options.addOption( vaultname );
				options.addOption( archivename );
				options.addOption( accesskey );
				options.addOption( secretkey );

	}
	private static void parseCommandLine(String[] args){
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
		    }else if (vaultName.isEmpty()){
		    	formatter.printHelp( "icetool", options );
		    	System.exit(10);
		    }
		    
		    if( line.hasOption( "archivename" ) ) {
		        archiveName = line.getOptionValue( "archivename" ).toString().trim();
		    }else{
		    	formatter.printHelp( "icetool", options );
		    	System.exit(10);
		    }
		    
		    if( line.hasOption( "accesskey" ) ) {
		        accessKey = line.getOptionValue( "accesskey" ).toString().trim();
		    }else if (vaultName.isEmpty()){
		    	formatter.printHelp( "icetool", options );
		    	System.exit(10);
		    }
		    
		    if( line.hasOption( "secretkey" ) ) {
		        secretKey = line.getOptionValue( "secretley" ).toString().trim();
		    }else if (!vaultName.isEmpty()){
		    	formatter.printHelp( "icetool", options );
		    	System.exit(10);
		    }

	    }
	    catch( ParseException exp ) {
	        // oops, something went wrong
	        System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
	        System.exit(10);
	    }

	}
	public static void writeCachedPropertiesFile() {
	
		try {
			Properties properties = new Properties();
			properties.setProperty("accessKey", accessKey);
			properties.setProperty("secretKey", secretKey);
			properties.setProperty("vaultName", vaultName);

			File file = new File("icetool.properties");
			FileOutputStream fileOut = new FileOutputStream(file);
			properties.store(fileOut, "IceTool cached parameters");
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void readCachedPropertiesFile() {
		try {
			File file = new File("test.properties");
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();

			properties.setProperty("accessKey", accessKey);
			properties.setProperty("secretKey", secretKey);
			properties.setProperty("vaultName", vaultName);
			
			Enumeration enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				if (key =="accessKey"){
					accessKey = properties.getProperty(key);
				} else if (key =="secretKey"){
					secretKey = properties.getProperty(key);
				} else if (key =="vaultName"){
					vaultName = properties.getProperty(key);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
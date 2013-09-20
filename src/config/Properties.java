/**
 * 
 */
package config;

import java.io.FileInputStream;
import java.io.IOException;

public class  Properties{

	private static java.util.Properties prop;

	public static String WORDNET_DICT_PATH;
	public static String BROWN_PATH;
	public static String CMATIX_PATH;	
	static{

		prop = new java.util.Properties();
		try {
			//load a properties file
			prop.load(new FileInputStream("spellcheck.conf"));

			//get the property value and print it out
			WORDNET_DICT_PATH = prop.getProperty("WORDNET_DICT_PATH");
			BROWN_PATH = prop.getProperty("BROWN_PATH");
			CMATIX_PATH = prop.getProperty("CMATIX_PATH");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}

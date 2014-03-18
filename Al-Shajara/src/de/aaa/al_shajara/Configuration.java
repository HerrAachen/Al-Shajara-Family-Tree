package de.aaa.al_shajara;

import java.util.Properties;

public class Configuration extends Properties {

	private static final String PRIMARY_ALPHABET = "PRIMARY_ALPHABET";
	private static final String USE_PSEUDO_NODES = "USE_PSEUDO_NODES";
	
	private static Configuration instance;
	
	private Configuration() { }
	
	static {
      //defaults:
      setPrimaryAlphabet("latin");
      setUsePseudoNodes(true);
	}
	
	private static Configuration getInstance(){
	  if (instance==null){
	    instance = new Configuration();
	  }
	  return instance;
	}

	public static void setPrimaryAlphabet(String value){
		getInstance().setProperty(PRIMARY_ALPHABET, value);
	}
	
	public static String getPrimaryAlphabet(){
		return getInstance().getProperty(PRIMARY_ALPHABET);
	}
	
	public static void setUsePseudoNodes(Boolean value){
		getInstance().setProperty(USE_PSEUDO_NODES, String.valueOf(value));
	}
	
	/**
	 * If true pseudo nodes will be used between parent nodes and their children in order to reduce the number of edges when there are many children
	 */
	public static boolean isUsePseudoNodes(){
		return getInstance().getBooleanValue(USE_PSEUDO_NODES, false);
	}
	
	private boolean getBooleanValue(String attribute, boolean defaultValue){
		String property = getProperty(attribute);
		if (property==null)
			return defaultValue;
		else return Boolean.parseBoolean(property);
	}
	
}

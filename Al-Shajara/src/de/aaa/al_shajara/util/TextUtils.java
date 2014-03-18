package de.aaa.al_shajara.util;

public class TextUtils {

	public static String capitalize(String text){
		if (text==null || text.isEmpty())
			return text;
		char capital = Character.toUpperCase(text.charAt(0));
		return capital + text.substring(1);
	}
}

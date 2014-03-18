package de.aaa.al_shajara.data;

import java.util.Locale;

public class LocalizedStringProvider {

	public static String getStatusString(int status, Gender gender, Locale locale){
		return getStatusString_en(status, gender);
	}

	private static String getStatusString_en(int status, Gender gender) {
		if (status==0)
			return "no children";
		if (status==1)
			return gender==Gender.MALE?"father":"mother";
		else {
			StringBuilder statusString = new StringBuilder();
			for(int i=0;i<status-2;i++){
				statusString.append("great-");
			}
			return statusString.append(gender==Gender.MALE?"grandfather":"grandmother").toString();
		}
	}

}

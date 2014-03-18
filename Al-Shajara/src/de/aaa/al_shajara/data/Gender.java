package de.aaa.al_shajara.data;

public enum Gender {
	FEMALE,
	MALE,
	UNKOWN,
	NOT_SPECIFIED;
	
	public static Gender string2Gender(String genderString){
		if (genderString == null)
			return UNKOWN;
		String genderLowerCase = genderString.toLowerCase();
		for(Gender g: Gender.values()){
		  if (genderLowerCase.equals(g.xmlString())){
		    return g;
		  }
		}
		return UNKOWN;
	}
	
	public String xmlString(){
	  return super.toString().toLowerCase();
	}
	
	public String toString(){
		switch (this){
			case FEMALE: return "Female";
			case MALE: return "Male";
			case UNKOWN: return "Unknown";
			case NOT_SPECIFIED: return "Not Specified";
			default: return null;
		}
	}
}

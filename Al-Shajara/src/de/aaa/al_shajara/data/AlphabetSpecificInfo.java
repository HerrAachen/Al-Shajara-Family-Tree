package de.aaa.al_shajara.data;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AlphabetSpecificInfo {
	
	private String firstName = null;
	private String middleName = null;
	private String lastName = null;
	private String pseudonym = null;
	private String biography = null;
	private String cityOfBirth = null;
	private String countryOfBirth = null;
	private String maidenName = null;
	private List<BiographicEvent> biographyEvents = new LinkedList<BiographicEvent>();
	
	private final String alphabet;
	
	public String getAlphabet() {
		return alphabet;
	}

	public AlphabetSpecificInfo(String alphabet){
		this.alphabet = alphabet;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPseudonym() {
		return pseudonym;
	}
	public void setPseudonym(String pseudonym) {
		this.pseudonym = pseudonym;
	}
	public String getBiography() {
		return biography;
	}
	public void setBiography(String biography) {
		this.biography = biography;
	}
	public String getCityOfBirth() {
		return cityOfBirth;
	}
	public void setCityOfBirth(String cityOfBirth) {
		this.cityOfBirth = cityOfBirth;
	}
	public String getCountryOfBirth() {
		return countryOfBirth;
	}
	public void setCountryOfBirth(String countryOfBirth) {
		this.countryOfBirth = countryOfBirth;
	}

	String getMaidenName() {
		return maidenName;
	}

	void setMaidenName(String maidenName) {
		this.maidenName = maidenName;
	}
	
	public void addBiographyEvent(PartiallyDefinedDate date, String eventText){
		biographyEvents.add(new BiographicEvent(date,eventText));
	}
	
	public List<BiographicEvent> getBiographyEvents(){
		return biographyEvents;
	}

	public void addBiographyEvent(BiographicEvent event) {
		biographyEvents.add(event);
	}
}

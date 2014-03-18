package de.aaa.al_shajara.data;

import java.io.File;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import de.aaa.al_shajara.Configuration;

public class Person {

	private File originFile = null;
	private String id = null;

	List<AlphabetSpecificInfo> alphabetSpecificInfo = new LinkedList<AlphabetSpecificInfo>();

	private Gender gender = null;

	private PartiallyDefinedDate birthday = null;
	private PartiallyDefinedDate dayOfDeath = null;
	Person mother;
	Person father;
	Map<Person,Person> child2Partner = new HashMap<Person, Person>();
	

	void addPartnerChildMapping(Person partner, Person child){
		child2Partner.put(child, partner);
	}

	public Set<Person> getChildren(){
		return child2Partner.keySet();
	}

	public Map<Person,Person> children2OtherParent(){
		return child2Partner;
	}

	/**
	 * Gets the first name of this person, preferably from the primary alphabet.
	 * @return
	 */
	public String getFirstName(){
		return getValueFromAppropriateAlphabet(new GetterMethod<AlphabetSpecificInfo>() {
			@Override
			public String get(AlphabetSpecificInfo from) {
				return from.getFirstName();
			}
		});
	}

	public String getMaidenName(){
		return getValueFromAppropriateAlphabet(new GetterMethod<AlphabetSpecificInfo>() {
			@Override
			public String get(AlphabetSpecificInfo from) {
				return from.getMaidenName();
			}
		});
	}
	
	public List<BiographicEvent> getBiographicEvents(){
		String alphabet = "latin";
		AlphabetSpecificInfo info = getAlphabetInfo(alphabet);
		if (info!=null){
			return info.getBiographyEvents();
		}
		return null;
	}

	public String getFirstName(String alphabet) {
		AlphabetSpecificInfo info = getAlphabetInfo(alphabet);
		if (info!=null)
			return info.getFirstName();
		return null;
	}

	public String getMaidenName(String alphabet) {
		AlphabetSpecificInfo info = getAlphabetInfo(alphabet);
		if (info!=null)
			return info.getMaidenName();
		return null;
	}
	
	public void addBiographicEvent(BiographicEvent event){
		String alphabetName = "latin";
		AlphabetSpecificInfo info = getOrCreateAlphabetInfo(alphabetName);
		info.addBiographyEvent(event);
	}
	
	public void clearBiographicEvents(){
		String alphabetName = "latin";
		AlphabetSpecificInfo info = getOrCreateAlphabetInfo(alphabetName);
		info.getBiographyEvents().clear();
	}

	public void setFirstName(String firstName, String alphabet) {
		AlphabetSpecificInfo info = getOrCreateAlphabetInfo(alphabet);
		info.setFirstName(firstName);
	}

	private AlphabetSpecificInfo getOrCreateAlphabetInfo(String alphabet) {
		AlphabetSpecificInfo info = getAlphabetInfo(alphabet);
		if (info==null){
			AlphabetSpecificInfo alphaInfo = new AlphabetSpecificInfo(alphabet);
			alphabetSpecificInfo.add(alphaInfo);
			return alphaInfo;
		}
		return info;
	}

	/**
	 * Returns the middle name of this person preferably from the primary alphabet
	 * @return
	 */
	public String getMiddleName() {
		return getValueFromAppropriateAlphabet(new GetterMethod<AlphabetSpecificInfo>() {
			@Override
			public String get(AlphabetSpecificInfo from) {
				return from.getMiddleName();
			}
		});
	}

	public String getMiddleName(String alphabet) {
		AlphabetSpecificInfo info = getAlphabetInfo(alphabet);
		if (info!=null)
			return info.getMiddleName();
		return null;
	}
	public void setMiddleName(String middleName, String alphabet) {
		AlphabetSpecificInfo info = getOrCreateAlphabetInfo(alphabet);
		info.setMiddleName(middleName);
	}

	/**
	 * Returns the middle name of this person preferably from the primary alphabet
	 * @return
	 */
	public String getLastName() {
		return getValueFromAppropriateAlphabet(new GetterMethod<AlphabetSpecificInfo>() {
			@Override
			public String get(AlphabetSpecificInfo from) {
				return from.getLastName();
			}
		});
	}

	public String getLastName(String alphabet) {
		AlphabetSpecificInfo info = getAlphabetInfo(alphabet);
		if (info!=null)
			return info.getLastName();
		return null;
	}
	public void setLastName(String lastName, String alphabet) {
		AlphabetSpecificInfo info = getOrCreateAlphabetInfo(alphabet);
		info.setLastName(lastName);
	}

	public void setMaidenName(String maidenName, String alphabet) {
		AlphabetSpecificInfo info = getOrCreateAlphabetInfo(alphabet);
		info.setMaidenName(maidenName);
	}
	public PartiallyDefinedDate getBirthday() {
		return birthday;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public void setBirthday(PartiallyDefinedDate birthday) {
		this.birthday = birthday;
	}
	public PartiallyDefinedDate getDayOfDeath() {
		return dayOfDeath;
	}
	public void setDayOfDeath(PartiallyDefinedDate dayOfDeath) {
		this.dayOfDeath = dayOfDeath;
	}
	public String getBiography(String alphabet) {
		AlphabetSpecificInfo info = getAlphabetInfo(alphabet);
		if (info!=null)
			return info.getBiography();
		return null;
	}
	public void setBiography(String biography, String alphabet) {
		AlphabetSpecificInfo info = getOrCreateAlphabetInfo(alphabet);
		info.setBiography(biography);
	}
	public Person getMother() {
		return mother;
	}
	public void setMother(Person mother) {
		this.mother = mother;
	}
	public Person getFather() {
		return father;
	}
	public void setFather(Person father) {
		this.father = father;
	}
	public String getId() {
		return id;
	}

	public String getCityOfBirth(){
		return getValueFromAppropriateAlphabet(new GetterMethod<AlphabetSpecificInfo>() {

			@Override
			public String get(AlphabetSpecificInfo from) {
				return from.getCityOfBirth();
			}
		});
	}

	/**
	 * Determines which alphabet contains the value specified by the the getter method and
	 * gets it from there. 
	 * @param getter a getter function for the value that is requested
	 * @return the value from the alphabet in which it occurs, preferably in the primary alphabet
	 */
	public String getValueFromAppropriateAlphabet(GetterMethod<AlphabetSpecificInfo> getter){
		AlphabetSpecificInfo primaryInfo = getAlphabetInfo(Configuration.getPrimaryAlphabet());
		String primaryAlphabetValue = null;
		if (primaryInfo!=null){
			primaryAlphabetValue = getter.get(primaryInfo);
			if (primaryAlphabetValue!=null){
				return primaryAlphabetValue;
			}
		}
		for(AlphabetSpecificInfo i: alphabetSpecificInfo){
			String value = getter.get(i);
			if (value!=null){
				return value;
			}
		}
		return primaryAlphabetValue;
	}

	public String getCityOfBirth(String alphabet) {
		AlphabetSpecificInfo info = getAlphabetInfo(alphabet);
		if (info!=null)
			return info.getCityOfBirth();
		return null;
	}
	public void setCityOfBirth(String cityOfBirth,String alphabet) {
		AlphabetSpecificInfo info = getOrCreateAlphabetInfo(alphabet);
		info.setCityOfBirth(cityOfBirth);
	}

	public String getCountryOfBirth(){
		return getValueFromAppropriateAlphabet(new GetterMethod<AlphabetSpecificInfo>() {
			@Override
			public String get(AlphabetSpecificInfo from) {
				return from.getCountryOfBirth();
			}
		});
	}

	public String getCountryOfBirth(String alphabet) {
		AlphabetSpecificInfo info = getAlphabetInfo(alphabet);
		if (info!=null)
			return info.getCountryOfBirth();
		return null;
	}
	public void setCountryOfBirth(String countryOfBirth,String alphabet) {
		AlphabetSpecificInfo info = getOrCreateAlphabetInfo(alphabet);
		info.setCountryOfBirth(countryOfBirth);
	}

	public String getPseudonym() {
		return getValueFromAppropriateAlphabet(new GetterMethod<AlphabetSpecificInfo>() {

			@Override
			public String get(AlphabetSpecificInfo from) {
				return from.getPseudonym();
			}
		});
	}

	public String getPseudonym(String alphabet) {
		AlphabetSpecificInfo info = getAlphabetInfo(alphabet);
		if (info!=null)
			return info.getPseudonym();
		return null;
	}

	public void setPseudonym(String pseudonym, String alphabet) {
		AlphabetSpecificInfo info = getAlphabetInfo(alphabet);
		if (info!=null)
			info.setPseudonym(pseudonym);
	}

	public File getOriginFile() {
		return originFile;
	}

	public void setOriginFile(File originFile) {
		this.originFile = originFile;
	}

	public String getFullName(){
		return getValueFromAppropriateAlphabet(new GetterMethod<AlphabetSpecificInfo>() {

			@Override
			public String get(AlphabetSpecificInfo from) {
				return getFullName(from.getAlphabet());
			}
		});
	}

	public String getFullName(String alphabet){
		AlphabetSpecificInfo info = getAlphabetInfo(alphabet);
		String firstNameString = info.getFirstName()!=null?info.getFirstName():"";
		String middleNameString = info.getMiddleName()!=null?" " + info.getMiddleName():"";
		String maidenNameString = info.getMaidenName()!=null?" " + info.getMaidenName():"";
		String lastNameString = info.getLastName()!=null?" " + info.getLastName():"";
		String pseudonymString = info.getPseudonym()!=null?" \"" + info.getPseudonym() + "\"":"";
		String fullName = firstNameString + middleNameString + pseudonymString + (info.getMaidenName()==null?lastNameString:maidenNameString);
		if (fullName.isEmpty())
			return null;
		return fullName;
	}

	public String getFullNameShort(){
		return getValueFromAppropriateAlphabet(new GetterMethod<AlphabetSpecificInfo>() {

			@Override
			public String get(AlphabetSpecificInfo from) {
				return getFullNameShort(from.getAlphabet());
			}
		});
	}

	public String getFullNameShort(String alphabet){
		AlphabetSpecificInfo info = getAlphabetInfo(alphabet);
		String firstNameString = info.getFirstName()!=null?info.getFirstName():"";
		String lastNameString;
		if (info.getMaidenName()!=null && !info.getMaidenName().isEmpty()){
			lastNameString = info.getMaidenName();
		}
		else {
			lastNameString = info.getLastName()!=null?info.getLastName():"";
		}
		String[] lastNameParts = lastNameString.split(" ");
		String fullName = firstNameString + " " + lastNameParts[0];
		if (fullName.trim().isEmpty()){
			String pseudonymString = info.getPseudonym()!=null?info.getPseudonym():"";
			return pseudonymString;
		}
		return fullName;
	}

	public String getShortName(){
		return getValueFromAppropriateAlphabet(new GetterMethod<AlphabetSpecificInfo>() {

			@Override
			public String get(AlphabetSpecificInfo from) {
				return getShortName(from.getAlphabet());
			}
		});	
	}

	/**
	 * Returns pseudonym or first name if not present
	 * @param alphabet
	 * @return
	 */
	public String getShortName(String alphabet){
		AlphabetSpecificInfo info = getAlphabetInfo(alphabet);
		if (info.getPseudonym()!=null)
			return info.getPseudonym();
		else return info.getFirstName();

	}

	/**
	 * Returns the alphabets used for this person
	 * @return
	 */
	public List<String> getAlphabets(){
		List<String> alphas = new LinkedList<String>();
		for(AlphabetSpecificInfo info: alphabetSpecificInfo){
			alphas.add(info.getAlphabet());
		}
		return alphas;
	}

	//	public String getFullNameSecondaryWritingSystem(){
	//		 String fullName = getFirstNameSecondaryWritingSystem() + " " + (getMiddleNameSecondaryWritingSystem()!=null?getMiddleNameSecondaryWritingSystem() + " ":"") + getLastNameSecondaryWritingSystem();
	//		 return fullName;
	//	}

	@Override
	public String toString() {
		if (!alphabetSpecificInfo.isEmpty()){
			AlphabetSpecificInfo info = alphabetSpecificInfo.get(0);
			return getFullName(info.getAlphabet());
		}
		return id;
		//		return "Person ["
		//				+ (id != null ? "id=" + id + ", " : "")
		//				+ (firstName != null ? "firstName=" + firstName + ", " : "")
		//				+ (middleName != null ? "middleName=" + middleName + ", " : "")
		//				+ (lastName != null ? "lastName=" + lastName + ", " : "")
		//				+ (gender != null ? "gender=" + gender + ", " : "")
		//				+ (firstNameSecondaryWritingSystem != null ? "firstNameSecondaryWritingSystem="
		//						+ firstNameSecondaryWritingSystem + ", "
		//						: "")
		//				+ (middleNameSecondaryWritingSystem != null ? "middleNameSecondaryWritingSystem="
		//						+ middleNameSecondaryWritingSystem + ", "
		//						: "")
		//				+ (lastNameSecondaryWritingSystem != null ? "lastNameSecondaryWritingSystem="
		//						+ lastNameSecondaryWritingSystem + ", "
		//						: "")
		//				+ (birthday != null ? "birthday=" + birthday + ", " : "")
		//				+ (dayOfDeath != null ? "dayOfDeath=" + dayOfDeath + ", " : "")
		//				+ (biography != null ? "biography=" + biography + ", " : "")
		//				+ (cityOfBirth != null ? "cityOfBirth=" + cityOfBirth + ", "
		//						: "")
		//				+ (countryOfBirth != null ? "countryOfBirth=" + countryOfBirth
		//						+ ", " : "")
		//				+ (mother != null ? "mother=" + mother.getFullName() + ", " : "")
		//				+ (father != null ? "father=" + father.getFullName() + ", " : "")
		////				+ (partnerToChildren != null ? "partnerToChildren="
		////						+ partnerToChildren : "") 
		//						+ "]";
	}

	public void setId(String id) {
		this.id = id;
	}

	public AlphabetSpecificInfo getAlphabetInfo(String alphabet) {
		for(AlphabetSpecificInfo info: alphabetSpecificInfo){
			if (info.getAlphabet().equals(alphabet)){
				return info;
			}
		}
		return null;
	}

	public PersonalStatistics calculateStatistics(){
		PersonalStatistics stats = new PersonalStatistics();
		stats.setDescendantCount(countDescendants(this));
		stats.setDescendanceDepth(calculateMaxDepth(this));
		stats.setAge(calculateAge());
		stats.setAgesAtBirths(getChildBirthAges());
		return stats;
	}

	/**
	 * @return the ages of this person when its children where born
	 */
	private List<PartiallyDefinedDate> getChildBirthAges(){
		if (child2Partner==null || child2Partner.isEmpty())
			return null;
		List<PartiallyDefinedDate> ages = new LinkedList<PartiallyDefinedDate>();
		for(Person child: child2Partner.keySet()){
			PartiallyDefinedDate childBirthday = child.getBirthday();
			if (childBirthday!=null && this.getBirthday()!=null){
				PartiallyDefinedDate age = this.getBirthday().getDifference(childBirthday);
				if (age!=null){
					ages.add(age);
				}
			}
			else {
				ages.add(new PartiallyDefinedDate(null, null, null));//insert dummy value
			}
		}
		return ages;
	}

	/**
	 * @return Age in years. If month or day of birthday is not specified 01 is assumed. 
	 * If birthyear also unspecified -1 will be returned  
	 */
	public PartiallyDefinedDate calculateAge() {
		PartiallyDefinedDate bday = getBirthday();
		if (bday==null)
			return null;
		if (bday.getCentury()==null || bday.getDecadeInCentury()==null || bday.getYearInDecade()==null)
			return null;
		PartiallyDefinedDate currentDate = new PartiallyDefinedDate(GregorianCalendar.getInstance());
		PartiallyDefinedDate difference = bday.getDifference(currentDate);
		return difference;
	}

	private static int calculateMaxDepth(Person person){
		if (person.child2Partner==null || person.child2Partner.isEmpty()){
			return 0;
		}
		int maxDepth = 0;
		for(Person child: person.child2Partner.keySet()){
			int depth = calculateMaxDepth(child);
			if (depth>maxDepth){
				maxDepth=depth;
			}
		}
		return maxDepth+1;
	}

	/**
	 * Assumes that mapping from children to partners is uptodate
	 * @param person
	 * @return
	 */
	private static int countDescendants(Person person) {
		if (person.child2Partner==null || person.child2Partner.isEmpty())
			return 0;
		int generationSize = person.child2Partner.size();
		for(Person child: person.child2Partner.keySet()){
			generationSize+=countDescendants(child);
		}
		return generationSize;
	}

	public String getHTMLToolTip(){
		StringBuffer res = new StringBuffer();
		PersonalStatistics stats = calculateStatistics();
		res.append("<HTML><head>/<body>");
		res.append("<h3>"+ getFullName() + "</h3>");
		String motherString = getMother()!=null?getMother().getFullName():"";
		String fatherString = getFather()!=null?getFather().getFullName():"";
		String countryOfBirthString = getCountryOfBirth()!=null?getCountryOfBirth():"";
		String cityOfBirthString = getCityOfBirth()!=null?getCityOfBirth():"";
		String genderString = getGender()!=null?getGender().toString():"";
		String birthdayString = getBirthday()!=null?getBirthday().toString():"";
		res.append("<table align=\"left\" border=\"1\">");
		res.append("<tr><td>Birthday</td><td>" + birthdayString + "</td></tr>");
		res.append("<tr><td>Mother</td><td>" + motherString + "</td></tr>");
		res.append("<tr><td>Father</td><td>" + fatherString + "</td></tr>");
		res.append("<tr><td>City of birth</td><td>" + cityOfBirthString + "</td></tr>");
		res.append("<tr><td>Country of birth</td><td>" + countryOfBirthString + "</td></tr>");
		res.append("<tr><td>Gender</td><td>" + genderString + "</td></tr>");
		res.append("<tr><td>Descendants</td><td>" + stats.getDescendantCount() + "</td></tr>");
		res.append("<tr><td>Status</td><td>" + LocalizedStringProvider.getStatusString(stats.getDescendanceDepth(),gender, Locale.ENGLISH) + "</td></tr>");
		res.append("</table>");
		res.append("</body></HTML>");
		return res.toString();
	}

}

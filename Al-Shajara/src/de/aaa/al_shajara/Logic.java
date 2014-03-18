package de.aaa.al_shajara;

import java.io.File;
import java.io.FileNotFoundException;

import de.aaa.al_shajara.data.FamilyTree;
import de.aaa.al_shajara.data.Person;

public class Logic {

	private FamilyTree familyTree;
	
	/** The last directory that has been used for loading a family tree */
	private File currentDir = null;

	public Logic(){
		familyTree = new FamilyTree();
		
	}
	
	public FamilyTree getFamilyTree() {
		return familyTree;
	}
	
	/**
	 * Loads a family tree from the specified directory
	 * @param dir must be a directory
	 * @throws FileNotFoundException 
	 */
	public void loadFamilyTree(File dir) throws FileNotFoundException{
		if (!dir.exists()){
			throw new FileNotFoundException("The specified load directory does not exist:" + dir);
		}
		if (!dir.isDirectory()){
			throw new FileNotFoundException("Error loading the family tree. The specified path is not a directory:" + dir);
		}
		currentDir = dir;
		familyTree = Serialization.loadFamilyTree(dir);
	}
	
	public void exportFamilyTree(){
		if (currentDir!=null)
			exportFamilyTree(currentDir);
	}
	
	/**
	 * Writes the entire current family tree to a directory
	 * @param dir the target directory
	 */
	public void exportFamilyTree(File dir){
		for(Person p: familyTree.getMembers()){
			File saveFile = new File(dir,getFileName(p));
			exportPerson(p,saveFile);
		}
		currentDir = dir;
	}
	
	/**
	 * Exports an xml about the person to the specified file
	 * @param p
	 * @param saveFile
	 */
	public void exportPerson(Person p, File saveFile){
      try {
          XMLHelper.xml2File(Serialization.person2Xml(p),saveFile);
      } catch (Exception e) {
          System.out.println("Person:\n" + p);
          e.printStackTrace();
      }
	}
	
	/**
	 * Exports an xml for the specified person into the directory that has most recently
	 * been used for loading a family tree
	 * @param p
	 */
	public void exportPerson(Person p){
	  if (currentDir == null){
	    throw new NullPointerException("No family tree has been loaded yet. Cannot export person to current directory");
	  }
	  File saveFile = new File(currentDir,getFileName(p));
	  exportPerson(p,saveFile);
	}
	
	/**
	 * Overwrites the file containing the information about the specified person 
	 * @param p
	 * @param oldID
	 */
	public void overwritePerson(Person p, String oldID){
	  if (!oldID.equals(p.getId())){
	    File oldFile = new File(currentDir,getFileName(p));
	    oldFile.delete();
	  }
	  exportPerson(p);
	}
	
	/**
	 * Creates a new family member
	 * @return the created family member
	 */
	public Person createNewMember(){
		Person newPerson = new Person();
		String Id = familyTree.getRandomUnusedId();
		newPerson.setId(Id);
		newPerson.setFirstName("Anthony",Configuration.getPrimaryAlphabet());
		newPerson.setLastName("Anonymous",Configuration.getPrimaryAlphabet());
		familyTree.addMember(newPerson);
		return newPerson;
	}
	
	public void deletePerson(Person person){
		File file = new File(currentDir,getFileName(person));
		file.delete();
		familyTree.deletePerson(person);
	}

	/**
	 * Tells if an id is unique in a family tree
	 * @param id
	 * @param person
	 * @return true iff the specified id is not used by another person than the specified person
	 */
	public boolean verifyIdUnique(String id, Person person) {
		Person member = familyTree.getMemberById(id);
		if (member!=null && member != person){
			return false;
		}
		return true;
	}
	
	private String getFileName(Person p){
		if (p.getOriginFile()!=null){
			return p.getOriginFile().getName();
		}
	  return p.getId() + ".xml";
	}

	public void exportGraphics(File imageFile) {
		
	}
	
}

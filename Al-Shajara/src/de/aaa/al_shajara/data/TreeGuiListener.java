package de.aaa.al_shajara.data;

public interface TreeGuiListener {

	public void personClicked(Person clickedPerson);
	
	public void addChildCommand(Person parent);

	public void showDetails(boolean show);
	
	public void editPerson(Person toEdit);
	
	public void addFatherCommand(Person child);
	
	public void addMotherCommand(Person child);
	
	public void deletePerson(Person person);
}

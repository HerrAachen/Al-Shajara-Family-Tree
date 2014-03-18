package de.aaa.al_shajara.data;

public interface TreeChangedListener {

	void personAdded(Person addedPerson);
	
	void personModified(Person modifiedPerson);
	
	void personRemoved(Person removedPerson);
	
}

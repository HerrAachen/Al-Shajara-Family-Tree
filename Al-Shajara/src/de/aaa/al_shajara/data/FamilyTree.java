package de.aaa.al_shajara.data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import de.aaa.al_shajara.Configuration;
import de.aaa.al_shajara.gui.ListenerManager;

public class FamilyTree {

	/** contains all family members */
	final Map<String,Person> id2Person;
	/** Contains all persons in sorted data structure. Has to be kept synchronized to id2Person! */
	final SortedSet<Person> personsSorted;
	
	private List<String> alphabets = new LinkedList<String>();
	
	ListenerManager listenerManager = new ListenerManager();
	
	public FamilyTree(){
		id2Person = new HashMap<String, Person>();
		personsSorted = new TreeSet(new PersonComparator());
	}
	
	private void resetChildLinks(){
		for(Person p: getMembers()){
			p.child2Partner.clear();
		}
	}
	
	public void addChildLinks(){
		for(Person p: getMembers()){
			Person mother = p.getMother();
			Person father = p.getFather();
			if (mother!=null){
				mother.addPartnerChildMapping(father, p);
			}
			if (father!=null){
				father.addPartnerChildMapping(mother, p);
			}
		}
	}
	
	public void addMember(Person p){
		id2Person.put(p.getId(),p);
		personsSorted.add(p);
//		addChildLinks();
		listenerManager.firePersonAdded(p);
	}
	
	public void removeMember(Person p){
		Person removed = id2Person.remove(p.getId());
		personsSorted.remove(p);
		removeFromChildLinks(p);
		if (removed!=null){
			listenerManager.firePersonRemoved(removed);
		}
	}
	
	private void removeFromChildLinks(Person toRemove) {
		for(Person p: getMembers()){
			p.children2OtherParent().remove(toRemove);
		}
	}

	public Person getMemberById(String id){
		return id2Person.get(id);
	}
	
	public Person[] getMembers(){
		return personsSorted.toArray(new Person[0]);
	}
	
	public int getMemberCount(){
		return id2Person.size();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Familytree:\n");
		for(Person p: id2Person.values()){
			builder.append(p + "\n");
		}
		return builder.toString();
	}
	
	/** Returns a random ID, and guarantees that is different from
	 * all other used IDs
	 * @return
	 */
	public String getRandomUnusedId(){
		Random r = new Random();
		String randomId = String.valueOf(r.nextLong());
		while(id2Person.containsKey(randomId)){
			randomId = String.valueOf(r.nextInt());
		}
		return randomId;
	}
	
	public void addTreeChangedListener(TreeChangedListener listener){
		listenerManager.addTreeChangedListener(listener);
	}
	
	public List<String> getAlphabets() {
		return alphabets;
	}

	public void addAlphabet(String alphabet) {
		this.alphabets.add(alphabet);
	}

	private static class PersonComparator implements Comparator<Person> {

		@Override
		public int compare(Person o1, Person o2) {
			String primaryAlphabet = Configuration.getPrimaryAlphabet();
			if (o1==null || o2==null){
				System.err.println("FamilyTree.PersonComparator: one comparison value is null");
			}
			if (o1==null){
				if (o2==null)
					throw new NullPointerException("FamilyTree.PersonComparator: one comparison value is null");
				return 1;
			}
			if (o2==null)
				return -1;
			int res;
			res = compare(o1.getLastName(primaryAlphabet),o2.getLastName(primaryAlphabet));
			if (res!=0)
				return res;
			res = compare(o1.getFirstName(primaryAlphabet),o2.getFirstName(primaryAlphabet));
			if (res!=0)
				return res;
			res = compare(o1.getCountryOfBirth(primaryAlphabet),o2.getCountryOfBirth(primaryAlphabet));
			if (res!=0)
				return res;
			res = compare(o1.getCityOfBirth(primaryAlphabet),o2.getCityOfBirth(primaryAlphabet));
			if (res!=0)
				return res;
			res = compare(o1.getId(),o2.getId());
			return res;
		}
		
		private int compare(String a, String b){
			if (a==null){
				if (b==null)
					return 0;
				return 1;
			}
			if (b==null)
				return -1;
			return a.compareTo(b);
		}
	}

	public void deletePerson(Person person) {
		personsSorted.remove(person);
		String id = null;
		for(Map.Entry<String, Person> entry: id2Person.entrySet()){
			if (entry.getValue()==person){
				id = entry.getKey();
				break;
			}
		}
		if (id!=null){
			id2Person.remove(id);
		}
		resetChildLinks();
		listenerManager.firePersonRemoved(person);
	}
}

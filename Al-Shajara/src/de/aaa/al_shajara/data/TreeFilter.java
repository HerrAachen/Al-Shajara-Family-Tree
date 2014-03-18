package de.aaa.al_shajara.data;

import java.util.LinkedList;
import java.util.List;

import de.aaa.al_shajara.util.Filter;


public class TreeFilter {

	public static Person[] filter(FamilyTree tree, Filter<Person> filter){
		return filter(tree,new Filter[]{filter});
	}
	
	public static Person[] filter(FamilyTree tree, Filter<Person>[] filters){
		List<Person> filtered = new LinkedList<Person>();
		for(Person p: tree.getMembers()){
			boolean filteredOut = false;
			for(Filter<Person> filter: filters){
				if (filter.filter(p)){
					filteredOut = true;
					break;
				}
			}
			if (!filteredOut){
				filtered.add(p);
			}
		}
		return filtered.toArray(new Person[0]);
	}
}

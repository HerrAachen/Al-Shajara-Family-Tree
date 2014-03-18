package de.aaa.al_shajara.data;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PersonalStatistics {

	private int descendantCount;
	private int descendanceDepth;
	private PartiallyDefinedDate age;
	private List<PartiallyDefinedDate> agesAtBirths;

	public int getDescendanceDepth() {
		return descendanceDepth;
	}

	public void setDescendanceDepth(int descendanceDepth) {
		this.descendanceDepth = descendanceDepth;
	}

	public int getDescendantCount() {
		return descendantCount;
	}

	public void setDescendantCount(int descendants) {
		this.descendantCount = descendants;
	}

	public PartiallyDefinedDate getAge() {
		return age;
	}

	public void setAge(PartiallyDefinedDate age) {
		this.age = age;
	}

	public List<PartiallyDefinedDate> getAgesAtBirths() {
		return agesAtBirths;
	}

	void setAgesAtBirths(List<PartiallyDefinedDate> agesAtBirths) {
		this.agesAtBirths = agesAtBirths;
		if (this.agesAtBirths!=null){
			Collections.sort(this.agesAtBirths);
		}
	}
}

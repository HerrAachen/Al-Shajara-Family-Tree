package de.aaa.al_shajara.util;

import java.util.Date;

import de.aaa.al_shajara.data.Gender;
import de.aaa.al_shajara.data.Person;

public class FilterFactory {

	/**
	 * Returns a filter that only lets through persons with the specified genders
	 * @param genders
	 * @return
	 */
	public static Filter<Person> createGenderFilter(final Gender... genders){
		return new Filter<Person>() {

			@Override
			public boolean filter(Person element) {
				for(Gender g: genders){
					Gender gender = element.getGender();
					if (gender==null || gender.equals(g)){
						return false;
					}
				}
				return true;
			}
		};
	}
	
	public static Filter<Person> createOlderThanFilter(final Date date){
		return new Filter<Person>() {

			@Override
			public boolean filter(Person element) {
				// TODO Auto-generated method stub
				return false;
			}
			
		};
	}
	
	/**
	 * Returns a filter that lets through all persons except the specified one
	 * @param exception
	 * @return
	 */
	public static Filter<Person> createExceptionFilter(final Person exception){
		return new Filter<Person>() {

			@Override
			public boolean filter(Person element) {
				if (exception==null)
					return false;
				return element.getId().equals(exception.getId());
			}
		}; 
	}
}

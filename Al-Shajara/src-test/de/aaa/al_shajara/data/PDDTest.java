package de.aaa.al_shajara.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class PDDTest {

	@Test
	public void testDifference(){
		PartiallyDefinedDate date1 = new PartiallyDefinedDate(1960, 9, 1);
		PartiallyDefinedDate date2 = new PartiallyDefinedDate(1964, 8, 1);
		PartiallyDefinedDate difference = date1.getDifference(date2);
		assertEquals(new Integer(3),difference.getYear());
		assertEquals(new Integer(11),difference.getMonthInYear());

		difference = new PartiallyDefinedDate(1960, 9, 10).getDifference(new PartiallyDefinedDate(1964, 9, 15));
		assertEquals(new Integer(4),difference.getYear());
		assertEquals(new Integer(5),difference.getDayInMonth());

		difference = new PartiallyDefinedDate(1960, 9, 10).getDifference(new PartiallyDefinedDate(1964, 9, 5));
		assertEquals(new Integer(3),difference.getYear());
		assertEquals(new Integer(11),difference.getMonthInYear());
		assertEquals(new Integer(26),difference.getDayInMonth());
		
	}
}

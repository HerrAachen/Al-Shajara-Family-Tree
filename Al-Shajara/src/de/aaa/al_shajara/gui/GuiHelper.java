package de.aaa.al_shajara.gui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;

import de.aaa.al_shajara.data.PartiallyDefinedDate;
import de.aaa.al_shajara.data.Person;

public class GuiHelper {

	private static final SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MM.yyyy");
	
	public static File getFileFromDialog(int mode){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(mode);
		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal==JFileChooser.APPROVE_OPTION){
			File selectedFile = fileChooser.getSelectedFile();
			return selectedFile;
		}
		return null; 
	}
	
	public static Person[] appendNullValueAtBeginning(Person[] persons){
		Person[] res = new Person[persons.length+1];
		res[0] = null;
		System.arraycopy(persons, 0, res, 1, persons.length);
		return res;
	}

	public static String formatForDisplay(Date d){
		if (d==null)
			return "";
		return displayFormat.format(d);
	}
	
	public static String formatForDisplay(PartiallyDefinedDate d){
		if (d==null)
			return "";
		return d.toString();
	}
}

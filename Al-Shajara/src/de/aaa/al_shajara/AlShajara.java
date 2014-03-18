package de.aaa.al_shajara;

import java.io.File;
import java.io.FileNotFoundException;

import de.aaa.al_shajara.gui.MainWindow;

public class AlShajara {

	private static final String dataDir = "data";
	public static final String VERSION = "0.2_alpha";
	
	public static void main(String[] args){
		Logic logic = new Logic();
		try {
			logic.loadFamilyTree(new File(dataDir));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		MainWindow mainWindow = new MainWindow(logic);
		mainWindow.setVisible(true);
	}
}

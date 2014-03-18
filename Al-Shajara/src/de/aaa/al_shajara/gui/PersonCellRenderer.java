package de.aaa.al_shajara.gui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import de.aaa.al_shajara.data.Person;

public class PersonCellRenderer extends DefaultListCellRenderer implements TableCellRenderer {

	private static PersonCellRenderer instance = null;
	DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
	
	public static PersonCellRenderer getInstance(){
		if (instance==null)
			instance = new PersonCellRenderer();
		return instance;
	}
	
	
	private PersonCellRenderer(){
		super();
	}
	
	private static final long serialVersionUID = -3998171843782650123L;
     public Component getListCellRendererComponent(JList list, Object value,int index,boolean isSelected,boolean cellHasFocus) {
    	 return super.getListCellRendererComponent(list, getPersonString((Person) value), index, isSelected, cellHasFocus);
     }


	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
		return defaultTableCellRenderer.getTableCellRendererComponent(table, getPersonString((Person) value), isSelected, hasFocus, row, column);
	}
	
	private String getPersonString(Person p){
		if (p==null)
			return "";
		return p.getFullName();
	}
	
}
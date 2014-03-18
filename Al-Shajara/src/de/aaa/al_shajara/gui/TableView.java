package de.aaa.al_shajara.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import de.aaa.al_shajara.Logic;
import de.aaa.al_shajara.data.PartiallyDefinedDate;
import de.aaa.al_shajara.data.Person;
import de.aaa.al_shajara.data.PersonalStatistics;

public class TableView extends JPanel {

	private static final long serialVersionUID = -6991873700752862504L;
	
	private final Logic logic;
	private static final int firstNameCol = 0;
	private static final int middleNameCol = 1;
	private static final int lastNameCol = 2;
	private static final int motherCol = 3;
	private static final int fatherCol = 4;
	private static final int birthdayCol = 5;
	private static final int cityOfBirthCol = 6;
	private static final int descendantsCol = 7;

	private static final String[] columnNames = new String[]{"First Name","Middle Name","Last Name","Mother","Father", "Birthday", "City Of Birth","Descendants"};

	public TableView(Logic logic) {
		super();
		this.logic = logic;
		this.createLayout();
	}

	private void createLayout() {
		JTable familyTable = new JTable();
		familyTable.setAutoCreateRowSorter(true);
		familyTable.setModel(new FamilyTableModel());
		familyTable.getColumnModel().getColumn(motherCol).setCellRenderer(PersonCellRenderer.getInstance());
		familyTable.getColumnModel().getColumn(fatherCol).setCellRenderer(PersonCellRenderer.getInstance());
		familyTable.getColumnModel().getColumn(birthdayCol).setCellRenderer(DateCellRenderer.getInstance());
		JScrollPane tablePanel = new JScrollPane(familyTable);
		this.setLayout(new BorderLayout());
		this.add(tablePanel, BorderLayout.CENTER);
	}
	
	private class FamilyTableModel extends DefaultTableModel {
		private static final long serialVersionUID = -2147464225509341209L;
		
		public String getColumnName(int columnIndex){
			return columnNames[columnIndex];
		}
		
		public int getColumnCount(){
			return columnNames.length;
		}
		
		public int getRowCount(){
			return logic.getFamilyTree().getMemberCount(); 
		}
		
		public Class<?> getColumnClass(int columnIndex){
			if (columnIndex==birthdayCol){
				return PartiallyDefinedDate.class;
			}
			if (columnIndex==descendantsCol){
				return Integer.class;
			}
			return String.class;
		}
		
		public Object getValueAt(int rowIndex, int columnIndex){
			Person p = logic.getFamilyTree().getMembers()[rowIndex];
			PersonalStatistics stats = p.calculateStatistics();
			switch(columnIndex){
				case firstNameCol: return p.getFirstName();
				case middleNameCol: return p.getMiddleName();
				case lastNameCol: return p.getLastName();
				case motherCol: return p.getMother();
				case fatherCol: return p.getFather();
				case birthdayCol: return p.getBirthday();
				case cityOfBirthCol: return p.getCityOfBirth();
				case descendantsCol: return new Integer(stats.getDescendantCount());
			}
			return null;
		}
	}

	public void refreshTable() {
		
	}
}

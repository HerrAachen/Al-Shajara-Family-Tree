package de.aaa.al_shajara.gui;

import java.awt.Component;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import de.aaa.al_shajara.data.PartiallyDefinedDate;

public class DateCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 5422314690518838427L;
	private static final DateCellRenderer instance = new DateCellRenderer();
	
	public static DateCellRenderer getInstance(){
		return instance;
	}
	
	private DateCellRenderer(){
		
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
		return super.getTableCellRendererComponent(table, GuiHelper.formatForDisplay((PartiallyDefinedDate)value), isSelected, hasFocus, row, column);
	}
}

package de.aaa.al_shajara.gui;

import javax.swing.JComboBox;

public class EnumBox<T extends Enum<?>> extends JComboBox {

	private static final long serialVersionUID = 8120934182632358850L;
	T[] values;
	
	public EnumBox(T[] values){
		super(values);
		this.values = values;
	}
	
	public T getSelectedItem(){
		Object selectedItem = super.getSelectedItem();	
		return (T)selectedItem;
	}
}

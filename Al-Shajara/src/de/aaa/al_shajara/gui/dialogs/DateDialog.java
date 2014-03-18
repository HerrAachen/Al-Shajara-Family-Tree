package de.aaa.al_shajara.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;

import de.aaa.al_shajara.data.PartiallyDefinedDate;
import de.aaa.al_shajara.gui.ListenerManager;
import de.aaa.al_shajara.util.SpringUtilities;

public class DateDialog extends AbstractDialog {

	private PartiallyDefinedDate currentDate;
	private JSpinner daySpinner;
	private JSpinner monthSpinner;
	private JSpinner yearSpinner;
	private JCheckBox yearUndefinedBox = new JCheckBox("Unknown");
	private JCheckBox monthUndefinedBox = new JCheckBox("Unknown");
	private JCheckBox dayUndefinedBox = new JCheckBox("Unknown");
	private String[] monthStrings;
	public ListenerManager listenerManager = new ListenerManager();

	public DateDialog(Window owner, String title, PartiallyDefinedDate currentDate) {
		super(owner);
		this.currentDate = currentDate;
		createLayout();
		this.setTitle(title);
		centerInParent();
	}

	//	private void createLayout() {
	//		GridLayout layout = new GridLayout(5,3);
	//		this.setLayout(layout);
	//	}

	public void createLayout() {
		this.setLayout(new BorderLayout());
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new SpringLayout());

		String[] labels = {"Day:","Month: ", "Year: "};
		int numPairs = labels.length;
		Calendar calendar = Calendar.getInstance();
		JFormattedTextField ftf = null;


		int currentDay;
		if (currentDate==null){
			currentDay = calendar.get(Calendar.DAY_OF_MONTH);
		}
		else {
			currentDay = 1;
			if (currentDate.getDayInMonth()!=null)
				currentDay = currentDate.getDayInMonth();
		}
		SpinnerModel dayModel = new SpinnerNumberModel(currentDay, 1, 31, 1);
		daySpinner = addLabeledSpinner(inputPanel, labels[0], dayModel);
		daySpinner.setEditor(new JSpinner.NumberEditor(daySpinner, "#"));
		inputPanel.add(dayUndefinedBox);

		//Add the first label-spinner pair.
		int currentMonth;
		if (currentDate==null){
			currentMonth = calendar.get(Calendar.MONTH);
		}
		else {
			currentMonth = 0;
			if (currentDate.getMonthInYear()!=null){
				currentMonth = currentDate.getMonthInYear()-1;
			}
		}
		monthStrings = getMonthStrings(); //get month names
		SpinnerListModel monthModel = null;
		monthModel = new SpinnerListModel(monthStrings);
		monthModel.setValue(monthStrings[currentMonth]);
		monthSpinner = addLabeledSpinner(inputPanel, labels[1], monthModel);

		//Tweak the spinner's formatted text field.
		ftf = getTextField(monthSpinner);
		if (ftf != null ) {
			ftf.setColumns(8); //specify more width than we need
			ftf.setHorizontalAlignment(JTextField.RIGHT);
		}

		inputPanel.add(monthUndefinedBox);


		//Add second label-spinner pair.
		int currentYear;
		currentYear = calendar.get(Calendar.YEAR);
		if (currentDate!=null && currentDate.getYear()!=null){
			currentYear = currentDate.getYear();
		}
		SpinnerModel yearModel = new SpinnerNumberModel(currentYear, 0, 10000, 1);
		yearSpinner = addLabeledSpinner(inputPanel, labels[2], yearModel);
		//Make the year be formatted without a thousands separator.
		yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));

		inputPanel.add(yearUndefinedBox);

		//Lay out the panel.
		SpringUtilities.makeCompactGrid(inputPanel,
				numPairs, 3, //rows, cols
				10, 10,        //initX, initY
				6, 10);       //xPad, yPad

		dayUndefinedBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				daySpinner.setEnabled(!dayUndefinedBox.isSelected());
			}
		});
		monthUndefinedBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				monthSpinner.setEnabled(!monthUndefinedBox.isSelected());
			}
		});
		yearUndefinedBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				yearSpinner.setEnabled(!yearUndefinedBox.isSelected());
			}
		});
		
		if (currentDate!=null && currentDate.getDayInMonth()==null)
			dayUndefinedBox.doClick();
		if (currentDate!=null && currentDate.getMonthInYear()==null)
			monthUndefinedBox.doClick();
		if (currentDate!=null && currentDate.getYear()==null)
			yearUndefinedBox.doClick();

		JPanel buttonPanel = new JPanel();
		JButton saveButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				PartiallyDefinedDate date = getDateFromGui();
				listenerManager.fireActionEvent(date, -1, "");
				close();
			}
		});

		this.add(inputPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.PAGE_END);
		this.setSize(300,200);
	}

	/**
	 * Return the formatted text field used by the editor, or
	 * null if the editor doesn't descend from JSpinner.DefaultEditor.
	 */
	public JFormattedTextField getTextField(JSpinner spinner) {
		JComponent editor = spinner.getEditor();
		if (editor instanceof JSpinner.DefaultEditor) {
			return ((JSpinner.DefaultEditor)editor).getTextField();
		} else {
			System.err.println("Unexpected editor type: "
					+ spinner.getEditor().getClass()
					+ " isn't a descendant of DefaultEditor");
			return null;
		}
	}

	public PartiallyDefinedDate getDateFromGui(){
		Integer year=null;
		Integer month=null;
		Integer day=null;
		if (!yearUndefinedBox.isSelected())
			year = (Integer) yearSpinner.getValue();
		if (!monthUndefinedBox.isSelected()){
			String monthString = (String) monthSpinner.getValue();
			for(int i=0;i<monthStrings.length;i++){
				String m = monthStrings[i];
				if (m.equals(monthString)){
					month = i;
					break;
				}
			}
		}
		if (!dayUndefinedBox.isSelected())
			day = (Integer) daySpinner.getValue();
		return new PartiallyDefinedDate(year, month, day);
	}

	/**
	 * DateFormatSymbols returns an extra, empty value at the
	 * end of the array of months.  Remove it.
	 */
	static protected String[] getMonthStrings() {
		String[] months = new java.text.DateFormatSymbols().getMonths();
		int lastIndex = months.length - 1;

		if (months[lastIndex] == null
				|| months[lastIndex].length() <= 0) { //last item empty
			String[] monthStrings = new String[lastIndex];
			System.arraycopy(months, 0,
					monthStrings, 0, lastIndex);
			return monthStrings;
		} else { //last item not empty
			return months;
		}
	}

	static protected JSpinner addLabeledSpinner(Container c, String label, SpinnerModel model) {
		JLabel l = new JLabel(label);
		c.add(l);

		JSpinner spinner = new JSpinner(model);
		l.setLabelFor(spinner);
		c.add(spinner);

		return spinner;
	}
}

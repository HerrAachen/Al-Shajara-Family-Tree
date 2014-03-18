package de.aaa.al_shajara.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.text.JTextComponent;

import de.aaa.al_shajara.Logic;
import de.aaa.al_shajara.Serialization;
import de.aaa.al_shajara.data.BiographicEvent;
import de.aaa.al_shajara.data.Gender;
import de.aaa.al_shajara.data.PartiallyDefinedDate;
import de.aaa.al_shajara.data.Person;
import de.aaa.al_shajara.data.TreeChangedListener;
import de.aaa.al_shajara.data.TreeFilter;
import de.aaa.al_shajara.data.TreeGuiListener;
import de.aaa.al_shajara.gui.dialogs.DateDialog;
import de.aaa.al_shajara.util.Filter;
import de.aaa.al_shajara.util.FilterFactory;
import de.aaa.al_shajara.util.SpringUtilities;
import de.aaa.al_shajara.util.TextUtils;

public class EditFormView extends JPanel implements TreeChangedListener, TreeGuiListener {

	private static final long serialVersionUID = -3802899504618508716L;

	private Logic logic;
	private Person person;
	private StatisticsPanel statisticsPanel;

	private JTextField idField;
	private Map<String,JTextComponent> alphabet2FirstNameField;
	private Map<String,JTextComponent> alphabet2MiddleNameField;
	private Map<String,JTextComponent> alphabet2MaidenNameField;
	private Map<String,JTextComponent> alphabet2LastNameField;
	private Map<String,JTextComponent> alphabet2pseudonymField;
	private Map<String,JTextComponent> alphabet2cityOfBirthField;
	private Map<String,JTextComponent> alphabet2countryOfBirthField;
	private Map<String,JTextComponent> alphabet2biographyField;
	private EnumBox<Gender> genderBox;
	private JTextField birthdayField;
	private JTextField dayOfDeathField;
	private JComboBox motherBox;
	private JComboBox fatherBox;
	private JPanel biographicEventsPanel;
	private Map<BiographicEvent,BiographicEventGuiItem> bioEvent2Gui = new HashMap<BiographicEvent, EditFormView.BiographicEventGuiItem>();
	private JPanel outerBiographicEventsPanel; 
	
	public ListenerManager listenerManager = new ListenerManager();

	public EditFormView(Logic logic){
		this.logic = logic;
		createLayout();
		this.listenerManager.addChangedListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				updateComboBoxes();
			}
		});
		this.logic.getFamilyTree().addTreeChangedListener(this);
	}

	private void createLayout() {
		JPanel editPanel = new JPanel();

		idField = new JTextField();
		alphabet2FirstNameField = new HashMap<String, JTextComponent>();
		alphabet2MiddleNameField = new HashMap<String, JTextComponent>();
		alphabet2MaidenNameField = new HashMap<String, JTextComponent>();
		alphabet2LastNameField = new HashMap<String, JTextComponent>();
		alphabet2pseudonymField = new HashMap<String, JTextComponent>();
		alphabet2cityOfBirthField = new HashMap<String, JTextComponent>();
		alphabet2countryOfBirthField = new HashMap<String, JTextComponent>();
		alphabet2biographyField = new HashMap<String, JTextComponent>();
		for(String alphabet: logic.getFamilyTree().getAlphabets()){
			alphabet2FirstNameField.put(alphabet, new JTextField());
			alphabet2MiddleNameField.put(alphabet, new JTextField());
			alphabet2MaidenNameField.put(alphabet, new JTextField());
			alphabet2LastNameField.put(alphabet, new JTextField());
			alphabet2pseudonymField.put(alphabet, new JTextField());
			alphabet2cityOfBirthField.put(alphabet, new JTextField());
			alphabet2countryOfBirthField.put(alphabet, new JTextField());
			JTextComponent bioField = new JTextArea();
			bioField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			alphabet2biographyField.put(alphabet,bioField);
		}

		genderBox = new EnumBox<Gender>(Gender.values());
		birthdayField = new JTextField();
		dayOfDeathField = new JTextField();
		motherBox = new JComboBox();
		motherBox.setRenderer(PersonCellRenderer.getInstance());
		fatherBox = new JComboBox();
		fatherBox.setRenderer(PersonCellRenderer.getInstance());
		updateComboBoxes();

		List<String> alphabets= logic.getFamilyTree().getAlphabets();
		editPanel.setLayout(new BorderLayout());
		JPanel alphabetSpecificPanel = new JPanel();
//		alphabetSpecificPanel.setLayout(new GridLayout(8,alphabets.size()+1));
		alphabetSpecificPanel.setLayout(new SpringLayout());
		alphabetSpecificPanel.add(new JLabel());
		for(String alpha: alphabets){
			alphabetSpecificPanel.add(new JLabel(TextUtils.capitalize(alpha)));
		}
		addAlphabetSpecificLine(alphabetSpecificPanel,new JLabel("First Name:"),alphabet2FirstNameField,alphabets);
		addAlphabetSpecificLine(alphabetSpecificPanel,new JLabel("Middle Name:"),alphabet2MiddleNameField,alphabets);
		addAlphabetSpecificLine(alphabetSpecificPanel,new JLabel("Maiden Name:"),alphabet2MaidenNameField,alphabets);
		addAlphabetSpecificLine(alphabetSpecificPanel,new JLabel("Last Name:"),alphabet2LastNameField,alphabets);
		addAlphabetSpecificLine(alphabetSpecificPanel,new JLabel("Pseudonym:"),alphabet2pseudonymField,alphabets);
		addAlphabetSpecificLine(alphabetSpecificPanel,new JLabel("City of Birth:"),alphabet2cityOfBirthField,alphabets);
		addAlphabetSpecificLine(alphabetSpecificPanel,new JLabel("Country of Birth:"),alphabet2countryOfBirthField,alphabets);
		SpringUtilities.makeCompactGrid(alphabetSpecificPanel, 8, alphabets.size()+1, 5, 5, 5, 0);
		//		editPanel.add(alphabetSpecificPanel,BorderLayout.CENTER);
		GridLayout gridLayout = new GridLayout(6, 2);
		JPanel alphabetIndependentPanel = new JPanel();
//		alphabetIndependentPanel.setLayout(gridLayout);
		alphabetIndependentPanel.setLayout(new SpringLayout());
		alphabetIndependentPanel.add(new JLabel("ID:"));
		alphabetIndependentPanel.add(idField);
		alphabetIndependentPanel.add(new JLabel("Gender:"));
		alphabetIndependentPanel.add(genderBox);
		alphabetIndependentPanel.add(new JLabel("Birthday:"));
		alphabetIndependentPanel.add(birthdayField);
		alphabetIndependentPanel.add(new JLabel("Day Of Death:"));
		alphabetIndependentPanel.add(dayOfDeathField);
		alphabetIndependentPanel.add(new JLabel("Mother:"));
		alphabetIndependentPanel.add(motherBox);
		alphabetIndependentPanel.add(new JLabel("Father:"));
		alphabetIndependentPanel.add(fatherBox);
		SpringUtilities.makeCompactGrid(alphabetIndependentPanel, 6, 2, 5, 5, 5, 0);

		birthdayField.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				createDialog("Birthday of " + person.getShortName(), person.getBirthday(), birthdayField, true);
			}
		});

		dayOfDeathField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				createDialog("Day of death of " + person.getShortName(), person.getDayOfDeath(), dayOfDeathField, false);
			}
		});

		//		editPanel.add(alphabetIndependentPanel,BorderLayout.PAGE_END);
		JPanel allExceptBiographyPanel = new JPanel();
		allExceptBiographyPanel.setLayout(new BoxLayout(allExceptBiographyPanel, BoxLayout.PAGE_AXIS));
		allExceptBiographyPanel.add(alphabetIndependentPanel);
		allExceptBiographyPanel.add(alphabetSpecificPanel);

		JPanel biographyPanel = new JPanel();
		biographyPanel.setLayout(new BorderLayout());
		biographicEventsPanel = new JPanel();
		outerBiographicEventsPanel = new JPanel();
		outerBiographicEventsPanel.setLayout(new SpringLayout());
		outerBiographicEventsPanel.add(new JLabel("Biographic Events"));
		outerBiographicEventsPanel.add(biographicEventsPanel);
		SpringUtilities.makeCompactGrid(outerBiographicEventsPanel, 1, 2, 5, 5, 5, 0);
		JPanel biographyFreetextPanel = new JPanel();
//		biographyFreetextPanel.setLayout(new GridLayout(1, alphabets.size()+1));
		biographyFreetextPanel.setLayout(new SpringLayout());
		addAlphabetSpecificLine(biographyFreetextPanel, new JLabel("Biography Free Text"), alphabet2biographyField, alphabets);
		SpringUtilities.makeCompactGrid(biographyFreetextPanel, 1, alphabets.size()+1, 5, 5, 5, 0);
		biographyPanel.add(biographyFreetextPanel,BorderLayout.CENTER);
		biographyPanel.add(outerBiographicEventsPanel,BorderLayout.PAGE_END);
		editPanel.add(allExceptBiographyPanel,BorderLayout.PAGE_START);
		editPanel.add(biographyPanel,BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton createButton = new JButton("New");
		JButton saveButton = new JButton("Save");
		JButton deleteButton = new JButton("Delete");
		createButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Person newP = logic.createNewMember();
				setPerson(newP);
			}
		});
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				deletePerson(person);
			}
		});
		buttonPanel.add(createButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(deleteButton);

		statisticsPanel = new StatisticsPanel();
		this.setLayout(new BorderLayout());
		this.add(buttonPanel, BorderLayout.PAGE_START);
		this.add(editPanel, BorderLayout.CENTER);
		this.add(statisticsPanel,BorderLayout.PAGE_END);
	}

	private void createDialog(String title,PartiallyDefinedDate date, final JTextField field, final boolean birthday){
		if (person==null)
			return;
		DateDialog dateDialog = new DateDialog((Window) getTopLevelAncestor(),title,date);
		dateDialog.listenerManager.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PartiallyDefinedDate date = (PartiallyDefinedDate) e.getSource();
				if (date.getYear()==null && date.getMonthInYear()==null && date.getDayInMonth()==null && date.getHour()==null && date.getMinute()==null && date.getSecond()==null)
					date = null;
				if (birthday)
					person.setBirthday(date);
				else 
					person.setDayOfDeath(date);
				setDate(field,date);
			}
		});
		dateDialog.setVisible(true);
	}

	private void addAlphabetSpecificLine(JPanel alphabetSpecificPanel, JLabel jLabel, Map<String, JTextComponent> alphabet2Field,List<String> alphabetOrder) {
		if (jLabel!=null)
			alphabetSpecificPanel.add(jLabel);
		for(String alphabet: alphabetOrder){
			JTextComponent field = alphabet2Field.get(alphabet);
			alphabetSpecificPanel.add(field);
		}
	}

	private void updateComboBoxes() {

		DefaultComboBoxModel motherModel = (DefaultComboBoxModel)motherBox.getModel();
		motherModel.removeAllElements();
		motherModel.addElement(null);
		Filter<Person> femaleFilter = FilterFactory.createGenderFilter(Gender.FEMALE,Gender.UNKOWN,Gender.NOT_SPECIFIED);
		Filter<Person> maleFilter = FilterFactory.createGenderFilter(Gender.MALE,Gender.UNKOWN,Gender.NOT_SPECIFIED);
		Filter<Person> exceptionFilter = FilterFactory.createExceptionFilter(person);
		for(Person person: TreeFilter.filter(logic.getFamilyTree(),new Filter[]{femaleFilter,exceptionFilter})){
			motherModel.addElement(person);
		}
		if (person!=null && person.getMother()!=null){
			motherModel.setSelectedItem(person.getMother());
		}
		DefaultComboBoxModel fatherModel = (DefaultComboBoxModel)fatherBox.getModel();
		fatherModel.removeAllElements();
		fatherModel.addElement(null);
		for(Person person: TreeFilter.filter(logic.getFamilyTree(),new Filter[]{maleFilter,exceptionFilter})){
			fatherModel.addElement(person);
		}
		if (person!=null && person.getFather()!=null){
			fatherModel.setSelectedItem(person.getFather());
		}

	}

	public void setPerson(Person person){
		this.person = person;
		idField.setText(this.person.getId());
		for(String alphabet: logic.getFamilyTree().getAlphabets()){
			JTextComponent firstNameField = alphabet2FirstNameField.get(alphabet);
			JTextComponent middleNameField = alphabet2MiddleNameField.get(alphabet);
			JTextComponent maidenNameField = alphabet2MaidenNameField.get(alphabet);
			JTextComponent lastNameField = alphabet2LastNameField.get(alphabet);
			JTextComponent pseudonymField = alphabet2pseudonymField.get(alphabet);
			JTextComponent cityOfBirthField = alphabet2cityOfBirthField.get(alphabet);
			JTextComponent countryOfBirthField = alphabet2countryOfBirthField.get(alphabet);
			JTextComponent biographyField = alphabet2biographyField.get(alphabet);
			setTextToField(firstNameField,person.getFirstName(alphabet));
			setTextToField(middleNameField,person.getMiddleName(alphabet));
			setTextToField(maidenNameField,person.getMaidenName(alphabet));
			setTextToField(lastNameField,person.getLastName(alphabet));
			setTextToField(pseudonymField,person.getPseudonym(alphabet));
			setTextToField(cityOfBirthField,person.getCityOfBirth(alphabet));
			setTextToField(countryOfBirthField,person.getCountryOfBirth(alphabet));
			setTextToField(biographyField,person.getBiography(alphabet));
		}
		List<BiographicEvent> biographicEvents = person.getBiographicEvents();
		biographicEventsPanel.removeAll();
		if (biographicEvents!=null && !biographicEvents.isEmpty()){
			biographicEventsPanel.setLayout(new GridLayout(biographicEvents.size(), 3));
			for(BiographicEvent event: biographicEvents){
				PartiallyDefinedDate startDate = event.getStartDate();
				PartiallyDefinedDate endDate = event.getEndDate();
				String eventText = event.getEventText();
				BiographicEventGuiItem guiItem = new BiographicEventGuiItem();
				setDate(guiItem.startDate, startDate);
				setDate(guiItem.endDate,endDate);
				if (eventText!=null){
					guiItem.eventText.setText(eventText);
				}
				bioEvent2Gui.put(event, guiItem);
				biographicEventsPanel.add(guiItem.startDate);
				biographicEventsPanel.add(guiItem.endDate);
				biographicEventsPanel.add(guiItem.eventText);
//				guiItem.startDate.addMouseListener(new MouseAdapter() {
//
//					@Override
//					public void mouseReleased(MouseEvent e) {
//						createDialog("Birthday of " + person.getShortName(), person.getBirthday(), guiItem.startDate, true);
//					}
//				});				
			}
		}
//		SpringUtilities.makeCompactGrid(outerBiographicEventsPanel, 1, 2, 5, 5, 5, 0);
		outerBiographicEventsPanel.validate();
//		biographicEventsPanel.validate();
		genderBox.setSelectedItem(this.person.getGender());
		//		firstNameSecondaryField.setText(this.person.getFirstNameSecondaryWritingSystem());
		//		middleNameSecondaryField.setText(this.person.getMiddleNameSecondaryWritingSystem());
		//		lastNameSecondaryField.setText(this.person.getLastNameSecondaryWritingSystem());
		setDate(birthdayField,this.person.getBirthday());
		setDate(dayOfDeathField,this.person.getDayOfDeath());
		updateComboBoxes();
		statisticsPanel.refresh(person);
	}

	private class BiographicEventGuiItem {
		JTextField startDate = new JTextField();
		JTextField endDate = new JTextField();
		JTextField eventText = new JTextField();

	}

	private void resetGui(){
		idField.setText("");
		for(String alphabet: logic.getFamilyTree().getAlphabets()){
			JTextComponent firstNameField = alphabet2FirstNameField.get(alphabet);
			JTextComponent middleNameField = alphabet2MiddleNameField.get(alphabet);
			JTextComponent maidenNameField = alphabet2MaidenNameField.get(alphabet);
			JTextComponent lastNameField = alphabet2LastNameField.get(alphabet);
			JTextComponent pseudonymField = alphabet2pseudonymField.get(alphabet);
			JTextComponent cityOfBirthField = alphabet2cityOfBirthField.get(alphabet);
			JTextComponent countryOfBirthField = alphabet2countryOfBirthField.get(alphabet);
			JTextComponent biographyField = alphabet2biographyField.get(alphabet);
			setTextToField(firstNameField,"");
			setTextToField(middleNameField,"");
			setTextToField(maidenNameField,"");
			setTextToField(lastNameField,"");
			setTextToField(pseudonymField,"");
			setTextToField(cityOfBirthField,"");
			setTextToField(countryOfBirthField,"");
			setTextToField(biographyField,"");
		}
		genderBox.setSelectedItem(null);
		birthdayField.setText("");
		dayOfDeathField.setText("");
		updateComboBoxes();
	}

	private void setTextToField(JTextComponent field, String text) {
		if (field==null || text==null)
			field.setText("");
		field.setText(text);
	}

	private void setDate(JTextField field, PartiallyDefinedDate d){
		try {
			if (d!=null){
				field.setText(d.xmlString());
			}
			else field.setText("");
		}
		catch(Exception e){
			field.setText("");
		}
	}

	private Date getDate(JTextField field){
		try {
			Date date = Serialization.xmlDateFormat.parse(field.getText());
			return date;
		}
		catch (Exception e){
			return null;
		}
	}

	private PartiallyDefinedDate getPartialDate(JTextField field){
		try {
			return PartiallyDefinedDate.parse(field.getText());
		}
		catch (Exception e){
			System.out.println("EditFormView " + e.getMessage());
			return null;
		}
	}

	public void save(){
		boolean idUnique = logic.verifyIdUnique(idField.getText(), person);
		if (!idUnique){
			JOptionPane.showMessageDialog(this.getTopLevelAncestor(), "The ID is already used for another person");
			return;
		}
		String oldID = person.getId();
		person.setId(idField.getText());
		for(String alphabet: logic.getFamilyTree().getAlphabets()){
			JTextComponent firstNameField = alphabet2FirstNameField.get(alphabet);
			JTextComponent middleNameField = alphabet2MiddleNameField.get(alphabet);
			JTextComponent maidenNameField = alphabet2MaidenNameField.get(alphabet);
			JTextComponent lastNameField = alphabet2LastNameField.get(alphabet);
			JTextComponent pseudonymField = alphabet2pseudonymField.get(alphabet);
			JTextComponent cityOfBirthField = alphabet2cityOfBirthField.get(alphabet);
			JTextComponent countryOfBirthField = alphabet2countryOfBirthField.get(alphabet);
			JTextComponent biographyField = alphabet2biographyField.get(alphabet);
			person.setFirstName(getTextValue(firstNameField),alphabet);
			person.setMiddleName(getTextValue(middleNameField),alphabet);
			person.setMaidenName(getTextValue(maidenNameField),alphabet);
			person.setLastName(getTextValue(lastNameField),alphabet);
			person.setPseudonym(getTextValue(pseudonymField),alphabet);
			person.setCityOfBirth(getTextValue(cityOfBirthField),alphabet);
			person.setCountryOfBirth(getTextValue(countryOfBirthField),alphabet);
			person.setBiography(getTextValue(biographyField), alphabet);
		}
		person.setGender(genderBox.getSelectedItem());
		person.setBirthday(getPartialDate(birthdayField));
		person.setDayOfDeath(getPartialDate(dayOfDeathField));
		person.setMother((Person) motherBox.getSelectedItem());
		person.setFather((Person) fatherBox.getSelectedItem());
		person.clearBiographicEvents();
		for(BiographicEventGuiItem item: bioEvent2Gui.values()){
			BiographicEvent event = new BiographicEvent(getPartialDate(item.startDate),getPartialDate(item.endDate),item.eventText.getText());
			person.addBiographicEvent(event);
		}

		listenerManager.fireChangedEvent(person, 0, oldID);
		listenerManager.firePersonModified(person);
	}

	private String getTextValue(JTextComponent textField){
		if (textField==null)
			return null;
		return textField.getText().trim().isEmpty()?null:textField.getText();
	}

	@Override
	public void personAdded(Person addedPerson) {
		updateComboBoxes();
	}

	@Override
	public void personModified(Person modifiedPerson) {
		updateComboBoxes();
	}

	@Override
	public void personRemoved(Person removedPerson) {
		updateComboBoxes();
	}

	@Override
	public void personClicked(Person clickedPerson) {
		setPerson(clickedPerson);
	}

	@Override
	public void addChildCommand(Person parent) {
		Person newP = logic.createNewMember();
		if (parent.getGender()==Gender.MALE){
			newP.setFather(parent);
		}
		else newP.setMother(parent);
		setPerson(newP);
		listenerManager.firePersonAdded(newP);
	}

	@Override
	public void showDetails(boolean show) {
		//do nothing
	}

	@Override
	public void editPerson(Person toEdit) {
		setPerson(toEdit);
	}

	@Override
	public void addFatherCommand(Person child) {
		Person newP = logic.createNewMember();
		newP.setGender(Gender.MALE);
		child.setFather(newP);
		setPerson(newP);
		listenerManager.firePersonAdded(newP);
	}

	@Override
	public void addMotherCommand(Person child) {
		Person newP = logic.createNewMember();
		newP.setGender(Gender.FEMALE);
		child.setMother(newP);
		setPerson(newP);
		listenerManager.firePersonAdded(newP);
	}

	@Override
	public void deletePerson(Person person) {
		logic.deletePerson(person);
		if (this.person==person){
			this.person=null;
			resetGui();
		}
		listenerManager.firePersonRemoved(person);
	}

}

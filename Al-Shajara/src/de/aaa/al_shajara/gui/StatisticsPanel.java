package de.aaa.al_shajara.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import de.aaa.al_shajara.data.LocalizedStringProvider;
import de.aaa.al_shajara.data.PartiallyDefinedDate;
import de.aaa.al_shajara.data.Person;
import de.aaa.al_shajara.data.PersonalStatistics;
import de.aaa.al_shajara.util.SpringUtilities;

public class StatisticsPanel extends JPanel {

	private JLabel descendantCountLabel = new JLabel();
	private JLabel statusLabel = new JLabel();
	private JLabel ageLabel = new JLabel();
	private JLabel childBirthAgesLabel = new JLabel();
	
	public StatisticsPanel() {
		super();
		createLayout();
	}

	private void createLayout() {
		this.setLayout(new BorderLayout());
		this.add(new JLabel("Statistics:"),BorderLayout.PAGE_START);
		JPanel statPanel = new JPanel();
//		GridLayout layout = new GridLayout(4, 2);
		SpringLayout layout = new SpringLayout();
		statPanel.setLayout(layout);
		
		statPanel.add(new JLabel("Descendants"));
		statPanel.add(descendantCountLabel);
		statPanel.add(new JLabel("Status"));
		statPanel.add(statusLabel);
		statPanel.add(new JLabel("Age"));
		statPanel.add(ageLabel);
		statPanel.add(new JLabel("Had children at ages"));
		statPanel.add(childBirthAgesLabel);
		SpringUtilities.makeCompactGrid(statPanel, 4, 2, 5, 5, 10, 0);
		this.add(statPanel,BorderLayout.CENTER);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	
	public void refresh(Person person){
		PersonalStatistics stats = person.calculateStatistics();
		descendantCountLabel.setText(String.valueOf(stats.getDescendantCount()));
		statusLabel.setText(String.valueOf(LocalizedStringProvider.getStatusString(stats.getDescendanceDepth(),person.getGender(),Locale.ENGLISH)));
		PartiallyDefinedDate age = stats.getAge();
		if (age!=null)
			ageLabel.setText(age.timeSpanString());
		else 
			ageLabel.setText("");
		List<PartiallyDefinedDate> agesAtBirths = stats.getAgesAtBirths();
		if (agesAtBirths!=null && !agesAtBirths.isEmpty()){
			StringBuilder builder = new StringBuilder();
			for(PartiallyDefinedDate bAge: agesAtBirths){
				builder.append((bAge==null || bAge.getYear()==null?"?":bAge.getYear()) + ",");
			}
			childBirthAgesLabel.setText(builder.substring(0, builder.length()-1));
		}
		else 
			childBirthAgesLabel.setText("");
	}
}

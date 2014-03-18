package de.aaa.al_shajara.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.MissingResourceException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import de.aaa.al_shajara.AlShajara;
import de.aaa.al_shajara.Logic;
import de.aaa.al_shajara.gui.dialogs.OptionsDialog;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 8256514606865269888L;

	private final Logic logic;
	ListView listView;
	TableView tableView;
	TreeEditView personalTreeView;

	public MainWindow(Logic logic){
		this.logic = logic;
		createLayout();
	}

	private void createLayout() {
		try {
//			ResourceBundle labels =  PropertyResourceBundle.getBundle("GUI_resource");
			this.setSize(new Dimension(1000, 700));
			this.setTitle("Al-Shajara Family Tree Editor V" + AlShajara.VERSION);
			createMenu();
			JTabbedPane tabbedPane = new JTabbedPane();
			listView = new ListView(logic);
			tableView = new TableView(logic);
			tabbedPane.addTab("List", listView);
			tabbedPane.addTab("Table", tableView);
			personalTreeView = new TreeEditView(logic);
			tabbedPane.addTab("Personal Tree", personalTreeView);
			tabbedPane.addChangeListener(personalTreeView);
			this.add(tabbedPane);

			//		listView.addChangedListener(new ActionListener() {
			//			@Override
			//			public void actionPerformed(ActionEvent e) {
			//				tableView.refreshTable();
			//			}
			//		});
		}
		catch(MissingResourceException e){
			System.out.println(e.getClassName());
			System.out.println(e.getKey());
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

	private void createMenu() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem importItem = new JMenuItem("Import");
		JMenuItem exportItem = new JMenuItem("Export XML");
		JMenuItem exportGraphicsItem = new JMenuItem("Export Graphics");
		JMenuItem saveAllItem = new JMenuItem("Save All");
		JMenuItem optionsItem = new JMenuItem("Options");
		JMenuItem quitItem = new JMenuItem("Quit");
		importItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File dir = GuiHelper.getFileFromDialog(JFileChooser.DIRECTORIES_ONLY);
				if (dir!=null){
					try {
						logic.loadFamilyTree(dir);
						listView.fillList();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		exportItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File dir = GuiHelper.getFileFromDialog(JFileChooser.DIRECTORIES_ONLY);
				if (dir!=null){
					logic.exportFamilyTree(dir);
				}
			}
		});
		exportGraphicsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File dir = GuiHelper.getFileFromDialog(JFileChooser.FILES_ONLY);
				if (dir!=null){
					Image image = personalTreeView.grabCurrentImage();
					try {
						ImageIO.write((RenderedImage) image, "png", dir);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		saveAllItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				logic.exportFamilyTree();
			}
		});
		final JFrame thisRef = this;
		optionsItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				OptionsDialog dialog = new OptionsDialog(thisRef,logic);
				dialog.setVisible(true);
			}
		});

		quitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(importItem);
		fileMenu.add(exportItem);
		fileMenu.add(exportGraphicsItem);
		fileMenu.add(saveAllItem);
		fileMenu.add(optionsItem);
		fileMenu.add(quitItem);
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
	}
}

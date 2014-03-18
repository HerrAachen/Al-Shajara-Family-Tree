package de.aaa.al_shajara.gui.graphics2D;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.aaa.al_shajara.Logic;
import de.aaa.al_shajara.data.Person;

public class TreeVisualizationAAA extends TreeGraphicPanel {

	private static final long serialVersionUID = 3845978897915743109L;
	
	private static final int horizontalOffset = 10;
	private static final int verticalOffset = 10;
	
	private List<PersonShape> shapes;
	private Map<Person,PersonShape> person2Shape;
	

	public TreeVisualizationAAA(Logic logic, Person centerPerson) {
		super(logic,centerPerson);
//		this.setSize(this.getWidth(), 1000);
//		this.setBounds(getX(), getY(), getWidth(), 1400);
	}
	
	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		AwtGraphicsAccess graphicsAccess = new AwtGraphicsAccess(g);
        calculateDimensions(graphicsAccess);
		calculatePositions(g);
		draw(graphicsAccess);
	}

	private void draw(AwtGraphicsAccess g){
		for(PersonShape shape: shapes){
			shape.paint(g);
		}
	}
	
	private void calculatePositions(Graphics g){
		int y =  verticalOffset;
		for(PersonShape shape: shapes){
			shape.setTopLeft(horizontalOffset,y);
			y+=shape.getHeight() + 10;
		}
		this.setPreferredSize(new Dimension(this.getWidth(), y));
		
		calculateRelativePositions();
		calculateAbsolutePositions();
	}

	private void calculateDimensions(GraphicsAccess g){
		shapes = new LinkedList<PersonShape>();
		person2Shape = new HashMap<Person, PersonShape>();
		for(Person p: logic.getFamilyTree().getMembers()){
			PersonShape personShape = new PersonShape(p);
			personShape.calculateDrawValues();
			shapes.add(personShape);
			person2Shape.put(p, personShape);
		}
	}
	
	private void calculateRelativePositions(){
		PersonShape centerShape = person2Shape.get(centerPerson);
		centerShape.setTopLeft(0,0);
	}
	
	private void calculateAbsolutePositions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image grabCurrentImage() {
		// TODO Auto-generated method stub
		return null;
	}
}

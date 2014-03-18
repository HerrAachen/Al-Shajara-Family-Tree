package de.aaa.al_shajara.gui.graphics2D.jgraphx;

import java.util.HashMap;
import java.util.Map;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;

import de.aaa.al_shajara.data.Person;

public class FamilyGraphJGX extends mxGraph {

	//	Set<Person> vertices = new HashSet<Person>();
	Map<Person,Object> person2Vertex = new HashMap<Person, Object>();

	final Object frameNode;

	public FamilyGraphJGX(){
		frameNode = getDefaultParent();
	}

	@Override
	public String convertValueToString(Object cell){
		if (cell instanceof mxCell)
		{
			Object value = ((mxCell) cell).getValue();
			if (value instanceof Person){
				return ((Person) value).getFullNameShort();
			}
		}
		return super.convertValueToString(cell);
	}

	@Override
	public mxRectangle getCellBounds(Object cell){
		String label = convertValueToString(cell);
		return new mxRectangle(0, 0, label.length()*3, 50);
	}

	public Object insertVertex(Object parent, String id, Object value, double x, double y, double width, double height, String style, boolean relative){
		if (value instanceof Person){
			boolean isNew = !person2Vertex.containsKey(value); 
			if (isNew){
				Object vertex = super.insertVertex(parent, id, value, x, y, width, height, style, relative);
				person2Vertex.put((Person)value, vertex);
				return vertex;
			}
			else return person2Vertex.get(value);
		}
		return super.insertVertex(parent, id, value, x, y, width,height, style, relative);
//		else throw new IllegalArgumentException("node value must be of type Person:" + value);
	}

	/**
	 * Adds the person to the family tree if not already present
	 * 
	 * @param nodeValue
	 */
	public Object insertVertex(Person nodeValue) {
		if (nodeValue==null){
			throw new NullPointerException();
		}
		return insertVertex(frameNode, null, nodeValue, -1, -1, nodeValue.getFullNameShort().length()*7, 30);
	}

	@Override
	public String getToolTipForCell(Object cell) {
		Object value = ((mxCell)cell).getValue();
		if (value instanceof Person){
			return ((Person)value).getHTMLToolTip();
		}
		return super.getToolTipForCell(cell);
	}

	public Object insertPseudoNode() {
		return insertVertex(frameNode, null, null, -1, -1, 1, 1);
	}

	public Object insertEdgeToPseudoNode(Object source, Object target){
		return super.insertEdge(frameNode, null, "", source, target,"startArrow=none;endArrow=none");
	}
	
	public Object insertEdgeToPerson(Object source, Object target){
		return super.insertEdge(frameNode, null, "", source, target);
	}
}

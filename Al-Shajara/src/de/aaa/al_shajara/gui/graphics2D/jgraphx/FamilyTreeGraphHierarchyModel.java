package de.aaa.al_shajara.gui.graphics2D.jgraphx;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.hierarchical.model.mxGraphHierarchyModel;
import com.mxgraph.layout.hierarchical.model.mxGraphHierarchyNode;
import com.mxgraph.model.mxCell;

import de.aaa.al_shajara.data.Person;

public class FamilyTreeGraphHierarchyModel extends mxGraphHierarchyModel {

	LinkedList<mxGraphHierarchyNode> startNodes;
	private final int NO_LAYER_ASSIGNED = Integer.MIN_VALUE;
	private int famTreeMaxRank = NO_LAYER_ASSIGNED; 
	private int famTreeMinRank = Integer.MAX_VALUE; 
	public FamilyTreeGraphHierarchyModel(mxHierarchicalLayout arg0,
			Object[] arg1, List<Object> arg2, Object arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	
	/**
	 * Overrides the original implementation with one that assigns the same layer 
	 * to all persons from the same generation to create the appearance of a 
	 * family tree.
	 */
	@Override
	public void initialRank(){
		Collection<mxGraphHierarchyNode> internalNodes = vertexMapper.values();
		startNodes = new LinkedList<mxGraphHierarchyNode>();

		if (roots != null) {
			Iterator<Object> iter = roots.iterator();

			while (iter.hasNext()) {
				mxGraphHierarchyNode internalNode = vertexMapper.get(iter.next());

				if (internalNode != null) {
					startNodes.add(internalNode);
				}
			}
		}

		Iterator<mxGraphHierarchyNode> iter = internalNodes.iterator();

		while (iter.hasNext()){
			mxGraphHierarchyNode internalNode = iter.next();
			// Mark the node as not having had a layer assigned
			internalNode.temp[0] = NO_LAYER_ASSIGNED;
		}

//		for(mxGraphHierarchyNode startNode: startNodes){
		while(!startNodes.isEmpty()){
			mxGraphHierarchyNode startNode = startNodes.getFirst();
			mxCell startPerson = null;
			for(Object sP: vertexMapper.keySet()){
				if (vertexMapper.get(sP)==startNode){
					startPerson = (mxCell) sP;
				}
			}
			System.out.println("FamilyTreeGraphHierarchyModel start node:" + (((Person)(startPerson.getValue())).getFullName("latin")));
			assignLayers(startPerson,0);
			startNodes.remove(startNode);
		}

		for(mxGraphHierarchyNode node: vertexMapper.values()){
			node.temp[0]=famTreeMaxRank-node.temp[0];
		}
		maxRank = famTreeMaxRank-famTreeMinRank;
//		printResults();
	}

	/**
	 * Recursively assigns layers to the nodes connected to the start node.
	 * Traverses parents and children, if they don't have a layer assigned yet
	 * @param node
	 * @param layer
	 */
	private void assignLayers(mxCell node, int layer) {
//		System.out.println("assign " + cellString(node) + ":" + layer);
		mxGraphHierarchyNode hierarchyNode = vertexMapper.get(node);
		hierarchyNode.temp[0]=layer;
		startNodes.remove(hierarchyNode);
		if (layer>famTreeMaxRank){
			famTreeMaxRank = layer;
		}
		if (layer<famTreeMinRank){
			famTreeMinRank=layer;
		}
		//traverse children (targets of outgoing edges)
		for(int i=0;i<node.getEdgeCount();i++){
			mxCell edge = (mxCell) node.getEdgeAt(i);
			if (edge!=null && edge.isEdge()){
				mxCell child = (mxCell) edge.getTarget();
				if (child!=null && child!=node){
					mxGraphHierarchyNode childHierarchyNode = vertexMapper.get(child);
					if (childHierarchyNode.temp[0]==NO_LAYER_ASSIGNED){
						assignLayers(child,layer+1);
					}
				}
			}
		}
		//traverse parents (sources of incoming edges)
		for(int i=0;i<node.getEdgeCount();i++){
			mxCell edge = (mxCell) node.getEdgeAt(i);
			if (edge!=null && edge.isEdge()){
				mxCell target = (mxCell) edge.getTarget();
				mxCell source = (mxCell) edge.getSource();
				if (target==node){
					mxGraphHierarchyNode parentHierarchyNode = vertexMapper.get(source);
					if (parentHierarchyNode.temp[0]==NO_LAYER_ASSIGNED){
						assignLayers(source,layer-1);
					}
				}
			}
		}
//		Object nodeValue = node.getValue();
//		if (nodeValue instanceof Person){
//			Person nodePerson = (Person) nodeValue;
//			Person mother = nodePerson.getMother();
//			Person father = nodePerson.getFather();
//			mxCell motherCell = searchCell(mother);
//			mxCell fatherCell = searchCell(father);
//			if (motherCell!=null){
//				mxGraphHierarchyNode motherhierarchyNode = vertexMapper.get(motherCell);
//				if (motherhierarchyNode.temp[0]==NO_LAYER_ASSIGNED){
//					assignLayers(motherCell, layer-1);
//				}
//			}
//			if (fatherCell!=null){
//				mxGraphHierarchyNode fatherHierarchyNode = vertexMapper.get(fatherCell);
//				if (fatherHierarchyNode.temp[0]==NO_LAYER_ASSIGNED){
//					assignLayers(fatherCell, layer-1);
//				}
//			}
//		}
	}

	/**
	 * @param toSearch
	 * @return the mxCell that corresponds to the specified person
	 */
	private mxCell searchCell(Person toSearch) {
		if (toSearch==null){
			return null;
		}
		for(Map.Entry<Object, mxGraphHierarchyNode> entry: vertexMapper.entrySet()){
			Object cell = entry.getKey();
			if (toSearch==((mxCell)cell).getValue()){
				return (mxCell) cell;
			}
		}
		return null;
	}

//	private void printResults() {
//		for(Object n: vertexMapper.keySet()){
//			System.out.println(cellString(n) + ":" + vertexMapper.get(n).temp[0]);
//		}
//		System.out.println("maxRank:" + maxRank);
//		System.out.println();
//	}
//	
//	private String cellString(Object cell){
//		return ((Person)((mxCell)cell).getValue()).getFullName("latin");
//	}
}

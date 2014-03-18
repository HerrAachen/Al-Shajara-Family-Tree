package de.aaa.al_shajara.gui.graphics2D.jgraphx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.hierarchical.model.mxGraphHierarchyModel;
import com.mxgraph.layout.hierarchical.stage.mxCoordinateAssignment;
import com.mxgraph.layout.hierarchical.stage.mxHierarchicalLayoutStage;
import com.mxgraph.layout.hierarchical.stage.mxMedianHybridCrossingReduction;
import com.mxgraph.view.mxGraph;

import de.aaa.al_shajara.Configuration;

public class FamilyTreeHierarchicalLayout extends mxHierarchicalLayout {

	public FamilyTreeHierarchicalLayout(mxGraph arg0) {
		super(arg0);
	}

	/**
	 * Is a copy of the original code. The only modification is that it uses FamilyTreeGraphHierarchyModel
	 */
	public void run(Object parent)
	{
		// Separate out unconnected hierarchies
		List<Set<Object>> hierarchyVertices = new ArrayList<Set<Object>>();
		Set<Object> allVertexSet = new LinkedHashSet<Object>();

		if (this.roots == null && parent != null)
		{
			Set<Object> filledVertexSet = filterDescendants(parent);

			this.roots = new ArrayList<Object>();

			while (!filledVertexSet.isEmpty())
			{
				List<Object> candidateRoots = findRoots(parent, filledVertexSet);

				for (Object root : candidateRoots)
				{
					Set<Object> vertexSet = new LinkedHashSet<Object>();
					hierarchyVertices.add(vertexSet);

					traverse(root, true, null, allVertexSet, vertexSet,
							hierarchyVertices, filledVertexSet);
				}

				this.roots.addAll(candidateRoots);
			}
		}
		else
		{
			// Find vertex set as directed traversal from roots

			for (int i = 0; i < roots.size(); i++)
			{
				Set<Object> vertexSet = new LinkedHashSet<Object>();
				hierarchyVertices.add(vertexSet);

				traverse(roots.get(i), true, null, allVertexSet, vertexSet,
						hierarchyVertices, null);
			}
		}

		// Iterate through the result removing parents who have children in this layout


		// Perform a layout for each separate hierarchy
		// Track initial coordinate x-positioning
		double initialX = 0;
		Iterator<Set<Object>> iter = hierarchyVertices.iterator();

		while (iter.hasNext())
		{
			Set<Object> vertexSet = iter.next();

//			if (Configuration.isUsePseudoNodes()){
//				this.model = new mxGraphHierarchyModel(this, vertexSet.toArray(),roots, parent);
//			}
//			else {
				this.model = new FamilyTreeGraphHierarchyModel(this, vertexSet.toArray(),roots, parent);
//			}

			cycleStage(parent);
			layeringStage();
			crossingStage(parent);
			initialX = placementStage(initialX, parent);
		}
	}

	@Override
	public void crossingStage(Object parent){
		mxHierarchicalLayoutStage crossingStage;
//		if (Configuration.isUsePseudoNodes()){
//			crossingStage = new mxMedianHybridCrossingReduction(this);//original implementation
//		}
//		else {
			crossingStage = new FamilyTreeCrossingReduction(this);
//		}
		crossingStage.execute(parent);
	}
}

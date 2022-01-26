package main.view;

import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

import tree.controllers.MyTreeSelectionController;

/**
 * @author StefanP
 * View za nas JTree
 * */
public class JTreeView extends JTree{
	private static final long serialVersionUID = 7136034661585918056L;

	public JTreeView() {
		setTree();
	}
	
	private void setTree() {
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);	//moze samo jedan objekat da se selektuje u jedno vreme
		addTreeSelectionListener(new MyTreeSelectionController());		
		setEditable(false);	
	}

}

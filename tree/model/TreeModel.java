package tree.model;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

/**
 * @author StefanP
 * */
public class TreeModel extends DefaultTreeModel{
	private static final long serialVersionUID = 886441262401450009L;

	public TreeModel(MutableTreeNode root) {
		super(root);
	}
}

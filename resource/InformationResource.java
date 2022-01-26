package resource;

import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import lombok.Data;
import lombok.ToString;

/**
 * @author StefanP
 * Ovo je nasa baza i ovo ce biti root u JTree-u
 * */
public class InformationResource extends CompositeNode{

	public InformationResource(String name) {
		super(name, null);	//nema parenta jer je root
	}

	@Override
	public void addChildren(SNode children) {
		if(children != null && children instanceof Entity) {
			Entity e= (Entity)children;
			this.getChildren().add(e);
		}			
	}
	
	//Metode potrebne za tree
	@Override
	public boolean getAllowsChildren() {
		return true;
	}
	
	@Override
	public TreeNode getChildAt(int childIndex) {
		return getChildren().get(childIndex);
	}
	
	@Override
	public int getChildCount() {
		return getChildren().size();
	}
	
	@Override
	public int getIndex(TreeNode node) {
		return getChildren().indexOf(node);
	}
	
	@Override
	public TreeNode getParent() {
		return getParentt();
	}
	
	@Override
	public boolean isLeaf() {
		return false;
	}
	@Override
	public String toString() {
		return getName();
	}
}
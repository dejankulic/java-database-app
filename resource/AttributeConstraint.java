package resource;

import javax.swing.tree.TreeNode;

import enums.ConstraintType;
import lombok.Data;
import lombok.ToString;

/**
 * @author StefanP
 * Jedini nasledjuje SNode jer nece imati decu i ovo predstavlja ogranicenja
 * */
public class AttributeConstraint extends SNode{
	private ConstraintType type;
	
	public AttributeConstraint(String name, SNode parent, ConstraintType type) {
		super(parent, name);
		this.type= type;
	}
	
	//Metode potrebne za tree
	@Override
	public boolean getAllowsChildren() {
		return false;
	}
		
	@Override
	public TreeNode getChildAt(int childIndex) {
		return null;
	}
		
	@Override
	public int getChildCount() {
		return 0;
	}
		
	@Override
	public int getIndex(TreeNode node) {
		return 0;
	}
		
	@Override
	public TreeNode getParent() {
		return getParentt();
	}
		
	@Override
	public boolean isLeaf() {
		return true;
	}

	public ConstraintType getType() {
		return type;
	}

	public void setType(ConstraintType type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}

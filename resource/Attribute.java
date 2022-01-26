package resource;

import javax.swing.tree.TreeNode;

import enums.AttributeType;
/**
 * @author StefanP
 * Ovo je kolona
 * */

public class Attribute extends CompositeNode{
	
	private AttributeType attributeType;
	private int lenth;
	
	public Attribute(String name, SNode parent) {
		super(name, parent);
	}
	
	public Attribute(String name, SNode parent, AttributeType attributeType, int length ) {
		super(name, parent);
		this.attributeType= attributeType;
		this.lenth= length;
	}
	
	@Override
	public void addChildren(SNode children) {
		if(children != null && children instanceof AttributeConstraint) {
			AttributeConstraint ac= (AttributeConstraint)children;
			this.getChildren().add(ac);
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

	public AttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}


	public int getLenth() {
		return lenth;
	}

	public void setLenth(int lenth) {
		this.lenth = lenth;
	}
}
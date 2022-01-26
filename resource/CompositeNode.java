package resource;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;
/**
 * @author StefanP
 * */

public abstract class CompositeNode extends SNode{
	
	private List<SNode> children;
	
	 public CompositeNode(String name, SNode parent) {
	        super(parent, name);
	        this.children = new ArrayList<>();
	    }
	 
	public abstract void addChildren(SNode children);

	public List<SNode> getChildren() {
		return children;
	}

	public void setChildren(List<SNode> children) {
		this.children = children;
	}
}
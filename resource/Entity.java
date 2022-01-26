package resource;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreeNode;

import observer.IListener;
import observer.IObserver;
import observer.Notification;

/**
 * @author StefanP
 * Ovo je tabela
 * */
public class Entity extends CompositeNode implements IObserver{
	private List<IListener> listeners;	//lista klasa koju slusaju
	private List<Entity> inRelationsWith;
	private List<String> primariKeys= new ArrayList<String>();
	private List<String> straniKeys= new ArrayList<String>();
	
	public Entity(String name, SNode parent) {
		super(name, parent);
		inRelationsWith= new ArrayList<Entity>();
		primariKeys= new ArrayList<String>();
		straniKeys= new ArrayList<String>();
	}

	@Override
	public void addChildren(SNode children) {
		if(children != null && children instanceof Attribute) {
			Attribute a= (Attribute)children;
			this.getChildren().add(a);
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
		return this.getName();
	}

	@Override
	public void addObserver(IListener listener) {
		if(listener == null)
			return;
		if(listeners == null)
			listeners= new ArrayList<IListener>();
		if(!listeners.contains(listener))
			listeners.add(listener);
		return;	
	}

	@Override
	public void removeObserver(IListener listener) {
		if(listener == null || listeners == null || !listeners.contains(listener))
			return;
		listeners.remove(listener);
	}

	@Override
	public void notifyObservers(Notification notification) {
		if(notification == null || listeners == null || listeners.isEmpty())
			return;
		
		for(IListener lis : listeners)
			lis.update(notification);	
	}

	@Override
	public int coutnObservers() {
		if(listeners == null || listeners.size() < 1)
			return 0;
		return listeners.size();
	}

	public List<Entity> getInRelationsWith() {
		return inRelationsWith;
	}

	public List<String> getPrimariKeys() {
		return primariKeys;
	}

	public List<String> getStraniKeys() {
		return straniKeys;
	}	
}
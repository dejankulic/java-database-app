package resource;

import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author StefanP
 * Ova klasa predstavlja najvecu apstrakciju nekog covra i generalno ovo ce biti
 * samo list u stablu
 * */
@Data
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class SNode implements MutableTreeNode{
	
	@ToString.Exclude		//ovo oznacava da pri pozivanju to string metode nece se ispisivati parent da ne bi usli u beskonacnu petlju
	private SNode parentt;	//svi sem root ce imati svog roditelja
	@ToString.Include
	private String name;
	
	@Override
	public TreeNode getChildAt(int childIndex) {return null;}
	@Override
	public int getChildCount() {return 0;}
	@Override
	public TreeNode getParent() {return null;}
	@Override
	public int getIndex(TreeNode node) {return 0;}
	@Override
	public boolean getAllowsChildren() {return false;}
	@Override
	public boolean isLeaf() {return false;}
	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration children() {return null;}
	@Override
	public void insert(MutableTreeNode child, int index) {return;}
	@Override
	public void remove(int index) {}
	@Override
	public void remove(MutableTreeNode node) {}
	@Override
	public void setUserObject(Object object) {}
	@Override
	public void removeFromParent() {}
	@Override
	public void setParent(MutableTreeNode newParent) {}
}
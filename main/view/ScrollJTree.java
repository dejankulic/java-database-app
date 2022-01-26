package main.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;

/**
 * @author StefanP
 * Podloga na koju kacimo JTree.
 * */
public class ScrollJTree extends JScrollPane{
	private static final long serialVersionUID = -4419100006496067561L;

	public ScrollJTree(JTreeView tree) {
		super(tree,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		postavka();
	}
	
	private void postavka() {
		setPreferredSize(new Dimension(150,150));
		setMinimumSize(new Dimension(100,100));
		setBackground(Color.WHITE);
	}
}

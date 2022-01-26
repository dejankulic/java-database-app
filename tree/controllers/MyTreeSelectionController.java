package tree.controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import main.view.JTreeView;
import main.view.MainFrame;
import resource.Entity;

/**
 * @author StefanP
 * Ovu klasu koristimo da slusa ako neko klikne na nesto u samom tree-u
 * */
public class MyTreeSelectionController implements TreeSelectionListener{

	@Override
	public void valueChanged(TreeSelectionEvent e) {
//		Object object= e.getPath().getLastPathComponent();
//		JTreeView v= (JTreeView)e.getSource();
//		v.setFo
//		check(object);
		check();
	}
	
	private void check() {
//		if(object instanceof Entity) {
//			Entity entity= (Entity)object;
//			System.out.println(entity);
//			MainFrame.getInstance().getCore().readDataFromTable(entity);
//			
//		}
		
		MainFrame.getInstance().getTreeView().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					if(MainFrame.getInstance().getTreeView().getLastSelectedPathComponent() instanceof Entity) {
						Entity ent = (Entity)MainFrame.getInstance().getTreeView().getLastSelectedPathComponent();
						MainFrame.getInstance().getCore().readDataFromTable(ent);
					}
					
				}
				
			}
		});
	}

}

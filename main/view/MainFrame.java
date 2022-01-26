package main.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import enums.NotificationCode;
import event.model.EventModel;
import event.model.InterfaceEventHandler;
import event.view.EventView;
import lombok.Data;
import main.model.AppCore;
import observer.IListener;
import observer.Notification;
import resource.Entity;
import resource.InformationResource;
import resource.SNode;
import table.model.TableModel;
import tree.model.TreeModel;

@SuppressWarnings("serial")
/**
 * @author StefanP
 * Glavni frame koji moze samo jednom da se instancira
 * */
@Data	//deo lombok biblioteke omogucava automatski generisanje svih get i set metoda
public class MainFrame extends JFrame implements IListener{
	
	private static MainFrame instance= null;	
	private InterfaceEventHandler eventHandler;
	private AppCore core;
	private JTreeView treeView;
	private ScrollJTree scrollForTree;
	private JSplitPane splitPane, desniSplitPane;
	private TreeModel modelTree;
	private JPanel gornjiPanel, donjiPanel;
	private TableView viewTa;
	
	
	private JTabbedPane tabbed, downTabbed;
	private List<TableView> allTableViews= new ArrayList<TableView>();
	private MainFrame() {}	//privatni konstruktor da bi ispostovali SingletonPatern
	
	public static MainFrame getInstance() {
		if(instance == null) {
			instance= new MainFrame();
			instance.ucitaj();
			instance.postavi();
		}
		return instance;
	}
	
	private void ucitaj() {
		eventHandler= new EventModel();
		((EventModel)eventHandler).addObserver(new EventView());
		
		
		gornjiPanel= new JPanel();
		gornjiPanel.setLayout(new BorderLayout());
		donjiPanel= new JPanel();
		donjiPanel.setLayout(new BorderLayout());
		tabbed= new JTabbedPane();
		downTabbed= new JTabbedPane();
		gornjiPanel.add(tabbed);
		donjiPanel.add(downTabbed);
		desniSplitPane= new JSplitPane(JSplitPane.VERTICAL_SPLIT,gornjiPanel,donjiPanel);
		desniSplitPane.setDividerLocation(0.5);
	}	
	
	//Postavlja stvari vezane za izgled samog MainFrame-a
	private void postavi() {
		Toolkit toolkit= Toolkit.getDefaultToolkit();
		Dimension dimension= toolkit.getScreenSize();
		int height= dimension.height, width= dimension.width;
		
		this.setSize(width / 2, (int) (height * 0.8));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		
		gornjiPanel.setPreferredSize(new Dimension(350,350));
		donjiPanel.setPreferredSize(new Dimension(100,100));
		this.addWindowListener(getWindowsListener(this));
		
		tabbed.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				JTabbedPane tb= (JTabbedPane)e.getSource();
				TableView tw= (TableView)tb.getSelectedComponent();
				
				putInFocus(tw.getName());
			}
			
		});
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.out.println("izzz maina");
			e.printStackTrace();
		}
	}
	
	

	
	private void makeTree(InformationResource i) {
		modelTree= new TreeModel(i);
		treeView= new JTreeView();
		treeView.setModel(modelTree);
		scrollForTree= new ScrollJTree(treeView);
		splitPane= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollForTree,desniSplitPane);	
		this.add(splitPane);
		SwingUtilities.updateComponentTreeUI(getTreeView());
	}
	
	
			
	private WindowListener getWindowsListener(MainFrame mf) {
		return new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {}
			
			@Override
			public void windowIconified(WindowEvent arg0) {}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				int provera= getEventHandler().generateErrorMessage(new Notification(NotificationCode.EXIT_MAINFRAME,null));
			
				if(provera != JOptionPane.YES_OPTION)
					mf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				else
					mf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			}
				
			@Override
			public void windowClosed(WindowEvent arg0) {}
			
			@Override
			public void windowActivated(WindowEvent arg0) {}
		};
	}
	
	/**
	 * Postavlja AppCore
	 * */
	public void setAppCore(AppCore appcore) {
		this.core= appcore;
		core.addObserver(this);
	}
	
	private TableView makeTableViews(TableModel model) {
		Entity e= (Entity)getTreeView().getLastSelectedPathComponent();
		if(!allTableViews.isEmpty()) {
			for(TableView t : allTableViews)
				if(((TableModel)t.getModel()).getName().equals(model.getName())) {
					t.setDefaultModel();
					putInFocus(t.getName());
					return t;
				}
		}
		TableView t= new TableView(model, model.getName());
		t.addRealitonTablesView();
		e.addObserver(t);
		allTableViews.add(t);
		
		tabbed.addTab(t.getName(), t);
		
		putInFocus(t.getName());
		
		return t;
	}
	
	public void putInFocus(String name) {
		downTabbed.removeAll();
		for(TableView t : allTableViews)
			if(t.getName().equals(name)) {
				if(t.getRelationsViews().size() != 0) {
					for(TableView tw : t.getRelationsViews()) {
						downTabbed.addTab(tw.getName(), tw);
						downTabbed.setSelectedComponent(tw);
					}
				}
				tabbed.setSelectedComponent(t);
			}
	}
	
//	public void putInFocusDown(TableView tv) {
//		downTabbed.removeAll();		//brisem sve iz donjeg
//		if(tv.getRelationsViews().size() != 0) {
//			TableView t= (TableView)tv.getRelationsViews().get(0);
//			downTabbed.setSelectedComponent(t);
//		}
//	}

	@Override
	public void update(Notification notification) {
		if(notification.getCode() == NotificationCode.RESOURCE_LOADED) {
			makeTree((InformationResource)notification.getObject());
		}else if(notification.getCode() == NotificationCode.RELOAD_TABLE){
			makeTableViews((TableModel) notification.getObject());
		}else if(notification.getCode() == NotificationCode.FILTER_TABLE) {
			for(TableView v : allTableViews) {
				if(v.getName().equals(((TableModel) notification.getObject()).getName()));
					v.setTemporaryTable((TableModel) notification.getObject());
			}
		}else if(notification.getCode() == NotificationCode.SORT_TABLE) {
			for(TableView v : allTableViews) {
				if(v.getName().equals(((TableModel) notification.getObject()).getName()));
					v.setTemporaryTable((TableModel) notification.getObject());
			}
		}else if(notification.getCode() == NotificationCode.RELATION_TABLE) {
			TableView v= new TableView((TableModel) notification.getObject(), ((TableModel) notification.getObject()).getName());
			downTabbed.addTab(v.getName(), v);
		}else if(notification.getCode() == NotificationCode.COUNT_TABLE) {
			TableView v= new TableView((TableModel) notification.getObject(), ((TableModel) notification.getObject()).getName());
			downTabbed.removeAll();
			downTabbed.addTab(v.getName(), v);
		}else if(notification.getCode() == NotificationCode.AVERAGE_TABLE) {
			TableView v= new TableView((TableModel) notification.getObject(), ((TableModel) notification.getObject()).getName());
			downTabbed.removeAll();
			downTabbed.addTab(v.getName(), v);
		}
	}
}
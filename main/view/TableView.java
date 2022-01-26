package main.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import enums.AttributeType;
import enums.ConstraintType;
import observer.IListener;
import observer.Notification;
import resource.Attribute;
import resource.AttributeConstraint;
import resource.Entity;
import resource.InformationResource;
import resource.SNode;
import table.model.TableModel;

/**
 * @author StefanP
 * Ovo ce biti "podmetac" za jtable koji cemo da ubacujemo u jtabbedpane.
 * */


public class TableView extends JPanel implements IListener{
	private static final long serialVersionUID = -2735083158466645018L;
	
	private String name;
	private TableModel model;
	private JTable tableView;
	private JScrollPane scroll;
	private JButton filter, sort, relation, countButt, averageButt, searchButt;
	private JPanel donji;
	
	private List<TableView> relationsViews= new ArrayList<TableView>();	
	
	public TableView(TableModel model, String name) {
		this.model= model;
		this.name= name;
		this.setLayout(new BorderLayout());
		ucitaj();
		postavka();
	}
	
	private void ucitaj() {
		tableView= new JTable(model);
		scroll= new  JScrollPane(tableView);
		
		filter= new JButton("Filter");
		sort= new JButton("Sort");
		relation= new JButton("Relation");
		countButt= new JButton("Count");
		averageButt= new JButton("Average");
		searchButt= new JButton("Search");
		donji= new JPanel();
		donji.add(filter);
		donji.add(sort);
		donji.add(relation);
		donji.add(countButt);
		donji.add(averageButt);
		donji.add(searchButt);
	}
	
	private void postavka() {
		tableView.setPreferredScrollableViewportSize(new Dimension(400,400));
		tableView.setFillsViewportHeight(true);
		tableView.setColumnSelectionAllowed(true);
		this.add(donji,BorderLayout.SOUTH);
		this.add(scroll);
		
		
		filter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TableView tw= (TableView)((JTabbedPane)MainFrame.getInstance().getTabbed()).getSelectedComponent();
				InformationResource ir= (InformationResource)MainFrame.getInstance().getTreeView().getModel().getRoot();
				ArrayList<String> str= new ArrayList<String>();
				for(SNode node : ir.getChildren()) {
					Entity entity= (Entity)node;
					if(entity.getName().equals(tw.getName()))
						for(SNode s : entity.getChildren())
							str.add(s.getName());
				}
				SmallFrame smallFrame= new SmallFrame(str,tw.getName());
			}
		});
		
		sort.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TableView tw= (TableView)((JTabbedPane)MainFrame.getInstance().getTabbed()).getSelectedComponent();
				InformationResource ir= (InformationResource)MainFrame.getInstance().getTreeView().getModel().getRoot();
				ArrayList<String> str= new ArrayList<String>();
				for(SNode node : ir.getChildren()) {
					Entity entity= (Entity)node;
					if(entity.getName().equals(tw.getName()))
						for(SNode s : entity.getChildren())
							str.add(s.getName());
				}
				SortFrame sf= new SortFrame(tw.getName(),str);
			}
		});
		
		relation.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> fkColName= new ArrayList<>();	//lista u kojoj skladistimo imena od fk kolona.
				ArrayList<String> fkColValues= new ArrayList<String>();
				
				String name= ((TableView)((JTabbedPane)MainFrame.getInstance().getTabbed()).getSelectedComponent()).getName();
				InformationResource ir= (InformationResource)MainFrame.getInstance().getTreeView().getModel().getRoot();
				for(SNode node : ir.getChildren()) {
					Entity entity= (Entity)node;
					if(name.equals(entity.getName()))
						for(SNode nod1 : entity.getChildren()) {
							Attribute a= (Attribute)nod1;
							for(SNode n : a.getChildren()) {
								AttributeConstraint ac= (AttributeConstraint)n;
								if(ac.getType() == ConstraintType.FOREIGN_KEY)
									fkColName.add(a.getName());
							}
						}
				}
				
				int row= tableView.getSelectedRow();
				ArrayList<String> koloneRedoslednomUTabeli= new ArrayList<String>();
				if(row >= 0) {
				for(int i=0; i < tableView.getColumnCount() ;i++) {
					String ime= tableView.getColumnName(i);
					for(String n : fkColName)
						if(n.equals(ime)) {
							if(tableView.getValueAt(row, i) == null) {
								koloneRedoslednomUTabeli.add(fkColName.get(i));
								fkColValues.add("nula:/");
							}
							else	{
								fkColValues.add(tableView.getValueAt(row, i).toString());
								koloneRedoslednomUTabeli.add(ime);
							}
					
						}
				}
				
				
				if(koloneRedoslednomUTabeli.size() != 0) {
					for(int i=0 ; i < koloneRedoslednomUTabeli.size() ;i++) {
						if(!fkColValues.get(i).equals("nula:/")) {
							String ime= koloneRedoslednomUTabeli.get(i);
							
							
							for(SNode node : ir.getChildren()) {
								Entity ent= (Entity)node;
								for(String s : ent.getPrimariKeys()) {
									if(ime.equalsIgnoreCase("manager_id") && s.equalsIgnoreCase("employee_id") && ent.getName().equalsIgnoreCase("EMPLOYEES")) {
											
										MainFrame.getInstance().getCore().relationTable(s, fkColValues.get(i), ent.getName());	
										
									}else if(ime.equalsIgnoreCase("employee_id") && ent.getName().equalsIgnoreCase("JOB_HISTORY")) {
										continue;
									}else if(s.equalsIgnoreCase(ime)) {
										MainFrame.getInstance().getCore().relationTable(s, fkColValues.get(i), ent.getName());
									}
								}}
						}
							
						
					}
				}
				
				
			
//				MainFrame.getInstance().getCore().relationTable(name, query);
				}}}
		);
		countButt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TableView tw= (TableView)((JTabbedPane)MainFrame.getInstance().getTabbed()).getSelectedComponent();
				InformationResource ir= (InformationResource)MainFrame.getInstance().getTreeView().getModel().getRoot();
				ArrayList<String> str= new ArrayList<String>();
				for(SNode node : ir.getChildren()) {
					Entity entity= (Entity)node;
					if(entity.getName().equals(tw.getName()))
						for(SNode s : entity.getChildren())
							str.add(s.getName());
				}
				new CountDialog(tw.getName(), str);
			}
		});
		averageButt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TableView tw= (TableView)((JTabbedPane)MainFrame.getInstance().getTabbed()).getSelectedComponent();
				InformationResource ir= (InformationResource)MainFrame.getInstance().getTreeView().getModel().getRoot();
				ArrayList<String> allColumns= new ArrayList<String>();
				ArrayList<String> numbValues= new ArrayList<String>();
				for(SNode node : ir.getChildren()) {
					Entity entity= (Entity)node;
					if(entity.getName().equals(tw.getName()))
						for(SNode s : entity.getChildren()) {
							allColumns.add(s.getName());
							Attribute att= (Attribute)s;
							System.out.println(att.getName() + " " + att.getAttributeType());
							if(att.getAttributeType() == AttributeType.DECIMAL || att.getAttributeType() == AttributeType.FLOAT || att.getAttributeType() == AttributeType.INT || att.getAttributeType() == AttributeType.REAL || att.getAttributeType() == AttributeType.NUMERIC)
								numbValues.add(s.getName());
						}
				}
				new AverageDialog(tw.getName(), allColumns, numbValues);
			}
		});
		
		searchButt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TableView tw= (TableView)((JTabbedPane)MainFrame.getInstance().getTabbed()).getSelectedComponent();
				InformationResource ir= (InformationResource)MainFrame.getInstance().getTreeView().getModel().getRoot();
				ArrayList<String> str= new ArrayList<String>();
				for(SNode node : ir.getChildren()) {
					Entity entity= (Entity)node;
					if(entity.getName().equals(tw.getName()))
						for(SNode s : entity.getChildren())
							str.add(s.getName());
				}
				new SearchDialog(tw.getName(), str);
			}
		});
	}
	
	public void setTemporaryTable(TableModel model) {
		tableView.setModel(model);
	}
	
	public void setDefaultModel() {
		tableView.setModel(model);
	}

	public TableModel getModel() {
		return model;
	}

	public String getName() {
		return name;
	}

	public void setModel(TableModel model) {
		this.model = model;
	}

	public void setName(String name) {
		this.name = name;

	}
	
	public void addRealitonTablesView() {
		if(model.getTableModels().size() != 0)
			for(TableModel m : model.getTableModels()) {
				TableView t= new TableView(m, m.getName());
				relationsViews.add(t);
			}
	}
	
	public List<TableView> getRelationsViews() {
		return relationsViews;
	}

	//povezan za Entity
	@Override
	public void update(Notification notification) {
		System.out.println("updateee" + name);
	}
}
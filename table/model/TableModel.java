package table.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import enums.AttributeType;
import lombok.Data;
import main.view.MainFrame;
import resource.Attribute;
import resource.Entity;
import resource.InformationResource;
import resource.Row;
import resource.SNode;

/**
 * @author StefanP
 * DefaultTreeModel koristi vektore zato redove pretvaramo u njih
 * */
public @Data class TableModel extends DefaultTableModel{
	private static final long serialVersionUID = 110454568736373801L;
	private String name;
	private List<Row> rows;
	private List<TableModel> tableModels= new ArrayList<TableModel>();
	public TableModel(String name) {
		this.name= name;
		
		addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				
				
				System.out.println(e.getColumn());
				System.out.println(e.getFirstRow());
				if(e.getColumn() != -1 && e.getFirstRow() != -1) {
					System.out.println(((TableModel)e.getSource()).getValueAt(e.getFirstRow(), e.getColumn()));
					System.out.println(((TableModel)e.getSource()).getColumnName(e.getColumn()));
					
					InformationResource ir= (InformationResource)MainFrame.getInstance().getTreeView().getModel().getRoot();
				
					for(SNode s : ir.getChildren()) {
						Entity ent= (Entity)s;
						for(SNode node : ent.getChildren()) {
							Attribute a= (Attribute)node;
							if(a.getName().equals(((TableModel)e.getSource()).getColumnName(e.getColumn())))
								System.out.println(a.getAttributeType());
							
						}
					}
					
					System.out.println(e.getLastRow() + "last");
					System.out.println(		rows.get(e.getFirstRow()));
				
					
				}
				
				
			}
		});
	}
	
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateModel(){
		 	
	        int columnCount = rows.get(0).getFields().keySet().size();

	        Vector columnVector = DefaultTableModel.convertToVector(rows.get(0).getFields().keySet().toArray());
	        Vector dataVector = new Vector(columnCount);

	        for (int i=0; i<rows.size(); i++){
	            dataVector.add(DefaultTableModel.convertToVector(rows.get(i).getFields().values().toArray()));
	        }
	        setDataVector(dataVector, columnVector);	//ovo automatski okida notify i osvezava samu tabelu	
	 }
	 
	 //Prima redove i pretvara ih u vektore koj
	 public void setRows(List<Row> rows) {
	        this.rows = rows;
	        updateModel();
	 }
	 
	 
}

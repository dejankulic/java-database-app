package main.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class SortFrame extends JDialog{
	private static final long serialVersionUID = -2256820244316450909L;
	
	private JPanel panel;
	private JButton addB, sortB;
	
	private ArrayList<String> columns= new ArrayList<String>();
	private String tableName;
	
	private JComboBox<String> kolone= new JComboBox<>();
	private JComboBox<String> poredjenje= new JComboBox<String>();
	
	private ArrayList<String> izabraneKolone= new ArrayList<String>(); 
	private ArrayList<String> izabranaPoredjenja= new ArrayList<String>();
	
	public SortFrame(String tableName, ArrayList<String> columns) {
		this.columns= columns;
		this.tableName= tableName;
		ucitaj();
		postavi();
	}
	
	private void ucitaj() {
		panel= new JPanel();
		addB= new JButton("add");
		sortB= new JButton("Go sort");
	}
		
	private void postavi() {
		this.setLayout(new BorderLayout());
		setTitle("Sort");
		setSize(400,400);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setLocationRelativeTo(null);
		
		addItemsToCombo();
		dugmad();
		this.add(panel);
		panel.add(kolone);
		panel.add(poredjenje);
		panel.add(addB);
		panel.add(sortB);
		
		this.setVisible(true);
		
	}
	
	private void dugmad() {
		addB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				izabraneKolone.add((String)kolone.getSelectedItem());
				if(((String)poredjenje.getSelectedItem()).equalsIgnoreCase("RASTUCE"))
					izabranaPoredjenja.add("asc");
				else
					izabranaPoredjenja.add("desc");
					
				kolone.removeItem((String)kolone.getSelectedItem());
				
				if(kolone.getItemCount() == 0) {
					MainFrame.getInstance().getCore().sortTable(tableName, izabraneKolone, izabranaPoredjenja);
					dispose();
				}					
			}
		});
		
		sortB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(izabranaPoredjenja.size() != 0) {
					MainFrame.getInstance().getCore().sortTable(tableName, izabraneKolone, izabranaPoredjenja);;
				}else
					System.out.println("POTREBNO JE DA SE DODA BAREM JEDNA KOLONA POMOCU ADD DA BI MOGLO DA SE SORTIRA INACE CE SVAKI PUT SAMO DA ISKLJUCI PROZOR");				
				dispose();
			}
		});
	}
	
	private void addItemsToCombo() {
		for(String s : columns)
			kolone.addItem(s);
		
		poredjenje.addItem("RASTUCE");
		poredjenje.addItem("OPADAJUCE");
	}
	
//	private String makeQuery() {
//		String query= "SELECT * FROM " + tableName + " order by ";
//		String orderDeo= "";
//		for(int i=0; i < izabraneKolone.size() ;i++) {
//			orderDeo+= izabraneKolone.get(i) + " " + izabranaPoredjenja.get(i);
//			if(i != izabranaPoredjenja.size() - 1)
//				orderDeo+= ", ";
//		}			
//		return query + orderDeo;
//	}
}

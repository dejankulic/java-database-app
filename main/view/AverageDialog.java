package main.view;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AverageDialog extends JDialog{
	private static final long serialVersionUID = 7324646783993674189L;
	
	private JPanel panel;
	private JButton daljeButt;
	private String tableName;
	private ArrayList<String> columns= new ArrayList<String>();
	private JComboBox<String> kolone= new JComboBox<>();
	private ArrayList<String> numericColumns= new ArrayList<String>();
	
	//pamti sta kako
	private String countValue;
	public AverageDialog(String tableName,ArrayList<String> columns, ArrayList<String> numericColumns) {
		this.tableName= tableName;
		this.columns= columns;
		this.numericColumns= numericColumns;
		ucitaj();
		postavi();
	}
	
	private void ucitaj() {
		panel= new JPanel();
		daljeButt= new JButton("Nastavi");
		
	}
	
	private void postavi() {
		this.setLayout(new BorderLayout());
		setTitle("Count");
		setSize(300,200);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setLocationRelativeTo(null);
		
		this.add(panel);
		panel.add(new JLabel("Oznacite po kojoj koloni zelite da izvrsite count:"));
		
		addItemsToCombo();
		panel.add(kolone);
		panel.add(daljeButt);
		dugmad();
		
		this.setVisible(true);
	}
	
		
	private void dugmad() {
		daljeButt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				countValue= (String)kolone.getSelectedItem();			
				columns.remove(countValue);
				new MiniDialogForCountAverage(countValue, columns, tableName,false);
				dispose();
			}
		});	
	}
	
	private void addItemsToCombo() {
		for(String s : numericColumns)
			kolone.addItem(s);
	}

	public ArrayList<String> getColumns() {
		return columns;
	}	
}

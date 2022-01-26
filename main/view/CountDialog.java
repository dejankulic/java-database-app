package main.view;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CountDialog extends JDialog{
	private static final long serialVersionUID = -931874349274164807L;
	
	private JPanel panel;
	private JButton daljeButt;
	private String tableName;
	private ArrayList<String> columns= new ArrayList<String>();
	private JComboBox<String> kolone= new JComboBox<>();
	
	//pamti sta kako
	private String countValue;
	public CountDialog(String tableName,ArrayList<String> columns) {
		this.tableName= tableName;
		this.columns= columns;
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
				System.out.println(e.getSource());
				countValue= (String)kolone.getSelectedItem();			
				columns.remove(countValue);
				new MiniDialogForCountAverage(countValue, columns, tableName,true);
				dispose();
			}
		});	
	}
	
	private void addItemsToCombo() {
		for(String s : columns)
			kolone.addItem(s);
	}

	public ArrayList<String> getColumns() {
		return columns;
	}	
}
package main.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchDialog extends JDialog{
	private static final long serialVersionUID = 4008025611928091988L;
	private JPanel panel;
	private JButton daljeButt, endButt;
	private String tableName;
	private ArrayList<String> columns= new ArrayList<String>();
	private JTextField textField= new JTextField();
	private ArrayList<String> columnsForSend= new ArrayList<String>();
	private ArrayList<String> logic= new ArrayList<String>();
	private ArrayList<String> znakovi= new ArrayList<>();
	
	public SearchDialog(String tableName, ArrayList<String> columns) {
		this.tableName= tableName;
		this.columns= columns;
		ucitaj();
		postavi();
		
	}
	
	private void ucitaj() {
		panel= new  JPanel();
		daljeButt= new JButton("Dalje");
		endButt= new JButton("Finish");
	}
	
	private void postavi() {
		setTitle("Count");
		setSize(300,200);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setLocationRelativeTo(null);
		textField.setPreferredSize(new Dimension(100,100));
		
		this.add(panel);
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel("opis koji treba da se doda!"),BorderLayout.NORTH);
		panel.add(textField,BorderLayout.CENTER);
		JPanel p= new JPanel();
		p.add(daljeButt);
		p.add(endButt);
		panel.add(p,BorderLayout.SOUTH);
		dugmad();
		this.setVisible(true);
	}
	
	private void dugmad() {
		daljeButt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String s= textField.getText();
				int n= columnsForSend.size()-1;
				if(columns.contains(s))
					columnsForSend.add(s);
				else if(s.equalsIgnoreCase("AND") || s.equalsIgnoreCase("OR"))
					logic.add(s);
				else
					znakovi.add(s);
				
				textField.setText("");
				System.out.println(columnsForSend);
				System.out.println(logic);
				System.out.println(znakovi);
			}
		});
	}
}

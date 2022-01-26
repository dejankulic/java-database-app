package main.view;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author StefanP
 */
public @Data @EqualsAndHashCode(callSuper = false) class SmallFrame extends JDialog{
	private static final long serialVersionUID = -1264624193254962516L;
	
	//component
	private JPanel panel;
	private JButton button;
	private ArrayList<String> columns= new ArrayList<String>();
	private ArrayList<JCheckBox> boxes;
	private String tableName;
	
	public SmallFrame(ArrayList<String> columns, String name) {
		this.columns= columns;
		this.tableName= name;
		boxes= new ArrayList<>();
		ucitavanje();
		postavka();
		}
	
	private void ucitavanje() {
		panel= new JPanel();
		button= new JButton("Go");
	}
	
	
	private void postavka() {
		//ZA SAM JFRAME
		this.setLayout(new BorderLayout());
		setTitle("Filter");
		setSize(300,200);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setLocationRelativeTo(null);
		
		
		//ZA JPANEL
		panel.add(button);
		
		
		for(int i= 0 ; i < columns.size() ;i++) {
			JCheckBox b= new JCheckBox(getColumns().get(i));
			b.setName(getColumns().get(i));
			boxes.add(b);
			panel.add(b);
		}
		
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> selected= new ArrayList<String>();
				for(JCheckBox j : boxes)
					if(j.isSelected())
						selected.add(j.getName());
				if(selected.size() != 0)		
					MainFrame.getInstance().getCore().filterTable(tableName, selected);
				dispose();
			}
		});
		
		
		
		add(panel);
			
		setVisible(true);
	}

}

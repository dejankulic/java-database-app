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

import enums.NotificationCode;
import observer.Notification;

public class MiniDialogForCountAverage extends JDialog{
	private static final long serialVersionUID = -708162929456025053L;
	private JPanel panel;
	private JButton finishButt;
	private String tableName, keyValue;
	private ArrayList<String> columns= new ArrayList<String>();
	private ArrayList<JCheckBox> boxes= new ArrayList<JCheckBox>();
	private boolean forCount;	//ako je false znaci da koristim ovaj dialog za average
	public MiniDialogForCountAverage(String keyValue, ArrayList<String> kolone, String tableName,boolean forCount) {
		this.tableName= tableName;
		this.columns= kolone;
		this.keyValue= keyValue;
		this.forCount= forCount;
		ucitaj();
		postavi();
	}
	private void ucitaj() {
		panel= new JPanel();
		finishButt= new JButton("Finis");
		
	}
	
	private void postavi() {
		this.setLayout(new BorderLayout());
		setTitle("Count");
		setSize(300,200);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setLocationRelativeTo(null);
		
		this.add(panel);
		panel.add(new JLabel("Oznacite po cemu zelite da grupisete(min 1 stavka):"));
		loadCheckBox();
		panel.add(finishButt);
		dugme();
		this.setVisible(true);
	}
	
	private void dugme() {
		finishButt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> selected= new ArrayList<String>();
				for(JCheckBox j : boxes)
					if(j.isSelected())
						selected.add(j.getName());
				if(selected.size() > 0) {
					System.out.println("=============" + forCount);
					if(forCount)
						MainFrame.getInstance().getCore().countTable(keyValue, tableName, selected);
					else
						MainFrame.getInstance().getCore().averageTable(keyValue, tableName, selected);
					dispose();
				}
				else
					MainFrame.getInstance().getEventHandler().generateErrorMessage(new Notification(NotificationCode.MUST_SELECT, null));
			}
		});
	}
	

	private void loadCheckBox() {
		for(int i= 0 ; i < columns.size() ;i++) {
			JCheckBox b= new JCheckBox(getColumns().get(i));
			b.setName(getColumns().get(i));
			boxes.add(b);
			panel.add(boxes.get(i));
		}
	}
	public ArrayList<String> getColumns() {
		return columns;
	}
	
	

}

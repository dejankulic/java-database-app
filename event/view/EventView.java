package event.view;

import javax.swing.JOptionPane;

import enums.NotificationCode;
import event.model.EventModel;
import observer.IListener;
import observer.Notification;

/**
 * @author StefanP
 * */
public class EventView implements IListener{

	@Override
	public void update(Notification notification) {
		if(notification.getCode() == NotificationCode.EXIT_MAINFRAME)
			generateExitMessage((EventModel)notification.getObject());
		if(notification.getCode() == NotificationCode.MUST_SELECT)
			generateMustSelectMess((EventModel)notification.getObject());
			
	}
	
	private void generateExitMessage(EventModel model) {
		model.setAns(JOptionPane.showConfirmDialog(null, "Exit app?", "" , JOptionPane.YES_NO_OPTION));
	}
	
	private void generateMustSelectMess(EventModel model) {
		JOptionPane.showMessageDialog(null, "Must select one", "" , JOptionPane.ERROR_MESSAGE);		
	}
}

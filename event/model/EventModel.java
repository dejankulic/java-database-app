package event.model;

import lombok.Data;
import observer.IObserverImplementation;
import observer.Notification;
/**
 * @author StefanP
 * */
@Data	//izgenerisace mi set i get ans sto mi treba 
public class EventModel extends IObserverImplementation implements InterfaceEventHandler{
	private int ans;	//odgovor tj da li je korisnik kliknu yes/no/cancle
	
	@Override
	public int generateErrorMessage(Notification notification) {
		this.notifyObservers(new Notification(notification.getCode(), this));
		return ans;
	}
}

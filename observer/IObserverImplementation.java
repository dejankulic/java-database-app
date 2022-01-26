package observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author StefanP
 * VAZNO: Oni koji nasledjuju ovu klasu ako postoji potreba da naslede jos neku klasu sem ovo
 * 		  moguce je da jednosavno umesto nasledjivanja ove klase implementiraju interfejs IObserver
 * 		  i redefinisu ove tri metode
 * */
public class IObserverImplementation implements IObserver{

	private List<IListener> listeners;	//lista klasa koju slusaju
	
	@Override
	public void addObserver(IListener listener) {
		if(listener == null)
			return;
		if(listeners == null)
			listeners= new ArrayList<IListener>();
		if(!listeners.contains(listener))
			listeners.add(listener);
		return;
	}

	@Override
	public void removeObserver(IListener listener) {
		if(listener == null || listeners == null || !listeners.contains(listener))
			return;
		listeners.remove(listener);
	}

	@Override
	public void notifyObservers(Notification notification) {
		if(notification == null || listeners == null || listeners.isEmpty())
			return;
		
		for(IListener lis : listeners)
			lis.update(notification);
	
	}

	@Override
	public int coutnObservers() {
		if(listeners == null || listeners.size() < 1)
			return 0;
		return listeners.size();
	}
	

}

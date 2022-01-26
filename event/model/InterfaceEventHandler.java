package event.model;

import observer.Notification;

/**
 * @author StefanP
 * Imacemo razne poruke i lakse je da ih sve menjamo i dodajemo na jednom mestu
 * */
public interface InterfaceEventHandler {
	public abstract int generateErrorMessage(Notification notification);
}

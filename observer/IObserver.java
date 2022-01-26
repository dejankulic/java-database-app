package observer;

/**
 * @author StefanP
 * Posmatramo kao Publisher klasu koju ce da slusaju oni koji su zakaceeni na nju.
 * */
public interface IObserver {
	void addObserver(IListener listener);
	void removeObserver(IListener listener);
	void notifyObservers(Notification notification);
	int coutnObservers();
}

package settings;

/**
 * @author StefanP
 * */
public interface SettingsInterface {
	 Object getParameter(String parameter);
	 void addParameter(String parameter, Object value);
}

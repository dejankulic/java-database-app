package settings;

import java.util.HashMap;
import java.util.Map;

public class SettingsImplementation implements SettingsInterface{
	
	private Map<String, Object> parameters= new HashMap<>();
	
	@Override
	public Object getParameter(String parameter) {
		return parameters.get(parameter);
	}

	@Override
	public void addParameter(String parameter, Object value) {
		parameters.put(parameter, value);	//pravi hashMap gde je paramtar kljuc do odjekta
	}
}

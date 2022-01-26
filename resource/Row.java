package resource;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
/**
 * @author StefanP
 * */
@Data
public class Row {
	private String name;
	private Map<String, Object> fields;
	
	public Row() {
		fields= new HashMap<String, Object>();
	}
	
	public void addField(String fieldName, Object value) {
        this.fields.put(fieldName, value);
    }

    public void removeField(String fieldName) {
        this.fields.remove(fieldName);
    }

}

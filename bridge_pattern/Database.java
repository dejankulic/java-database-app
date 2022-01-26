package bridge_pattern;

import java.util.ArrayList;
import java.util.List;

import resource.Row;
import resource.SNode;

/**
 * @author StefanP
 * Ovaj interface cemo da koristimo za pristup komponentama koje pokriva
 * */
public interface Database {
	SNode loadResource();	//za ucitavanje resursa
	List<Row> readDataFromTable(String tableName);	//cita stvari iz tabele i vraca nam listu redova
	
	List<Row> filterTable(String tableName, ArrayList<String> columns);
	
	List<Row> sortTable(ArrayList<String> izabraneKolone, ArrayList<String> izabranaPoredjenja,String nameTable);
	
	List<Row> relationTable(String fkName, String fkValue,String tableName);

	List<Row> countTable(String countValue, String tableName, ArrayList<String> selectedArgs);
	
	List<Row> averageTable(String countValue, String tableName, ArrayList<String> selectedArgs);

}

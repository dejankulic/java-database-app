package bridge_pattern;

import java.util.ArrayList;
import java.util.List;

import resource.Row;
import resource.SNode;

/**
 * @author StefanP
 * Treca komponenta Bridge sablona.
 * Interfejs koji je implementor.
 * */
public interface Repository {
	SNode getShema();	//kako se na server skladisti unutrasnja struktura neke baze
	List<Row> get(String from);	//za izvlacenje redova iz tabele
	List<Row> filterTable(String tableName, ArrayList<String> columns);
	List<Row> sortTable(ArrayList<String> izabraneKolone, ArrayList<String> izabranaPoredjenja,String tableName);
	List<Row> relationTable(String fkName, String fkValue, String tableName);
	List<Row> countTable(String countValue, String tableName, ArrayList<String> selectedArgs);
	List<Row> averageTable(String countValue, String tableName, ArrayList<String> selectedArgs);

}

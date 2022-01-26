package bridge_pattern;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import resource.Row;
import resource.SNode;

/**
 * @author StefanP
 * Druga komponenta naseg Bridge sablona.
 * Je objekat koji nastaje  i on se oslanja na  Repository i jedino sto radi kad baza zeli da procita 
 * nesto iz tabele jeste da prosledjuje Repositoriju sta treba da se konkretno desi.
 * */
@Data
@AllArgsConstructor
public class DatabaseImplementation implements Database{
	
	private Repository repository;
	
	@Override
	public SNode loadResource() {
		return repository.getShema();	//ovo mozemo da iskorisimo da bi dobili root za jtree
	}

	@Override
	public List<Row> readDataFromTable(String tableName) {
		return repository.get(tableName);
	}

	@Override
	public List<Row> filterTable(String tableName,ArrayList<String> columns) {
		return repository.filterTable(tableName, columns);
	}

	@Override
	public List<Row> sortTable(ArrayList<String> izabraneKolone, ArrayList<String> izabranaPoredjenja,String tableName) {
		return repository.sortTable(izabraneKolone,izabranaPoredjenja,tableName);
	}

	@Override
	public List<Row> relationTable(String fkName,String fkValue,String tableName) {
		return repository.relationTable(fkName,fkValue,tableName);
	}

	@Override
	public List<Row> countTable(String countValue, String tableName, ArrayList<String> selectedArgs) {
		return repository.countTable(countValue, tableName, selectedArgs);
	}

	@Override
	public List<Row> averageTable(String countValue, String tableName, ArrayList<String> selectedArgs) {
		return repository.averageTable(countValue, tableName, selectedArgs);
	}

}

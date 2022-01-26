package main.model;

import java.util.ArrayList;
import java.util.List;

import bridge_pattern.Database;
import bridge_pattern.DatabaseImplementation;
import bridge_pattern.MSSQLrepository;
import enums.NotificationCode;
import lombok.Data;
import observer.IObserverImplementation;
import observer.Notification;
import resource.Entity;
import resource.InformationResource;
import settings.SettingsImplementation;
import settings.SettingsInterface;
import table.model.TableModel;
import utils.Constants;

/**
 *@author StefanP
 *Model za MainFrame 
 *Da bi ispostovali sam MVC neophodno je da povezemo core sa svojim view-om preko observera
 * */
@Data
public class AppCore extends IObserverImplementation{
	private List<TableModel> allTables;
	private Database database;
	private SettingsInterface settings;
	
	public AppCore() {
		allTables= new ArrayList<TableModel>();
		settings= initSettings();
		database= new DatabaseImplementation(new MSSQLrepository(settings));
	}
	
	private SettingsInterface initSettings() {
		SettingsInterface settImp = new SettingsImplementation();
		settImp.addParameter("mssql_ip", Constants.MSSQL_IP);
		settImp.addParameter("mssql_database", Constants.MSSQL_DATABASE);
		settImp.addParameter("mssql_username", Constants.MSSQL_USERNAME);
		settImp.addParameter("mssql_password", Constants.MSSQL_PASSWORD);
	    return settImp;
	}
	
	public void loadResource() {
		InformationResource i= (InformationResource)database.loadResource();
		this.notifyObservers(new Notification(NotificationCode.RESOURCE_LOADED, i));
	}
	
	private TableModel makeTable(String name) {
		if(!allTables.isEmpty()) {
			for(TableModel model : allTables)
				if(model.getName().equals(name))
					return model;
		}
		
		TableModel m= new TableModel(name);
		allTables.add(m);
		
		return m;	
	}

	public void readDataFromTable(Entity entity) {
		TableModel model= makeTable(entity.getName());
		model.setRows(database.readDataFromTable(entity.getName()));
		//ucitavam dodatne tabele sa kojima je u relaciji
		if(entity.getInRelationsWith().size() != 0)
			for(Entity e : entity.getInRelationsWith()) {
				TableModel m= new TableModel(e.getName());
				m.setRows(database.readDataFromTable(e.getName()));
				model.getTableModels().add(m);
			}
				
		this.notifyObservers(new Notification(NotificationCode.RELOAD_TABLE, model));
	}
	
	public void filterTable(String tableName, ArrayList<String> selected) {
		TableModel model= new TableModel(tableName);
		model.setRows(database.filterTable(tableName, selected));
		this.notifyObservers(new Notification(NotificationCode.FILTER_TABLE, model));	
	}
	
	public void sortTable(String tableName, ArrayList<String> izabraneKolone, ArrayList<String>izabranaPoredjenja) {
		TableModel model= new TableModel(tableName);
		model.setRows(database.sortTable(izabraneKolone, izabranaPoredjenja, tableName));
		this.notifyObservers(new Notification(NotificationCode.SORT_TABLE, model));	
	}
	
	public void relationTable(String fkName, String fkValue,String downTableName) {		
		TableModel model= new TableModel(downTableName);
		model.setRows(database.relationTable(fkName, fkValue, downTableName));
		this.notifyObservers(new Notification(NotificationCode.RELATION_TABLE, model));	
	}
	
	public void countTable(String countValue, String tableName, ArrayList<String> selectedArgs) {
		TableModel model= new TableModel(tableName);
		model.setRows(database.countTable(countValue, tableName, selectedArgs));
		this.notifyObservers(new Notification(NotificationCode.COUNT_TABLE, model));	
		
	}
	
	public void averageTable(String countValue, String tableName, ArrayList<String> selectedArgs) {
		TableModel model= new TableModel(tableName);
		model.setRows(database.averageTable(countValue, tableName, selectedArgs));
		this.notifyObservers(new Notification(NotificationCode.AVERAGE_TABLE, model));	
		
	}
}
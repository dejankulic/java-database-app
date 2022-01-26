package bridge_pattern;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import enums.AttributeType;
import enums.ConstraintType;
import resource.Attribute;
import resource.AttributeConstraint;
import resource.Entity;
import resource.InformationResource;
import resource.Row;
import resource.SNode;
import settings.SettingsInterface;

/**
 * @author StefanP
 * Poslednja komponenta Bridge sablona. 
 * Sve ostale nisu znale sa kojom bazom rade, ova ce da zna da radi sa MSSQL-om.
 * Cela poenta BridgeSablona je ovo da mi sad ako zelimo da radimo sa Oracle bazom
 * samo cemo da napravimo klasu ORACLErepositoty i nakacimo na Repository i sve pre toga
 * ne treba da se menja!!!
 * */
public class MSSQLrepository implements Repository{
	
	private Connection connection;
	private SettingsInterface settings;
	
	public MSSQLrepository(SettingsInterface settings) {
		this.settings= settings;
	}
	
	private void initConnection() throws SQLException,ClassNotFoundException{
		Class.forName("net.sourceforge.jtds.jdbc.Driver");
		String 	ip= (String) settings.getParameter("mssql_ip"),
				database= (String) settings.getParameter("mssql_database"),
				username= (String) settings.getParameter("mssql_username"),
				password= (String) settings.getParameter("mssql_password");
		Class.forName("net.sourceforge.jtds.jdbc.Driver");
		connection= DriverManager.getConnection("jdbc:jtds:sqlserver://"+ip+"/"+database,username,password);
	}
	
	/**
	 * Zatvaranje konekcije
	 * */
	private void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			connection= null;
		}
	}
	
	@Override
	public SNode getShema() {
		try {
			initConnection();
			
			DatabaseMetaData metaData= connection.getMetaData();
			InformationResource ir= new InformationResource(metaData.getDatabaseProductName());
			
			String tableType[]= {"TABLE"};
			ResultSet tables= metaData.getTables(connection.getCatalog(), null, null, tableType);
			
			while(tables.next()) {
				
				String tableName= tables.getString("TABLE_NAME");
				
				//deo koji omogucava da se ne ucitaju one dve nepotrebne tabele
				if(tableName.contains("map"))
					continue;
				
				Entity newTable= new Entity(tableName, ir);
				ir.addChildren(newTable);
				
				ResultSet columns= metaData.getColumns(connection.getCatalog(), null, tableName, null);
				
				while(columns.next()) {
					String  columnName= columns.getString("COLUMN_NAME"),
							columnType= columns.getString("TYPE_NAME");
					int columnSize= Integer.parseInt(columns.getString("COLUMN_SIZE"));
					Attribute attribute= new Attribute(columnName,newTable,AttributeType.valueOf(columnType.toUpperCase()),columnSize);
					newTable.addChildren(attribute);		
					
					//ucitavanje kljuceva i ogranicenja
					
					ResultSet pKey= metaData.getPrimaryKeys(connection.getCatalog(), null, tableName);
					ResultSet fKey= metaData.getImportedKeys(connection.getCatalog(), null, tableName);
					
					//uzimam sve primarne kljuceve i uporedjujem sa imenom kolone ako mu odgovara dodam mu ga kao child
					while(pKey.next()) {
						if(columnName.equalsIgnoreCase(pKey.getString("COLUMN_NAME"))) {
							AttributeConstraint ac= new AttributeConstraint(ConstraintType.PRIMARY_KEY.toString(), attribute, ConstraintType.PRIMARY_KEY);
							//mozda dodati pk u entitet jer kao svaka tabela ima jedan pk
							attribute.addChildren(ac);
							newTable.getPrimariKeys().add(attribute.getName());
						}
					}
					
					//uzimam sve strane kljuceve i uporedjujem sa imenom kolone ako mu odgovara dodam mu ga kao child
					while(fKey.next()) {
						if(columnName.equalsIgnoreCase(fKey.getString("FKCOLUMN_NAME"))) {
							AttributeConstraint ac= new AttributeConstraint(ConstraintType.FOREIGN_KEY.toString(), attribute, ConstraintType.FOREIGN_KEY);	
							attribute.addChildren(ac);
							newTable.getStraniKeys().add(attribute.getName());
						}
					}
					
					String nul= columns.getString("IS_NULLABLE");
					if(nul.equalsIgnoreCase("NO")) {
						AttributeConstraint ac= new AttributeConstraint(ConstraintType.NOT_NULL.toString(), attribute, ConstraintType.NOT_NULL);	
						attribute.addChildren(ac);
					}	
				}
			}
			
			//Uzimam listu soba i 
			for(SNode e : ir.getChildren()) {
				//uzimam strane kljuceve za odredjenu tabelu
				ResultSet fKey= metaData.getImportedKeys(connection.getCatalog(), null, e.getName());
				while(fKey.next()) {
					for(SNode e2 : ir.getChildren()) {
						Entity entity= (Entity)e2;
						if(e2.getName().equals(fKey.getString("PKTABLE_NAME"))) {
							Entity e3= (Entity)e;
							//fk postavljam sa pk 
							e3.getInRelationsWith().add(entity);
						}
					}
				}
			}
			return ir;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
			closeConnection();
		}
		return null;
	}

	@Override
	public List<Row> get(String from) {
		List<Row> rows= new ArrayList<Row>();
		
		try {
			this.initConnection();
			
			String query= "SELECT * FROM " + from;
			//konekciji kazemo da se pripremi da izvrsi upit
			PreparedStatement preparedStatement= connection.prepareStatement(query);
			//dobijamo objekat result set koji je najslicniji onome sto dobijemo
			//u Live SQL-u kad generisemo upit samo sto ovde imamo dodat kursor za prolazenje
			ResultSet set= preparedStatement.executeQuery();
			
			while(set.next()) {
				Row r= new Row();
				r.setName(from);
				
				ResultSetMetaData resultSetMeta= set.getMetaData();
				for(int i=1; i <= resultSetMeta.getColumnCount() ;i++)
					r.addField(resultSetMeta.getColumnName(i), set.getString(i));
				rows.add(r);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
			this.closeConnection();
		}
		return rows;
	}

	@Override
	public List<Row> filterTable(String tableName, ArrayList<String> columns) {
		List<Row> rows= new ArrayList<Row>();
		
	
		try {
			this.initConnection();
			String query= "SELECT " + columns.toString().substring(1,columns.toString().length() - 1) + " FROM " + tableName;
			PreparedStatement preparedStatement= connection.prepareStatement(query);
				
			ResultSet set= preparedStatement.executeQuery();
			
			while(set.next()) {
				Row r= new Row();
				r.setName(tableName);
				
				ResultSetMetaData resultSetMeta= set.getMetaData();
				for(int i=1; i <= resultSetMeta.getColumnCount() ;i++)
					r.addField(resultSetMeta.getColumnName(i), set.getString(i));
				rows.add(r);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			this.closeConnection();
		
		return rows;
	}

	@Override
	public List<Row> sortTable(ArrayList<String> izabraneKolone, ArrayList<String> izabranaPoredjenja,String tableName) {
		List<Row> rows= new ArrayList<Row>();
		
		
		try {
			this.initConnection();
			String query= "SELECT * FROM " + tableName + " order by ";
			String orderDeo= "";
			for(int i=0; i < izabraneKolone.size() ;i++) {
				orderDeo+= izabraneKolone.get(i) + " " + izabranaPoredjenja.get(i);
				if(i != izabranaPoredjenja.size() - 1)
					orderDeo+= ", ";
			}	
			PreparedStatement preparedStatement= connection.prepareStatement(query + orderDeo);
				
			ResultSet set= preparedStatement.executeQuery();
			
			while(set.next()) {
				Row r= new Row();
				r.setName(tableName);
				
				ResultSetMetaData resultSetMeta= set.getMetaData();
				for(int i=1; i <= resultSetMeta.getColumnCount() ;i++)
					r.addField(resultSetMeta.getColumnName(i), set.getString(i));
				rows.add(r);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			this.closeConnection();
		
		return rows;

	}

	@Override
	public List<Row> relationTable(String fkName,String fkValue,String tableName) {
		List<Row> rows= new ArrayList<Row>();
		
		try {
			this.initConnection();
			String query= "SELECT * FROM " + tableName + " WHERE " + fkName + " ='" + fkValue + "'"; 
			PreparedStatement preparedStatement= connection.prepareStatement(query);
				
			ResultSet set= preparedStatement.executeQuery();
			
			while(set.next()) {
				Row r= new Row();
				r.setName(tableName);
				
				ResultSetMetaData resultSetMeta= set.getMetaData();
				for(int i=1; i <= resultSetMeta.getColumnCount() ;i++) {
					r.addField(resultSetMeta.getColumnName(i), set.getString(i));
				}
				rows.add(r);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			this.closeConnection();
		
		return rows;
	}

	@Override
	public List<Row> countTable(String countValue, String tableName, ArrayList<String> selectedArgs) {
		List<Row> rows= new ArrayList<Row>();
		
		try {
			this.initConnection();
			String query= "SELECT COUNT(" + countValue + "), ";
			
			for(int i=0; i < selectedArgs.size() ;i++) {
				query+= selectedArgs.get(i);
				if(i < selectedArgs.size() - 1)
					query+=", ";
			}
			
			query+= " FROM " + tableName + " group by ";
			
			for(int i=0; i < selectedArgs.size() ;i++) {
				query+= selectedArgs.get(i);
				if(i < selectedArgs.size() - 1)
					query+=", ";
			}
			
			PreparedStatement preparedStatement= connection.prepareStatement(query);
				
			ResultSet set= preparedStatement.executeQuery();
			
			while(set.next()) {
				Row r= new Row();
				r.setName(tableName);
				
				ResultSetMetaData resultSetMeta= set.getMetaData();
				for(int i=1; i <= resultSetMeta.getColumnCount() ;i++) {
					r.addField(resultSetMeta.getColumnName(i), set.getString(i));
				}
				rows.add(r);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			this.closeConnection();
		
		return rows;		
	}

	@Override
	public List<Row> averageTable(String countValue, String tableName, ArrayList<String> selectedArgs) {
	List<Row> rows= new ArrayList<Row>();
		
		try {
			this.initConnection();
			String query= "SELECT AVG(" + countValue + "), ";
			
			for(int i=0; i < selectedArgs.size() ;i++) {
				query+= selectedArgs.get(i);
				if(i < selectedArgs.size() - 1)
					query+=", ";
			}
			
			query+= " FROM " + tableName + " group by ";
			
			for(int i=0; i < selectedArgs.size() ;i++) {
				query+= selectedArgs.get(i);
				if(i < selectedArgs.size() - 1)
					query+=", ";
			}
			
			PreparedStatement preparedStatement= connection.prepareStatement(query);
				
			ResultSet set= preparedStatement.executeQuery();
			
			while(set.next()) {
				Row r= new Row();
				r.setName(tableName);
				
				ResultSetMetaData resultSetMeta= set.getMetaData();
				for(int i=1; i <= resultSetMeta.getColumnCount() ;i++) {
					r.addField(resultSetMeta.getColumnName(i), set.getString(i));
				}
				rows.add(r);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			this.closeConnection();
		
		return rows;	}
	
}
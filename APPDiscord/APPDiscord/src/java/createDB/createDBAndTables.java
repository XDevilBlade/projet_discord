/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package createDB;

import java.sql.SQLException;

/**
 *
 * @author Rémy
 */
public class createDBAndTables {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
    
        createAllTables lcreateAllTables = new createAllTables();
        
        if(!(lcreateAllTables.tableExist("MESSAGES")))
        {
            lcreateAllTables.createTableMessages();
        }
        
        if(!(lcreateAllTables.tableExist("HIBERNATE_UNIQUE_KEY")))
        {
            lcreateAllTables.createTableHibernate_Unique_Key();
            lcreateAllTables.inserHibernate_Unique_Key();
        }
        
        if(!(lcreateAllTables.tableExist("UTILISATEURS")))
        {
            lcreateAllTables.createTableUtilisateurs();
            lcreateAllTables.inserUtilisateur();
        }
        
        if(!(lcreateAllTables.tableExist("ROOM")))
        {
            lcreateAllTables.createTableRoom();
        }
        
        
        
        
        
       
    }
}

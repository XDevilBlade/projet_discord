/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package createDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Rémy
 */
public class createAllTables {
    ResultSet rs = null;
    private connexionDB _laConnexion = new connexionDB();
    private Statement _sta;
    
    
    public void createTableMessages() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        java.sql.Connection laConnexion = _laConnexion.returnConnexion();
        _sta = laConnexion.createStatement();
        _sta.execute("CREATE TABLE MESSAGES "
                + "("
                + "idmessages INT,\n"
                + "contenuemessage VARCHAR(2000),\n"
                + "nomroom VARCHAR(100),\n"
                + "nomexpediteur VARCHAR(100),\n"
                + "nomdestinataire VARCHAR(100),\n"
                + "PRIMARY KEY (idmessages))");

    }
    
    public void createTableHibernate_Unique_Key() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        java.sql.Connection laConnexion = _laConnexion.returnConnexion();
        _sta = laConnexion.createStatement();
        _sta.execute("CREATE TABLE HIBERNATE_UNIQUE_KEY "
                + "("
                + "TABLENAME VARCHAR(100),\n"
                + "NEXT_HI VARCHAR(100))");

    }
    
    public void createTableUtilisateurs() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        java.sql.Connection laConnexion = _laConnexion.returnConnexion();
        _sta = laConnexion.createStatement();
         _sta.execute("CREATE TABLE UTILISATEURS "
                + "("
                + "idutilisateur INT,\n"
                + "pseudo VARCHAR(100),\n"
                + "mdp VARCHAR(2000),\n"
                + "PRIMARY KEY (idutilisateur))");

    }
    
    public void createTableRoom() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        java.sql.Connection laConnexion = _laConnexion.returnConnexion();
        _sta = laConnexion.createStatement();
        _sta.execute("CREATE TABLE ROOM "
                + "("
                + "idroom INT,\n"
                + "nomroom VARCHAR(100),\n"
                + "PRIMARY KEY (idroom))");

    }
    
    public void inserHibernate_Unique_Key() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        java.sql.Connection laConnexion = _laConnexion.returnConnexion();
        _sta = laConnexion.createStatement();
        _sta.execute("INSERT INTO HIBERNATE_UNIQUE_KEY (TABLENAME, NEXT_HI) \n"
                + "VALUES ('test','test')");

        
    }
    
    public void inserUtilisateur() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        java.sql.Connection laConnexion = _laConnexion.returnConnexion();
        _sta = laConnexion.createStatement();
        _sta.execute("INSERT INTO UTILISATEURS (idutilisateur, pseudo, mdp) \n"
                + "VALUES (0,'aze','aze')");
        _sta.execute("INSERT INTO UTILISATEURS (idutilisateur, pseudo, mdp) \n"
                + "VALUES (1,'azer','azer')");
        _sta.execute("INSERT INTO UTILISATEURS (idutilisateur, pseudo, mdp) \n"
                + "VALUES (2,'azert','azert')");

        
    }
    
     public boolean tableExist(String nomTable) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        java.sql.Connection laConnexion = _laConnexion.returnConnexion();
        boolean exist = false;
        try (ResultSet rs = laConnexion.getMetaData().getTables(null, null, nomTable, null)) {
            if (rs.next()) {
                exist = true;
            }
        }

        return exist;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaClassServer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

/**
 *
 * @author Rémy
 */
@Entity
@Table(name="messages")
public class Messages implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="idmessages")
    private int id;

    @Column(name="contenuemessage")
    private String contenue;
    
    @Column(name="nomRoom")
    private String nomRoom;
    
    @Column(name="nomexpediteur")
    private String nomExpediteur;
    
    @Column(name="nomdestinataire")
    private String nomDestinataire;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenue() {
        return contenue;
    }

    public void setContenue(String contenue) {
        this.contenue = contenue;
    }

    public String getNomRoom() {
        return nomRoom;
    }

    public void setNomRoom(String nomRoom) {
        this.nomRoom = nomRoom;
    }

    public String getNomExpediteur() {
        return nomExpediteur;
    }

    public void setNomExpediteur(String nomExpediteur) {
        this.nomExpediteur = nomExpediteur;
    }

    public String getNomDestinataire() {
        return nomDestinataire;
    }

    public void setNomDestinataire(String nomDestinataire) {
        this.nomDestinataire = nomDestinataire;
    }
    
    

    
    
    
    

    

    @Override
    public String toString() {
        return "javaClassServer.Messages[ id=" + id + " ]";
    }
    
    
    
    
}

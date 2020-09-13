/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaClassServer;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Rémy
 */
@Entity
@Table(name="utilisateurs")
public class Utilisateurs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="idutilisateur")
    private int id;

    @Column(name="pseudo")
    private String pseudoUtilisateur;
    
    @Column(name="mdp")
    private String mdpUtilisateur;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPseudoUtilisateur() {
        return pseudoUtilisateur;
    }

    public void setPseudoUtilisateur(String pseudoUtilisateur) {
        this.pseudoUtilisateur = pseudoUtilisateur;
    }

    public String getMdpUtilisateur() {
        return mdpUtilisateur;
    }

    public void setMdpUtilisateur(String mdpUtilisateur) {
        this.mdpUtilisateur = mdpUtilisateur;
    }

    
    
    
    

    

    @Override
    public String toString() {
        return "javaClassServer.Messages[ id=" + id + " ]";
    }
    
    
    
    
}

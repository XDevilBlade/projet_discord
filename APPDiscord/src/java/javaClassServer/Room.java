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
@Table(name="room")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="idroom")
    private int id;

    @Column(name="nomroom")
    private String nomRoom;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomRoom() {
        return nomRoom;
    }

    public void setNomRoom(String nomRoom) {
        this.nomRoom = nomRoom;
    }
       

    @Override
    public String toString() {
        return "javaClassServer.Messages[ id=" + id + " ]";
    }
    
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaClassServer;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Rémy
 */
public class AccessRoomJPA {
   private EntityManager em = null;
    
    public EntityManager getEntityManager(){
        if(em==null)
        {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("APPDiscordPU");
            em = emf.createEntityManager();
        }
        return em;
    }
    
    public void persist(Room r){
        
        EntityTransaction trans = null;
        trans= this.getEntityManager().getTransaction();
        trans.begin();
        try{
            this.getEntityManager().persist(r);
            trans.commit();
        }
        catch(Exception e){
            trans.rollback();
        }
        finally{
            this.getEntityManager().close();
        }
        
    }
    
    public List<Room> getRoom(){
     
            Query query =  this.getEntityManager().createQuery("SELECT r FROM Room r");
            List<Room> listRoom=(List<Room>)query.getResultList();
        
        return listRoom;
    }
    
    public Room findRoom(int id){
        return this.getEntityManager().find(Room.class, id);
    }
    
    public void deleteRoom(int id){
        Room r = this.findRoom(id);
        EntityTransaction trans = null;
        trans= this.getEntityManager().getTransaction();
        trans.begin();
        try{
            this.getEntityManager().remove(r);
            trans.commit();
        }
        catch(Exception e){
            trans.rollback();
        }
        finally{
            this.getEntityManager().close();
        }
    }
    
    public Room getUnRoomParSonNom(String nameRoom){
        
        Room r = null;
        
        Query query = this.getEntityManager().createQuery("SELECT r FROM Room r WHERE r.nomRoom :unNomRoom");
        //query.setParameter("unIdMessage", "32768");
        query.setParameter("unNomRoom", nameRoom);
        r = (Room)query.getSingleResult();
        
        return r;
        
    }
    
    public void setUnRoom(int id, String nameRoom){
        Room r = this.findRoom(id);
        EntityTransaction trans = null;
        trans= this.getEntityManager().getTransaction();
        trans.begin();
        try{
            Query query = this.getEntityManager().createQuery("update Room r set r.nomRoom = :newNameRoom where r.id = :unIdRoom");
            query.setParameter("newNameRoom", nameRoom);
            query.setParameter("unIdRoom", id);
            query.executeUpdate();
            trans.commit();
        }
        catch(Exception e){
            trans.rollback();
        }
        finally{
            this.getEntityManager().close();
        }
    }
    
    public Long getNbElement(){
        Query query = this.getEntityManager().createQuery("select count(r) from  Room r");
        Long nbElement=(Long) query.getSingleResult();
        return nbElement;
        
    } 
}

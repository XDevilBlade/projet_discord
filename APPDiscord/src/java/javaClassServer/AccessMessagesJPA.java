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
public class AccessMessagesJPA {
    private EntityManager em = null;
    
    public EntityManager getEntityManager(){
        if(em==null)
        {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("APPDiscordPU");
            em = emf.createEntityManager();
        }
        return em;
    }
    
    public void persist(Messages m){
        
        EntityTransaction trans = null;
        trans= this.getEntityManager().getTransaction();
        trans.begin();
        try{
            this.getEntityManager().persist(m);
            trans.commit();
        }
        catch(Exception e){
            trans.rollback();
        }
        finally{
            this.getEntityManager().close();
        }
        
    }
    
    public List<Messages> getMessages(){
        
        
        
        
        
            Query query =  this.getEntityManager().createQuery("SELECT m FROM Messages m");
            List<Messages> listMessages=(List<Messages>)query.getResultList();
        
        return listMessages;
    }
    
    public Messages findMessage(int id){
        return this.getEntityManager().find(Messages.class, id);
    }
    
    public void deleteMessages(int id){
        Messages m = this.findMessage(id);
        EntityTransaction trans = null;
        trans= this.getEntityManager().getTransaction();
        trans.begin();
        try{
            this.getEntityManager().remove(m);
            trans.commit();
        }
        catch(Exception e){
            trans.rollback();
        }
        finally{
            this.getEntityManager().close();
        }
    }
    
    public List<Messages> getMessagesDUnChannel(String nomChannel){
        Query query =  this.getEntityManager().createQuery("SELECT m FROM Messages m WHERE m.nomRoom= :unNomRoom");
        query.setParameter("unNomRoom", nomChannel);
        List<Messages> listMessages=(List<Messages>)query.getResultList();
        return listMessages;
    }
    
    public List<Messages> getMessagesDUneConversation(String nomExpediteur,String nomDestinataire){
        Query query =  this.getEntityManager().createQuery("SELECT m FROM Messages m WHERE m.nomExpediteur= :nomexpediteur AND m.nomDestinataire= :nomdestinataire");
        query.setParameter("nomexpediteur", nomExpediteur);
        query.setParameter("nomdestinataire", nomDestinataire);
        List<Messages> listMessages=(List<Messages>)query.getResultList();
        return listMessages;
    }
    
    
    public Messages getUnMessageParSonContenue(String contenue){
        
        Messages m = null;
        
        Query query = this.getEntityManager().createQuery("SELECT m FROM Messages m WHERE m.contenue= :contenueMessage");
        //query.setParameter("unIdMessage", "32768");
        query.setParameter("contenueMessage", contenue);
        m = (Messages)query.getSingleResult();
        
        return m;
        
    }
    
    public void setUnMessage(int id, String newContenue){
        Messages m = this.findMessage(id);
        EntityTransaction trans = null;
        trans= this.getEntityManager().getTransaction();
        trans.begin();
        try{
            Query query = this.getEntityManager().createQuery("update Messages m set m.contenue = :newContenue where m.id = :unIdMessage");
            query.setParameter("newContenue", newContenue);
            query.setParameter("unIdMessage", id);
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
        Query query = this.getEntityManager().createQuery("select count(m) from  Messages m");
        Long nbElement=(Long) query.getSingleResult();
        return nbElement;
        
    }
}

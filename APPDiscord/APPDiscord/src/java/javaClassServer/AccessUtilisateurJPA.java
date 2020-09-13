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
public class AccessUtilisateurJPA {
    private EntityManager em = null;
    
    public EntityManager getEntityManager(){
        if(em==null)
        {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("APPDiscordPU");
            em = emf.createEntityManager();
        }
        return em;
    }
    
    public void persist(Utilisateurs u){
        
        EntityTransaction trans = null;
        trans= this.getEntityManager().getTransaction();
        trans.begin();
        try{
            this.getEntityManager().persist(u);
            trans.commit();
        }
        catch(Exception e){
            trans.rollback();
        }
        finally{
            this.getEntityManager().close();
        }
        
    }
    
    public List<Utilisateurs> getUtilisateurs(){
     
            Query query =  this.getEntityManager().createQuery("SELECT u FROM Utilisateurs u");
            List<Utilisateurs> listUtilisateurs=(List<Utilisateurs>)query.getResultList();
        
        return listUtilisateurs;
    }
    
    public Utilisateurs findUtilisateur(int id){
        return this.getEntityManager().find(Utilisateurs.class, id);
    }
    
    public void deleteUtilisateurs(int id){
        Utilisateurs u = this.findUtilisateur(id);
        EntityTransaction trans = null;
        trans= this.getEntityManager().getTransaction();
        trans.begin();
        try{
            this.getEntityManager().remove(u);
            trans.commit();
        }
        catch(Exception e){
            trans.rollback();
        }
        finally{
            this.getEntityManager().close();
        }
    }
    
    
    
    public Utilisateurs getUnUtilisateurParSonNomUtilisateur(String nomUtilisateur){
        
        Utilisateurs u = null;
        
        Query query = this.getEntityManager().createQuery("SELECT u FROM Utilisateurs u WHERE u.pseudoUtilisateur :nomPseudo");
        //query.setParameter("unIdMessage", "32768");
        query.setParameter("nomPseudo", nomUtilisateur);
        u = (Utilisateurs)query.getSingleResult();
        
        return u;
        
    }
    
    public Utilisateurs getUnUtilisateurParSonNomUtilisateurEtSonMDP(String nomUtilisateur, String mdp){
        
        Utilisateurs u = null;
        try
        {
            Query query = this.getEntityManager().createQuery("SELECT u FROM Utilisateurs u WHERE u.pseudoUtilisateur= :nomPseudo AND u.mdpUtilisateur= :unMDP");
            //query.setParameter("unIdMessage", "32768");
            query.setParameter("nomPseudo", nomUtilisateur);
            query.setParameter("unMDP", mdp);
            u = (Utilisateurs)query.getSingleResult();
            return u;
        }
        catch(javax.persistence.NoResultException e)
        {
            return null;
        }
        
        
        
        
    }
    
    public void setUnMDP(String pseudo, String newMDP){
        Utilisateurs u = this.getUnUtilisateurParSonNomUtilisateur(pseudo);
        EntityTransaction trans = null;
        trans= this.getEntityManager().getTransaction();
        trans.begin();
        try{
            Query query = this.getEntityManager().createQuery("update Utilisateurs u set u.mdpUtilisateur = :newMdp where u.pseudoUtilisateur = :unPseudoUtilisateur");
            query.setParameter("newMdp", newMDP);
            query.setParameter("unPseudoUtilisateur", pseudo);
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
        Query query = this.getEntityManager().createQuery("select count(u) from  Utilisateurs u");
        Long nbElement=(Long) query.getSingleResult();
        return nbElement;
        
    } 
    
    public boolean isExist(String nomUtilisateur){

        Utilisateurs u = null;
        try
        {
            Query query = this.getEntityManager().createQuery("select u from  Utilisateurs u WHERE u.pseudoUtilisateur= :unNomUtilisateur");
            query.setParameter("unNomUtilisateur", nomUtilisateur);
            u = (Utilisateurs)query.getSingleResult();
            return true;
        }
        catch(javax.persistence.NoResultException e)
        {
            return false;
        }
        
    }
}

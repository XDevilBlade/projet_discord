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

/**
 *
 * @author Rémy
 */
public class teset {
    
    
    public static void main(String[] args)
    {
      AccessMessagesJPA lAccessMessagesJPA = new AccessMessagesJPA();
      AccessUtilisateurJPA lAccessUtilisateurJPA = new AccessUtilisateurJPA();
      AccessRoomJPA lAccessRoomJPA = new AccessRoomJPA();
      
      /*Room r = new Room();
      r.setId(0);
      r.setNomRoom("tt");
      lAccessRoomJPA.persist(r);*/
      
      //System.out.println(lAccessRoomJPA.findRoom(0).getNomRoom());
      //lAccessRoomJPA.setUnRoom(0, "test");
      /*Utilisateurs unUtilisateurs = new Utilisateurs();
      
      unUtilisateurs.setId(15);
      unUtilisateurs.setPseudoUtilisateur("azert");
      unUtilisateurs.setMdpUtilisateur("azert");*/
      
      //lAccessUtilisateurJPA.persist(unUtilisateurs);
      
      //System.out.println(lAccessUtilisateurJPA.findUtilisateur(1).getPseudoUtilisateur()+" "+lAccessUtilisateurJPA.findUtilisateur(1).getMdpUtilisateur());
      
      //lAccessUtilisateurJPA.setUnMDP("Devil", "yolo");
      /*long nbElementLong = lAccessUtilisateurJPA.getNbElement();
      int nbElement = (int)nbElementLong;
      System.out.println(nbElement);
      
      
      
      
      /*Messages m = new Messages();
      m.setId(0);
      m.setContenue("test");
      m.setIdExpediteur("test");
      
      lAccessMessagesJPA.persist(m);*/
      
      //System.out.println(lAccessMessagesJPA.getUnMessageParSonContenue(m.getTest()).getId());
      /*
      lAccessMessagesJPA.persist(m);*/
        List<Messages> listMessages=lAccessMessagesJPA.getMessages();
        for(Messages unMessages : listMessages)
        {
            System.out.println(unMessages.getId()+" "+unMessages.getContenue()+" "+unMessages.getNomExpediteur()+" "+unMessages.getNomRoom());
        }
        /*if(listMessages==null)
        {
            System.out.println("failed");
        }
        for(Messages message : listMessages)
        {
            System.out.println(message.getId()+" "+message.getContenue());
        }
        
        System.out.println(lAccessMessagesJPA.getNbElement());
        
        //lAccessMessagesJPA.setUnMessage(10, "nice");*/
      }
    
}

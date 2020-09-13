/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaClassServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

/**
 *
 * @author rtaillandier
 */
@ApplicationScoped
public class SessionHandler {
    private final Set<Session> sessions = new HashSet<>();
    private final ArrayList<Session> sessionsChannelL3 = new ArrayList<Session>();//tableau de session qui va permettre au serveur endpoint, de rediriger les messages envoyés dans le channel L3, aux bonnes personnes
    private final ArrayList<Session> sessionsChannelTI = new ArrayList<Session>();//tableau de session qui va permettre au serveur endpoint, de rediriger les messages envoyés dans le channel TI, aux bonnes personnes
    private final HashMap<Session,String> associationNomUtilisateurAvecSession = new HashMap<Session,String>();//tableau de session qui va me permettre d'affecter à chaque session, un nom utilisateur, pour que le serveur puisse rediriger les messages privées aux bonnes personnes

    public void addSession(Session session) {
        sessions.add(session);
    }
    
    
        
    public void sendToAllConnectedSessions(String message, Session uneSession, String channel, String nomExpediteur, String nomDestinataire) {
        if(channel.equals("L3"))//le serveur doit rediriger le message à tout ce qui se trouve dans le channel L3 
        {                       //ou tout ce qui ont accès au channel L3
            for(Session uneSessionChannelL3 : sessionsChannelL3)
                {
                    sendToSession(uneSessionChannelL3, message);
                }
        }
        else if(channel.equals("TI"))//le serveur doit rediriger le message à tout ce qui se trouve dans le channel TI 
        {                       //ou tout ce qui ont accès au channel TI
              for(Session uneSessionChannelTI : sessionsChannelTI)
                {
                    sendToSession(uneSessionChannelTI, message);
                }
        }
        else if(channel.equals("ConversationAmi"))//on renvoie le message aux deux personnes qui se trouvent dans la discussion privée 
        {
            for (Object o : associationNomUtilisateurAvecSession.keySet()) {
                if (associationNomUtilisateurAvecSession.get(o).equals(nomExpediteur)) {
                  sendToSession((Session)o, message);
                  break;
                }
            }
            for (Object o : associationNomUtilisateurAvecSession.keySet()) {
                if (associationNomUtilisateurAvecSession.get(o).equals(nomDestinataire)) {
                  sendToSession((Session)o, message);
                  break;
                }
            }
        }
        
    }
    
    public void deleteSession(Session session){
        this.sessions.remove(session);
        if(this.sessionsChannelL3.indexOf(session)!=-1){
            this.removeSessionInChannelL3(session);
        }
        if(this.sessionsChannelTI.indexOf(session)!=-1){
            this.removeSessionInChannelTI(session);
        }
        if(associationNomUtilisateurAvecSession.containsKey(session))
        {
            this.removeCorrespondanceNomUtilisateurAvecSession(session);
        }
    }
    
    public boolean isSessionInChannelL3(Session session){
        boolean isExist = false;
        if(this.sessionsChannelL3.indexOf(session)!=-1){
            isExist = true;
        }
        
        return isExist;
    }
    
    public boolean isSessionInChannelTI(Session session){
        boolean isExist = false;
        if(this.sessionsChannelTI.indexOf(session)!=-1){
            isExist = true;
        }
        
        return isExist;
    }

    private void sendToSession(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException ex) {
            //sessions.remove(session);
            Logger.getLogger(SessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addSessionInChannelL3(Session session){
        if(!(this.isSessionInChannelL3(session))){
            sessionsChannelL3.add(session);
        }
        
    }
    
    public void removeSessionInChannelL3(Session session){
        sessionsChannelL3.remove(session);
    }
    
    public void addSessionInChannelTI(Session session){
        
        if(!(this.isSessionInChannelTI(session))){
            sessionsChannelTI.add(session);
        }
    }
    
    public void removeSessionInChannelTI(Session session){
        sessionsChannelTI.remove(session);
    }
   
    
    public void addCorrespondanceNomUtilisateurAvecSession(Session session, String nomUtilisateur){
        if(!(associationNomUtilisateurAvecSession.containsKey(session))){
            associationNomUtilisateurAvecSession.put(session,nomUtilisateur);
        }
    }
    
    public void removeCorrespondanceNomUtilisateurAvecSession(Session session){
        associationNomUtilisateurAvecSession.remove(session);
    }
    
    
}
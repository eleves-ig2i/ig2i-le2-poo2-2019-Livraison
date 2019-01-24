/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planning;

import java.util.*;
import reseau.*;


/**
 *
 * @author user
 */
public class Planning {
    
    
    private Set<Tournee> ensTournees;       // D'après l'énoncé, cet attribut est un ensemble de tournées. (set = ensemble)
    
    /**
     * Distance parcourue par l'ensemble des camions.
     */
    private double distanceTotaleParcourue;
   
    private int nbTotalCaissesLivrees;
    
    /**
     * Capacité de chaque camion.
     */
    private int capaciteCamions;
    
    /**
     * Attribut utile lors de la planification par Naysson.
     * Permet de faciliter l'utilisation de l'ensemble de clients.
     */
    private HashClientsUtilises h;

    public Set<Tournee> getEnsTournees() {
        return ensTournees;
    }

  
    
    public Planning(int capaciteCamion)
    {
        if( capaciteCamion >= 0)
            this.capaciteCamions = capaciteCamion;
        else
            this.capaciteCamions = 0;
        
        ensTournees = new HashSet<>();      // On utilise un HashSet au lieu d'un TreeSet (à priori, on a pas besoin d'effectuer de tri d'élément)
        
        distanceTotaleParcourue = 0;
        nbTotalCaissesLivrees = 0;
        
        h = null;
    }
    
    
    public Planning(int capaciteCamion, int nbClients)
    {
        this(capaciteCamion);
        h = new HashClientsUtilises(nbClients);
    }
    
    
    public double getDistanceTotaleParcourue() {
        return distanceTotaleParcourue;
    }

    public int getNbTotalCaissesLivrees() {
        return nbTotalCaissesLivrees;
    }

    public int getCapaciteCamions() {
        return capaciteCamions;
    }
    
    public int getNombreTournees()
    {
        return this.ensTournees.size();
    }

    
    
    
    /**
     * ajoute une tournée à l’ensemble des tournées.
     * Cette méthode mettra à jour les attributs pour la distance totale et le nombre total de caisses livrées.
     * @param t la tournée à ajouter.
     * @return Cette méthode renvoie true si l’ajout a bien eu lieu, et false sinon.
     */
    private boolean ajouterTournee(Tournee t)
    {
        if( t!= null)
        {
            //System.out.println("ajouterTournee - Distance de la tournée actuelle : " + t.getDistanceParcourue() );    // KO
            this.ensTournees.add(t);
            this.distanceTotaleParcourue += t.getDistanceParcourue();
            this.nbTotalCaissesLivrees += t.getNbCaissesLivrees();           
            return true;
        }
        else
            return false;
    }    
    
    /**
     * Ajoute une tournée à l'ensemble des tournée.
     * Utilisée dans la planificationMinTournee.
     */
    private boolean ajouterTourneeV2(Tournee t)
    {
        if( t!= null)
        {
            this.ensTournees.add(t);
            return true;
        }
        else
            return false;
    }
            
            
    /**
     * remplit l'ensemble de tournées à l'aide de la liste de clients.
     * Cette méthode commence par créer une tournée vide, que nous nommerons tournée courante. 
     * Puis, elle itère sur l’ensemble des clients et les ajoute à la tournée courante. 
     * Lorsque l’ajout d’un client à la tournée courante n’est pas possible, alors la tournée courante est ajoutée au planning (avec la méthode ajouter-Tournee). 
     * Puis une nouvelle tournée courante vide est créée et le client qui n’avait pas pu être ajouté précédemment lui est ajouté. 
     * Il suffit ensuite de réitérer ce processus.
     * @param depot
     * @param clients 
     */
    public void planificationBasique(Depot depot, Set<Client> clients)
    {
        if( depot != null && clients != null)
        {
            Iterator<Client> itClients = clients.iterator();
            Client clientActuel = null;
            boolean tourneeRemplie = false;     // tournee Remplie <===> il reste un client à ajouter.
            
            // on est obligé de passer par un itérateur
            // Un "for each" aurait suffi si l'on devait pas ajouter un client qui n'avait pas pu être ajouté précédemment, dans une tournée vide.
            while( itClients.hasNext() || tourneeRemplie )        
            {
                Tournee tourneeCourante = new Tournee(depot, this.capaciteCamions);
                if( tourneeRemplie )    // si la tournée précédente était remplie
                {
                    if( !tourneeCourante.ajouterClient(clientActuel) )
                    {
                        System.out.println("Problème : on ne peut ajouter un client dans une tournée vide.");       // OK
                        return;
                    }
                    tourneeRemplie = false;
                }
                
                do
                {
                    if( itClients.hasNext() )
                    {
                        clientActuel = itClients.next();
                        //System.out.println("planificationBasique - Client actuel : " + clientActuel.toString() ); // OK
                        tourneeRemplie = !tourneeCourante.ajouterClient(clientActuel);      // la tournée est remplie lorsque cette méthode renvoie false
                        //System.out.println("Tournee remplie : " + tourneeRemplie);  // OK
                    }
                }while( itClients.hasNext() && !tourneeRemplie );
                
                //System.out.println("planificationBasique - Tournée ajouté : " + tourneeCourante ); // OK
                //System.out.println("Tournee remplie : " + tourneeRemplie + " && Liste des clients totalement utilisée : " + itClients.hasNext() );  // OK
                this.ajouterTournee(tourneeCourante);
            }
            
        }
    }
    
    
    /**
     * Remplit les tournées à l'aide d'une liste de clients.
     * cherche d’abord à insérer un client dans une des tournées existantes. 
     * Seulement si aucune tournée ne dispose de la capacité nécessaire pour livrer les caisses de bière requises par le client, 
     * alors une nouvelle tournée est créée. 
     * Notez que le calcul de la distance totale dans la classe Planning pourra alors être réalisé lorsque tous les clients ont été ajoutés dans une tournée. 
     */
    public void planificationMinTournee(Depot depot, Set<Client> clients)
    {
        for(Client c : clients)
        {
            this.ajouterClient(depot, c);
        }
        
        reActualiserCriteres();
    }

    private void reActualiserCriteres() {
        this.distanceTotaleParcourue = 0;
        this.nbTotalCaissesLivrees = 0;
        Iterator<Tournee> itTournees = this.ensTournees.iterator();
        
        while( itTournees.hasNext() )
        {
            Tournee t = itTournees.next();
            this.distanceTotaleParcourue += t.getDistanceParcourue();
            this.nbTotalCaissesLivrees += t.getNbCaissesLivrees();
        }
    }
    
    /**
     * Ajoute un client dans un planning, selon la méthode de planification décrite dans planificationMinTournee.
     * cherche d’abord à insérer un client dans une des tournées existantes. 
     * Si aucune tournée ne dispose de la capacité nécessaire pour livrer les caisses de bière requises par le client, 
     * alors une nouvelle tournée est créée. 
     * Notez que le calcul de la distance totale dans la classe Planning pourra alors être réalisé lorsque tous les clients ont été ajoutés dans une tournée. 
     */
    private void ajouterClient(Depot depot, Client client)
    {
        Iterator<Tournee> itTournees = this.ensTournees.iterator();
        boolean clientAjoute = false;
        
        while( itTournees.hasNext() && !clientAjoute )
        {
            Tournee tourneeActuelle = itTournees.next();
            if( tourneeActuelle.ajouterClient(client) )
            {
                clientAjoute = true;
            }
        }    
        
        if( !clientAjoute)
        {
            Tournee nouvelleTournee = new Tournee(depot, capaciteCamions);
            if( !nouvelleTournee.ajouterClient(client) )
            {
                System.err.println("Problème : on ne peut ajouter un client dans une tournée vide.");       // OK
                return;
            }
            else
            {
                this.ajouterTourneeV2(nouvelleTournee);
            }
        }
    }

    
    
    @Override
    public String toString() {
        // TP5
        /*
        String sReturn = new String("Planning{" + "distanceTotaleParcourue=" + distanceTotaleParcourue + ", nbTotalCaissesLivrees=" + nbTotalCaissesLivrees + ", capaciteCamions=" + capaciteCamions + ",ensTournees = {");
        for( Tournee t : this.ensTournees)
        {
             sReturn += t.toString();
             sReturn += '\n';
        }
        sReturn += "}}";
        */
        String sReturn = new String();
        sReturn += "Planning - Nombre de tournées = " + this.ensTournees.size();
        sReturn += " | Distance totale : " + this.distanceTotaleParcourue;
        sReturn += " | Nombre de caisses livrées : " + this.nbTotalCaissesLivrees;
        for(Tournee t : this.ensTournees )
        {
            sReturn += "\n";
            sReturn += t.toString();
          
        }
        
        return sReturn;
    }
    
    /**
     * Utilisé pour les fichiers passant dans le checker.
     */
    public String toStringSimple() {
        String sReturn = new String();
        
        for(Tournee t : this.ensTournees)
        {
            sReturn += t.toStringSimple();
            sReturn += "\n";
        }
        return sReturn;
    }
    
    
    
    /**
     * Cette méthode parcourt les tournées actuelles du planning et,
     * pour chacune, fait appel à la méthode calculerBestInsertion. 
     * Puis, elle réalise effectivement l’insertion à l’endroit qui permet de faire le plus petit détour. 
     * Si un client n’a pu être inséré dans aucune tournée, alors on crée une nouvelle tournée.
     */
    private void planificationBestInsertion(Depot depot, Client nouveauClient)
    {
        
       BestInsertion bBest = new BestInsertion();
       
       // On détermine la meilleure insertion à l'aide de la distance ajoutée à chaque fois.
       for(Tournee t : this.ensTournees)
       {
           BestInsertion b = t.calculerBestInsertion(nouveauClient);
           if( b != null && b.getDistanceAjoutee() < bBest.getDistanceAjoutee() )
           {
               bBest = b;
           }           
       }
       
       if( bBest.getT() == null)    // Si un client n’a pu être inséré dans aucune tournée
       {
           Tournee newTournee = new Tournee(depot, capaciteCamions);
           if( !newTournee.ajouterClient(nouveauClient))
           {
               System.err.println("Client impossible à ajouter.");
               return;
           }
           else
           {
               this.ajouterTourneeV2(newTournee);
           }          
       }       
       else // on insère le client selon la bestInsertion
       {
           Tournee t = bBest.getT();
           t.insererClient(nouveauClient, bBest);
       }
    }
    
    /**
     * Cette méthode parcourt la liste de clients, afin de les insérer selon la méthode de planification par BestInsertion.
     */
    public void planificationBestInsertion(Depot depot, Set<Client> clients)
    {
       for(Client c : clients)
       {
           planificationBestInsertion(depot, c);
       }
       
       this.reActualiserCriteres();
    }
    
   
    

}

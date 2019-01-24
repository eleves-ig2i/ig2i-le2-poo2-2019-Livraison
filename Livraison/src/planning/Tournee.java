/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planning;

import mesExceptions.*;
import reseau.*;
import java.util.*;

/**
 *
 * @author user
 */
public class Tournee {
    
    
    private Depot pointDepart;
    
    /**
     *  distance parcourue depuis le départ du dépôt jusqu’au retour au dépôt.
     */
    private double distanceParcourue;
    
    /**
     * nombre de caisses de bière livrées durant la tournée.
     */
    private int nbCaissesLivrees;
    
    /**
     * Un camion a une capacité limitée (en nombre de caisses). Le nombre de caisses livrées dans la tournée ne doit pas dépasser la capacité.
     */
    private int capaciteCamion;
    
    /**
     * La liste des client à livrer.
     */
    private LinkedList<Client> listeClients;
    
    
    /**
     * Constructeur par données. Si capacite est négatif ou nul, l'attribut associé vaudra 0.
     * @param d
     * @param nbClients La capacité du camion.
     */
    public Tournee(Depot d, int capacite)
    {
        this.pointDepart = d;
        if( capacite >= 0)
            this.capaciteCamion = capacite;
        else
            this.capaciteCamion = 0;
        
        listeClients = new LinkedList<Client>();
        
    }

    public double getDistanceParcourue() {
        return distanceParcourue;
    }

    public int getNbCaissesLivrees() {
        return nbCaissesLivrees;
    }

    public int getCapaciteCamion() {
        return capaciteCamion;
    }
    
    public int getNombreClients()
    {
        return this.listeClients.size();
    }
    
    
   
    /**
     * ajoute le client dans la collection des clients livrés (on suppose que ce client est ajouté à la fin de la tournée, avant de retourner au dépôt). 
     * Cette méthode renvoie true si l’ajout a bien eu lieu, et false sinon.
     * 
     * Cette méthode doit mettre à jour la distance de la tournée ainsi que le nombre de caisses livrées.
     * @param client
     * @return 
     */
    public boolean ajouterClient(Client client)
    {
        if( client != null && (this.nbCaissesLivrees + client.getNbCaissesBieresALivrer() <= this.capaciteCamion) )
        {
            this._majTournee(client);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    /**
     * Insère le nouveau client selon les critères de la meilleure insertion.
     */
    public boolean insererClient(Client client, BestInsertion b)
    {
        if( client != null && b != null )
        {
            this.listeClients.add(b.getPositionTournee(), client);
            this.distanceParcourue += b.getDistanceAjoutee();
            this.nbCaissesLivrees += client.getNbCaissesBieresALivrer();
            return true;
        }
        else
            return false;
    }
    
    
    /**
     * Met à jour la tournée, lorsqu'on ajoute un client VALIDE.
     */
    private void _majTournee(Client nouveauClient)
    {       

        if( !this.listeClients.isEmpty() )      // Est-ce le premier client valide ?
        {
            Client dernierClient = this.listeClients.getLast();
            this.distanceParcourue -= dernierClient.distance(this.pointDepart); // on retire la distance entre le depot et le dernier client
            this.distanceParcourue += dernierClient.distance(nouveauClient);    // on ajoute la distance entre le dernier client et le nouveau client.
            this.distanceParcourue += nouveauClient.distance(this.pointDepart);      // et enfin on ajoute la distance entre le point de départ et le nouveau client.
        }
        else
        {
            // Premier client valide => la distance parcourue est l'aller retour entre le client et le dépot.
            this.distanceParcourue = 2*nouveauClient.distance(this.pointDepart);
        }
        
        this.nbCaissesLivrees += nouveauClient.getNbCaissesBieresALivrer();
        this.listeClients.add(nouveauClient);
    }
   
    
    
    /**
     * Ajoute  tous  les  clients  de l’ensemble ensembleClient à la tournée (en mettant à jour la distance parcourue et la quantité livrée). 
     * @param ensembleClient
     * @return true si tous les ajouts ont bien eu lieu, et false sinon (certains clients n’ont pas pu être ajoutés)
     */
    public boolean ajouterClient(Set<Client>  ensembleClient)
    {
        boolean res = true;
        for( Client c : ensembleClient)
        {
            if( !this.ajouterClient(c) && res == true )
                res = false;
        }
        
        return res;
    }

    @Override
    public String toString() {
        String sReturn = new String();
        /*
        String sReturn = new String("Tournee{" + "pointDepart=" + pointDepart + ", distanceParcourue=" + distanceParcourue + ", nbCaissesLivrees=" + nbCaissesLivrees + ", capaciteCamion=" + capaciteCamion + ", listeClients={\n");
        for( Client c : this.listeClients )
        {
            sReturn += c.toString();
            sReturn += "\n";
        }
        sReturn += "}}";
        */
        sReturn = " Tournee - Nombre de clients visités = " + this.listeClients.size() + " | Longueur = " + this.distanceParcourue + " | Nombre de caisses livrées par la tournée = " + this.nbCaissesLivrees + "\n";
        sReturn += " {\n  ";
        for( Client c : this.listeClients )
        {
            sReturn += c.toString();
            sReturn += "\n";
        }
        sReturn += "\n},";
        return sReturn;  
    }
    
    
     public String toStringSimple() {
        String sReturn = new String();
        
        sReturn = "" + this.pointDepart.id();
        for( Client c : this.listeClients )
        {
            sReturn += " " + c.id();
        }
        sReturn += " " + this.pointDepart.id();
        return sReturn;  
    }
     
     
    /**
     * Renvoie la distance ajoutée par l'insertion du client entre le point actuel et le point suivant.
     */ 
    private double simulerInsertion(Point pActuel, Client nouveauClient, Point pSuivant)
    {
        return pActuel.distance(nouveauClient) + nouveauClient.distance(pSuivant) - pActuel.distance(pSuivant); 
    }
    
     /**
      * Cette méthode calcule la meilleure insertion du nouveauClient dans la tournée courante (sans réaliser cette insertion) 
      * et renvoie une instance de la classe BestInsertion avec les informations associées à la meilleure insertion. 
      * Si l’insertion n’est pas réalisable,il faudra renvoyer null ou bien une instance "par défaut" de BestInsertion.
      */
    public BestInsertion calculerBestInsertion(Client nouveauClient)
    {
        if (this.nbCaissesLivrees + nouveauClient.getNbCaissesBieresALivrer() > this.capaciteCamion )
            return null;
        
        Iterator<Client> itClients = this.listeClients.iterator();
        boolean rechercheTerminee = false;
        
        // On simule la première insertion.
        Point pActuel = this.pointDepart;
        Point pSuivant = itClients.next();
        int positionMeilleureInsertion = 0;
        double distanceAjoutee = simulerInsertion(pActuel, nouveauClient, pSuivant);
        BestInsertion b = new BestInsertion(this,distanceAjoutee,0);

        
        while( !rechercheTerminee )
        {
            positionMeilleureInsertion++;
            pActuel = pSuivant;
            
            if( itClients.hasNext() )
            {        
                pSuivant = itClients.next();            
            }
            else
            {
                pSuivant = this.pointDepart;
                rechercheTerminee = true;
            }
            
            distanceAjoutee = simulerInsertion(pActuel, nouveauClient, pSuivant);
            
            if( distanceAjoutee < b.getDistanceAjoutee() )   // si la distance de la nouvelle insertion est inférieur à la meilleure insertion, alors on la change !
            {
                b.setDistanceAjoutee(distanceAjoutee);
                b.setPositionTournee(positionMeilleureInsertion);
            } 
        }
        
        return b;
    }


    public static void main(String[] args) {  
        try {
            Depot d1 = new Depot(0, 0);
            Client c1 = new Client(5, 5, 10);
            Client c2 = new Client(-5, 5, 10);
            Client c3 = new Client(-5, -5, 10);
            Client c4 = new Client(5, -5, 10);
            Set<Point> ensPoint = new HashSet<>();
            ensPoint.add(c1); 
            ensPoint.add(c2); 
            ensPoint.add(c3); 
            ensPoint.add(c4);
            d1.ajouterRoutes(ensPoint);
            ensPoint.add(d1);
            c1.ajouterRoutes(ensPoint);
            c2.ajouterRoutes(ensPoint);
            c3.ajouterRoutes(ensPoint);
            c4.ajouterRoutes(ensPoint);
            Set<Client> ensClient = new HashSet<>();
            ensClient.add(c1); 
            ensClient.add(c2); 
            ensClient.add(c3); 
            ensClient.add(c4); 
            Tournee t = new Tournee(d1, 50);
            t.ajouterClient(ensClient);
            System.out.println(t); 
        }
        catch( ErrQuantite ex)
        {
            System.out.println("Erreur: quantité negative (" + ex.getQuantite() + ")");
            System.exit(-1);
        }
    }
    
}

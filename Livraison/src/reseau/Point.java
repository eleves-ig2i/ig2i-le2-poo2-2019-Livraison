/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reseau;

import java.util.*;
import planning.HashClientsUtilises;

/**
 * @class Point
 * @author nsalez
 */
public abstract class Point {
    private static int dernierId = 0;
    
    /**
     * Chaque point possède un identifiant unique, car il est possible que plusieurs clients soient au même endroit. 
     */
    private int id; 
    private double abscisse;
    private double ordonnee;
    
    private Map<Point,Route> destinations;
    
    
    public Point(double x, double y)
    {
        this.abscisse = x;
        this.ordonnee = y;
        
        this.id = dernierId++; 
        
        destinations = new HashMap<Point,Route>();
               
    }

    public int id() {
        return id;
    }

    public double x() {
        return abscisse;
    }

    public double y() {
        return ordonnee;
    }

    public void setAbscisse(double abscisse) {
        this.abscisse = abscisse;
    }

    public void setOrdonnee(double ordonnee) {
        this.ordonnee = ordonnee;
    }

    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.id;
        return hash;
        //return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

   
    /**
     * Remplit la liste de routes.
     * @param mesDestinations n ensemble de points
     */
    public void ajouterRoutes(Set<Point> mesDestinations)
    {
        for( Point pDest : mesDestinations)
        {
            Route rAsso = new Route(this,pDest);
            this.destinations.put( pDest, rAsso );
        }
    }
    
    /**
     * renvoie la distance entre le point (this) et le point p. On renverra infini (Double.POSITIVE_INFINITY) si p n’est pas associé au point.
     * @param p
     */
    public double distance(Point p)
    {                    
        if( this.destinations.containsKey(p) )
            return this.destinations.get(p).getDistance();
        else            
            return Double.POSITIVE_INFINITY;
    }
    
    public int nbRoutes()
    {
        return this.destinations.size();
    }
    
    
    /**
     * Renvoie une référence sur le point le plus proche dans l'ensemble points
     * @param clients l'ensemble de clients, pouvant contenir l'instance utilisant la méthode.
     * @param h le tableau de hashcodes permettant de vérifier si le client le plus proche n'est pas déjà utilisé
     * @return une référence sur le point le plus proche, ou null si clients  vaut null, ou this s'il y a aucun autre point dans l'ensemble points.
     */
    public Client getClosestClient( Set<Client> clients, HashClientsUtilises h)
    {
        if( clients == null || h == null)
        {
            return null;
        }
        double distanceMin = Double.POSITIVE_INFINITY;
        Client closestOne = null;
        for(Client c : clients)
        {
            if( !h.ClientDejaUtilise(c) )
            {
                double distanceActuelle = this.distance(c);
                if( distanceActuelle != 0 && distanceActuelle < distanceMin )
                {                
                    distanceMin = distanceActuelle;
                    closestOne = c;
                }
            }
        }
        
        return closestOne;
    }
    
    

    @Override
    public String toString() {
        
        String sReturn = new String("Point {" + "id=" + id + ", abscisse=" + abscisse + ", ordonnee=" + ordonnee + "}");
        /*
        if( this.destinations.size() == 0 )
        {
            sReturn += "}";
            return sReturn;
        }

        sReturn += ",\n";
            
        for(Map.Entry<Point,Route> entry : this.destinations.entrySet())
        {
            Point pDest = entry.getKey();
            Route rAsso = entry.getValue();
            
            sReturn += "Point destination{" + "id=" + pDest.id + ", abscisse=" + pDest.abscisse + ", ordonnee=" + pDest.ordonnee + "}, ";
            sReturn += "Distance de la route associée : " + rAsso.getDistance();
            sReturn += "\n";
            
        }
        sReturn += "}";
        */
        
        return sReturn;
        
    }
    
    public static void main(String[] args) {
        /*Point p1 = new Point(3.14,2.35);
        Point p2 = new Point( -5.1, 2.5);
        
        System.out.println(p1);
        System.out.println(p2);*/
    }
    
    
    
    
    
    
}

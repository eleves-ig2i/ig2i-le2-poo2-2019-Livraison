/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reseau;

import java.math.*;
import java.util.Objects;

/**
 *
 * @author user
 */
public class Route {
    private Point origine;
    private Point destination;
    private double distance;
    
    /**
     * Constructeur par données de la classe Route.
     * Si un paramètre vaut null, le point associé a pour coordonnées (0,0).
     */
    public Route(Point depart, Point arrivee)
    {
        this.origine = depart;
        this.destination = arrivee;
        
        if( depart == null || arrivee == null )
            distance = -1;
        else
            distance = this._distance();
    }

    /**
     * Passage par adresse d'un point, que l'on ne pourra pas modifier !
     * @return 
     */
    public Point getDepart() {
        return origine;
    }

    /**
     * Passage par adresse d'un point, que l'on ne pourra pas modifier !
     * @return 
     */
    public Point getArrivee() {
        return destination;
    }
    
    /**
     * Calcule la distance euclidienne de la route avec la formule sqrt( différenceAbscisses² + differenceOrdonnees² )
     * @return 
     */
    private double _distance()
    {
        double dx = this.destination.x() - this.origine.x();
        double dy = this.destination.y() - this.origine.y();
        return Math.sqrt(  dx*dx + dy*dy  );
    }

    
    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Route{" + "origine=" + origine + ", destination=" + destination + ", distance=" + distance + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.origine);
        hash = 97 * hash + Objects.hashCode(this.destination);
        return hash;
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
        final Route other = (Route) obj;
        if (!Objects.equals(this.origine, other.origine)) {
            return false;
        }
        if (!Objects.equals(this.destination, other.destination)) {
            return false;
        }
        return true;
    }

    
   
    
    public static void main(String[] args) 
    {
        /*
        Point p1 = new Point(3.14,2.35);
        Point p2 = new Point( -5.1, 2.5);
        Route r1 = new Route(p1,p2);
        Route r2 = new Route(null,p1);
        
        System.out.println(r1);
        System.out.println(r2);
        */
    }
    
    
    
    
    

    
    
    
}

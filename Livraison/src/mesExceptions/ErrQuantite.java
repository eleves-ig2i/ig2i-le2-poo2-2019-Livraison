/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesExceptions;

/**
 *
 * @author user
 */
public class ErrQuantite extends Exception {

    private int quantite;
    
    public ErrQuantite(int quantite) {
        super("Quantité invalide (" + quantite + ")");
        this.quantite = quantite;
       
    }

    public int getQuantite() {
        return quantite;
    }

    @Override
    public String toString() {
        return "ErrQuantite{" + "quantite=" + quantite + '}';
    }
    
    
    
    
}

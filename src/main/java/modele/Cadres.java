package modele;

import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;

import java.util.ArrayList;

@Entity
@NamedQueries(
        {
                @NamedQuery(name = "Cadres.findAll", query = "SELECT p FROM Cadres p"),
                @NamedQuery(name = "Cadres.findByNumCadre", query = "SELECT p FROM Cadres p WHERE p.numCadre = :id")
        }
)
public class Cadres extends Personnels {

    /** Numéro d'identification du cadre*/
    @Column(name = "IDCADRE")
    private int numCadre;

    /** Liste des plannings du cadre*/
    @OneToMany(mappedBy = "cadreResponsable")
    private ArrayList<Plannings> plannings;

    /** Constructeur par défaut de la classe Cadres*/
    public Cadres() {
        super();
        this.numCadre = 0;
    }

    /** Constructeur de la classe Cadres
     * @param nom Nom du cadre
     * @param prenom Prénom du cadre
     * @param dateDeNaissance Date de naissance du cadre
     * @param login Login du cadre
     * @param motDePasse Mot de passe du cadre
     * @param adresse Adresse du cadre
     * @param id Numéro d'identification du cadre
     */
    public Cadres(String nom, String prenom, String dateDeNaissance, String login, String motDePasse, String adresse,int id) {
        super(nom, prenom,dateDeNaissance,login,motDePasse,adresse);
        if (id >= 0)
            this.numCadre = id;
    }

    /** Getter de l'attribut numCadre
     * @return int
     */
    public int getNumIdentification() {
        return numCadre;
    }
}

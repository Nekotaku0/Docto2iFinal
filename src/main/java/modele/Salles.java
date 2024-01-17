package modele;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries(
        {
                @jakarta.persistence.NamedQuery(name = "Salles.findAll", query = "SELECT p FROM Salles p"),
                @jakarta.persistence.NamedQuery(name = "Salles.findByNumSalle", query = "SELECT p FROM Salles p WHERE p.numSalle = :numSalle"),
                @jakarta.persistence.NamedQuery(name = "Salles.findByNom", query = "SELECT p FROM Salles p WHERE p.nom = :nom"),
                @jakarta.persistence.NamedQuery(name = "Salles.findById", query = "SELECT p FROM Salles p WHERE p.id = :id"),
        }
)

public class Salles {

    /** Numéro d'identification de la salle*/
    @Id
    private int id;

    /** Numéro de la salle*/
    @Column(name = "NUMSALLE")
    private int numSalle;

    /** Nom de la salle*/
    @Column(name = "NOM")
    private String nom;

    /** Liste des rendez-vous de la salle*/
    @OneToMany(mappedBy = "salle")
    private List<Rdvs> rdvs;

    /** Liste des plannings de la salle*/
    @ManyToMany(mappedBy = "salleDispo")
    private List<Plannings> dispo;

    /** Constructeur par défaut de la classe Salles*/
    public Salles() {
        this.id = 0;
        this.numSalle = 0;
        this.nom = "Non Renseigné";
        this.rdvs = null;
        this.dispo = null;
    }

    /** Constructeur de la classe Salles
     * @param id Numéro d'identification de la salle
     * @param numSalle Numéro de la salle
     * @param nom Nom de la salle
     */
    public Salles(int id, int numSalle, String nom) {
       // si id est supérieur ou égal à 0, on affecte la valeur de id à l'attribut id, sinon on affecte 0
        if (id >= 0)
            this.id = id;
        else
            this.id = 0;
        // si numSalle est supérieur ou égal à 0, on affecte la valeur de numSalle à l'attribut numSalle, sinon on affecte 0
        if (numSalle >= 0)
            this.numSalle = numSalle;
        else
            this.numSalle = 0;
        // si nom est différent de null, on affecte la valeur de nom à l'attribut nom, sinon on affecte "Non Renseigné"
        if (nom != null)
            this.nom = nom;
        else
            this.nom = "Non Renseigné";

        this.rdvs = new ArrayList<>();
        this.dispo = new ArrayList<>();

    }

    /** Getter du nom de la salle
     * @return String
     */
    public String getNom() {
        return nom;
    }

    /** Getter de la liste des disponibilités de la salle
     * @return List<Rdvs>
     */
    public List<Plannings> getDispo() {
        return dispo;
    }

    /** Getter de l'attribut id
     * @return int
     */
    public int getId() {
        return id;
    }

    /** Méthode toString de la classe Salles
     * @return String
     */
    @Override
    public String toString() {
        return numSalle + " - " + nom;
    }
}

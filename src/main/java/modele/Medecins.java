package modele;

import jakarta.persistence.*;

import java.awt.geom.Dimension2D;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;


@NamedQueries(
        {
                @NamedQuery(name = "getMedecinByPersonnelId", query = "SELECT m FROM Medecins m WHERE m.id = :id"),
        }
)

@Entity
public class Medecins extends Personnels {

    /** Numéro d'identification du médecin*/
    @Column(name = "NUMPROSANTE")
    private int numProSante;
    /** Spécialisation du médecin*/
    @Column (name = "SPECIALISATION")
    private boolean specialisation;
    /** Liste des plannings du médecin*/
    @ManyToMany (mappedBy = "medecinDispo")
    private List<Plannings> dispo;

    /** Constructeur par défaut de la classe Medecins*/
    public Medecins() {
        super();
        this.numProSante = 0;
        this.specialisation = false;
        this.dispo = null;
    }

    /** Constructeur de la classe Medecins
     * @param nom Nom du médecin
     * @param prenom Prénom du médecin
     * @param dateDeNaissance Date de naissance du médecin
     * @param login Login du médecin
     * @param motDePasse Mot de passe du médecin
     * @param adresse Adresse du médecin
     * @param specialisation Spécialisation du médecin
     * @param numProSante Numéro de professionnel de santé du médecin
     */
    public Medecins(String nom, String prenom, String dateDeNaissance, String login, String motDePasse, String adresse, boolean specialisation,int numProSante) {
        super(nom, prenom, dateDeNaissance, login, motDePasse, adresse);
        this.specialisation = specialisation;
        if (numProSante >= 0)
            this.numProSante = numProSante;
        this.dispo = new ArrayList<>();

    }

    /** Getter de l'attribut numProSante
     * @return int
     */
    public int getNumProSante() {
        return numProSante;
    }

    /** Getter de l'attribut specialisation
     * @return boolean
     */
    public boolean getSpecialisation() {
        return specialisation;
    }

    /** Méthode toString de la classe Medecins
     * @return String
     */
    @Override
    public String toString() {
        return this.getNom() + " " + this.getPrenom();
    }

    /** Getter de l'attribut dispo
     * @return Collection<Plannings>
     */
    public Collection<Plannings> getPlanningDispo() {
        return dispo;
    }

}

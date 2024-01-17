package modele;

import jakarta.persistence.*;

import java.util.Date;


@Table(uniqueConstraints=
    @UniqueConstraint(columnNames = {"NOM", "PRENOM"})
)

@MappedSuperclass
public abstract class Personne {
    /** Nom de la personne*/
    @Column(name = "NOM",
            length = 60,
            nullable = false
    )
    private String nom;

    /** Prénom de la personne*/
    @Column(name = "PRENOM",
            length = 60,
            nullable = false
    )
    private String prenom;

    /** Date de naissance de la personne*/
    @Column (name = "DATEDENAISSANCE")
    private String dateDeNaissance;

    /** Constructeur par défaut de la classe Personne*/
    public Personne() {
        this.dateDeNaissance = "dateDeNaissance";
        this.nom = "nom";
        this.prenom = "prenom";
    }

    /** Constructeur de la classe Personne
     * @param nom Nom de la personne
     * @param prenom Prénom de la personne
     * @param dateDeNaissance Date de naissance de la personne
     */
    public Personne(String nom, String prenom, String dateDeNaissance) {
        if (nom != null )
            this.nom = nom;
        if (prenom != null )
            this.prenom = prenom;
        if (dateDeNaissance != null )
            this.dateDeNaissance = dateDeNaissance;
    }

    /** Getter de l'attribut nom
     * @return String
     */
    public String getNom() {
        return nom;
    }

    /** Getter de l'attribut prenom
     * @return String
     */
    public String getPrenom() {
        return prenom;
    }

    /** Méthode toString de la classe Personne
     * @return String
     */
    @Override
    public String toString() {
        return "Personne{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateDeNaissance='" + dateDeNaissance + '\'' +
                '}';
    }
}

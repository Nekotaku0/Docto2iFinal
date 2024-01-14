package modele;

import jakarta.persistence.*;

import java.util.Date;


@Table(uniqueConstraints=
    @UniqueConstraint(columnNames = {"NOM", "PRENOM"})
)

@MappedSuperclass
public abstract class Personne {
    @Column(name = "NOM",
            length = 60,
            nullable = false
    )
    private String nom;

    @Column(name = "PRENOM",
            length = 60,
            nullable = false
    )
    private String prenom;

    @Column (name = "DATEDENAISSANCE")
    private String dateDeNaissance;

    public Personne() {
        this.dateDeNaissance = "dateDeNaissance";
        this.nom = "nom";
        this.prenom = "prenom";
    }

    public Personne(String nom, String prenom, String dateDeNaissance) {
        if (nom != null )
            this.nom = nom;
        if (prenom != null )
            this.prenom = prenom;
        if (dateDeNaissance != null )
            this.dateDeNaissance = dateDeNaissance;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public String toString() {
        return "Personne{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateDeNaissance='" + dateDeNaissance + '\'' +
                '}';
    }
}

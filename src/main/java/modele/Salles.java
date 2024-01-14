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

    @Id
    private int id;

    @Column(name = "NUMSALLE")
    private int numSalle;

    @Column(name = "NOM")
    private String nom;

    @OneToMany(mappedBy = "salle")
    private List<Rdvs> rdvs;

    @ManyToMany(mappedBy = "salleDispo")
    private List<Plannings> dispo;

    public Salles() {
        this.id = 0;
        this.numSalle = 0;
        this.nom = "Non Renseigné";
        this.rdvs = null;
        this.dispo = null;
    }

    public Salles(int id, int numSalle, String nom) {
        if (id >= 0)
            this.id = id;
        else
            this.id = 0;
        if (numSalle >= 0)
            this.numSalle = numSalle;
        else
            this.numSalle = 0;
        if (nom != null)
            this.nom = nom;
        else
            this.nom = "Non Renseigné";

        this.rdvs = new ArrayList<>();
        this.dispo = new ArrayList<>();

    }

    public String getNom() {
        return nom;
    }

    public List<Plannings> getDispo() {
        return dispo;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return numSalle + " - " + nom;
    }
}

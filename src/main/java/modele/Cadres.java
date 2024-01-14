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

    @Column(name = "IDCADRE")
    private int numCadre;

    @OneToMany(mappedBy = "cadreResponsable")
    private ArrayList<Plannings> plannings;


    public Cadres() {
        super();
        this.numCadre = 0;
    }

    public Cadres(String nom, String prenom, String dateDeNaissance, String login, String motDePasse, String adresse,int id) {
        super(nom, prenom,dateDeNaissance,login,motDePasse,adresse);
        if (id >= 0)
            this.numCadre = id;
    }

    public int getNumIdentification() {
        return numCadre;
    }
}

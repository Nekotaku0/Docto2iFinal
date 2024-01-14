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

    @Column(name = "NUMPROSANTE")
    private int numProSante;
    @Column (name = "SPECIALISATION")
    private boolean specialisation;
    @ManyToMany (mappedBy = "medecinDispo")
    private List<Plannings> dispo;

    public Medecins() {
        super();
        this.numProSante = 0;
        this.specialisation = false;
        this.dispo = null;
    }

    public Medecins(String nom, String prenom, String dateDeNaissance, String login, String motDePasse, String adresse, boolean specialisation,int numProSante) {
        super(nom, prenom, dateDeNaissance, login, motDePasse, adresse);
        this.specialisation = specialisation;
        if (numProSante >= 0)
            this.numProSante = numProSante;
        this.dispo = new ArrayList<>();

    }

    public int getNumProSante() {
        return numProSante;
    }

    public boolean getSpecialisation() {
        return specialisation;
    }

    @Override
    public String toString() {
        return this.getNom() + " " + this.getPrenom();
    }

    public Collection<Plannings> getPlanningDispo() {
        return dispo;
    }

}

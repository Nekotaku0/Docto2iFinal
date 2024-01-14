package modele;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@NamedQueries(
        {
                @NamedQuery(name = "Patients.findAll", query = "SELECT p FROM Patients p"),
                @NamedQuery(name = "Patients.findByNumIdentification", query = "SELECT p FROM Patients p WHERE p.numIdentification = :numIdentification")
        }
)
public class Patients extends Personne {

    @Id
    @Column(name = "NUMIDENTIFICATION")
    private String numIdentification;

    @Column(name = "DEMANDESPE")
    private Boolean demandeSpe;

    public Patients() {
        super();
    }

    public Patients(String nom, String prenom, String dateDeNaissance, String numIdentification,Boolean demandeSpe) {
        super(nom, prenom, dateDeNaissance);
        this.numIdentification = numIdentification;
        this.demandeSpe = demandeSpe;

    }

    @Override
    public String toString() {
        return "Patients{" +
                "numIdentification='" + numIdentification + '\'' +
                '}';
    }
}

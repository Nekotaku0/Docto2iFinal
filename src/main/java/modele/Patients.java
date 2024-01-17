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

    /** Numéro d'identification du patient*/
    @Id
    @Column(name = "NUMIDENTIFICATION")
    private String numIdentification;

    /** Demande spéciale du patient*/
    @Column(name = "DEMANDESPE")
    private Boolean demandeSpe;

    /** Constructeur par défaut de la classe Patients*/
    public Patients() {
        super();
    }

    /** Constructeur de la classe Patients
     * @param nom Nom du patient
     * @param prenom Prénom du patient
     * @param dateDeNaissance Date de naissance du patient
     * @param numIdentification Numéro d'identification du patient
     * @param demandeSpe Demande spéciale du patient
     */
    public Patients(String nom, String prenom, String dateDeNaissance, String numIdentification,Boolean demandeSpe) {
        super(nom, prenom, dateDeNaissance);
        this.numIdentification = numIdentification;
        this.demandeSpe = demandeSpe;

    }

    /** Getter de l'attribut numIdentification
     * @return String
     */
    @Override
    public String toString() {
        return "Patients{" +
                "numIdentification='" + numIdentification + '\'' +
                '}';
    }
}

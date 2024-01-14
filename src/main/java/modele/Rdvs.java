package modele;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

@Entity
public class Rdvs {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private int heure;
    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Medecins medecin;
    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Salles salle;
    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Patients patient;
    @ManyToOne  (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "PLANNINGNO")
    private Plannings appartientA;
    private boolean demandeSpeciale;

    public Rdvs() {
        this.heure = 12;
        this.salle = null;
        this.patient = null;
        this.medecin = null;
    }

    public Rdvs(int heure, Salles salle, Patients patient, Medecins medecin, Boolean demandeSpeciale) {
        if (heure > 8  || heure < 20)
            this.heure = heure;
        else
            this.heure = 12;
        if (salle != null)
            this.salle = salle;
        else
            this.salle = null;
        if (patient != null)
            this.patient = patient;
        else
            this.patient = null;
        if (medecin != null)
            this.medecin = medecin;
        else
            this.medecin = null;

        this.demandeSpeciale = demandeSpeciale;
    }

    public int getHeure() {
        return heure;
    }

    public Salles getSalle() {
        return salle;
    }

    public Patients getPatient() {
        return patient;
    }

    public Medecins getMedecin() {
        return medecin;
    }

    public boolean isDemandeSpeciale() { return demandeSpeciale;}

    public void setHeure(int heure) {
        this.heure = heure;
    }

    public void setMedecin(Medecins medecin) {
        this.medecin = medecin;
    }

    public void setSalle(Salles salle) {
        this.salle = salle;
    }

    @Override
    public String toString() {
        return " heure=" + heure + ", medecin=" + medecin + ", patient=" + patient + ", demandeSpeciale=" + demandeSpeciale;
    }
}

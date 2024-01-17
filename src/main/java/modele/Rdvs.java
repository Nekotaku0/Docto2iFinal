package modele;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

@Entity
public class Rdvs {

    /** Id du rdv */
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    /** Heure du rdv */
    private int heure;
    /** Medecin du rdv */
    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Medecins medecin;
    /** Salle du rdv */
    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Salles salle;
    /** Patient du rdv */
    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Patients patient;
    /** Planning auquel appartient le rdv */
    @ManyToOne  (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "PLANNINGNO")
    private Plannings appartientA;
    /** Demande spéciale du rdv */
    private boolean demandeSpeciale;

    /** Constructeur par défaut */
    public Rdvs() {
        this.heure = 12;
        this.salle = null;
        this.patient = null;
        this.medecin = null;
    }

    /** Constructeur de la classe Rdvs
     * @param heure Heure du rdv
     * @param salle Salle du rdv
     * @param patient Patient du rdv
     * @param medecin Medecin du rdv
     * @param demandeSpeciale Demande spéciale du rdv
     */
    public Rdvs(int heure, Salles salle, Patients patient, Medecins medecin, Boolean demandeSpeciale) {
        // Si l'heure est renseignée, on l'ajoute, sinon on met 12
        if (heure > 8  || heure < 20)
            this.heure = heure;
        else
            this.heure = 12;
        // Si la salle est renseignée, on l'ajoute, sinon on met null
        if (salle != null)
            this.salle = salle;
        else
            this.salle = null;
        // Si le patient est renseigné, on l'ajoute, sinon on met null
        if (patient != null)
            this.patient = patient;
        else
            this.patient = null;
        // Si le medecin est renseigné, on l'ajoute, sinon on met null
        if (medecin != null)
            this.medecin = medecin;
        else
            this.medecin = null;
        // Si la demande spéciale est renseignée, on l'ajoute, sinon on met false
        this.demandeSpeciale = demandeSpeciale;
    }

    /** Getter de l'heure du rdv
     * @return int
     */
    public int getHeure() {
        return heure;
    }

    /** Getter de la salle du rdv
     * @return Salles
     */
    public Salles getSalle() {
        return salle;
    }

    /** Getter du patient du rdv
     * @return Patients
     */
    public Patients getPatient() {
        return patient;
    }

    /** Getter du medecin du rdv
     * @return Medecins
     */
    public Medecins getMedecin() {
        return medecin;
    }

    /** Getter de la demande spéciale du rdv
     * @return boolean
     */
    public boolean isDemandeSpeciale() { return demandeSpeciale;}

    /** Setter de l'heure du rdv
     * @param heure Heure du rdv
     */
    public void setHeure(int heure) {
        this.heure = heure;
    }

    /** Setter du médécin du rdv
     * @param medecin - Medecin du rdv
     */
    public void setMedecin(Medecins medecin) {
        this.medecin = medecin;
    }

    /** Setter de la salle du rdv
     * @param salle - Salle du rdv
     */
    public void setSalle(Salles salle) {
        this.salle = salle;
    }

    /** Méthode toString de la classe Rdvs
     * @return String
     */
    @Override
    public String toString() {
        return " heure=" + heure + ", medecin=" + medecin + ", patient=" + patient + ", demandeSpeciale=" + demandeSpeciale;
    }
}

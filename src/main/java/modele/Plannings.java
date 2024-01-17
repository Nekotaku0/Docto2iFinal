package modele;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries(
        {
                @NamedQuery(name = "findByDate" , query = "SELECT p FROM Plannings p WHERE p.date = :date"),
                @NamedQuery(name = "getMedecinsDispo" , query = "SELECT m FROM Plannings p JOIN p.medecinDispo m WHERE p.date = :date"),
                @NamedQuery(name = "getSallesDispo" , query = "SELECT s FROM Plannings p JOIN p.salleDispo s WHERE p.date = :date"),
                @NamedQuery(name = "getPlanningByDateAndMedecin", query = "SELECT p FROM Plannings p JOIN p.medecinDispo m WHERE p.date = :date AND m.id = :id"),
                @NamedQuery(name = "getNbMedSpécialisé", query = "SELECT COUNT(m) FROM Plannings p JOIN p.medecinDispo m WHERE p.date = :date AND m.specialisation = true"),
                @NamedQuery(name = "getNbRdvSpécialisé", query = "SELECT COUNT(r) FROM Plannings p JOIN p.rdvs r WHERE p.date = :date AND r.demandeSpeciale = true"),
                @NamedQuery(name = "getAllSpecializedMed", query = "SELECT m FROM Plannings p JOIN p.medecinDispo m WHERE p.date = :date AND m.specialisation = true"),
                @NamedQuery(name = "getCadre", query = "SELECT p.cadreResponsable FROM Plannings p WHERE p.date = :date"),
        }
)
public class Plannings {

    /** Numéro d'identification du planning*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLANNINGNO")
    private int id;

    /** Date du planning*/
    private String date;

    /** Cadre responsable du planning*/
    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "IDCADRE")
    private Cadres cadreResponsable;

    /** Liste des rdvs du planning*/
    @OneToMany (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Rdvs> rdvs;

    /** Liste des médecins disponibles pour le planning*/
    @ManyToMany (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(
            name = "dispoMedecins",
            joinColumns = @JoinColumn(name = "planning_id"),
            inverseJoinColumns = @JoinColumn(name = "medecin_id"))
    private List<Medecins> medecinDispo;

    /** Liste des salles disponibles pour le planning*/
    @ManyToMany (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(
            name = "dispoSalles",
            joinColumns = @JoinColumn(name = "planning_id"),
            inverseJoinColumns = @JoinColumn(name = "salle_id"))

    private List<Salles> salleDispo;


    /** Constructeur par défaut*/
    public Plannings() {
        this.date = "Non Renseigné";
        this.cadreResponsable = null;
        this.rdvs = null;
        this.medecinDispo = null;
        this.salleDispo = null;
    }

    /** Constructeur de la classe Plannings
     * @param date Date du planning
     * @param cadre Cadre responsable du planning
     * @param rdv Liste des rdvs du planning
     * @param medecinDispo Liste des médecins disponibles pour le planning
     * @param salleDispo Liste des salles disponibles pour le planning
     */
    public Plannings(String date, Cadres cadre, List<Rdvs> rdv, List<Medecins> medecinDispo, List<Salles> salleDispo) {

        // si la date est renseignée, on l'ajoute, sinon on met "Non Renseigné"
        if (date != null)
            this.date = date;
        else
            this.date = "Non Renseigné";

        // si le cadre est renseigné, on l'ajoute, sinon on met null
        if (cadre != null)
            this. cadreResponsable = cadre;
        else
            this. cadreResponsable = null;

        // Si la liste des rdvs est renseignée, on l'ajoute, sinon on met null
        this.rdvs = new ArrayList<>();
        if (rdv != null)
            this.rdvs = rdv;
        else
            this.rdvs = null;

        // Si la liste des médecins disponibles est renseignée, on l'ajoute, sinon on met null
        this.medecinDispo = new ArrayList<>();
        if (medecinDispo != null)
            this.medecinDispo = medecinDispo;
        else
            this.medecinDispo = null;

        // Si la liste des salles disponibles est renseignée, on l'ajoute, sinon on met null
        this.salleDispo = new ArrayList<>();
        if (salleDispo != null)
            this.salleDispo = salleDispo;
        else
            this.salleDispo = null;
    }

    /** Getter de la liste des salles disponibles
     * @return List<Salles>
     */
    public List<Salles> getSalleDispo() {
        return salleDispo;
    }

    /** méthode toString de la classe Plannings
     * @return String
     */
    @Override
    public String toString() {
        return "Plannings{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", cadreResponsable=" + cadreResponsable +
                ", rdv=" + rdvs +
                ", medecinDispo=" + medecinDispo +
                ", salleDispo=" + salleDispo +
                '}';
    }

    /** Getter de la liste des rdvs
     * @return List<Rdvs>
     */
    public List<Rdvs> getRdv() {
        return rdvs;
    }

    /** Getter de la liste des médecins disponibles
     * @return List<Medecins>
     */
    public List<Medecins> getMedecinDispo() {
        return medecinDispo;
    }



}

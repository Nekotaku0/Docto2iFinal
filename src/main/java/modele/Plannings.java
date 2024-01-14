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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLANNINGNO")
    private int id;

    private String date;

    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "IDCADRE")
    private Cadres cadreResponsable;

    @OneToMany (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Rdvs> rdvs;

    @ManyToMany (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(
            name = "dispoMedecins",
            joinColumns = @JoinColumn(name = "planning_id"),
            inverseJoinColumns = @JoinColumn(name = "medecin_id"))
    private List<Medecins> medecinDispo;

    @ManyToMany (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(
            name = "dispoSalles",
            joinColumns = @JoinColumn(name = "planning_id"),
            inverseJoinColumns = @JoinColumn(name = "salle_id"))

    private List<Salles> salleDispo;


    public Plannings() {
        this.date = "Non Renseigné";
        this.cadreResponsable = null;
        this.rdvs = null;
        this.medecinDispo = null;
        this.salleDispo = null;
    }

    public Plannings(String date, Cadres cadre, List<Rdvs> rdv, List<Medecins> medecinDispo, List<Salles> salleDispo) {


        if (date != null)
            this.date = date;
        else
            this.date = "Non Renseigné";

        if (cadre != null)
            this. cadreResponsable = cadre;
        else
            this. cadreResponsable = null;

        this.rdvs = new ArrayList<>();
        if (rdv != null)
            this.rdvs = rdv;
        else
            this.rdvs = null;

        this.medecinDispo = new ArrayList<>();
        if (medecinDispo != null)
            this.medecinDispo = medecinDispo;
        else
            this.medecinDispo = null;

        this.salleDispo = new ArrayList<>();
        if (salleDispo != null)
            this.salleDispo = salleDispo;
        else
            this.salleDispo = null;
    }

    public List<Salles> getSalleDispo() {
        return salleDispo;
    }

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

    public List<Rdvs> getRdv() {
        return rdvs;
    }

    public List<Medecins> getMedecinDispo() {
        return medecinDispo;
    }



}

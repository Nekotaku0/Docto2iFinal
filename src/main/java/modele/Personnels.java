package modele;

import jakarta.persistence.*;
import modele.Personne;



@Table(uniqueConstraints=
    @UniqueConstraint(columnNames = {"LOGIN"})
)
@NamedQueries(
        {
                @NamedQuery(name = "Personnels.findAll", query = "SELECT p FROM Personnels p"),
                @NamedQuery(name = "Personnels.findByLoginAndMdp", query = "SELECT p FROM Personnels p WHERE p.login = :login AND p.motDePasse = :mdp"),
        }
)

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public abstract class Personnels extends Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "MOTDEPASSE")
    private String motDePasse;

    @Column(name = "ADRESSE")
    private String Adresse;


    public Personnels(String login, String motDePasse, String adresse) {
        this.login = login;
        this.motDePasse = motDePasse;
        Adresse = adresse;
    }

    public Personnels(String nom, String prenom, String dateDeNaissance, String login, String motDePasse, String adresse) {
        super(nom, prenom, dateDeNaissance);
        this.login = login;
        this.motDePasse = motDePasse;
        Adresse = adresse;
    }

    public Personnels() {
        super();
        this.login = "login";
        this.motDePasse = "motDePasse";
        Adresse = "adresse";
    }

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }


}

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

    /** id du personnel*/
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;

    /** Login du personnel*/
    @Column(name = "LOGIN")
    private String login;

    /** Mot de passe du personnel*/
    @Column(name = "MOTDEPASSE")
    private String motDePasse;

    /** Adresse du personnel*/
    @Column(name = "ADRESSE")
    private String Adresse;


    /** Constructeur de la classe Personnels
     * @param login Login du personnel
     * @param motDePasse Mot de passe du personnel
     * @param adresse Adresse du personnel
     */
    public Personnels(String login, String motDePasse, String adresse) {
        this.login = login;
        this.motDePasse = motDePasse;
        Adresse = adresse;
    }

    /** Constructeur de la classe Personnels
     * @param nom Nom du personnel
     * @param prenom Prénom du personnel
     * @param dateDeNaissance Date de naissance du personnel
     * @param login Login du personnel
     * @param motDePasse Mot de passe du personnel
     * @param adresse Adresse du personnel
     */
    public Personnels(String nom, String prenom, String dateDeNaissance, String login, String motDePasse, String adresse) {
        super(nom, prenom, dateDeNaissance);
        this.login = login;
        this.motDePasse = motDePasse;
        Adresse = adresse;
    }

    /** Constructeur par défaut de la classe Personnels*/
    public Personnels() {
        super();
        this.login = "login";
        this.motDePasse = "motDePasse";
        Adresse = "adresse";
    }

    /** Getter de l'attribut login
     * @return String
     */
    public String getLogin() {
        return login;
    }

    /** Getter de l'attribut id
     * @return int
     */
    public int getId() {
        return id;
    }


}

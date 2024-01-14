package vueControle;

// Import modele
import Utilities.DatabaseGenerator;
import modele.Personnels;
import modele.Cadres;
import modele.Medecins;

import jakarta.persistence.*;

import javax.swing.*;
import java.awt.*;

public class Connexion extends JDialog {
    // Objets graphiques
    private JPanel contentPane;

    /** Bouton de connexion */
    private JButton connexionBout;

    /** Champs de saisie du login */
    private JTextField loginField;

    /** Champs de saisie du mot de passe */
    private JPasswordField passwordField;
    private EntityManager manager;

    public Connexion() {

        // Connexion à la base de données et génération de celle-ci si elle n'existe pas
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Docto2iPU");
        manager = emf.createEntityManager();
        generateDatabase();

        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);
        setModal(true);

        //modifie la taille de la fenêtre (page entière)
        Toolkit outil = getToolkit();
        this.setSize(outil.getScreenSize());
        setLocationRelativeTo(null);
        setTitle("Connexion");

        //Ajout du logo DOCTO2I co
        ImageIcon image = new ImageIcon("src\\main\\resources\\Images\\DOCTO2I.png");

        //Création de l'icone
        Image img = image.getImage();
        Image imgScale = img.getScaledInstance(550, 250, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imgScale);

        //affiche l'icone
        JLabel label = new JLabel(scaledIcon);
        //x = centre de la fenêtre - moitié de la largeur de l'icone
        int xicone = (outil.getScreenSize().width - scaledIcon.getIconWidth()) / 2;
        label.setBounds(xicone, 0, 550, 250);
        //ajoute l'icone à la fenêtre
        contentPane.add(label);

        //etiquette login en dessous de l'icone
        JLabel label2 = new JLabel("Identifiant");
        label2.setBounds(xicone, 250, 550, 50);
        label2.setFont(new Font("Arial", Font.BOLD, 40));
        label2.setForeground(new Color(0x376795));
        label2.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(label2);

        //login
        loginField = new JTextField();
        int fieldWidth = 300;
        int fieldHeight = 50;
        //en dessous de l'etiqueete
        loginField.setBounds(xicone+125, 300, fieldWidth, fieldHeight);
        loginField.setFont(new Font("Arial", Font.BOLD, 30));
        contentPane.add(loginField);

        //etiquette password en dessous de login
        JLabel label3 = new JLabel("Mot de passe");
        label3.setBounds(xicone, 400, 550, 50);
        label3.setFont(new Font("Arial", Font.BOLD, 40));
        label3.setForeground(new Color(0x376795));
        label3.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(label3);

        //password
        passwordField = new JPasswordField();
        int fieldWidthpass = 300;
        int fieldHeightpass = 50;
        //en dessous de l'etiqueete
        passwordField.setBounds(xicone+125, 450, fieldWidthpass, fieldHeightpass);
        passwordField.setFont(new Font("Arial", Font.BOLD, 40));
        contentPane.add(passwordField);

        //bouton OK
        connexionBout = new JButton("Se connecter");
        int buttonWidth = 300;
        int buttonHeight = 50;
        //en dessous de password
        connexionBout.setBounds(xicone+125, 550, buttonWidth, buttonHeight);
        connexionBout.setFont(new Font("Arial", Font.BOLD, 30));
        contentPane.add(connexionBout);

        getRootPane().setDefaultButton(connexionBout);
        //listener bouton OK
        connexionBout.addActionListener(e -> onOK());

        this.setVisible(true);

        //kill the program when the window is closed
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }


    private void generateDatabase() {
        final EntityTransaction et = manager.getTransaction();
        try {
            et.begin();
            DatabaseGenerator db = new DatabaseGenerator(manager);
            et.commit();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données");
            ex.printStackTrace();
            System.out.println("exception " + ex);
            System.out.println("rollback");
            et.rollback();
        }
    }

    private void onOK() {

        //récupère login
        String login = loginField.getText();

        //récupère mdp
        String password = new String(passwordField.getPassword());

        Query query = manager.createNamedQuery("Personnels.findByLoginAndMdp");
        query.setParameter("login", login);
        query.setParameter("mdp", password);

        try {
            Personnels p = (Personnels) query.getSingleResult();
            dispose();

            if (p instanceof modele.Medecins) {
                Medecins m = (Medecins) p;
                VueMedecin vueMedecin = new VueMedecin(m,manager);
            } else {
                Cadres c = (Cadres) p;
                VueCadre vueCadre = new VueCadre(c,manager);
            }
        } catch (NoResultException e){
            JOptionPane.showMessageDialog(this, "Erreur de connexion");
            System.out.println(e.fillInStackTrace());
        }

    }

    //run the program
    public static void main(String[] args) {
        Connexion dialog = new Connexion();
    }
}
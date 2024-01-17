package vueControle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import modele.Medecins;
import modele.Plannings;
import org.hibernate.query.sqm.function.SelfRenderingOrderedSetAggregateFunctionSqlAstExpression;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VueMedecin extends VuePlanning {
    /** Panel principal */
    private JPanel contentPane;
    /** Bouton de connexion */
    private JButton buttonOK;
    /** Chaine de caractère avec le login */
    private String login;
    /** Champs de saisie du mot de passe */
    private EntityManager manager;

    /** Constructeur de la classe VueMedecin
     * @param med Médecin connecté
     * @param manager Entity manager
     */
    public VueMedecin(Medecins med, EntityManager manager) {
        this.manager = manager;
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);
        setModal(true);

        //modifie la taille de la fenêtre (page entière)
        Toolkit outil = getToolkit();
        this.setSize(outil.getScreenSize());

        //centre la fenêtre
        setLocationRelativeTo(null);

        //titre de la fenêtre
        setTitle("Planning " + med.getPrenom() + " " + med.getNom());

        //Ajout du logo DOCTO2I
        ImageIcon image = new ImageIcon("src\\main\\resources\\Images\\DOCTO2I.png");

        //Création de l'icone
        Image img = image.getImage();
        Image imgScale = img.getScaledInstance(275, 125, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imgScale);

        //affiche l'icone
        JLabel label = new JLabel(scaledIcon);

        //icone dans le cote gauche en haut
        label.setBounds(0, 40, 275, 125);

        //ajoute l'icone à la fenêtre
        contentPane.add(label);

        //affiche nom médecin en haut à droite avec un background noir
        JLabel label2 = new JLabel(med.getLogin());
        label2.setBounds(outil.getScreenSize().width-325, 50, 275, 125);
        label2.setFont(new Font("Arial", Font.BOLD, 40));
        label2.setForeground(new Color(0x376795));
        label2.setHorizontalAlignment(JLabel.CENTER);
        label2.setBackground(Color.BLACK); // DON'T WORK !!!
        contentPane.add(label2);

        //récupère la date du système dans une variable
        LocalDateTime date = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateDuJour = date.format(formatter);
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String dateDuJour2 = date.format(formatter2);
        dateDuJour2 = "2023/12/26";


        // Display the formatted date in the label
        JLabel label3 = new JLabel(dateDuJour);
        label3.setBounds((outil.getScreenSize().width / 2) - 125, 50, 250, 125);
        label3.setFont(new Font("Arial", Font.BOLD, 40));
        label3.setForeground(new Color(0x376795));
        label3.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(label3);

        //bouton de déconnexion
        JButton deconnexion = new JButton("Déconnexion");
        deconnexion.setBounds(outil.getScreenSize().width-275, outil.getScreenSize().height-175, 200, 75);
        deconnexion.setFont(new Font("Arial", Font.BOLD, 20));
        deconnexion.setForeground(Color.BLACK);
        deconnexion.setHorizontalAlignment(JLabel.CENTER);
        deconnexion.setBackground(Color.WHITE);
        deconnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
        contentPane.add(deconnexion);


         try {
            JTable tb = generatePlanningMedecin(med,dateDuJour2,manager);
            contentPane.add(tb);
         } catch (Exception e) {
             int width = outil.getScreenSize().width;
             int height = outil.getScreenSize().height;
             JLabel label4 = new JLabel("Vous n'avez pas de planning aujourd'hui");
             label4.setBounds((width / 2) - 125, height / 2, 400 , 125);
             label4.setFont(new Font("Arial", Font.BOLD, 20));
             label4.setForeground(new Color(0x376795));
             label4.setHorizontalAlignment(JLabel.CENTER);
             contentPane.add(label4);
         }

        //affiche la page
        setVisible(true);


        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /** Méthode de déconnexion
     * @return void
     */
    private void onOK() {
        //ferme la fenêtre
        manager.close();
        dispose();
        Connexion connexion = new Connexion();
    }

    /** méthode main de la classe VueMedecin
     * @param args Arguments
     */
    public static void main(String[] args) {
            VueMedecin vueMedecin = new VueMedecin(new Medecins(), null);
        }
}

package vueControle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import modele.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VueCadre extends VuePlanning {

    //objets graphiques
    /** Panel principal */
    private JPanel contentPane;
    /** Date du jour */
    String dateDuJour;
    /** Bouton de déconnexion */
    private JButton buttonOK;
    /** Tableau contenant le planning */
    private JTable planning;
    /** Liste des médecins */
    private JComboBox<Object> medecinsList;
    /** Liste des salles */
    private JComboBox<Object> sallesList;
    /** Liste des plannings */
    private  JComboBox<Object> listePlanning;
    /** Cadre connecté */
    private Cadres connectedCadre;
    /** Entity manager */
    private EntityManager manager;


    /** Constructeur de la classe VueCadre
     * @param c Cadre connecté
     * @param manager Entity manager
     */
    public VueCadre(Cadres c, EntityManager manager) {
        this.manager = manager;
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);
        setModal(true);

        // Vérifie si un cadre est connecté
        if (c == null) {
            System.out.println("Erreur : Cadre non connecté");
            System.exit(1);
        } else {
            this.connectedCadre = c;
        }

        //modifie la taille de la fenêtre (page entière)
        Toolkit outil = getToolkit();
        this.setSize(outil.getScreenSize());

        //centre la fenêtre
        setLocationRelativeTo(null);

        //titre de la fenêtre
        setTitle("Planning " + c.getPrenom() + " " + c.getNom());

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
        JLabel label2 = new JLabel(c.getLogin());
        label2.setBounds(outil.getScreenSize().width-325, 50, 275, 125);
        label2.setFont(new Font("Arial", Font.BOLD, 40));
        label2.setForeground(new Color(0x376795));
        label2.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(label2);

        //récupère la date du système dans une variable
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formattedDate = date.format(formatter);
        this.dateDuJour = date.format(formatter2);
        this.dateDuJour = "2023/12/26";

        // affiche la date en haut au milieu
        JLabel label3 = new JLabel(formattedDate);
        label3.setBounds((outil.getScreenSize().width / 2) - 125, 50, 250, 125);
        label3.setFont(new Font("Arial", Font.BOLD, 40));
        label3.setForeground(new Color(0x376795));
        label3.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(label3);

        // Vérifie si le cadre a un planning aujourd'hui
        if (connectedCadre != null && connectedCadre == manager.createNamedQuery("getCadre").setParameter("date", dateDuJour).getSingleResult()) {

            // Ajout d'une liste déroulante pour choisir le type de planning
            String[] planning = {"Global", "Médecin", "Salle"};
            listePlanning = createComboBox(planning, 10, 150, 200, 50);
            contentPane.add(listePlanning);

            listePlanning.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    contentPane.remove(VueCadre.this.planning);
                    if (medecinsList != null) contentPane.remove(medecinsList);
                    if (sallesList != null) contentPane.remove(sallesList);
                    medecinsList = null;
                    sallesList = null;

                    switch (listePlanning.getSelectedIndex()) {
                        case 0:
                            VueCadre.this.planning = generatePlanningGlobal(dateDuJour, manager);
                            contentPane.add(VueCadre.this.planning);
                            break;

                        case 1:
                            addMedListe();
                            medecinsList.setSelectedIndex(0);
                            break;
                        case 2:
                            addSalleListe();
                            sallesList.setSelectedIndex(0);
                            break;
                    }
                    VueCadre.this.listePlanning.setBounds(VueCadre.this.planning.getX(), 150, 200, 50);
                    if (medecinsList != null)
                        VueCadre.this.medecinsList.setBounds(VueCadre.this.listePlanning.getX() + VueCadre.this.listePlanning.getWidth(), VueCadre.this.listePlanning.getY(), 300, 50);
                    if (sallesList != null)
                        VueCadre.this.sallesList.setBounds(VueCadre.this.listePlanning.getX() + VueCadre.this.listePlanning.getWidth(), VueCadre.this.listePlanning.getY(), 300, 50);
                    contentPane.repaint();
                    contentPane.revalidate(); // SOLUTION PROVENANT DE la doc de IntelliJ
                }
            });
            this.planning = generatePlanningGlobal(dateDuJour, manager);
            contentPane.add(this.planning);
        }
        else {
            int width = outil.getScreenSize().width;
            int height = outil.getScreenSize().height;
            JLabel label4 = new JLabel("Vous n'avez pas de planning aujourd'hui");
            label4.setBounds((width / 2) - 125, height / 2, 400 , 125);
            label4.setFont(new Font("Arial", Font.BOLD, 20));
            label4.setForeground(new Color(0x376795));
            label4.setHorizontalAlignment(JLabel.CENTER);
            contentPane.add(label4);
        }


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

        //affiche la page
        setVisible(true);

        //ferme la fenêtre
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /** Méthode appelée lors de l'appui sur le bouton de déconnexion */
    private void onOK() {
        //ferme la fenêtre
        manager.close();
        dispose();
        Connexion connexion = new Connexion();
    }

    /** Méthode d'ajout d'une liste déroulante pour choisir le médecin */
    private void addMedListe(){
        Toolkit outil = getToolkit();
        // Ajout d'une liste déroulante pour choisir le médecin
        Query query = manager.createNamedQuery("getMedecinsDispo");
        query.setParameter("date", dateDuJour);
        Medecins[] resultats = new Medecins[query.getResultList().size()];

        // Récupération des médecins disponibles
        for (int i = 0; i < query.getResultList().size(); i++) resultats[i] = (Medecins) query.getResultList().get(i);

        medecinsList = createComboBox(resultats, listePlanning.getX() + listePlanning.getWidth(), listePlanning.getY(), 300, 50);
        medecinsList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Medecins med = (Medecins) medecinsList.getSelectedItem();
                contentPane.remove(VueCadre.this.planning);
                VueCadre.this.planning = generatePlanningMedecin(med, dateDuJour,VueCadre.this.manager);
                contentPane.add(VueCadre.this.planning);
                contentPane.repaint();
                contentPane.revalidate(); // SOLUTION PROVENANT DE la doc de IntelliJ
            }
        });
        contentPane.add(medecinsList);
    }

    /** Méthode d'ajout d'une liste déroulante pour choisir la salle */
    private void addSalleListe(){
        Toolkit outil = getToolkit();
        // Ajout d'une liste déroulante pour choisir le médecin
        Query query = manager.createNamedQuery("getSallesDispo");
        query.setParameter("date", dateDuJour);
        Salles[] resultats = new Salles[query.getResultList().size()];

        for (int i = 0; i < query.getResultList().size(); i++) resultats[i] = (Salles) query.getResultList().get(i);

        sallesList = createComboBox(resultats, listePlanning.getX() + listePlanning.getWidth(), listePlanning.getY(), 200, 50);

        sallesList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Salles s = (Salles) sallesList.getSelectedItem();
                contentPane.remove(VueCadre.this.planning);
                VueCadre.this.planning = generatePlanningSalle(s, dateDuJour,VueCadre.this.manager);
                contentPane.add(VueCadre.this.planning);
                contentPane.repaint();
                contentPane.revalidate(); // SOLUTION PROVENANT DE la doc de IntelliJ
            }
        });
        contentPane.add(sallesList);
    }

    /** Méthode main de la classe VueCadre
     * @param args String[]
     */
    public static void main(String[] args) {
        VueCadre vueMedecin = new VueCadre(new Cadres(), null);
    }
}

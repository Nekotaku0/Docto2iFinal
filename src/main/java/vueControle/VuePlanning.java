package vueControle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import modele.Medecins;
import modele.Plannings;
import modele.Salles;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Arrays;

public abstract class VuePlanning extends JDialog {

    public enum TypePlanning {
        GLOBAL,
        MEDECIN,
        SALLE
    }


    private EntityManager manager;

    public JTable generatePlanningGlobal(String date, EntityManager manager){

        this.manager = manager;
        Plannings planning = getPlanningByDate(date);
        int nbRdvMaxParCreneau = planning.getSalleDispo().size();

        String [][] data = new String[13][nbRdvMaxParCreneau + 1];
        data[0][0] = "Horaire";
        for (int i = 0; i < planning.getSalleDispo().size() ; i++) data[0][i + 1] = "Salle : " + planning.getSalleDispo().get(i).getNom();

        try {
            int nbRdv = planning.getRdv().size();
            planning.getRdv().sort((o1, o2) -> o1.getHeure() - o2.getHeure());

            for (int i = 1; i < 13; i++) {
                data[i][0] = i + 7 + "h";
                for (int j = 1; j <= nbRdvMaxParCreneau; j++)
                    data[i][j] = "-";
            }

            for (int j = 0; j < nbRdv; j++) {
                int heure = planning.getRdv().get(j).getHeure() - 8;
                int salle;
                for (int i = 1; i < nbRdvMaxParCreneau + 1 ; i++) {
                    if (planning.getRdv().get(j).getSalle() == planning.getSalleDispo().get(i - 1)) {
                        salle = i;
                        StringBuilder htmlBuilder = new StringBuilder();
                        htmlBuilder.append("<html>");
                        htmlBuilder.append(
                                "<h4>" + planning.getRdv().get(j).getMedecin().getNom() + " " +
                                        planning.getRdv().get(j).getMedecin().getPrenom() +
                                        "</h4>");
                        htmlBuilder.append(
                                "<h4>" + planning.getRdv().get(j).getPatient().getNom() + " " +
                                        planning.getRdv().get(j).getPatient().getPrenom() +
                                        "</h4>");
                        htmlBuilder.append("</html>");
                        data[heure + 1][salle] = htmlBuilder.toString();
                    }
                }

            }

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            for (int i = 1; i < 13; i++) {
                data[i][0] = i + 7 + "h";
                data[i][1] = "-";
                data[i][2] = "-";
            }
        }
        return createPlanningGraphique(data,nbRdvMaxParCreneau + 1,TypePlanning.GLOBAL);
    }

    public JTable generatePlanningMedecin(Medecins med, String date, EntityManager manager){
        this.manager = manager;

        Plannings planning = getPlanningByMedecin(date, med);
        String[][] data = new String[13][3];
        data[0][0] = "Horaire";
        data[0][1] = "Patient";
        data[0][2] = "Salle";

        String[] columnNames = {"Heure", "Patient", "Salle"};
        try {
            int nbRdv = planning.getRdv().size();
            planning.getRdv().sort((o1, o2) -> o1.getHeure() - o2.getHeure());
            for (int i = 1; i < 13; i++) {
                data[i][0] = i + 7 + "h";
                data[i][1] = "-";
                data[i][2] = "-";
                for (int j = 0; j < planning.getRdv().size(); j++) {

                    if (planning.getRdv().get(j).getHeure() == i + 7 && planning.getRdv().get(j).getMedecin().getId() == med.getId()) {
                        String nom = planning.getRdv().get(j).getPatient().getNom();
                        String prenom = planning.getRdv().get(j).getPatient().getPrenom();
                        String nomSalle = planning.getRdv().get(j).getSalle().getNom();

                        data[i][1] = nom + " " + prenom ;
                        data[i][2] = nomSalle;
                    }
                }
            }
        } catch (Exception e) {
            for (int i = 1; i < 13; i++) {
                data[i][0] = i + 7 + "h";
                data[i][1] = "-";
                data[i][2] = "-";
            }
        }
        return createPlanningGraphique(data,3,TypePlanning.MEDECIN);
    }


    public JTable generatePlanningSalle(Salles s, String date, EntityManager manager){
        this.manager = manager;

        Plannings planning = getPlanningByDate(date);
        String[][] data = new String[13][3];
        data[0][0] = "Horaire";
        data[0][1] = "Patient";
        data[0][2] = "Médecin";

        try {
            int nbRdv = planning.getRdv().size();
            planning.getRdv().sort((o1, o2) -> o1.getHeure() - o2.getHeure());
            for (int i = 1; i < 13; i++) {
                data[i][0] = i + 7 + "h";
                data[i][1] = "-";
                data[i][2] = "-";
                for (int j = 0; j < planning.getRdv().size(); j++) {
                    if (planning.getRdv().get(j).getHeure() == i + 7 && planning.getRdv().get(j).getSalle().getId() == s.getId()) {
                        String nom = planning.getRdv().get(j).getPatient().getNom();
                        String prenom = planning.getRdv().get(j).getPatient().getPrenom();
                        String nomMed = planning.getRdv().get(j).getMedecin().getNom();

                        data[i][1] = nom + " " + prenom ;
                        data[i][2] = nomMed;
                    }
                }
            }
        } catch (Exception e) {
            for (int i = 1; i < 13; i++) {
                data[i][0] = i + 7 + "h";
                data[i][1] = "-";
                data[i][2] = "-";
            }
        }
        return createPlanningGraphique(data,3,TypePlanning.MEDECIN);
    }



    private JTable createPlanningGraphique(Object[][] data, int nbRdvMaxParCreneau, TypePlanning typePlanning){
        JTable table = null;
        String columnNames[] = new String[nbRdvMaxParCreneau];
        Toolkit outil = getToolkit();
        int sizeOfColumn = 0 ;
        int rowHeight = 50;
        int xPos = 0;
        int fontSize = 10;

        //Mise de chaque cellule au format
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

        switch (typePlanning){
            case GLOBAL:
                sizeOfColumn = outil.getScreenSize().width / nbRdvMaxParCreneau;
                data[0][0] = "Horaire";
                columnNames[0] = "Horaire";
                for (int i = 1; i < nbRdvMaxParCreneau ; i++) columnNames[i] = "Rdv " + i;
                xPos = 10;
                fontSize = 15;
                break;

            case MEDECIN:
                sizeOfColumn = 500;
                columnNames[0] = "Horaire";
                columnNames[1] = "Patient";
                columnNames[2] = "Salle";
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                xPos = outil.getScreenSize().width - nbRdvMaxParCreneau * sizeOfColumn - 300;
                fontSize = 20;
                break;

            case SALLE:
                sizeOfColumn = 500;
                columnNames[0] = "Horaire";
                columnNames[1] = "Patient";
                columnNames[2] = "Médecin";
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                xPos = outil.getScreenSize().width - nbRdvMaxParCreneau * sizeOfColumn - 300;
                fontSize = 20;
                break;
        }

        //création du tableau en enlevant la couleur
        table = new JTable(data, columnNames);

        //tableau centré avec la colonne de gauche plus fine
        table.setBounds(xPos, 200, nbRdvMaxParCreneau * sizeOfColumn - 10, rowHeight * 13);
        table.setFont(new Font("Arial", Font.BOLD, fontSize));
        table.setRowHeight(150);


        //centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        Color couleur;
        for (int j = 1; j < 13; j++) {
            table.setRowHeight(rowHeight);
            for (int i = 1; i < nbRdvMaxParCreneau; i++) {
                table.getColumnModel().getColumn(i).setPreferredWidth(sizeOfColumn);
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        //change le background de la première ligne
        table.setRowSelectionInterval(0, 0);
        table.setSelectionBackground(Color.ORANGE);
        table.setSelectionForeground(Color.WHITE);

        //valeur non modifiable
        table.setEnabled(false);

        return table;
    }

    private int nombreDeRdvMaxParCreneau(Plannings planning) {
        int[] maxLocal = new int[20];
        int max = 0;

        for (int i = 0; i < 13 ; i++) maxLocal[i] = 0;
        for (int i = 0; i < planning.getRdv().size(); i++) maxLocal[planning.getRdv().get(i).getHeure()]++;

        Arrays.sort(maxLocal);
        return maxLocal[maxLocal.length-1];
    }

    private Plannings getPlanningByDate(String date) {
        Query query = manager.createNamedQuery("findByDate");
        query.setParameter("date", date);
        return (Plannings) query.getSingleResult();
    }

    private Plannings getPlanningByMedecin(String date,Medecins med) {
        Query query = manager.createNamedQuery("getPlanningByDateAndMedecin");
        query.setParameter("date", date);
        query.setParameter("id", med.getId());
        return (Plannings) query.getSingleResult();
    }

    public JComboBox<Object> createComboBox(Object[] contenu,int x, int y, int width, int height){
        JComboBox<Object> comboBox = new JComboBox<>(contenu);
        comboBox.setBounds(x, y, width, height);
        comboBox.setFont(new Font("Arial", Font.BOLD, 20));
        comboBox.setForeground(new Color(0x376795));
        return comboBox;

    }
}

package Utilities;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import modele.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jakarta.persistence.EntityManager;


public class DatabaseGenerator {

    /** Collection de Médecin */
    private ArrayList<Medecins> medecinsCol;

    /** Collection de Médecin */
    private ArrayList<Cadres> cadresCol;

    /** Collection de Médecin */
    private ArrayList<Salles> sallesCol;

    private EntityManager manager;

    // Chemin vers les fichiers dans le dossier datas
    private String dataDir = "src\\main\\resources\\Datas";
    private String fileNameMed = "medecins.txt"; ;
    private String fileNameCad = "cadres.txt";
    private String fileNameSalles = "salles.txt";

    public DatabaseGenerator(EntityManager em) throws IOException {
        this.manager = em;
        createPlannings();
    }

    public void createPlannings() throws IOException {
        ArrayList<Plannings> plannings = new ArrayList<Plannings>();

        /** Liste des fichiers **/
        File[] listOfFiles = new File(dataDir).listFiles();

        /** Nom d'un fichier **/
        String fichier;

        /** Regex pour vérifier le nom d'un fichier **/
        String regex = "\\d+";
        Pattern p = Pattern.compile(regex);


        /** Récupération des médecins si fichier **/
        medecinsCol = createMedecins(dataDir + "\\" + fileNameMed);

        /** Récupération des cadres **/
        cadresCol = createCadres(dataDir + "\\" + fileNameCad);

        /** Récupération des salles **/
        sallesCol = createSalles(dataDir + "\\" + fileNameSalles);


        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                fichier = listOfFiles[i].getName();
                Matcher m = p.matcher(fichier);
                if (m.find() && fichier.endsWith(".txt")) plannings.add(createPlanning(dataDir + "\\" + fichier));
            }
        }
        Plannification ord;
        for (Plannings p1 : plannings) {
            ord = new Plannification(p1);
        }
    }

    public ArrayList<Salles> createSalles(String fileName) throws IOException {

        /** Collection de salles **/
        ArrayList<Salles> sallesCol = new ArrayList<Salles>();

        /** Variables File Reader **/
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;

        /** Récupération des salles **/
        while((line = br.readLine()) != null) {
            String[] tab = line.split(";");
            Salles salle = new Salles(
                    Integer.parseInt(tab[0]), // id
                    Integer.parseInt(tab[1]), // numSalle
                    tab[2] // nom
            );
            sallesCol.add(salle);
            manager.persist(salle);
        }

        // Tri des salles par id
        sallesCol.sort(Comparator.comparing(Salles::getId));

        /** Fermeture du file reader **/
        br.close();
        fr.close();

        return sallesCol;
    }

    public ArrayList<Cadres> createCadres(String fileName) throws IOException {
        /** Collection de cadres **/
        ArrayList<Cadres> cadresCol = new ArrayList<Cadres>();

        /** Variables **/
        String[] tab;

        /** Variables File Reader **/
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;

        /** Récupération des cadres **/
        while((line = br.readLine()) != null) {
            tab = line.split(";");
            Cadres cadre = new Cadres(
                    tab[4], // nom
                    tab[3], // prenom
                    tab[5], // date
                    tab[1], // login
                    tab[2], // mdp
                    tab[6], // adresse
                    Integer.parseInt(tab[0]) // id
            );
            cadresCol.add(cadre);
            manager.persist(cadre);
        }

        /** Fermeture du file reader **/
        br.close();
        fr.close();
        return cadresCol;
    }

    public ArrayList<Medecins> createMedecins(String fileName) throws IOException {

        /** Variables **/
        String[] tab;
        boolean estSpecialiste = false;

        /** Collection de médecins **/
        ArrayList<Medecins> medecinsCol = new ArrayList<Medecins>();

        /** Variables File Reader **/
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;

        /** Récupération des médecins **/
        while((line = br.readLine()) != null) {
                tab = line.split(";");
                estSpecialiste = false;
                if (tab[7].equals("0")) estSpecialiste = true;
                Medecins med = new Medecins(tab[4], tab[3], tab[5], tab[1], tab[2], tab[6], estSpecialiste, Integer.parseInt(tab[0]));
                medecinsCol.add(med);
                manager.persist(med);
        }
        br.close();
        fr.close();


        return medecinsCol;
    }

    public Plannings createPlanning(String fileName) throws IOException {

        /** Collection de dispo **/
        ArrayList<Medecins> dispoMed = new ArrayList<>();
        ArrayList <Salles> dispoSalles = new ArrayList() ;

        /** Variables utilisées **/
        String[] datasLue;

        /** File Reader **/
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String ligne;

        /** Récupération du cadre **/
        Cadres c = new Cadres();
        ligne = br.readLine();
        datasLue = ligne.split(";");

        /** On recherche le cadre dans la collection **/
        for (Cadres cadre : cadresCol)
            if (cadre.getNumIdentification() == Integer.parseInt(datasLue[0]))  c = cadre;

        br.readLine();

        /** -- Récupération des médecins et des salles disponibles et instantiation -- **/
        ligne = br.readLine();
        datasLue = ligne.split(";");

        for (int i = 0; i < datasLue.length; i++) {
            for (Medecins m : medecinsCol)
                if (m.getNumProSante() == Integer.parseInt(datasLue[i])) dispoMed.add(m);
        }

        ligne = br.readLine();
        datasLue = ligne.split(";");

        for (int i = 0; i < datasLue.length; i++) {
            for (Salles s : sallesCol)
                if (s.getId() == Integer.parseInt(datasLue[i])) dispoSalles.add(s);
        }

        String date = getDateFromFileName(fileName);
        Plannings planning = new Plannings(
                date, // date
                c, // cadre
                createRdvs(br) ,// rdv
                dispoMed, // medecinDispo
                dispoSalles // salleDispo
        );

        for (Medecins m : dispoMed) m.getPlanningDispo().add(planning);
        for (Salles s : dispoSalles) s.getDispo().add(planning);

        manager.persist(planning);

        br.close();
        fr.close();
        return planning;
    }

    public String getDateFromFileName(String fileName) {
        String annee = fileName.substring(fileName.length() - 12, fileName.length() - 8);
        String mois = fileName.substring(fileName.length() - 8, fileName.length() - 6);
        String jour = fileName.substring(fileName.length() - 6, fileName.length() - 4);
        String date = annee + "/" + mois + "/" + jour;
        return date;
    }

    public ArrayList<Rdvs> createRdvs(BufferedReader br) throws IOException {
        String[] rdvTab ;
        Patients rdvPat;
        Medecins rdvMed = new Medecins();
        Rdvs currRdv;
        Salles salle;
        Query query;
        String line;
        ArrayList<Rdvs> rdvsCol = new ArrayList<Rdvs>();

        while((line = br.readLine()) != null) {

            salle = new Salles();
            rdvTab = line.split(";");

            query = manager.createNamedQuery("Patients.findByNumIdentification");
            query.setParameter("numIdentification", rdvTab[6]);

            /** Récupération du patient s'il existe sinon le crée **/
            try {
                rdvPat = (Patients) query.getSingleResult();
            } catch (NoResultException e) {
                rdvPat = new Patients(
                        rdvTab[4], // nom
                        rdvTab[3], // prenom
                        rdvTab[5], // date
                        rdvTab[6],// id
                        !Boolean.parseBoolean(rdvTab[7])// demandeSpeciale
                );
            }

            /** Récupération de la salle concernée par le RDV **/
            for (Salles s : sallesCol)
                if (s.getId() == Integer.parseInt(rdvTab[1])) salle = s;

            /** Récupération du médecin concerné par le RDV **/
            int numProSante = Integer.parseInt(rdvTab[0]);
            int i = 0;

            /** Recherche du médecin **/
            for (Medecins m : medecinsCol)
                if (m.getNumProSante() == numProSante) rdvMed = m;

            if (rdvTab[7].equals("1")) {
                /** Création du rdv **/
                currRdv = new Rdvs(
                        Integer.parseInt(rdvTab[2]), // heure
                        salle, // salle
                        rdvPat, // patient
                        rdvMed, // medecin
                        true // demandeSpeciale
                );
            }
            else {
                /** Création du rdv **/
                currRdv = new Rdvs(
                        Integer.parseInt(rdvTab[2]), // heure
                        salle, // salle
                        rdvPat, // patient
                        rdvMed, // medecin
                        false // demandeSpeciale
                );
            }


            /** Ajout du rdv à la collection **/
            rdvsCol.add(currRdv);
            manager.persist(currRdv);
        }
        return rdvsCol;
    }
}

package Utilities;

import jakarta.persistence.EntityManager;
import modele.*;


public class Plannification {

        private Plannings planning;
        private Rdvs[][] rdvs;

        private int nbRdvs[] ;

        int getNbMedecinsSpe;

        public Plannification(Plannings planning) {
            if (planning != null) {
                this.planning = planning;
                nbRdvs = new int[planning.getMedecinDispo().size()];
                for (int i = 0 ; i < planning.getMedecinDispo().size() ; i++) nbRdvs[i] = 0;
                basicOrdo();

            } else
                this.planning = null;
        }

        public int basicOrdo() {
            rdvs = new Rdvs[planning.getSalleDispo().size()][12];
            int score = 0;


            for (int i = 0; i < planning.getRdv().size(); i++) {
                int salle = 0;
                Rdvs rdv = planning.getRdv().get(i);
                while (rdv.getSalle() != planning.getSalleDispo().get(salle)) salle++;
                int heure = rdv.getHeure() - 8;
                rdvs[salle][heure] = rdv;
            }


            for (int i = 0; i < planning.getMedecinDispo().size(); i++) {
                score += getScore(planning.getMedecinDispo().get(i));
            }
            System.out.println("Score Planning de base  : " + score);
            System.out.println("--------------------");

            // Optimisation des salles
            reorgaSalles();

            score = 0;
            for (int i = 0; i < planning.getMedecinDispo().size(); i++) {
                score += getScore(planning.getMedecinDispo().get(i));
            }
            System.out.println("Score Planning optimisé 1 : " + score);
            System.out.println("--------------------");



            // Optimisation des médecins
            reorgaMedecins();

            score = 0;
            for (int i = 0; i < planning.getMedecinDispo().size(); i++) {
                score += getScore(planning.getMedecinDispo().get(i));
            }
            System.out.println("Score Planning optimisé 2 : " + score);


            System.out.println("--------------------");

            return 0;
        }


        private Rdvs[][] reorgaSalles(){


            for (int i = 0; i < 12 ; i++) {
                for (int j = 0; j <= planning.getSalleDispo().size() - 2; j++) {
                    for (int k = j + 1; k <= planning.getSalleDispo().size() - 1; k++) {
                        if (rdvs[j][i] == null && rdvs[k][i] != null) {
                            rdvs[j][i] = rdvs[k][i];
                            rdvs[k][i] = null;
                            rdvs[j][i].setSalle(planning.getSalleDispo().get(j));
                        }
                    }
                }
            }

            return rdvs;
        }

        private Rdvs[][] reorgaMedecins(){
            getNbMedecinsSpe = getNbMedecinsSpe();
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < planning.getSalleDispo().size(); j++) {
                    for (int k = 0; k < 12; k++) {
                        if (rdvs[j][k] != null)
                            rdvs[j][k].setMedecin(null);
                    }
                }
            }

            for (int i = 0; i < 12; i++) {
                int nbRdvsSpe = 0;
                for (int j = 0; j < planning.getSalleDispo().size(); j++) {
                    if (rdvs[j][i] != null && rdvs[j][i].isDemandeSpeciale()) nbRdvsSpe++;
                }

                if (nbRdvsSpe == getNbMedecinsSpe){
                    for (int j = 0; j < planning.getSalleDispo().size(); j++) {
                        if (rdvs[j][i] != null && rdvs[j][i].isDemandeSpeciale()){
                            for (int k = 0; k < planning.getMedecinDispo().size(); k++) {
                                if (planning.getMedecinDispo().get(k).getSpecialisation() && estDispo(planning.getMedecinDispo().get(k),i)){
                                    rdvs[j][i].setMedecin(planning.getMedecinDispo().get(k));
                                    nbRdvs[k]++;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            for (int salle = 0 ; salle < planning.getSalleDispo().size() ; salle++){
                for (int heure = 0 ; heure < 12 ; heure++){
                    verifZoneCrit(heure);
                    if (rdvs[salle][heure] != null && rdvs[salle][heure].getMedecin() == null){
                        int medecin = getBestMedecin(salle,heure);
                        if (medecin != -1) {
                            rdvs[salle][heure].setMedecin(planning.getMedecinDispo().get(medecin));
                            nbRdvs[medecin]++;
                        }
                        else
                        {
                            System.out.println("Erreur : Aucun médecin disponible pour le rdv");
                        }
                    }
                }
            }
            return rdvs;
        }

        private int getBestMedecin(int salle,int heure){

            Rdvs precedent = null;
            if (heure > 0) precedent = rdvs[salle][heure - 1];

            Rdvs rdvATraiter = rdvs[salle][heure];

            if (rdvATraiter.isDemandeSpeciale())
            {
                // Si le rdv précédent est une demande spéciale
                if (precedent != null && precedent.isDemandeSpeciale() && estDispo(precedent.getMedecin(),heure)) {
                    return planning.getMedecinDispo().indexOf(precedent.getMedecin());
                }
                else
                {
                    // Si un des rdv precedent est un rdv spécialisé
                    for (int i = heure - 1; i >= 0; i--) {
                        if (rdvs[salle][i] != null && rdvs[salle][i].isDemandeSpeciale()) {
                            if (estDispo(rdvs[salle][i].getMedecin(),heure)) {
                                return planning.getMedecinDispo().indexOf(rdvs[salle][i].getMedecin());
                            }
                        }
                    }

                    // Si aucun rdv precedent n'est un rdv spécialisé on prend le premier médecin spécialisé
                    for (int i = 0; i < planning.getMedecinDispo().size(); i++) {
                        if (planning.getMedecinDispo().get(i).getSpecialisation() && estDispo(planning.getMedecinDispo().get(i),heure)) {
                            return i;
                        }
                    }
                }
            }
            else
            {
                // Si le rdv précédent n'est une demande spéciale
                if ( precedent != null){
                    if (estDispo(precedent.getMedecin(),heure))
                        return planning.getMedecinDispo().indexOf(precedent.getMedecin());
                }

                // Si un des rdv suivant est un rdv spécialisé
                for (int i = heure + 1; i < heure + 4 ; i++) {
                    if ( i < 12 && rdvs[salle][i] != null && rdvs[salle][i].isDemandeSpeciale()) {
                        if ( precedent != null &&  estDispo(precedent.getMedecin(),heure))
                            return planning.getMedecinDispo().indexOf(rdvs[salle][i].getMedecin());
                    }
                }

                // Si aucun rdv precedent n'est un rdv spécialisé on prend le premier médecin non spécialisé
                for (int i = 0; i < planning.getMedecinDispo().size(); i++) {
                    if (estDispo(planning.getMedecinDispo().get(i),heure) && !planning.getMedecinDispo().get(i).getSpecialisation()) {
                        return i;
                    }
                }

                // Sinon on prend le premier médecin disponible
                for (int i = 0; i < planning.getMedecinDispo().size(); i++) {
                    if (estDispo(planning.getMedecinDispo().get(i),heure)) {
                        return i;
                    }
                }
            }
            return -1;
    }

    public int getScore(Medecins medecin){
        int lastRdv = -1;
        int lastSalle = -1;
        int scoreTrou = 0;
        int scoreDeplacement = 0;

        // affichage du planning
        //System.out.println("Planning de " + medecin.getNom() + " " + medecin.getPrenom());
        //System.out.println("--------------------------------------------------");
        //System.out.println("Heure | Salle | Patient | Spécialité | ");
        //System.out.println("--------------------------------------------------");
        for (int i = 0; i < 12 ; i++) {
            for (int j = 0; j < planning.getSalleDispo().size() ; j++) {
                if (rdvs[j][i] != null && rdvs[j][i].getMedecin() == medecin) {
                    //System.out.println("  " + (i + 8) + "  |   " + j + "   |   " + rdvs[j][i].getPatient().getNom() + "   |   " + rdvs[j][i].isDemandeSpeciale() + "   |  " );

                    if (lastRdv != -1) {
                        if (lastSalle != j ) {
                            scoreDeplacement = scoreDeplacement + 1;
                        }
                        if (i > lastRdv + 1) {
                            scoreTrou = scoreTrou + (i - lastRdv );
                        }
                        //System.out.println("Score Trou : " + scoreTrou + "\n Score Deplacement : " + scoreDeplacement );
                    }

                    lastRdv = i;
                    lastSalle = j;
                }
            }
        }

        //System.out.println("Medecin : " + medecin.getNom() + " " + medecin.getPrenom());
        //System.out.println("Score Trou : " + scoreTrou + "\n Score Deplacement : " + scoreDeplacement + "\n Score Total : " + (scoreTrou + scoreDeplacement));

        return scoreTrou + scoreDeplacement;
    }

    private boolean estDispo(Medecins medecin,int heure){
        for (int i = 0; i < planning.getSalleDispo().size(); i++) {
            if ((rdvs[i][heure] != null && rdvs[i][heure].getMedecin() == medecin) || nbRdvs[planning.getMedecinDispo().indexOf(medecin)] >= 8) return false;
        }
        return true;
    }

    private int getNbMedecinsSpe(){
        int nbMedecinsSpe = 0;
        for (int i = 0; i < planning.getMedecinDispo().size(); i++) {
            if (planning.getMedecinDispo().get(i).getSpecialisation()) nbMedecinsSpe++;
        }
        return nbMedecinsSpe;
    }

    public int getNbMedecinsSpeRestant(int heure){
        int nbMedecinsSpe = 0;
        for (int i = 0; i < planning.getMedecinDispo().size(); i++) {
            if (planning.getMedecinDispo().get(i).getSpecialisation() && estDispo(planning.getMedecinDispo().get(i),heure)) nbMedecinsSpe++;
        }
        return nbMedecinsSpe;

    }

    private void verifZoneCrit(int heure) {
            // On recalcule les disponibilités des médecins spécialisés pour voir si des horaires sont critiques
        for (int i = heure; i < 12; i++) {
            int nbRdvsSpe = 0;
            for (int j = 0; j < planning.getSalleDispo().size(); j++) {
                if (rdvs[j][i] != null && rdvs[j][i].isDemandeSpeciale() && rdvs[j][i].getMedecin() == null ) nbRdvsSpe++;
            }

            if (nbRdvsSpe == getNbMedecinsSpeRestant(i)) {
                for (int j = 0; j < planning.getSalleDispo().size(); j++) {
                    if (rdvs[j][i] != null && rdvs[j][i].isDemandeSpeciale() && rdvs[j][i].getMedecin() == null) {
                        for (int k = 0; k < planning.getMedecinDispo().size(); k++) {
                            if (planning.getMedecinDispo().get(k).getSpecialisation() && estDispo(planning.getMedecinDispo().get(k), i)) {
                                rdvs[j][i].setMedecin(planning.getMedecinDispo().get(k));
                                nbRdvs[k]++;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

}

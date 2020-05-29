package automate;

import java.util.Scanner;

/**
 * @program: Automate
 * @description:
 * @author: xin
 * @create: 2020-05-09 13:06
 **/
public class Main {
    public static void main(String[] args) {
        Fonction fonction = new Fonction();
        Automate automate = new Automate();
        boolean flag = true;

        while (flag) {
            automate = fonction.readAutomate();

            //Déterminisation et complétion
            if (fonction.est_un_automate_asynchrone(automate)) {
                automate = fonction.determinisation_et_completion_automate_asynchrone(automate);
            } else {
                if (fonction.est_un_autotmate_deterministe(automate)) {
                    if (!fonction.est_un_automate_complet(automate)) {
                        fonction.completion(automate);
                    }
                } else {
                    automate = fonction.determinisation_et_completion_automate_asynchrone(automate);
                }
            }
            System.out.println("AFDC");
            automate.afficher_automate();

            //Minimisation
            automate = fonction.minimisation(automate);
            System.out.println("AFDCM");
            automate.afficher_automate();

            //Reconnaissance de mots
            System.out.println("\n\n");
            fonction.reconnaissance_de_mots(automate);
            System.out.println("\n\n");


            //Langage complémentaire
            fonction.automate_complementaire(automate);
            System.out.println("Automate complementaire");
            automate.afficher_automate();

            //Standardisation
            System.out.println("Automate standard");
            fonction.automate_standard(automate);
            automate.afficher_automate();

            //Reconnaissance de mots
            System.out.println("\n\n");
            fonction.reconnaissance_de_mots(automate);
            System.out.println("\n\n");


            System.out.println("Do you want to restart the pogrom? y/n");
            Scanner scanner = new Scanner(System.in);
            String s = scanner.next();
            if (!"y".equals(s)) {
                flag = false;
            }
        }
    }


}

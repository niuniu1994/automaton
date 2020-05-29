package automate;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.io.*;
import java.util.*;

/**
 * @program: Automate
 * @description:
 * @author: xin
 * @create: 2020-05-04 22:31
 **/
public class Automate {
    private ArrayList<State> automate;
    private ArrayList<String> characters;
    private String fileName;

    public Automate() {
        automate = new ArrayList<>();
        characters = new ArrayList<>();
        fileName = null;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<State> getAutomate() {
        return automate;
    }

    public void setAutomate(ArrayList<State> automate) {
        this.automate = automate;
    }

    public ArrayList<String> getCharacters() {
        return characters;
    }

    public void setCharacters(ArrayList<String> characters) {
        this.characters = characters;
    }

    public void afficher_automate() {
        int sizeOfChars = characters.size();
        int sizeOfStates = automate.size();
        AsciiTable at = new AsciiTable();
        int xLen = sizeOfChars + 2;
        int yLen = sizeOfStates + 1;
        String[][] table = new String[yLen][xLen];

        //initialize the table
        for (int i = 0; i < yLen; i++) {
            for (int j = 0; j < xLen; j++) {
                table[i][j] = " ";
            }
        }

        //for the first line
        for (int i = 2; i < xLen; i++) {
            table[0][i] = characters.get(i - 2);
        }

        for (int i = 1; i < yLen; i++) {
            State state = automate.get(i - 1);

            if (state.isStart() && state.isAccept()) {
                table[i][0] = "E/S";
            } else if (state.isAccept()) {
                table[i][0] = "S";
            } else if (state.isStart()) {
                table[i][0] = "E";
            }

            table[i][1] = state.getStateId();

            for (int j = 2; j < xLen; j++) {
                ArrayList<State> arrives = state.getAllTransition(table[0][j]);
                if (arrives != null) {
                    table[i][j] = allArrive(arrives);
                }
            }
        }

        for (int i = 0; i < yLen; i++) {
            at.addRule();
            at.addRow(table[i]);
        }
        at.addRule();
        at.setTextAlignment(TextAlignment.CENTER);
        String rend = at.render();
        rend = rend + "\n";

        try {
            File file = new File(fileName);
            OutputStream os = new BufferedOutputStream(new FileOutputStream(file,true));
            byte[] bytes = rend.getBytes();
            os.write(bytes);
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(rend);
    }

    private String allArrive(ArrayList<State> arrives) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arrives.size(); i++) {
            String id = arrives.get(i).getStateId();
            builder.append(id);
            if (arrives.size() - 1 != i) {
                builder.append(",");
            }
        }
        return builder.toString();
    }
}



package automate;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @program: main
 * @description:
 * @author: xin
 * @create: 2020-05-04 11:03
 **/
public class Fonction {

    public Automate readAutomate() {
        System.out.println("Please enter the number of file you want to test");
        Scanner scanner = new Scanner(System.in);
        String fileName = "src/main/java/file/L3NEW-MpI-9";
        String fileNum = scanner.next();
        fileName = fileName + "-" + fileNum + ".txt";
        String traceName = "src/main/java/trace/L3NEW-MpI-9trace-" + fileNum + ".txt";
        Automate automate = new Automate();

        ArrayList<State> states = new ArrayList<>();

        File file = new File(fileName);

        try {
            FileInputStream fis = new FileInputStream(file);
            scanner = new Scanner(new BufferedInputStream(fis));

            int line = 0;

            while (scanner.hasNextLine()) {
                //get all the characters
                String[] str = scanner.nextLine().split(" ");

                if (line == 0) {
                    ArrayList<String> characters = new ArrayList<>(Arrays.asList(str));
                    automate.setCharacters(characters);
                    line++;
                    continue;
                }

                //get all the states
                if (line == 1) {

                    for (String s : str) {
                        State state = new State(s);
                        states.add(state);
                    }
                    line++;
                    continue;
                }

                if (line == 2) {

                    for (int i = 0; i < str.length; i++) {
                        switch (str[i]) {
                            case ("S"):
                                states.get(i).setStart(true);
                                break;
                            case ("A"):
                                states.get(i).setAccept(true);
                                break;
                            case ("T"):
                                states.get(i).setStart(true);
                                states.get(i).setAccept(true);
                                break;
                            default:
                                break;
                        }
                    }
                    line++;
                    continue;
                }

                State beg = null;
                State end = null;
                for (State s : states) {
                    if (s.getStateId().equals(str[0])) {
                        beg = s;
                    }
                    if (s.getStateId().equals(str[2])) {
                        end = s;
                    }
                }

                if (beg != null && end != null) {
                    beg.addTransition(end, str[1]);

                } else {
                    throw new IllegalArgumentException("format of file is not correct");
                }
            }

            automate.setAutomate(states);
        } catch (FileNotFoundException e) {
            System.out.println("Can not open " + fileName);
        }

        automate.setFileName(traceName);

        return automate;
    }

    public boolean est_un_automate_asynchrone(Automate automate) {
        ArrayList<String> list = automate.getCharacters();
        if (list.contains("#")) {
            return true;
        }
        return false;
    }

    public boolean est_un_automate_complet(Automate automate) {
        ArrayList<State> states = automate.getAutomate();
        ArrayList<String> chars = automate.getCharacters();
        for (State s : states) {
            for (String c : chars) {
                if (s.getAllTransition(c).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean est_un_autotmate_deterministe(Automate automate) {
        ArrayList<State> states = automate.getAutomate();
        ArrayList<String> chars = automate.getCharacters();

        int i = 0;

        for (State s : states) {
            if (s.isStart()) {
                i++;
            }
        }

        if (i >= 2) {
            return false;
        }

        for (State s : states) {
            for (String c : chars) {
                ArrayList<State> ss = s.getAllTransition(c);
                if (ss.size() > 1) {
                    return false;
                }
            }
        }

        return true;
    }

    public Automate determinisation_et_completion_automate_asynchrone(Automate af) {
        ArrayList<String> characters = af.getCharacters();
        characters.remove("#");
        Automate afdc = new Automate();
        afdc.setFileName(af.getFileName());
        afdc.setCharacters(characters);

        Set<State> setOfInit = new HashSet<>();
        Stack<State> stack = new Stack<>();

        for (State s : af.getAutomate()) {
            if (s.isStart()) {
                setOfInit.add(s);
            }
        }

        //get the group of states Init of DFA
        cloture(setOfInit);

        //state Init of DFA
        String stateId = connectStateId(setOfInit);
        State newStateInit = new State(stateId, setOfInit);
        afdc.getAutomate().add(newStateInit);
        stack.push(newStateInit);

        while (!stack.isEmpty()) {
            State state = stack.pop();

            for (String c : characters) {

                Set<State> setOfState = moveToState(c, state.getStates());

                //in case that there doest exist any transition by using c
                if (setOfState.isEmpty()) {
                    continue;
                }

                cloture(setOfState);

                //check if the new state of afd is already exits or not
                State st = null;
                boolean flag = false;
                for (State s : afdc.getAutomate()) {
                    if (setOfState.size() == s.getStates().size() && s.getStates().containsAll(setOfState)) {
                        flag = true;
                        st = s;
                    }
                }

                // if setOfState not exist in afd
                if (!flag) {
                    stateId = connectStateId(setOfState);
                    State newState = new State(stateId, setOfState);
                    afdc.getAutomate().add(newState);
                    stack.push(newState);
                    state.addTransition(newState, c);
                } else {
                    state.addTransition(st, c);
                }
            }
        }
        if (!est_un_automate_complet(afdc)){
            completion(afdc);
        }
        return afdc;
    }

    public void completion(Automate automate) {
        State poupelle = new State("p");
        ArrayList<State> states = automate.getAutomate();
        states.add(poupelle);
        ArrayList<String> characters = automate.getCharacters();

        for (State s : states) {
            for (String c : characters) {
                if (s.getAllTransition(c).isEmpty()) {
                    s.addTransition(poupelle, c);
                }
            }
        }
    }

    public Automate minimisation(Automate automate) {

        ArrayList<State> states = automate.getAutomate();
        ArrayList<String> chars = automate.getCharacters();

        Automate newAutomate = new Automate();
        newAutomate.setFileName(automate.getFileName());
        ArrayList<State> newStates = new ArrayList<>();
        newAutomate.setCharacters(chars);


        ArrayList<Set<State>> o = new ArrayList<>();
        Set<State> T = new HashSet<>();
        Set<State> NT = new HashSet<>();

        //group state accept and group of state start
        for (State s : states) {
            if (s.isAccept()) {
                T.add(s);
            } else {
                NT.add(s);
            }
        }

        o.add(NT);
        o.add(T);
        //o中的二级集合
        //fl for first list
        boolean end = false;
        while (!end) {
            ArrayList<Set<State>> newO = new ArrayList<>();
            for (Set<State> fl : o) {
                //state in second level group
                if (fl.size() > 1) {
                    for (State s : fl) {
                        //compare states in the same second level group
                        Set<State> newSet = new HashSet<>();

                        for (State state1 : fl) {
                            //judge the arrive state using c belongs to the same second level group or not
                            boolean flag = true;
                            for (String ch : chars) {
                                //in AFDC there is only single arrive state for each character
                                State s1 = s.getAllTransition(ch).get(0);
                                State s2 = state1.getAllTransition(ch).get(0);
                                for (Set<State> tl : o) {
                                    if (tl.contains(s1) && !tl.contains(s2)) {
                                        flag = false;
                                        break;
                                    } else if (!tl.contains(s1) && tl.contains(s2)) {
                                        flag = false;
                                        break;
                                    }
                                }
                                if (!flag) {
                                    break;
                                }
                            }
                            //when states arrive belong to the same group
                            if (flag) {
                                newSet.add(state1);
                            }
                        }

                        if (!newO.contains(newSet)) {
                            newO.add(newSet);
                        }

                    }
                } else if (fl.size() == 1) {
                    if (!hasData(newO, fl)) {
                        newO.add(fl);
                    }
                }
            }
            if (o.size() == newO.size() && o.containsAll(newO)) {
                end = true;
            }
            o = newO;
        }

        //put new states in the new automaton
        for (Set<State> set : o) {
            String stateId = connectStateId(set);
            State state = new State(stateId, set);
            newStates.add(state);
        }

        //add transitions
        for (State state : newStates) {
            Set<State> set = state.getStates();
            for (State state1 : set) {
                for (String c : chars) {
                    State s = state1.getAllTransition(c).get(0);
                    for (State state2 : newStates) {
                        if (state2.getStates().contains(s)) {
                            state.addTransition(state2, c);
                        }
                    }
                }
                break;
            }
        }

        newAutomate.setAutomate(newStates);
        return newAutomate;
    }

    //this function is just for AFDC
    public void reconnaissance_de_mots(Automate automate) {
        boolean flag = true;
        ArrayList<String> arr = null;
        List list = Arrays.asList("e", "n", "d");
        ArrayList<String> end = new ArrayList<>(list);


        while (flag) {
            arr = lire_mot();
            if (arr.equals(end)) {
                flag = false;
                continue;
            }
            reconnaitre_mot(arr, automate);
        }
    }

    private void reconnaitre_mot(ArrayList<String> mot, Automate automate) {
        ArrayList<State> states = automate.getAutomate();
        State state = null;

        for (State s : states) {
            if (s.isStart()) {
                state = s;
            }
        }

        if ( mot.size() == 1 && "#".equals(mot.get(0)) && state.isAccept()){
            System.out.println("Known word");
            return;
        }

        for (String c : mot) {
            ArrayList<State> arr = state.getAllTransition(c);



            if (arr.isEmpty()) {
                System.out.println("UnKnown word");
                return;
            } else {
                state = arr.get(0);
            }
        }
        if (!state.isAccept()) {
            System.out.println("Unknown word");
            return;
        }
        System.out.println("Known word");
    }


    public void automate_complementaire(Automate automate) {
        ArrayList<State> states = automate.getAutomate();

        for (int i = 0; i < states.size(); i++) {
            if (states.get(i).isAccept()) {
                states.get(i).setAccept(false);
            } else {
                states.get(i).setAccept(true);
            }
        }
    }

    /***
     * @Parame: automaton
     * @description: considering standardization is the after determinism
     * which makes automaton only possess single state initial
     * @return: void
     * @author: xin kaining
     * @Date 2020/5/9
     **/
    public void automate_standard(Automate automate) {
        ArrayList<State> states = automate.getAutomate();
        ArrayList<String> chars = automate.getCharacters();
        Set<State> starts = new HashSet<>();
        Map<String, HashSet<State>> ends = new HashMap<>();

        for (int i = 0; i < states.size(); i++) {
            if (states.get(i).isStart()) {
                states.get(i).setStart(false);
                starts.add(states.get(i));
            }
        }

        for (State start : starts){
            for (String ch : chars){
                ends.put(ch,new HashSet<>());
                for (State state : start.getAllTransition(ch)){
                    ends.get(ch).add(state);
                }
            }
        }

        State i = new State("i");
        i.setStart(true);

        for (String s : ends.keySet()){
            for (State state : ends.get(s)){
                i.addTransition(state,s);
            }
        }

        states.add(i);
    }


    /******************utils******************/
    /***
     * @Parame: set of state may having transition epsilon
     * @description: get all states which we can get by trasition epsilon
     * @return: a new set of state with out having transition epsilon
     * @author: xin kaining
     * @Date 2020/5/5
     **/
    private void cloture(Set<State> set) {
        Stack<State> stack = new Stack<>();

        for (State s : set) {
            stack.push(s);
        }

        while (!stack.isEmpty()) {
            State s = stack.pop();
            ArrayList<State> list = s.getAllTransition("#");
            if (!list.isEmpty()) {
                for (State st : list) {
                    if (!set.contains(st)) {
                        set.add(st);
                        stack.push(st);
                    }
                }
            }
        }
    }

    /***
     * @Parame: c character of transition, set of states of depart
     * @description: get all arriving state by reading c
     * @return: set of state of arriving
     * @author: xin kaining
     * @Date 2020/5/5
     **/
    private Set<State> moveToState(String c, Set<State> set) {
        Set<State> res = new HashSet<>();
        for (State s : set) {
            ArrayList<State> arriveStates = s.getAllTransition(c);
            if (!arriveStates.isEmpty()) {
                for (State st : arriveStates) {
                    res.add(st);
                }
            }
        }
        return res;
    }

    private String connectStateId(Set<State> states) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        Iterator iterator = states.iterator();
        while (iterator.hasNext()) {
            State state = (State) iterator.next();
            String s = state.getStateId();
            stringBuilder.append(s);
            if (iterator.hasNext()) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private boolean hasData(ArrayList<Set<State>> list1, Set<State> list2) {
        for (Set<State> list : list1) {
            if (list.containsAll(list2)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<String> lire_mot() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input the mot in a single line without using any special character except \"# \" which represent mot vide");
        System.out.println("Input \"end\" to finish");
        String str = scanner.next();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            list.add(String.valueOf(str.charAt(i)));
        }
        return list;
    }

}

package automate;

import java.util.*;

/**
 * @program: Automate
 * @description:
 * @author: xin
 * @create: 2020-05-04 12:06
 **/
public class State {
    private String stateId;

    //all the transitions starting from this state
    private Map<String, ArrayList<State>> trans;

    //states is for DFA which state is a sub group of NFA state
    private Set <State> states;

    private boolean accept;
    private boolean start;

    //constructor for NFA
    public State(String stateId) {
        this.stateId = stateId;
        this.trans = new HashMap<>();
        accept = false;
        start = false;
    }

    public State(String stateId, Set<State> states){
        this.states = states;
        this.stateId = stateId;
        this.trans = new HashMap<>();
        this.start = false;

        for (State s : states) {
            if (s.isAccept()) {
                this.accept = true;
            }

            if (s.isStart()){
                this.start = true;
            }
        }
    }

    public void addTransition(State next, String key){

        if (!trans.containsKey(key)){
            ArrayList<State> states = new ArrayList<>();
            states.add(next);
            trans.put(key,states);
        }else {
            trans.get(key).add(next);
        }
    }

    public ArrayList<State> getAllTransition(String c){
        return trans.getOrDefault(c, new ArrayList<>());
    }

    public String  getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public Set<State> getStates() {
        return states;
    }

    public void setStates(Set<State> states) {
        this.states = states;
    }

    public Map<String, ArrayList<State>> getTrans() {
        return trans;
    }

    public void setTrans(Map<String, ArrayList<State>> trans) {
        this.trans = trans;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "State{" +
                "stateId='" + stateId +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return accept == state.accept &&
                start == state.start &&
                Objects.equals(stateId, state.stateId) &&
                Objects.equals(trans, state.trans) &&
                Objects.equals(states, state.states);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
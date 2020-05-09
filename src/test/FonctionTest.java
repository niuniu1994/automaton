package test;

import automate.Automate;
import automate.Fonction;
import automate.State;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;


import static org.junit.Assert.assertEquals;

/** 
* Main Tester. 
* 
* @author <Authors name> 
* @since <pre>5月 6, 2020</pre> 
* @version 1.0 
*/ 
public class FonctionTest {
    Automate automate = new Automate();

@Before
public void before() throws Exception {

} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: readAutomate(String fileName) 
* 
*/ 
@Test
public void testReadAutomate() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: determinisation_et_completion_automate_asynchrone(Automation nfa) 
* 
*/ 
@Test
public void testDeterminisation_et_completion_automate_asynchrone() throws Exception { 
//TODO: Test goes here... 
} 


/** 
* 
* Method: cloture(Set<State> set) 
* 
*/ 
@Test
public void testCloture() throws Exception {
    State state1 = new State("1");
    State state2 = new State("2");
    State state3 = new State("3");
    State state5 = new State("5");
    State state9 = new State("9");

    state3.addTransition(state5,"#");
    state5.addTransition(state9,"#");
    state1.addTransition(state2,"#");
    state1.addTransition(state3,"#");

    Set<State> set = new HashSet<>();
    set.add(state1);
    set.add(state2);

    Class c = Fonction.class;
    Object obj = c.newInstance();
    Method method = c.getDeclaredMethod("cloture", Set.class);
    method.setAccessible(true);
    Object res = method.invoke(obj,set);

    System.out.println(set);

} 

/** 
* 
* Method: moveToState(Character c, Set<State> set) 
* 
*/ 
@Test
public void testMoveToState() throws Exception {
    State state1 = new State("1");
    State state2 = new State("2");
    State state3 = new State("3");
    State state5 = new State("5");
    State state9 = new State("9");

    state3.addTransition(state5,"n");
    state5.addTransition(state9,"#");
    state2.addTransition(state9,"a");
    state1.addTransition(state2,"a");
    state1.addTransition(state3,"#");

    Set<State> set = new HashSet<>();
    set.add(state1);
    set.add(state2);

    Class c = Fonction.class;
    Object obj = c.newInstance();
    Method method = c.getDeclaredMethod("moveToState", String.class, Set.class);
    method.setAccessible(true);
    Object res = method.invoke(obj,"a",set);

    System.out.println(res);
} 

/** 
* 
* Method: connectStateId(Set<State> states) 
* 
*/ 
@Test
public void testConnectStateId() throws Exception {
    State state1 = new State("2");
    State state2 = new State("3");
    Set<State> set = new HashSet<>();
    set.add(state2);
    set.add(state1);


    Class c = Fonction.class;
    Object obj = c.newInstance();
    Method method = c.getDeclaredMethod("connectStateId", Set.class);
    method.setAccessible(true);
    Object res = method.invoke(obj,set);
    assertEquals("{3,2}",res);
} 

} 

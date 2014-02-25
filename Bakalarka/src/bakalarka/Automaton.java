/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author raf
 */
/* tato vynimka sa vyhodi, ked sa pokusime pouzit id neexistujuceho stavu */
class NoSuchStateException extends Exception
{
      //Parameterless Constructor
      public NoSuchStateException(Object stateId) {
          System.out.println("No such state" + stateId. toString());
      }

      //Constructor that accepts a message
      public NoSuchStateException(String message)
      {
         super(message);
      }
}


/* tato vynimka sa vyhodi, ked sa pokusime nahradit existujuci stav nejakym
inym, co uz tiez existuje v metode replaceStateId()
*/
class IdCollisionException extends Exception
{
      //Parameterless Constructor
      public IdCollisionException(Identificator id) {
          System.out.println("ID " + id + " collides");
      }

      //Constructor that accepts a message
      public IdCollisionException(String message)
      {
         super(message);
      }
}


public class Automaton{
    private HashMap<Identificator,State> idStateMap; // tu si pamatame k idcku stav
    private HashSet<Identificator> allStatesIds; // mnozina idcok vsetkych stavov
    private SetOfIdentificators finalStatesIds; // mnozina idciek akceptacnych stavov
    private SetOfIdentificators initialStatesIds; // pociatocne stavy - pripusta sa ich viac
    // aj ked to neni celkom kosher, ale je to kvoli minimalizacii DKA cez reverz automatu
    private Identificator currentStateId; // aktualny stav
    private int numberOfTransitions = 0; // pocet prechodov
    
     /* transition map zadany ako dvojica matic */
    HashMap<Character,Matrix> transitionMap;
    
    
    public Automaton(){
        // parameterless konstruktor
        idStateMap = new HashMap<>();
        allStatesIds = new HashSet<>();
        finalStatesIds = new SetOfIdentificators();
        initialStatesIds = new SetOfIdentificators();
        numberOfTransitions = 0;
    }
    
    /* vymeni prechody na 1 a na 0 v automate - predpoklad je, ze uz mame transition map zadany
    ako dvojicu matic v konstruktore automatu
    */
    public Automaton switchLetters() throws Exception{
        HashMap<Character,Matrix> switchedLettersTransitionMap = new HashMap<>();
        if (this.transitionMap != null){
            
            /* !!!! this improvement works only on 2-letter alphabet */
            if (Variables.alphabet.size() != 2) try {
                throw new Exception("You have to disable some optimisations due to alphabet size - look for !!!! in comments");
            } catch (Exception ex) {
                Logger.getLogger(AutomatonIterator.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*------------------*/
            
            for(int i = 0;i < 2;i++){
                switchedLettersTransitionMap.put(Variables.alphabet.get(i), transitionMap.get(Variables.alphabet.get(1 - i)));
            }
            return new Automaton(switchedLettersTransitionMap,this.initialStatesIds.iterator().next(),this.finalStatesIds);
        }
        else {
            throw new Exception("THIS AUTOMATON HAS NOT TRANSITION MAP IN MATRIX");
        }
    }
    
    
    /* konstruktor automatu na zaklade matice prechodov, mnoziny akc. stavov a pociatocneho stavu */
    public Automaton(HashMap<Character,Matrix> transitionMap, Identificator initialStateId, HashSet<Identificator> finalStatesIds) throws Exception{
        this.idStateMap = new HashMap<>();
        this.allStatesIds = new HashSet<>();
        this.finalStatesIds = new SetOfIdentificators();
        this.initialStatesIds = new SetOfIdentificators();
        this.transitionMap = transitionMap;
        
        int n = transitionMap.get(Variables.alphabet.get(0)).n;
        for (int i = 0;i < n;i++){
            this.addState(new Identificator(i));
        }
        
        for (Character c : Variables.alphabet){
            Matrix transitionsIds = transitionMap.get(c);
            for (int i = 0;i < n;i++){
                for (int j = 0;j < n;j++){
                    if (transitionsIds.get(i, j)){
                        this.addTransition(new Identificator(i), new Identificator(j), c);
                    }
                }
            }
        }
        
        for (Identificator id: finalStatesIds){
            this.finalStatesIds.add(id);
        }
        
        this.setInitialStateId(initialStateId);
    }
    
    public Automaton(Automaton a){ // konstruktor ktoreho cielom je naklonovat automat a
        if (this.currentStateId != null){
            this.currentStateId = a.currentStateId.copy();
        }
        
        this.allStatesIds = new HashSet<>();
        this.initialStatesIds = new SetOfIdentificators();
        
        this.idStateMap = new HashMap<>();
        
        for (Identificator id : a.initialStatesIds){
            this.initialStatesIds.add(id.copy());
        }
        this.numberOfTransitions = a.numberOfTransitions; // pocet prechodov je rovnaky
        for (Identificator id : a.allStatesIds){
            this.allStatesIds.add(id.copy());
            this.idStateMap.put(id.copy(), a.idStateMap.get(id).copy());
        }
        
        this.finalStatesIds = new SetOfIdentificators();
        for (Identificator id : a.finalStatesIds){
            this.finalStatesIds.add(id.copy());
        }
        
    }
    
    
    /* vykonanie prechodu automatu - funguje len pre deterministicke - pre
    nedeterministicke si to pozrie len prvu moznost - nevetvi sa to*/
    public boolean doTransition(Character input) throws NoSuchStateException{
        //System.out.println("Current state is" + this.currentStateId);
        if(!Variables.alphabet.contains(input)){ 
            throw new IllegalArgumentException();
        }
        
        if (idStateMap.get(currentStateId).get(input) == null){
            throw new NoSuchStateException("Can't make this transition");
        }
        
        currentStateId = idStateMap.get(currentStateId).get(input).iterator().next();

        if(currentStateId == null){ 
            throw new IllegalStateException(); 
        }
        
        //.out.println(currentStateId);
        return finalStatesIds.contains(currentStateId);
    }

    
    /*nastavenie pociatocneho stavu pomocou idcka stavu - tento stav sa nastavi
    aj ako current state*/
    public void setInitialStateId(Identificator stateId) throws NoSuchStateException{
        // nastavenie pociatocneho stavu
        if (idStateMap.get(stateId) != null){
            this.initialStatesIds.clear(); // vymazeme predosly pociatocny stav
            this.initialStatesIds.add(stateId);
            setCurrentState(stateId);
        }
        else{ // ak stav nie je v allStates
            throw new NoSuchStateException(stateId);
        }
        //this.hash_cache = BigIntegerTuple.minusOne();//BigInteger.valueOf(-1);//BigIntegerTuple.minusOne(); // resetnutie hash_cache po zmene automatu
    }
    
    
    /* vrati pocet stavov automatu */
    public int numberOfStates(){
        return this.allStatesIds.size();
    }
    
    /* wrapper setInitialStateId to int */
    public void setInitialStateId(int stateId) throws NoSuchStateException{
        this.setInitialStateId(new Identificator(stateId));
    }
    
    /* pridanie pociatocneho stavu prostrednictvom idcka */
    public void addInitialState(Identificator stateId) throws NoSuchStateException{
        State s = idStateMap.get(stateId);
        if (s != null){
            initialStatesIds.add(s.id);
        }
        else{
            throw new NoSuchStateException("FAILED TO ADD FINAL STATE");
        }
        //this.hash_cache = BigIntegerTuple.minusOne();//BigInteger.valueOf(-1);//BigIntegerTuple.minusOne(); // resetnutie hash_cache po zmene automatu
    }
    
    
    /* nastavenie aktualneho stavu automatu */
    public void setCurrentState(Identificator stateId) throws NoSuchStateException{
        if (idStateMap.get(stateId) != null){
            this.currentStateId = stateId;
        }
        else{ // ak stav nie je v allStates
            throw new NoSuchStateException(stateId);
        }
    }
    
    /* vrati true, ak sa podarilo vlozit stav, inak vrati false - ak stav uz vlozeny bol */
    public final boolean addState(Identificator stateId) throws Exception{
        if (allStatesIds.contains(stateId)){
            //System.out.println(allStatesIds + "MARHA");
            
            return false;
            //throw new IdAlreadyExistsException(stateId);
            //return false;
        }
        State s = new State(stateId);
        idStateMap. put(stateId, s);
        allStatesIds.add(stateId);
        //this.hash_cache = BigIntegerTuple.minusOne();//BigInteger.valueOf(-1);//BigIntegerTuple.minusOne(); // resetnutie hash_cache po zmene automatu
        return true;
    }
    
    
    /* wrapper addState to int */
    public void addState(int stateId) throws Exception{
        this.addState(new Identificator(stateId));
    }
    
    
    /* pridanie prechodu do automatu prostrednictvom idciek stavov*/
    public final void addTransition(Identificator idFrom, Identificator idTo, Character c) throws NoSuchStateException, Exception{
        State from = idStateMap. get(idFrom);
        State to = idStateMap. get(idTo);
        
        if ((from != null) && (to != null)){
            boolean isNew = from.addTransition(c, idTo);
            // ak je pridany prechod novy, poznacime si, ze pocet prechodov narastol
            if (isNew) this.numberOfTransitions++;
        }
        else{
            throw new NoSuchStateException("FAILED TO ADD TRANSITION");
        }
        //this.hash_cache = BigIntegerTuple.minusOne();//BigInteger.valueOf(-1);//BigIntegerTuple.minusOne(); // resetnutie hash_cache po zmene automatu
    }
    
    public void addTransition(int idFrom, int idTo, Character c) throws Exception{
        this.addTransition(new Identificator(idFrom), new Identificator(idTo), c);
    }
    
    
    /* pridanie akceptacneho stavu prostrednictvom idcka */
    public void addFinalState(Identificator stateId) throws NoSuchStateException{
        State s = idStateMap.get(stateId);
        if (s != null){
            finalStatesIds.add(s.id);
        }
        else{
            throw new NoSuchStateException("FAILED TO ADD FINAL STATE");
        }
        //this.hash_cache = BigIntegerTuple.minusOne();//BigInteger.valueOf(-1);//BigIntegerTuple.minusOne(); // resetnutie hash_cache po zmene automatu
    }
    
    /* wrapper addFinalState() to int */
    public void addFinalState(int stateId) throws NoSuchStateException{
        this.addFinalState(new Identificator(stateId));
    }
    
    public State getState(Identificator id){
        return idStateMap.get(id);
    }
    
    
    public Automaton determinize(boolean allowTrashState) throws Exception{
        
        // ak sme tento automat determinizovali uz niekedy predty, tak to vyuzijeme
        // vypli sme cachovanie determinizovaneho automatu aby sme usetrili pamat
        /*if (cacheDeterminized != null) {
            return cacheDeterminized;
        }*/
        // determinizacia automatu
        // ak automat nema konecne alebo pociatocne stavy, tak proste vratime prazdny automat
        if ((this.finalStatesIds.isEmpty()) || (this.initialStatesIds.isEmpty())){
            Automaton ret = new Automaton();
            Identificator id = new Identificator(0);
            ret.addState(id);
            ret.setInitialStateId(id);
            for(Character c : Variables.alphabet){
                ret.addTransition(id, id, c); // pridame prechody, nech je to uplna delta funkcia
            }
            return ret;
        }
        
        Automaton pom = new Automaton(this); // naklonujeme nas automat
        
        Automaton ret = new Automaton(); // sem ulozime determinizovany automat
        
        
        int currentMaxStateId = 0; // pamatame si dosial najvyssie pouzite id stavu pri vytvarani DKA

        // pociatocny stav determinizovaneho automatu je set obsahujuci pociatocne stavy nedeterminizovaneho
        // stavy reprezentujuce mnoziny automaticky premenuvame na cisla kvoli rychlejsej praci - nenechaj sa zmiast zbytocne
        
        // zoznam videnych mnozin stavov - ukazuje sa, ze je praktickejsie uz vyrabat automat s celocislenymi stavmi - je to rychlejsie ako pomenuvat stavy mnozinami
        HashMap<SetOfIdentificators, Identificator> MapOfSeenPowerSets = new HashMap<>(); 
        
        // pociatocna mnozina - mnozina pociatocnych stavov v povodnom automate
        SetOfIdentificators InitialPowerSetStatesIds = new SetOfIdentificators(this.initialStatesIds);
        Identificator retInitialStateId = new Identificator(currentMaxStateId++);
        // pridame tuto dvojicu - mnozina s pociatocnymi stavmi + jej ciselny reprezentant do nasej mapy aby sme vedeli rychlo potom prekladat mnoziny na cisla
        MapOfSeenPowerSets.put(InitialPowerSetStatesIds, retInitialStateId);
        
        
        ret.addState(retInitialStateId);
        
        ret.setInitialStateId(retInitialStateId);
        ret.setCurrentState(retInitialStateId);
        
        // fronta - v nej si pamatame este neexpandovane stavy
        Queue<SetOfIdentificators> queue = new LinkedList<>();
        queue.add(InitialPowerSetStatesIds);
        // overime, ci pociatocne stavy nie su zaroven aj niektory z nich akceptacne
        for(Identificator id : InitialPowerSetStatesIds){
            if (pom.finalStatesIds.contains(id)){
                ret.addFinalState(retInitialStateId);
                break;
            }
        }
        
        
        while (!queue.isEmpty()){
            // vyberieme z fronty prvy stav
            SetOfIdentificators currentRetId = queue.peek();
            
            for (Character c : Variables.alphabet){ // prechadzame moznymi znakmi abecedy
                SetOfIdentificators newId = new SetOfIdentificators();
                boolean thisIsFinalState = false; 
                
                // prechadzame cez stavy stareho automatu obsiahnute v stave co prave expandujeme
                for (Identificator IdentificatorInPom : currentRetId){
                    
                    if (pom.getState(IdentificatorInPom).getTransition(c) != null){
                        // iterujeme cez vsetkych susedov aktualneho stavu cez pismeno c
                        for(Identificator identificatorOfTransitionState : pom.getState(IdentificatorInPom).getTransition(c)){
                            // overime, ci prave generovany stav nie je aj akceptacny
                            if (pom.finalStatesIds.contains(identificatorOfTransitionState)){
                                thisIsFinalState = true;
                            }
                            newId.add(identificatorOfTransitionState);
                        }
                    }
                }
                if (!newId.isEmpty()){ // ak je vysledny stav neprazdny, pridame ho
                        
                        // pozreme sa, ci uz nemame tuto mnozinu zaznamenanu, ak ne, tak vytvorime si novy vlastny identifikator
                        Identificator idToAdd = MapOfSeenPowerSets.get(newId);
                        if (idToAdd == null){ 
                        // ak sme este takouto mnozinou nepresli, tak vytvorime novy stav, co ju bude reprezentovat
                            idToAdd = new Identificator(currentMaxStateId++);
                            MapOfSeenPowerSets.put(newId,idToAdd);
                            ret.addState(idToAdd);
                            // novo objavene mnoziny pridame do fronty, nech preskumame ich susedov vzhladom na delta funkciu
                            queue.add(newId);
                        }
                        // ak mnozina obsahovala konecny stav, tak konecny stav to bude aj vo vyslednom automate
                        if (thisIsFinalState){
                            ret.finalStatesIds.add(idToAdd);
                        }
                    ret.addTransition(MapOfSeenPowerSets.get(currentRetId), MapOfSeenPowerSets.get(newId), c);
                }
                // toto chcelo byt pridanie odpadoveho stavu - zbytocne?
                else if (allowTrashState){
                    // ak sme este odpadovy stav neevidovali, tak ho pridame
                    if (!MapOfSeenPowerSets.containsKey(newId)){
                        Identificator idToAdd = new Identificator(currentMaxStateId++);
                        ret.addState(idToAdd);
                        MapOfSeenPowerSets.put(newId, idToAdd);
                    }
                    ret.addTransition(MapOfSeenPowerSets.get(currentRetId), MapOfSeenPowerSets.get(newId), c);
                }
            }
            queue.remove();
        }
        
        //.out.println(ret.allStatesIds);
        // ak niektore stavy smeruju do prazdnej mnoziny, tak tej prazdnej mnozine nastavime, nech sa cykli do seba na kazdom pismene
        SetOfIdentificators emptyState = new SetOfIdentificators();
        Identificator emptyStateInRet = MapOfSeenPowerSets.get(emptyState);
        if ((allowTrashState) && (emptyStateInRet != null)){
            for (Character c : Variables.alphabet){
                ret.addTransition(emptyStateInRet, emptyStateInRet, c);
            }
        }
        return ret;
    }
    
    
    public Automaton reverse() throws Exception{
        Automaton pom = new Automaton(this);
        Automaton ret = new Automaton();
        
        ret.initialStatesIds = pom.finalStatesIds;
        ret.finalStatesIds = pom.initialStatesIds;
        
        // ak nemame ziadne konecne alebo pociatocne stavy, tak vratime jednoducho prazdny automat
        if ((ret.finalStatesIds.isEmpty()) || (ret.initialStatesIds.isEmpty())) return new Automaton();
        // aktualny stav vysledneho automatu nastavime na niektory z jeho pociatocnych stavov
        ret.currentStateId = ret.initialStatesIds.iterator().next();
        
        for (Identificator id : pom.allStatesIds){
            ret.addState(id);
        }
        
        
        for(Identificator pomStateFromId : pom.allStatesIds){
            State s = idStateMap.get(pomStateFromId);
            for(Character c : Variables.alphabet){
                if(s.get(c) != null){
                    for(Identificator pomStateToId : s.get(c)){
                        ret.addTransition(pomStateToId, pomStateFromId, c);
                    }
                }
            }
        }
        
        return ret;
    }
    
    
    @Override
    public String toString(){
        // TODO
        String ret = "The states are " + this.allStatesIds.toString() + "\n" +
                "The transitions: \n";
        for (Identificator id : this.allStatesIds){
            for (Character c : Variables.alphabet){
                if (this.getState(id).getTransition(c) != null){
                    ret = ret + id.toString() + "-" + c + "->" + this.getState(id).getTransition(c) + "\n";
                }
            }
        }
        ret = ret + "The initial states: " + this.initialStatesIds.toString() + "\n";
        ret = ret + "The final states: " + this.finalStatesIds.toString() + "\n";
        return ret;
    }
    

    /* metoda na vypisanie automatu - lepsie parsovatelne ako to, co vypise toString() */
    public void print(FastPrint out, long counter) throws IOException, Exception{
        // vypis poctu stavov
        
        if (counter != -1){
            out.println("/" + Long.valueOf(counter).toString());
        }
        out.println(new Integer(this.allStatesIds.size()).toString());
        // vypis pociatocneho stavu
        if(this.initialStatesIds.iterator().hasNext()){
            out.println(this.initialStatesIds.iterator().next().toString());
        }
        else{
            out.println("-1"); // ak to nahodou nema pociatocny stav
        }
        // vypis poctu konecnych stavov
        out.println(new Integer(this.finalStatesIds.size()).toString());
        // vypis konecnych stavov
        for(Identificator id : this.finalStatesIds){
            out.println(id.toString());
        }
        
        out.println(new Integer(this.numberOfTransitions).toString());
        for (Identificator idFrom : this.allStatesIds){
            for (Character c : Variables.alphabet){
                if (this.getState(idFrom).getTransition(c) != null){
                    for(Identificator idTo : this.getState(idFrom).getTransition(c)){
                        out.println(idFrom.toString() + " " + idTo.toString() + " "+ c);
                    }
                }
            }
        }
        
        if (counter != -1) {
            this.minimalDFA().print(out, -1);
            out.println("----");
        }
        
        if (counter != -1){
            Variables.outputStream.println(this.getNumberOfStates() + " " + this.minimalDFA().getNumberOfStates());
        }
   }
    
    
    /* vrati pocet stavov automatu */
    public int getNumberOfStates(){
        return allStatesIds.size();
    }
    
    /* vrati pociatocne stavy */
    public HashSet<Identificator> getInitialStatesIds(){
        return this.initialStatesIds;
    }
    
    /* vrati konecne stavy */
    public HashSet<Identificator> getFinalStatesIds(){
        return this.finalStatesIds;
    }
    
    
    Automaton cache_minDFA = null;
    /* vrati to minimalny DFA z nasho automatu */
    public Automaton minimalDFA() throws Exception{
        if (cache_minDFA != null){
            return cache_minDFA;
        }
        Automaton pom = new Automaton(this);
        // odpadove stavy tu nie su vitane, preto false
        Automaton cache_minDFA = pom.determinize(Variables.disableTrashState).reverse().determinize(Variables.disableTrashState).reverse().determinize(Variables.allowTrashState);
        return cache_minDFA;
    }
    
    
    /* tato metoda vyrobi automat komplementarny k tomu nasmu - prehodi akceptacne
    a neakceptacne stavy */
    public Automaton complement(){
        Automaton ret = new Automaton(this);
        SetOfIdentificators complementFinalStatesIds = new SetOfIdentificators();
        for(Identificator id : ret.allStatesIds){
            if (!ret.finalStatesIds.contains(id)){
                complementFinalStatesIds.add(id);
            }
        }
        ret.finalStatesIds = complementFinalStatesIds;
        return ret;
    }

    
    /* vrati true, ak automat akceptuje prazdny jazyk, inak false */
    public boolean emptyLanguage(){
        // prazdnost jazyka zistujeme prehladavanim do sirky
        if ((this.finalStatesIds.isEmpty()) || (this.initialStatesIds.isEmpty())){
            return true;
        }
        
        Queue<Identificator> queue = new LinkedList<>();
        HashSet<Identificator> seen = new HashSet<>();
        
        for(Identificator id : this.initialStatesIds){
            queue.add(id);
            seen.add(id);
        }
        
        
        while(!queue.isEmpty()){
            Identificator currentStateId = queue.peek();
            if (this.finalStatesIds.contains(currentStateId)){
                return false;
            }
            
            for(Character c : Variables.alphabet){
                if (this.getState(currentStateId).getTransition(c) != null){
                    for(Identificator id : this.getState(currentStateId).getTransition(c)){
                        if(!seen.contains(id)){
                            queue.add(id);
                            seen.add(id);
                        }
                    }
                }
            }
            queue.remove();
        }
            
        return true;
    }
    
    
    /* tato metoda z minimalneho DFA vyrobi hashovaciu dvojicu, pomocou ktorej 
    budeme vediet jednoznacne porovnavat automaty 
    MYSLIENKA - minimalny DFA prevedieme na kanonicky - s presne urcenym pomenovanim
    stavov a z matice susednosti vyplodime hash
    */
    private static Tuple hashFromMinDFA(Automaton minA) throws Exception{
        Queue<Identificator> statesToVisit = new LinkedList<>();
        // pridame do fronty pociatocny stav
        statesToVisit.add(minA.initialStatesIds.iterator().next());
        SetOfIdentificators seen = new SetOfIdentificators(); 
        seen.add(minA.initialStatesIds.iterator().next());
        int counter = 0;
        // tuna si pamatame prevody medzi stavmi
        HashMap<Integer, Identificator> renumberingMapFromCanonicalToMin = new HashMap<>();
        HashMap<Identificator, Integer> renumberingMapFromMinToCanonical = new HashMap<>();
        while(!statesToVisit.isEmpty()){
            // precislujeme stavy podla poradia v akom ich navstivime
            renumberingMapFromCanonicalToMin.put(counter, statesToVisit.peek());
            renumberingMapFromMinToCanonical.put(statesToVisit.peek(), counter);
            counter++;
            // pridavame do fronty susedov podla toho, aky prechod tam vedie
            for(Character c : Variables.alphabet){
                for(Identificator id : minA.getState(statesToVisit.peek()).getTransition(c)){
                    if (!seen.contains(id)){
                        statesToVisit.add(id);
                        seen.add(id);
                    }
                }
            }
            statesToVisit.remove(); // popneme z fronty prave navstiveny stav
        }
        BigInteger mat = BigInteger.valueOf(0);
        // teraz z toho vyrobime maticu susednosti
        for(Character c : Variables.alphabet){
            Matrix m = new Matrix(counter); // proste tolko stavov, co ma ten minimalny NFA
            
            // teraz sa ideme vnorit do automatu a zratat matice prechodu pre jednotlive znaky
            for(int i = 0;i < counter; i++){
                Identificator idFrom = renumberingMapFromCanonicalToMin.get(i);
                for(Identificator idTo : minA.getState(idFrom).getTransition(c)){
                    m.set(i, renumberingMapFromMinToCanonical.get(idTo), true);
                }
            }
            
            // ciselne reprezentacie matic zretazime za seba a ulozime do jedneho BigIntegeru
            mat = mat.shiftLeft(minA.numberOfStates() * minA.numberOfStates()).add(m.getNumericRepresentationDFA());
            
        }
        
        SetOfIdentificators canonicalFinalStates = new SetOfIdentificators();
        for(Identificator id : minA.finalStatesIds){
            canonicalFinalStates.add(new Identificator(renumberingMapFromMinToCanonical.get(id)));
        }
        
        return new Tuple(mat,canonicalFinalStates.getBitMap());
    }
    
    /* prehladavanie vsetkych moznych vygenerovanych slov do hlbky, resp. dlzky maxDepth 
       najdene slova sa ulozia do HashSetu generatedWords
    */
    private void dfsWordsSearch(int maxDepth, Identificator stateId, String currentWord, HashSet<String> generatedWords) throws Exception{
        if((maxDepth >= 0)&&(this.finalStatesIds.contains(stateId))){
            generatedWords.add(new String(currentWord));
        }
        if (maxDepth == 0){
            return;
        }
        
        
        // pokial su stavy cele cisla a pokial mame danu tranisition map
        if((this.transitionMap != null) && (stateId.getClass() == Identificator.class)){
            for(Character c : Variables.alphabet){
                for(int id = 0;id < this.getNumberOfStates();id++){
                    if(this.transitionMap.get(c).get(((Identificator) stateId).getValue(),id)){
                        dfsWordsSearch(maxDepth - 1,new Identificator(id),currentWord + c,generatedWords);
                        // urezeme posledny znak aby sme sa mohli vratit spat
                        //currentWord = currentWord.substring(0, currentWord.length()-1);
                    }
                }
            }
        }
        else{
            for(Character c : Variables.alphabet){
                if(this.getState(stateId).getTransition(c) != null){
                    for(Identificator id : this.getState(stateId).getTransition(c)){
                        dfsWordsSearch(maxDepth - 1,id,currentWord + c,generatedWords);
                        // urezeme posledny znak aby sme sa mohli vratit spat
                        //currentWord = currentWord.substring(0, currentWord.length()-1);
                    }
                }
            }
        }
    }
    
    public HashSet<String> allWordsOfLength(int n) throws Exception{
        HashSet<String> ret = new HashSet<>();
        for(Identificator id : this.initialStatesIds){
            dfsWordsSearch(n, id, "", ret);
        }
        return ret;
    }

    
    /* vyrata to hashCode s akceptovanych slov do dlzky length
       - predpoklada sa, ze Variables.wordToNumberMap je inicializovana
    */
    public BigInteger hashCodeFromWords(int length) throws Exception{
        HashSet<String> words = this.allWordsOfLength(length);
        BigInteger ret = BigInteger.ZERO;
        for(String word : words){
            ret = ret.add(BigInteger.ONE.shiftLeft(Variables.wordToNumberMap.get(word)));
        }
        return ret;
    }
    
    
    /* hash_cache - premenna, kde si zapamatame hashCode automatu, aby sme ho nemuseli opakovane ratat */
    //BigInteger hash_cache = BigInteger.valueOf(-1);
    Tuple hash_cache = Tuple.minusOne();
    
    public Tuple myHashCode() throws Exception{
        if (!hash_cache.equals(Tuple.minusOne())){
            return hash_cache;
        }
        hash_cache = hashFromMinDFA(this.minimalDFA());
        return hash_cache;
    }
    
    
    /* vytvori matice susednosti pre tento automat - nemusi ich totiz mat defaultne, 
    zavisi od konstruktora*/
    public void buildTransitionMap(){
        this.transitionMap = new HashMap<>();
        int maxId = 0;
        // zistime najvacsie id a podla neho nadimenzujeme maticu
        for(Identificator id : this.allStatesIds){
            if (id.getValue() > maxId){
                maxId = id.getValue();
            }
        }
        
        for(Character c : Variables.alphabet){
            Matrix m = new Matrix(maxId + 1);
            for(Identificator idFrom : this.allStatesIds){
                if (this.idStateMap.get(idFrom).getTransition(c) != null){
                    for(Identificator idTo : this.idStateMap.get(idFrom).getTransition(c)){
                        // pre kombinaciu idFrom,idTo zaznacime, ze je medzi nimi prechod
                        m.set(idFrom.getValue(), idTo.getValue(), true);
                    }
                }
            }
            this.transitionMap.put(c, m);
        }
    }
    
    
    
    /* 
    nacitanie automatu zo suboru, predpokladany format vstupu:
    initial state is fixed to 0 the format of output is the following
    /number of the automaton
    number of states
    id of initial state (-1 if none)
    number of final states followed by final states enumeration
    number of transitions followed by its enumeration
    from_state to_state character
    */
    public static Automaton readAutomaton(Scanner s) throws FileNotFoundException, Exception{
        Automaton ret = new Automaton();
        while(!s.hasNextInt()){
            if (!s.hasNext()) return null;
            s.nextLine();
        }
        int numberOfStates = s.nextInt();
        for(int i = 0;i < numberOfStates;i++) ret.addState(i);
            
        int initialStateId = s.nextInt();
        ret.setInitialStateId(initialStateId);
            
        int numberOfFinalStates = s.nextInt();
        for(int i = 0;i < numberOfFinalStates;i++){
            ret.addFinalState(s.nextInt());
        }
            
        int numberOfTransitions = s.nextInt();
        for(int i = 0;i < numberOfTransitions;i++){
            int idFrom = s.nextInt();
            int idTo = s.nextInt();
            Character c = s.next().charAt(0);
            ret.addTransition(idFrom, idTo, c);
        }
        ret.buildTransitionMap();
        return ret;
    }
    
    
    // more methods go here
}     
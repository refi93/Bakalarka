/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
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


/* tato vynimka sa vyhodi ked sa pokusime pridat uz existujuce id stavu do automatu*/
class IdAlreadyExistsException extends Exception
{
      //Parameterless Constructor
      public IdAlreadyExistsException(Object id) {
          //System.out.println("ID " + id + " ALREADY EXISTS");
      }

      //Constructor that accepts a message
      public IdAlreadyExistsException(String message)
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
    private HashSet<Identificator> finalStatesIds; // mnozina idciek akceptacnych stavov
    private HashSet<Identificator> initialStatesIds; // pociatocne stavy - pripusta sa ich viac
    // aj ked to neni celkom kosher, ale je to kvoli minimalizacii DKA cez reverz automatu
    private Identificator currentStateId; // aktualny stav
    private int numberOfTransitions = 0; // pocet prechodov
    
     /* transition map zadany ako dvojica matic */
    HashMap<Character,Matrix> transitionMap;
    
    
    public Automaton(){
        // parameterless konstruktor
        idStateMap = new HashMap<>();
        allStatesIds = new HashSet<>();
        finalStatesIds = new HashSet<>();
        initialStatesIds = new HashSet<>();
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
        this.finalStatesIds = new HashSet<>();
        this.initialStatesIds = new HashSet<>();
        this.transitionMap = transitionMap;
        
        int n = transitionMap.get(Variables.alphabet.get(0)).n;
        for (int i = 0;i < n;i++){
            this.addState(new IntegerIdentificator(i));
        }
        
        for (Character c : Variables.alphabet){
            Matrix transitionsIds = transitionMap.get(c);
            for (int i = 0;i < n;i++){
                for (int j = 0;j < n;j++){
                    if (transitionsIds.get(i, j)){
                        this.addTransition(new IntegerIdentificator(i), new IntegerIdentificator(j), c);
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
        this.initialStatesIds = new HashSet<>();
        
        this.idStateMap = new HashMap<>();
        
        for (Identificator id : a.initialStatesIds){
            this.initialStatesIds.add(id.copy());
        }
        this.numberOfTransitions = a.numberOfTransitions; // pocet prechodov je rovnaky
        for (Identificator id : a.allStatesIds){
            this.allStatesIds.add(id.copy());
            this.idStateMap.put(id.copy(), a.idStateMap.get(id).copy());
        }
        
        this.finalStatesIds = new HashSet<>();
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
        this.hash_cache = BigInteger.valueOf(-1); // resetnutie hash_cache po zmene automatu
    }
    
    /* wrapper setInitialStateId to int */
    public void setInitialStateId(int stateId) throws NoSuchStateException{
        this.setInitialStateId(new IntegerIdentificator(stateId));
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
        this.hash_cache = BigInteger.valueOf(-1); // resetnutie hash_cache po zmene automatu
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
            throw new IdAlreadyExistsException(stateId);
            //return false;
        }
        State s = new State(stateId);
        idStateMap. put(stateId, s);
        allStatesIds.add(stateId);
        this.hash_cache = BigInteger.valueOf(-1); // resetnutie hash_cache po zmene automatu
        return true;
    }
    
    
    /* wrapper addState to int */
    public void addState(int stateId) throws Exception{
        this.addState(new IntegerIdentificator(stateId));
    }
    
    
    /* pridanie prechodu do automatu prostrednictvom idciek stavov*/
    public final void addTransition(Identificator idFrom, Identificator idTo, Character c) throws NoSuchStateException, Exception{
        State from = idStateMap. get(idFrom);
        State to = idStateMap. get(idTo);
        
        // vymazeme stare zaznamy, aby sme ich nasledne aktualizovali
        idStateMap.remove(idFrom);
        
        if ((from != null) && (to != null)){
            boolean isNew = from.addTransition(c, idTo);
            // ak je pridany prechod novy, poznacime si, ze pocet prechodov narastol
            if (isNew) this.numberOfTransitions++;
        }
        else{
            throw new NoSuchStateException("FAILED TO ADD TRANSITION");
        }
        this.hash_cache = BigInteger.valueOf(-1); // resetnutie hash_cache po zmene automatu
        // vlozime naspat aktualizovany zaznam
        idStateMap.put(idFrom, from);
    }
    
    public void addTransition(int idFrom, int idTo, Character c) throws Exception{
        this.addTransition(new IntegerIdentificator(idFrom), new IntegerIdentificator(idTo), c);
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
        this.hash_cache = BigInteger.valueOf(-1); // resetnutie hash_cache po zmene automatu
    }
    
    /* wrapper addFinalState() to int */
    public void addFinalState(int stateId) throws NoSuchStateException{
        this.addFinalState(new IntegerIdentificator(stateId));
    }
    
    public State getState(Identificator id){
        return idStateMap.get(id);
    }
    
    // tu si pamatame, ci a ako sme determinizovali niekedy predtym tento automat
    Automaton cacheDeterminized = null;
    
    public Automaton determinize(boolean allowTrashState) throws Exception{
        
        // ak sme tento automat determinizovali uz niekedy predty, tak to vyuzijeme
        if (cacheDeterminized != null) {
            return cacheDeterminized;
        }
        // determinizacia automatu
        // ak automat nema konecne alebo pociatocne stavy, tak proste vratime prazdny automat
        if ((this.finalStatesIds.isEmpty()) || (this.initialStatesIds.isEmpty())){
            Automaton ret = new Automaton();
            ret.addState(new PowerSetIdentificator());
            return ret;
        }
        
        Automaton pom = new Automaton(this); // naklonujeme nas automat
        
        Automaton ret = new Automaton(); // sem ulozime determinizovany automat
        
        // pociatocny stav determinizovaneho automatu je set obsahujuci pociatocne stavy nedeterminizovaneho
        PowerSetIdentificator retInitialStatesIds = new PowerSetIdentificator(this.initialStatesIds);
        
        ret.addState(retInitialStatesIds);
        
        ret.setInitialStateId(retInitialStatesIds);
        ret.setCurrentState(retInitialStatesIds);
        
        // fronta - v nej si pamatame este neexpandovane stavy
        Queue<PowerSetIdentificator> queue = new LinkedList<>();
        queue.add(retInitialStatesIds);
        // overime, ci pociatocne stavy nie su zaroven aj niektory z nich akceptacne
        for(Identificator id : retInitialStatesIds){
            if (pom.finalStatesIds.contains(id)){
                ret.addFinalState(retInitialStatesIds);
            }
        }
        
        
        while (!queue.isEmpty()){
            // vyberieme z fronty prvy stav
            PowerSetIdentificator currentRetId = queue.peek();
            
            for (Character c : Variables.alphabet){ // prechadzame moznymi znakmi abecedy
                PowerSetIdentificator newId = new PowerSetIdentificator();
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
                    try{
                        if (thisIsFinalState){
                            ret.finalStatesIds.add(newId);
                        }
                        ret.addState(newId);
                        queue.add(newId);
                    }
                    catch(Exception e){
                        //System.out.println("ale nic");
                    }
                    ret.addTransition(currentRetId, newId, c);
                }
                // toto chcelo byt pridanie odpadoveho stavu - zbytocne
                else if (allowTrashState){
                    if (!ret.allStatesIds.contains(newId)){
                        ret.addState(newId);
                    }
                    ret.addTransition(currentRetId, newId, c);
                }
            }
            queue.remove();
        }
        
        //.out.println(ret.allStatesIds);
        // ak niektore stavy smeruju do prazdnej mnoziny, tak tej prazdnej mnozine nastavime, nech sa cykli do seba na kazdom pismene
        PowerSetIdentificator emptyState = new PowerSetIdentificator();
        if (ret.allStatesIds.contains(emptyState)){
            for (Character c : Variables.alphabet){
                ret.addTransition(emptyState, emptyState, c);
            }
        }
        this.cacheDeterminized = ret;
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
    public void print(FastPrint out) throws IOException, Exception{
        // vypis poctu stavov
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
    }
    
    
    /* overi, ci je dany automat deterministicky */
    public boolean isDeterministic(){
        for (Object stateId : allStatesIds) {
            if (!idStateMap.get(stateId).isDeterministic()){
                return false;
            }
        }
        return true;
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
    
    /* vrati to minimalny DFA z nasho automatu */
    public Automaton minimalDFA() throws Exception{
        Automaton pom = new Automaton(this);
        // odpadove stavy tu nie su vitane, preto false
        Automaton ret = pom.determinize(Variables.disableTrashState).reverse().determinize(Variables.disableTrashState).reverse().determinize(Variables.allowTrashState);
        return ret;
    }
    
    
    /* vrati automat s "normalne" pomenovanymi stavmi, cize to budu cele cisla a nie mnoziny */
    public Automaton normalize() throws IdCollisionException, NoSuchStateException{
        Automaton ret = new Automaton(this);
        int counter = 0;
        for (Identificator id : this.allStatesIds){ // this.allStatesIds, lebo ret.allStatesIds sa bude menit za behu cyklu a to by Java nerozchodila
            ret.replaceStateId(id, new IntegerIdentificator(counter));
            counter++;
        }
        return ret;
    }
    
    
    /* zamena idcka nejakeho stavu v automate */
    public void replaceStateId(Identificator oldId,Identificator newId) throws IdCollisionException, NoSuchStateException{
        if (this.allStatesIds.contains(newId)){
            throw new IdCollisionException(newId);
        }
        else if (!this.allStatesIds.contains(oldId)){
            throw new NoSuchStateException(oldId);
        }
        else{
            // najprv kazdemu stavu dame vediet, ze sa zmenilo idcko stavu
            for (Identificator id : this.allStatesIds){
                State s = this.idStateMap.get(id);
                s.replaceStateId(oldId,newId);
                this.idStateMap.remove(id);
                if (id.equals(oldId)){ // ked ideme aktualizovat stav s idckom, ktore prave menime
                    this.idStateMap.put(newId, s);
                }
                else{
                    this.idStateMap.put(id, s);
                }
            }
            
            // potom prepiseme idcko stavu aj v nasej mnozine vsetkych stavov
            this.allStatesIds.remove(oldId);
            this.allStatesIds.add(newId);
            
            // aktualizujeme aj aktualny stav automatu
            if(this.currentStateId != null){
                if (this.currentStateId.equals(oldId)){
                    this.currentStateId = newId;
                }
            }
            
            // aktualizujeme akceptacne stavy automatu
            if (this.finalStatesIds.contains(oldId)){
                this.finalStatesIds.remove(oldId);
                this.finalStatesIds.add(newId);
            }
            
            // aktualizujeme pociatocne stavy outomatu
            if (this.initialStatesIds.contains(oldId)){
                this.initialStatesIds.remove(oldId);
                this.initialStatesIds.add(newId);
            }
        }
        //System.out.println(this.allStatesIds);
    }
    
    
    /* tato metoda vyrobi automat komplementarny k tomu nasmu - prehodi akceptacne
    a neakceptacne stavy */
    public Automaton complement(){
        Automaton ret = new Automaton(this);
        HashSet<Identificator> complementFinalStatesIds = new HashSet<>();
        for(Identificator id : ret.allStatesIds){
            if (!ret.finalStatesIds.contains(id)){
                complementFinalStatesIds.add(id);
            }
        }
        ret.finalStatesIds = complementFinalStatesIds;
        return ret;
    }
    
    
    /* vyratanie kartezskeho sucinu s druhym automatom */
    public Automaton intersect(Automaton b) throws Exception{
        Automaton pomA = new Automaton(this);
        Automaton pomB = new Automaton(b);
        Automaton ret = new Automaton();
        
        // vytvorime stav pre kazdu dvojicu stavov z A a B
        for (Identificator idA : pomA.allStatesIds){
            for (Identificator idB : pomB.allStatesIds){
                TupleIdentificator cartesian = new TupleIdentificator(idA,idB);
                ret.addState(cartesian);
                
                if ((pomA.finalStatesIds.contains(idA)) && (pomB.finalStatesIds.contains(idB))){
                    ret.addFinalState(cartesian);
                }
                
                if ((pomA.initialStatesIds.contains(idA)) && (pomB.initialStatesIds.contains(idB))){
                    ret.addInitialState(cartesian);
                }
            }
        }
        
        // [a,b] -c-> [a',b'] <-> [a] -c-> [a'] a zaroven [b]-c->[b'], kde c je znak z abecedy
        for (Identificator idA : pomA.allStatesIds){
            for (Identificator idB : pomB.allStatesIds){
                for (Character c : Variables.alphabet){
                    if (pomA.getState(idA).getTransition(c) != null){
                        for (Identificator idATransition : pomA.getState(idA).getTransition(c)) {
                            if (pomB.getState(idB).getTransition(c) != null){
                                for (Identificator idBTransition : pomB.getState(idB).getTransition(c)){
                                    ret.addTransition(new TupleIdentificator(idA,idB), new TupleIdentificator(idATransition,idBTransition), c);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // ak automat nema pociatocne stavy, vratime prazdny automat
        if (ret.initialStatesIds.isEmpty()){
            return new Automaton();
        }
        // nastavime aktualny stav na pociatocny
        ret.currentStateId = ret.initialStatesIds.iterator().next();
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
    
    
    /* vrati true, ak nas automat je ekvivalentny automatu b, inak false */
    public boolean equivalent(Automaton b) throws Exception{
        // zdeterminizujeme oba automaty a porovname, ci prienik s komplementom je prazdny
        // na komplement treba odpadove stavy
        Automaton detA = this.determinize(Variables.allowTrashState);
        Automaton detB = b.determinize(Variables.allowTrashState);
        
        boolean aEmpty = detA.emptyLanguage();
        boolean bEmpty = detB.emptyLanguage();
        
        if (((aEmpty) && (!bEmpty)) || ((!aEmpty) && (bEmpty))){
            return false;
        }
        else if (aEmpty && bEmpty){
            return true;
        }
        
        Automaton pom1 = detA.intersect(detB.complement());
        Automaton pom2 = detB.intersect(detA.complement());
        return ((pom1.emptyLanguage()) && (pom2.emptyLanguage()));
    }
    
    
    /* prehladavanie vsetkych moznych vygenerovanych slov do hlbky, resp. dlzky maxDepth 
       najdene slova sa ulozia do HashSetu generatedWords
    */
    private void dfsWordsSearch(int maxDepth, Identificator stateId, BinaryWord currentWord, HashSet<BinaryWord> generatedWords) throws Exception{
        if((maxDepth >= 0)&&(this.finalStatesIds.contains(stateId))){
            generatedWords.add(new BinaryWord(currentWord));
        }
        if (maxDepth == 0){
            return;
        }
        
        
        // pokial su stavy cele cisla a pokial mame danu tranisition map
        if((this.transitionMap != null) && (stateId.getClass() == IntegerIdentificator.class)){
            for(Character c : Variables.alphabet){
                for(int id = 0;id < this.getNumberOfStates();id++){
                    if(this.transitionMap.get(c).get(((IntegerIdentificator) stateId).getValue(),id)){
                        dfsWordsSearch(maxDepth - 1,new IntegerIdentificator(id),currentWord.append(Character.getNumericValue(c)),generatedWords);
                        currentWord.cutLast();
                    }
                }
            }
        }
        else{
            for(Character c : Variables.alphabet){
                if(this.getState(stateId).getTransition(c) != null){
                    for(Identificator id : this.getState(stateId).getTransition(c)){
                        dfsWordsSearch(maxDepth - 1,id,currentWord.append(Character.getNumericValue(c)),generatedWords);
                        currentWord.cutLast();
                    }
                }
            }
        }
    }
    
    public HashSet<BinaryWord> allWordsOfLength(int n) throws Exception{
        HashSet<BinaryWord> ret = new HashSet<>();
        for(Identificator id : this.initialStatesIds){
            dfsWordsSearch(n, id, new BinaryWord(), ret);
        }
        return ret;
    }

    
    /* hash_cache - premenna, kde si zapamatame hashCode automatu, aby sme ho nemuseli opakovane ratat */
    BigInteger hash_cache = BigInteger.valueOf(-1);
    
    /* hashCode automatu - je to vlastne hashCode slov do dlzky 4, ktore vygeneruje */
    public BigInteger myHashCode() throws Exception{
        if (hash_cache.equals(BigInteger.valueOf(-1))){
            hash_cache = BigInteger.valueOf(0);
            HashSet<BinaryWord> words = allWordsOfLength(Variables.hashWordLength);
            if (Variables.alphabet.size() == 2){
                for(BinaryWord word : words){
                    int pos = Variables.WordToNumberMap.get(word);
                    hash_cache = hash_cache.setBit(pos);
                    //hash_cache = hash_cache | (((long)1) << pos);
                }
                hash_cache = new BigInteger(hash_cache.toString() + myHashCode2(Variables.hashString1).value + myHashCode2(Variables.hashString2).value);
            }
            else {
                hash_cache = BigInteger.valueOf(words.hashCode());
            }
        }
        return hash_cache;
    }
    
    
    public BinaryWord myHashCode2(String hashString) throws Exception{
        int charIndex = 0; //index znaku v hashovacom slove 
        PowerSetIdentificator currentStates = new PowerSetIdentificator(this.initialStatesIds);
        BinaryWord ret = new BinaryWord();
        
        while((charIndex < hashString.length())&&(!currentStates.isEmpty())){
            Character c = hashString.charAt(charIndex);
            
            PowerSetIdentificator newStates = new PowerSetIdentificator();
            boolean isFinalState = false;
            for(Identificator id : currentStates){
                if (this.getState(id).getTransition(c) != null){
                    for(Identificator transitionStateId : this.getState(id).getTransition(c)){
                        newStates.add(transitionStateId);
                        if(this.finalStatesIds.contains(transitionStateId)){
                            isFinalState = true;
                        }
                    }
                }
            }
            
            currentStates = newStates;
            ret.append(isFinalState?1:0);
            charIndex++;
        }
        
        while(ret.length < Variables.hashString1.length()){
            ret.append(0);
        }
        return ret;
    }
    // more methods go here
}     
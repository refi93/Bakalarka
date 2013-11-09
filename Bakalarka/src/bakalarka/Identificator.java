/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

/**
 *
 * @author raf
 * tento interface urcuje, ako by mal vyzerat identifikator stacu automatu
 */
public interface Identificator {
    abstract public Identificator copy();
    @Override
    abstract public int hashCode();
    @Override
    abstract public boolean equals(Object o);
}

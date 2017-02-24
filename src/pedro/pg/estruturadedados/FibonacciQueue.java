/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.pg.estruturadedados;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author administrador
 */
public class FibonacciQueue extends FibonacciHeap
{
    //private HashMap<Integer, FibonacciHeap.FibNode> fstore = new HashMap<>();
    private Map<Integer, FibNode> fstore = new HashMap<>();
    
    public FibNode insereNodo( int id, int peso )
    {
        FibNode x = super.push(id, peso);
        fstore.put(x.getIdVertice(), x);
        return x;
    }
    
    public void decreaseK( FibNode x, int k )
    {
        fstore.remove(x.getIdVertice());
        //fstore.put(k, x)
    }
    
}

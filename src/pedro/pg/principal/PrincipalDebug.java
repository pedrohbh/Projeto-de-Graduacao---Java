/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.pg.principal;

import pedro.pg.estruturadedados.FibonacciHeap;
import pedro.pg.estruturadedados.FibonacciHeap.FibNode;

/**
 *
 * @author administrador
 */
public class PrincipalDebug 
{
    public static void main(String[] args) 
    {
        FibonacciHeap<Integer> heap;
        heap = new FibonacciHeap<>();
        FibNode nodo1 = heap.criaNovoNodo( 1, 2);
        FibNode nodo2 = heap.criaNovoNodo( 2, 4);
        FibNode nodo3 = heap.criaNovoNodo( 3, 1);
        
        heap.insert(nodo1);
        heap.insert(nodo2);
        heap.insert(nodo3);
        heap.decrease_key(nodo2, 1);
        heap.decrease_key( nodo1, 0);
        FibNode minimo = heap.extractMin();
        minimo = heap.extractMin();
        minimo = heap.extractMin();
        System.out.println("SUCESS");
        
        //FibNode<Integer> nodo1;
        
        
    }
    
}

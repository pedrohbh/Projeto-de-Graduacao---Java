/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.pg.principal;

import pedro.pg.estruturadedados.FibonacciHeap;

/**
 *
 * @author administrador
 */
public class PrincipalDebug 
{
    public static void main(String[] args) 
    {
        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.FibNode nodo1 = heap.criaNodo(0, 0);
        FibonacciHeap.FibNode nodo2 = heap.criaNodo( 1,  1);
        FibonacciHeap.FibNode nodo3 = heap.criaNodo( 3, 3 );
        heap.insert(nodo1);
        heap.insert(nodo2);
        heap.insert(nodo3);
        //heap.decreaseKey(nodo3, 0);
        //heap.decreaseKey(nodo2, 0);
        FibonacciHeap.FibNode atualNodo = heap.extractMin();
        heap.decreaseKey(nodo3, 0);
        atualNodo = heap.extractMin();
        atualNodo = heap.extractMin();
        atualNodo = heap.extractMin();
        System.out.println("Sucess");
        //nodo1 = new FibonacciHeap.FibNode(0, 0);
        
        /*FibonacciHeapPrototipo<Integer> heap;
        heap = new FibonacciHeapPrototipo<>();
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
        
        //FibNode<Integer> nodo1;*/
        
        
    }
    
}

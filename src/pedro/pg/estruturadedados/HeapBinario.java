/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.pg.estruturadedados;

/**
 *
 * @author administrador
 */
public class HeapBinario 
{
    private NodoHeapBinario []heap;
    private int tamanhoHeap;
    
    public HeapBinario( int tamanho )
    {
        heap = new NodoHeapBinario[tamanho];
        tamanhoHeap = 0;
    }
    
    // Parent(i) return [i/2]
    public int getParent( int i )
    {
        return Math.floorDiv(i, 2);
        //return i / 2;
    }
    
    public int getLeft( int i )
    {
        return ( 2*i + 1 );
    }
    
    public int getRight( int i )
    {
        return ( 2*i + 2 );
    }
    
    private void swap( NodoHeapBinario n1, NodoHeapBinario n2 )
    {
        NodoHeapBinario temp = n1;
        n1 = n2;
        n2 = temp;
    }
    
    public void minHeapify( int i )
    {
        int l = getLeft(i);
        int r = getRight(i);
        int menor;
        if ( l <= (tamanhoHeap-1) && heap[ l ].peso < heap[ i ].peso )
            menor = l;
        else
            menor = i;
        
        if ( r <= (tamanhoHeap-1) && heap[ r ].peso < heap[ menor ].peso )
            menor = r;
        
        if ( menor != i )
        {
            swap( heap[ i ], heap[ menor ] );
            minHeapify(menor);
        }
    }
    
    
    public void decreaseKey( int id, int novoPeso )
    {
        if ( novoPeso > heap[ id ].peso )
        {
            System.err.println("A chave inserida \"" + novoPeso + "\". Cujo ID é \"" + id + "\" é maior que valor atual contido nesta mesma posição (" + heap[ id ].peso + "). Encerrando o programa");
            System.exit( 1 );
        }
        
        heap[ id ].peso = novoPeso;
        while ( id > 0 && heap[ getParent(id) ].peso > heap[ id ].peso )
        {
            swap( heap[ id ], heap[ getParent(id) ] );
            id = getParent( id );
        }        
    }
    
    public int extractMin()
    {
        if ( tamanhoHeap < 1 )
            System.err.println("Heap undeflow");
        
        NodoHeapBinario min = heap[ 0 ];
        heap[ 0 ] = heap[ tamanhoHeap - 1 ];
        tamanhoHeap = tamanhoHeap - 1;
        minHeapify( 0 );
        
        
        return min.idVertice;
    }
    
    
    public void insert( int idVertice, int peso )
    {
        tamanhoHeap = tamanhoHeap + 1;
        heap[ tamanhoHeap-1 ] = new NodoHeapBinario(idVertice, Integer.MAX_VALUE );
        decreaseKey(tamanhoHeap-1, peso);
        
    }
    
    
    
    public class NodoHeapBinario
    {
        int idVertice;
        int peso;

        public NodoHeapBinario(int idVertice, int peso) 
        {
            this.idVertice = idVertice;
            this.peso = peso;
        }
        
        
        
    }
    
}

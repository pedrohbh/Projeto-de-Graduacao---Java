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
    private int heapSize;
    private HeapNode []heap;
    
    public HeapBinario( int tamanhoHeap )
    {
        heap = new HeapNode[tamanhoHeap];
        heapSize = -1;
    }
    
    public int parent( int indice )
    {
        return (indice - 1) / 2;
    }
    
    public int left( int indice )
    {
        return (2 * indice + 1);
    }
    
    public int right( int indice )
    {
        return ( 2 * indice + 2 );
    }
    
    public HeapNode extractMin()
    {
        if ( heapSize < 0 )
        {
            System.err.println("Heap Underflow");
            System.exit( 1 );
        }
        HeapNode min = heap[ 0 ];
        heap[ 0 ] = heap[ heapSize ];
        heap[ heapSize ] = min;
        heapSize = heapSize-1;
        minHeapify( 0 );
        return min;
    }
    
    //public void swap( )
    
    public void decreaseKey(int i, long key)
    {
        // 1
        if ( key > heap[ i ].key )
        {
            // 2
            System.err.println("Error: A chave inserida é maior do que a já existente");
            System.exit( 1 );
        }
        // 3
        heap[ i ].key = key;
        // 4
        while ( i > 0 && heap[ parent(i) ].key > heap[ i ].key )
        {
            HeapNode temp = heap[ parent(i) ];
            heap[ parent(i) ] = heap[ i ];
            heap[ i ] = temp;
            i = parent(i);            
        }       
        
    }
    
    
    
    
    public void insertHeap( HeapNode novoNodo, long key )
    {
        heapSize = heapSize + 1;
        novoNodo.key = Long.MAX_VALUE;
        heap[ heapSize ] = novoNodo;
        decreaseKey( heapSize, key);
    }

    private void minHeapify(int i)
    {
        int l = left(i);
        int r = right(i);
        int menor;
        if ( l <= heapSize && heap[ l ].key < heap[ i ].key )
        {
            menor = l;
        }
        else
            menor = i;
        
        if ( r <= heapSize && heap[ r ].key < heap[ menor ].key )
            menor = r;
        
        if ( menor != i )
        {
            HeapNode temp = heap[ i ];
            heap[ i ] = heap[ menor ];
            heap[ menor ] = temp;
            minHeapify( menor );
        }       
    }
    
    
    
    public class HeapNode
    {
        private int idVertice;
        private long key;
        
        public HeapNode( int id, long key )
        {
            this.idVertice = id;
            this.key = key;
        }
    }
    
}

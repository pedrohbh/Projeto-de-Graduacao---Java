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
    
    public HeapNode getMin()
    {
        return heap[ 0 ];
    }
    
    public HeapNode getElementoPosicao( int i )
    {
        return heap[ i ];
    }
    
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
        if ( getHeapSize() < 0 )
        {
            System.err.println("Heap Underflow");
            System.exit( 1 );
        }
        // Inicio Swap
        HeapNode min = heap[ 0 ];
        heap[ 0 ] = heap[ getHeapSize() ];
        heap[ getHeapSize() ] = min;
        
        int tempNum = heap[ 0 ].getIndiceAtual();
        heap[ 0 ].setIndiceAtual(heap[ getHeapSize() ].getIndiceAtual() );
        heap[ getHeapSize() ].setIndiceAtual(tempNum);
        // Fim Swap
        
        heapSize = getHeapSize()-1;
        minHeapify( 0 );
        return min;
    }
    
    //public void swap( )
    
    public void decreaseKey(int i, long key)
    {
        // 1
        if ( key > heap[ i ].getKey() )
        {
            // 2
            System.err.println("Error: A chave inserida é maior do que a já existente");
            System.exit( 1 );
        }
        // 3
        heap[ i ].setKey(key);
        // 4
        while ( i > 0 && heap[ parent(i) ].getKey() > heap[ i ].getKey() )
        {
            // Início Swap
            HeapNode temp = heap[ parent(i) ];
            heap[ parent(i) ] = heap[ i ];
            heap[ i ] = temp;
            
            int tempNum = heap[ parent(i) ].getIndiceAtual();
            heap[ parent( i ) ].setIndiceAtual( heap[ i ].getIndiceAtual() );
            heap[ i ].setIndiceAtual(tempNum);
            // Fim swap
            
            i = parent(i);            
        }       
        
    }
    
    public HeapNode insertHeap( int id, long key )
    {
        HeapNode novoNodo = new HeapNode(id, key);
        heapSize = getHeapSize() + 1;
        novoNodo.setKey(Long.MAX_VALUE);
        novoNodo.indiceAtual = getHeapSize();
        heap[ getHeapSize() ] = novoNodo;
        decreaseKey(getHeapSize(), key);
        return novoNodo;
    }

    private void minHeapify(int i)
    {
        int l = left(i);
        int r = right(i);
        int menor;
        if ( l <= getHeapSize() && heap[ l ].getKey() < heap[ i ].getKey() )
        {
            menor = l;
        }
        else
            menor = i;
        
        if ( r <= getHeapSize() && heap[ r ].getKey() < heap[ menor ].getKey() )
            menor = r;
        
        if ( menor != i )
        {
            // Início Swap
            HeapNode temp = heap[ i ];
            heap[ i ] = heap[ menor ];
            heap[ menor ] = temp;
            
            int tempNum = heap[ i ].getIndiceAtual();
            heap[ i ].setIndiceAtual( heap[ menor ].getIndiceAtual() );
            heap[ menor ].setIndiceAtual(tempNum);
            // Fim Swap
            
            minHeapify( menor );
        }       
    }
    
    public class HeapNode
    {
        private final int idVertice;
        private long key;
        private int indiceAtual;
        
        public HeapNode( int id, long key )
        {
            this.idVertice = id;
            this.key = key;
            this.indiceAtual = id;
        }

        /**
         * @return the idVertice
         */
        public int getIdVertice() {
            return idVertice;
        }

        /**
         * @return the key
         */
        public long getKey() {
            return key;
        }

        /**
         * @return the indiceAtual
         */
        public int getIndiceAtual() {
            return indiceAtual;
        }

        /**
         * @param indiceAtual the indiceAtual to set
         */
        public void setIndiceAtual(int indiceAtual) {
            this.indiceAtual = indiceAtual;
        }

        /**
         * @param key the key to set
         */
        public void setKey(long key) {
            this.key = key;
        }
    }

    /**
     * @return the heapSize
     */
    public int getHeapSize() {
        return heapSize;
    }
    
}

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
    private int []rastreador;
    
    public HeapBinario( int tamanho )
    {
        heap = new NodoHeapBinario[tamanho];
        this.rastreador = null;
        tamanhoHeap = 0;
    }
    
    public void setRastreador( int []rastreador )
    {
        this.rastreador = rastreador;
    }
    
    public int getPosicaoRastreador( int id)
    {
        return rastreador[ id ];
    }
    
    public int getDistanciaNodo( int id )
    {
        return heap[ id ].peso;
    }
    
    // Parent(i) return [i/2]
    public int getParent( int i )
    {
        int pai;
        if ( i % 2 == 0 )
            pai = Math.floorDiv(i, 2) - 1;
        else
            pai = Math.floorDiv(i, 2);
        
        //System.out.printf("Filho: %d. Pai %d%n", i, pai);
        return pai;
        //return i / 2;
    }
    
    public int getLeft( int i )
    {
        //System.out.printf("Pai: %d. Filho Esq: %d%n", i, (2*i + 1));
        return ( 2*i + 1 );
    }
    
    public int getRight( int i )
    {
        //System.out.printf("Pai: %d. Filho Dir: %d%n", i, (2*i + 2));
        return ( 2*i + 2 );
    }
    
    private void swap( NodoHeapBinario n1, NodoHeapBinario n2, int p1, int p2 )
    {
        rastreador[ n1.idVertice ] = p2;
        rastreador[ n2.idVertice ] = p1;
        this.heap[ p1 ] = n2;
        this.heap[ p2 ] = n1;
        /*NodoHeapBinario temp = n1;
        n1 = n2;
        n2 = temp;*/
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
            swap( heap[ i ], heap[ menor ], i, menor );
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
            swap( heap[ id ], heap[ getParent(id) ], id, getParent(id) );
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
        
        
        //return min.idVertice;
        return heap[ 0 ].idVertice;
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

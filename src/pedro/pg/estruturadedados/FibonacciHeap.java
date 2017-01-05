/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pedro.pg.estruturadedados;




/**
 *
 * @author administrador
 * @param <T>
 */
public class FibonacciHeap<T extends Comparable<T>>
{
    private int n;
    private FibNode<T> min;
    
    
    public FibonacciHeap()
    {
        this.n = 0;
        this.min = null;        
    }
    
    public void insert( FibNode<T> x )
    {
        // 1
        x.degree = 0;
        // 2
        x.p = null;
        // 3
        x.child = null;
        // 4
        x.mark = false;
        // 5 
        if ( min == null )
        {
            // 6, 7
            min = x.left = x.right = x;
        }
        else
        {
            // 8
            min.left.right = x;
            x.left = min.left;
            min.left = x;
            x.right = min;
            // 9
            if ( x.getPeso().compareTo(min.getPeso()) < 0 )
            {
                // 10
                min = x;
            }
        }
        // 11
        ++n;
    }
    
    public FibNode<T> criaNovoNodo( int id, T peso )
    {
        FibNode<T> nodo = new FibNode<>( id, peso );
        return nodo;
    }
    
    
    public FibNode<T> extractMin()
    {
        FibNode<T> z, x, next;
        FibNode<T> []childList;
        
        // 1
        z = min;
        // 2
        if ( z != null )
        {
            // 3
            x = z.child;
            if ( x != null )
            {
                childList = new FibNode[z.degree];
                next = x;
                for ( int i = 0; i < z.degree; i++ )
                {
                    childList[ i ] = next;
                    next = next.right;
                }
                for ( int i = 0; i < z.degree; i++ )
                {
                    x = childList[ i ];
                    // 4
                    min.left.right = x;
                    x.left = min.left;
                    min.left = x;
                    x.right = min;
                    // 5
                    x.p = null;
                }
               
            }
            // 6
            z.left.right = z.right;
            z.right.left = z.left;
            // 7
            if ( z == z.right )
            {
                // 8
                min = null;
            }
            else
            {
                // 9
                min = z.right;
                // 10
                consolidate();
            }
            // 11
            n--;
        }
        // 12
        return z;
    }

    public void consolidate() 
    {
        FibNode<T> w, next, x, y, temp;
        FibNode<T> []A;
        FibNode<T> []rootList;
        // Max degree <= log base golden ratio of n
        int d, rootSize;
        // static_cast<int>(floor(log(static_cast<double>(n))/log(static_cast<double>(1 + sqrt(static_cast<double>(5)))/2)));
        int max_degree = (int)(Math.floor(Math.log((double)(n))/Math.log((double)(1 + Math.sqrt((double)(5)))/2)));
        
        // 1
        A = new FibNode[max_degree+2];
        // 2, 3
        for ( int i = 0; i < (max_degree + 2); i++ )
            A[ i ] = null;
        // 4
        w = min;
        rootSize = 0;
        next = w;
        do
        {
            rootSize++;
            next = next.right;
        } while ( next != w );
        
        rootList = new FibNode[rootSize];
        for ( int i = 0; i < rootSize; i++ )
        {
            rootList[ i ] = next;
            next = next.right;
        }
        for ( int i = 0; i < rootSize; i++ )
        {
            w = rootList[ i ];
            // 5
            x = w;
            // 6 
            d = x.degree;
            // 7
            while ( A[ d ] != null )
            {
                // 8
                y = A[ d ];
                // 9 
                if ( x.getPeso().compareTo(y.getPeso()) > 0 )
                {
                    // 10
                    temp = x;
                    x = y;
                    y = temp;
                }
                // 11
                fib_heap_link( y, x );
                // 12
                A[ d ] = null;
                // 13
                d++;
            }
            // 14
            A[ d ] = x;
        }
        // 15
        min = null;
        // 16
        for ( int i = 0; i < (max_degree + 2); i++ )
        {
            // 17
            if ( A[ i ] != null )
            {
                // 18
                if ( min == null )
                {
                    // 19, 20
                    min = A[ i ].left = A[ i ].right = A[ i ];
                }
                else
                {
                    // 21
                    min.left.right = A[ i ];
                    A[ i ].left = min.left;
                    min.left = A[ i ];
                    A[ i ].right = min;
                    // 22
                    if ( A[ i ].getPeso().compareTo(min.getPeso()) < 0 )
                    {
                        // 23
                        min = A[ i ];
                    }
                }
            }
        }
    }

    public void fib_heap_link(FibNode<T> y, FibNode<T> x)
    {
        // 1
        y.left.right = y.right;
        y.right.left = y.left;
        // 2
        if ( x.child != null )
        {
            x.child.left.right = y;
            y.left = x.child.left;
            x.child.left = y;
            y.right = x.child;
        }
        else
        {
            x.child = y;
            y.right = y;
            y.left = y;
        }
        y.p = x;
        x.degree++;
    }
    
    public void decrease_key( FibNode<T> x, T k )
    {
        FibNode<T> y;
        
        // 1
        if ( k.compareTo(x.getPeso()) > 0  )
        {
            // 2
            System.err.println("A chave inserida \"" + k + "\". Cujo ID é \"" + x.getIdVertice() + "\" é maior que valor atual contido nesta mesma posição (" + x.getPeso() + "). Encerrando o programa");
            System.exit( 1 );
        }
        // 3
        x.peso = k;
        // 4
        y = x.p;
        // 5
        if (  y != null && x.getPeso().compareTo(y.getPeso()) < 0 )
        {
            // 6
            cut( x, y );
            // 7
            cascading_cut( y );
        }
        // 8
        if ( x.getPeso().compareTo(min.getPeso()) < 0 )
        {
            // 9
            min = x;
        }
    }

    public void cut(FibNode<T> x, FibNode<T> y) 
    {
        // 1
        if ( x.right == x )
        {
            y.child = null;
        }
        else
        {
            x.right.left = x.left;
            x.left.right = x.right;
            if ( y.child == x )
            {
                y.child = x.right;
            }
        }
        y.degree--;
        // 2
        min.right.left = x;
        x.right = min.right;
        min.right = x;
        x.left = x;
        // 3
        x.p = null;
        // 4
        x.mark = false;
    }

    public void cascading_cut(FibNode<T> y) 
    {
        FibNode<T> z;
        
        // 1
        z = y.p;
        // 2
        if ( z != null )
        {
            // 3
            if ( y.mark == false )
            {
                // 4
                y.mark = true;
            }
            else
            {
                // 5
                cut(y, z);
                // 6
                cascading_cut( z );
                
            }
        }
    }
    
    FibNode<T> retornaMinimo()
    {
        return min;
    }
    
    public class FibNode<T extends Comparable<T>>
    {
        private int idVertice;
        private T peso;
        boolean mark;
        FibNode<T> p;
        FibNode<T> left;
        FibNode<T> right;
        FibNode<T> child;
        int degree;
        int payload;

        public FibNode(int idVertice, T k )
        {
            this.idVertice = idVertice;
            this.peso = k;
            this.mark = false;
            this.p = null;
            this.left = null;
            this.right = null;
            this.child = null;
            this.degree = -1;
            this.payload =0;
        }    

        /**
         * @return the idVertice
         */
        public int getIdVertice() {
            return idVertice;
        }

        /**
         * @return the peso
         */
        public T getPeso() {
            return peso;
        }
    }
    
}

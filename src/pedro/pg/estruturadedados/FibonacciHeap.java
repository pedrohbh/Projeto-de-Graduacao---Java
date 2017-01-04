/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.pg.estruturadedados;

class FibNode<T extends Comparable<T>>
{
    int idVertice;
    T peso;
    boolean mark;
    FibNode<T> p;
    FibNode<T> left;
    FibNode<T> right;
    FibNode<T> child;
    int degree;
    int payload;

    FibNode(int idVertice, T k, int p1 )
    {
        this.idVertice = idVertice;
        this.peso = k;
        this.mark = false;
        this.p = null;
        this.left = null;
        this.right = null;
        this.child = null;
        this.degree = -1;
        this.payload = p1;
    }
    
    
    
    
    
    
}


/**
 *
 * @author administrador
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
            if ( x.peso.compareTo(min.peso) < 0 )
            {
                // 10
                min = x;
            }
        }
        // 11
        ++n;
    }
    
    
    FibNode<T> extractMin()
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

    private void consolidate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

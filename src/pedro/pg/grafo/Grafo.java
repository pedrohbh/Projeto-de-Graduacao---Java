/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.pg.grafo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import pedro.pg.estruturadedados.FibonacciHeap;
import pedro.pg.estruturadedados.HeapBinario;

/**
 *
 * @author administrador
 */
public class Grafo 
{
    private int numeroVertices;
    private int numeroArestas;
    private Vertice []verticesGrafo;
    private long []cordenadasX;
    private long []cordenadasY;
    private static Scanner input;
    
 
    public Grafo()
    {
        numeroArestas = 0;
        numeroVertices = 0;
        verticesGrafo = null;
        cordenadasX = null;
        cordenadasY = null;
    }
    
    public void buscaEmLargutaPorVertice( int idVertice )
    {
        List<Integer> verticesChegaveis = new LinkedList<>();
        Queue<Integer> fila = new LinkedList<>();
        Cores []cor = new Cores[ getNumeroVertices() ];
        for ( int i = 0; i < getNumeroVertices(); i++ )
        {
            cor[ i ] = Cores.WHITE;
        }
        
        cor[ idVertice ] = Cores.GRAY;
        fila.add(idVertice);
        while ( !fila.isEmpty() )
        {
            int verticeAtual = fila.remove();
            for ( Aresta a: verticesGrafo[ verticeAtual ].arestasAdjacentes )
            {
                if ( cor[ a.idVerticeDestino ] == Cores.WHITE )
                {
                    cor[ a.idVerticeDestino ] = Cores.GRAY;
                    fila.add(  a.idVerticeDestino );
                }
            }
            
            cor[ verticeAtual ] = Cores.BLACK;
            verticesChegaveis.add( verticeAtual );
        }
        System.out.println("Tamanho Total: " + verticesChegaveis.size());
        for ( int i = 0; i < verticesChegaveis.size(); i++ )
        {
            if ( (i+1) % 5 == 0 )
                System.out.printf("%d%n", verticesChegaveis.get( i ));
            else
                System.out.printf("%d ", verticesChegaveis.get( i ) );
        }
        
    }
    
    private void dsfVisit( int id, Cores []cor, List<Integer> verticesChegaveis )
    {
        cor[ id ] = Cores.GRAY;
        //time = time + 1;
        for ( Aresta a: verticesGrafo[ id ].arestasAdjacentes )
        {
            if ( cor[ a.idVerticeDestino ] == Cores.WHITE )
            {
                //pi[ a.idVerticeDestino ] = a.idVeticeOrigem;
                //verticesChegaveis.add(id)
                dsfVisit( a.idVerticeDestino, cor, verticesChegaveis);
            }
        }
        //time = time + 1;
        cor[ id ] = Cores.BLACK;
        verticesChegaveis.add(id);
        /*if ( time % 5 == 0 )
        {
            System.out.printf("%d%n", id);
        }
        else
        {
            System.out.printf("%d ", id );
        }*/
        
        
        
    }
    
    public void buscaEmProfundidadePorVertice( int idVertice )
    {
        Cores []cor = new Cores[ getNumeroVertices() ];
        List<Integer> verticesChegaveis = new LinkedList<>();
        int []pi = new int[ getNumeroVertices() ];
        int time;
        
        for ( int i = 0; i < getNumeroVertices(); i++ )
        {
            cor[ i ] = Cores.WHITE;
            pi[ i ] = -1;            
        }
        
        time = 0;
        System.out.println("Vertices chegáveis partindo de " +  idVertice );
        dsfVisit(idVertice, cor, verticesChegaveis);
        for ( int i = 0; i < verticesChegaveis.size(); i++ )
        {
            if ( (i+1) % 5 == 0 )
                System.out.printf("%d%n", verticesChegaveis.get( i ));
            else
                System.out.printf("%d ", verticesChegaveis.get( i ) );
        }
    }
                
    public void dijkstraHeapFibonacci( int idOrigem )
    {
        int verticesVisitados = getNumeroVertices();
        int []antecessor = new int[ getNumeroVertices() ];
        boolean []isDeterminado = new boolean[ getNumeroVertices() ];
        FibonacciHeap.FibNode []rastreador = new FibonacciHeap.FibNode[ getNumeroVertices() ];
        FibonacciHeap heap = new FibonacciHeap();
        
        for ( int i = 0; i < getNumeroVertices(); i++ )
        {
            FibonacciHeap.FibNode novoNodo = heap.criaNodo(i, Integer.MAX_VALUE );
            isDeterminado[ i ] = false;
            antecessor[ i ] = i;
            rastreador[ i ] = novoNodo;
            heap.insert(novoNodo);
        }
        
        
        FibonacciHeap.FibNode nodoAtual = rastreador[ idOrigem ];
        heap.decreaseKey(nodoAtual, 0);
        while ( verticesVisitados > 0 )
        {
            nodoAtual = heap.extractMin();
            int verticeAtual = nodoAtual.getIdVertice();
            isDeterminado[ verticeAtual ] = true;
            for ( Aresta a: verticesGrafo[ verticeAtual ].getArestasAdjacentes() )
            {
                int verticeDestino = a.idVerticeDestino;
                FibonacciHeap.FibNode nodoDestino = rastreador[ verticeDestino ];
                if ( isDeterminado[ verticeDestino ] == true )
                    continue;
                
                if ( nodoDestino.getKey() > ( a.peso + nodoAtual.getKey() ) )
                {
                    if ( ( a.peso + nodoAtual.getKey() ) >= 0 )
                    {
                        heap.decreaseKey(nodoDestino, (a.peso + nodoAtual.getKey() ) );
                        antecessor[ verticeDestino ] = verticeAtual;
                    }
                }               
            }
            verticesVisitados--;
            
            
        }
        
        
        /*System.out.println("Imprimindo antecessores de Heap de Fibonacci");
        for ( int i = 0; i < antecessor.length; i++ )
        {
            System.out.printf("Antecessor( %d ): %d%n", i, antecessor[ i ] );
        }*/
        
        
        
    }
    
    public static void imprimeDistanciaEAntecessor( int []antecessor, long []distancias, String nomeArquivoSaida )
    {
        Formatter saida = null;
        
        // Usado para verificar se a saida deve ser gravada em um arquivo
        if ( nomeArquivoSaida != null )
        {
            try 
            {
                saida = new Formatter(nomeArquivoSaida);
            } catch (FileNotFoundException ex) 
            {
                System.err.println("Erro ao escrever no arquivod e saida \"" + nomeArquivoSaida + "\".");
                ex.printStackTrace();
                System.exit( 1 );
            }
            
        }
        
        if ( distancias != null && antecessor != null )
        {
            System.out.println("Imprimindo antecessores e distâncias");
            if ( antecessor.length != distancias.length )
            {
                System.err.printf("Error: O tamanho do vetor de distâncias é diferente do tamanho do vetor de antecessores.%nIsso não deveria acontecer. Verifique o que pode estar occorendo%n");
                System.exit( 1 );
            }
            for ( int i = 0; i < antecessor.length; i++  )
            {
                if ( saida == null )
                    System.out.printf( "Distância( %d ): %d\tAntecessor( %d ): %d%n", i, distancias[ i ], i, antecessor[ i ] );
                else
                {
                    saida.format( "Distância( %d ): %d\tAntecessor( %d ): %d%n", i, distancias[ i ], i, antecessor[ i ] );
                }
            }
        }
        else if ( distancias == null )
        {
            for ( int i = 0; i < antecessor.length; i++ )
            {
                if ( saida == null )
                    System.out.printf("Antecessor( %d ): %d%n", i, antecessor[ i ] );
                else
                    saida.format("Antecessor( %d ): %d%n", i, antecessor[ i ] );                
            }
        }
        else
        {
            for ( int i = 0; i < distancias.length; i++ )
            {
                if ( saida == null )
                    System.out.printf("Distância( %d ): %d%n", i, distancias[ i ] );
                else
                    saida.format("Distância( %d ): %d%n", i, distancias[ i ] );
            }
        }
        
        if ( saida != null )
            saida.close();
        
    }
    
    public void publicaCaminho( int []antecessores, int idOrigem, int idDestino )
    {
        Stack<Integer> pilha = new Stack<>();
        int ultimoVisitado = idDestino;
        
        while ( ultimoVisitado != idOrigem )
        {
            pilha.push( ultimoVisitado );
            ultimoVisitado = antecessores[ ultimoVisitado ];
        }
        
        pilha.push( idOrigem );
        
        int ultimoElemento = pilha.firstElement();
        while ( !pilha.isEmpty() )
        {
            int elemento = pilha.pop();
            if ( elemento == ultimoElemento )
                System.out.printf("%d%n", elemento );
            else
                System.out.printf("%d -> ", elemento );
        }
    }
    
    public void algoritmoAEstrelaBeta( int idOrigem, int idDestino )
    {
        HeapBinario heap = new HeapBinario( getNumeroVertices() );
        int []antecessor = new int[ getNumeroVertices() ];
        boolean []isDeterminado = new boolean[ getNumeroVertices() ];
        long []distanciaHeuristica = new long[ getNumeroVertices() ];
        long []distanciaReal = new long[ getNumeroVertices() ];
        HeapBinario.HeapNode []rastreador = new HeapBinario.HeapNode[ getNumeroVertices() ];
        
        for ( int i = 0; i < getNumeroVertices(); i++ )
        {
            antecessor[ i ] = i;
            isDeterminado[ i ] = false;
            rastreador[ i ] = heap.insertHeap(i, Long.MAX_VALUE );
            distanciaHeuristica[ i ] = 0;
            distanciaReal[ i ] = Long.MAX_VALUE;
        }
        
        heap.decreaseKey(idOrigem, 0 );
        distanciaReal[ idOrigem ] = 0;
        while ( isDeterminado[ idDestino ] == false )
        {
            HeapBinario.HeapNode nodoAtual = heap.extractMin();
            int idNodoAtual = nodoAtual.getIdVertice();
            isDeterminado[ idNodoAtual ] = true;
            
            for ( Aresta a: verticesGrafo[ idNodoAtual ].getArestasAdjacentes() )
            {
                int idVerticeDestino = a.idVerticeDestino;
                if ( isDeterminado[ idVerticeDestino ] == true )
                    continue;
                
                // Função Heurística - Distância Euclidiana
                if ( distanciaHeuristica[ idVerticeDestino ] == 0 )
                {                    
                    distanciaHeuristica[ idVerticeDestino ] = Math.round( Math.sqrt( Math.pow( cordenadasX[ idVerticeDestino ] - cordenadasX[ idDestino ], 2 ) + Math.pow( cordenadasY[ idVerticeDestino ] - cordenadasY[ idDestino ] , 2 ) ) );
                }
                
                HeapBinario.HeapNode nodoDestino = rastreador[ idVerticeDestino ];
                long chaveDestino = a.peso + distanciaReal[ idNodoAtual ];
                //long distanciaPrevista = ( a.peso + nodoAtual.getKey() + distanciaHeuristica[ idVerticeDestino ] - distanciaHeuristica[ idNodoAtual ] );
                if ( distanciaReal[ idVerticeDestino ] > chaveDestino && chaveDestino >= 0  )
                {
                    distanciaReal[ idVerticeDestino ] = chaveDestino;
                    heap.decreaseKey( nodoDestino.getIndiceAtual(), chaveDestino + distanciaHeuristica[ idVerticeDestino ] );
                    antecessor[ idVerticeDestino ] = idNodoAtual;
                }
            }
        }
        
        
        publicaCaminho(antecessor, idOrigem, idDestino);
        
    }
    
    public int contaNumeroDeVerticesAbertosAEstrelaNaoAdmissivel( int idOrigem, int idDestino )
    {
        HeapBinario heap = new HeapBinario( getNumeroVertices() );
        int []antecessor = new int[ getNumeroVertices() ];
        EstadosVertice []estados = new EstadosVertice[ getNumeroVertices() ];
        boolean []isDeterminado = new boolean[ getNumeroVertices() ];
        long []distanciaHeuristica = new long[ getNumeroVertices() ];
        HeapBinario.HeapNode []rastreador = new HeapBinario.HeapNode[ getNumeroVertices() ];
        int numeroAbertos = 1;
        
        for ( int i = 0; i < getNumeroVertices(); i++ )
        {
            antecessor[ i ] = i;
            isDeterminado[ i ] = false;
            rastreador[ i ] = heap.insertHeap(i, Long.MAX_VALUE );
            distanciaHeuristica[ i ] = 0;
            estados[ i ] = EstadosVertice.NEUTRO;
        }
        
        heap.decreaseKey(idOrigem, 0 );
        distanciaHeuristica[ idOrigem ] = 0;
        
        HeapBinario.HeapNode nodoAtual = heap.extractMin();
        
        while ( nodoAtual.getIdVertice() != idDestino )
        {
            
            int idNodoAtual = nodoAtual.getIdVertice();
            isDeterminado[ idNodoAtual ] = true;
            estados[ idNodoAtual ] = EstadosVertice.FECHADO;
            
            for ( Aresta a: verticesGrafo[ idNodoAtual ].getArestasAdjacentes() )
            {
                int idVerticeDestino = a.idVerticeDestino;
                if ( isDeterminado[ idVerticeDestino ] == true )
                    continue;
                
                if ( distanciaHeuristica[ idVerticeDestino ] == 0 )
                {                    
                    distanciaHeuristica[ idVerticeDestino ] = ( Math.abs( cordenadasX[ idVerticeDestino ] - cordenadasX[ idDestino ] ) + Math.abs( cordenadasY[ idVerticeDestino ] - cordenadasY[ idDestino ] ) );
                }
                
                HeapBinario.HeapNode nodoDestino = rastreador[ idVerticeDestino ];
                long distanciaPrevista = ( a.peso + nodoAtual.getKey() + distanciaHeuristica[ idVerticeDestino ] - distanciaHeuristica[ idNodoAtual ] );
                if ( nodoDestino.getKey() > distanciaPrevista && distanciaPrevista >= 0  )
                {
                    heap.decreaseKey( nodoDestino.getIndiceAtual(), distanciaPrevista );
                    antecessor[ idVerticeDestino ] = idNodoAtual;
                    if ( estados[ idVerticeDestino ] == EstadosVertice.NEUTRO )
                    {
                        estados[ idVerticeDestino ] = EstadosVertice.ABERTO;
                        numeroAbertos += 1;
                    }
                }
            }
            nodoAtual = heap.extractMin();
        }
        
        
        //publicaCaminho(antecessor, idOrigem, idDestino);
        
        return numeroAbertos;
        
    }
    
    public int contaNumeroDeVerticesAbertosAEstrela( int idOrigem, int idDestino )
    {
        HeapBinario heap = new HeapBinario( getNumeroVertices() );
        int []antecessor = new int[ getNumeroVertices() ];
        EstadosVertice []estados = new EstadosVertice[ getNumeroVertices() ];
        boolean []isDeterminado = new boolean[ getNumeroVertices() ];
        long []distanciaHeuristica = new long[ getNumeroVertices() ];
        HeapBinario.HeapNode []rastreador = new HeapBinario.HeapNode[ getNumeroVertices() ];
        int numeroAbertos = 1;
        
        for ( int i = 0; i < getNumeroVertices(); i++ )
        {
            antecessor[ i ] = i;
            isDeterminado[ i ] = false;
            rastreador[ i ] = heap.insertHeap(i, Long.MAX_VALUE );
            distanciaHeuristica[ i ] = 0;
            estados[ i ] = EstadosVertice.NEUTRO;
        }
        
        heap.decreaseKey(idOrigem, 0 );
        distanciaHeuristica[ idOrigem ] = 0;
        
        HeapBinario.HeapNode nodoAtual = heap.extractMin();
        
        while ( nodoAtual.getIdVertice() != idDestino )
        {
            
            int idNodoAtual = nodoAtual.getIdVertice();
            isDeterminado[ idNodoAtual ] = true;
            estados[ idNodoAtual ] = EstadosVertice.FECHADO;
            
            for ( Aresta a: verticesGrafo[ idNodoAtual ].getArestasAdjacentes() )
            {
                int idVerticeDestino = a.idVerticeDestino;
                if ( isDeterminado[ idVerticeDestino ] == true )
                    continue;
                
                if ( distanciaHeuristica[ idVerticeDestino ] == 0 )
                {                    
                    distanciaHeuristica[ idVerticeDestino ] = Math.round( Math.sqrt( Math.pow( cordenadasX[ idVerticeDestino ] - cordenadasX[ idDestino ], 2 ) + Math.pow( cordenadasY[ idVerticeDestino ] - cordenadasY[ idDestino ] , 2 ) ) );
                }
                
                HeapBinario.HeapNode nodoDestino = rastreador[ idVerticeDestino ];
                long distanciaPrevista = ( a.peso + nodoAtual.getKey() + distanciaHeuristica[ idVerticeDestino ] - distanciaHeuristica[ idNodoAtual ] );
                if ( nodoDestino.getKey() > distanciaPrevista && distanciaPrevista >= 0  )
                {
                    heap.decreaseKey( nodoDestino.getIndiceAtual(), distanciaPrevista );
                    antecessor[ idVerticeDestino ] = idNodoAtual;
                    if ( estados[ idVerticeDestino ] == EstadosVertice.NEUTRO )
                    {
                        estados[ idVerticeDestino ] = EstadosVertice.ABERTO;
                        numeroAbertos += 1;
                    }
                }
            }
            nodoAtual = heap.extractMin();
        }
        
        
        //publicaCaminho(antecessor, idOrigem, idDestino);
        
        return numeroAbertos;
        
    }
    
    public long algoritmoAEstrelaManhattan( int idOrigem, int idDestino, final boolean imprimeRota, final boolean retornaCusto )
    {
        HeapBinario heap = new HeapBinario( getNumeroVertices() );
        int []antecessor = new int[ getNumeroVertices() ];
        boolean []isDeterminado = new boolean[ getNumeroVertices() ];
        long []distanciaHeuristica = new long[ getNumeroVertices() ];
        HeapBinario.HeapNode []rastreador = new HeapBinario.HeapNode[ getNumeroVertices() ];
        
        for ( int i = 0; i < getNumeroVertices(); i++ )
        {
            antecessor[ i ] = i;
            isDeterminado[ i ] = false;
            rastreador[ i ] = heap.insertHeap(i, Long.MAX_VALUE );
            distanciaHeuristica[ i ] = 0;
        }
        
        heap.decreaseKey(idOrigem, 0 );
        distanciaHeuristica[ idOrigem ] = 0;
        
        HeapBinario.HeapNode nodoAtual = heap.extractMin();
        
        while ( nodoAtual.getIdVertice() != idDestino )
        {
            
            int idNodoAtual = nodoAtual.getIdVertice();
            isDeterminado[ idNodoAtual ] = true;
            
            for ( Aresta a: verticesGrafo[ idNodoAtual ].getArestasAdjacentes() )
            {
                int idVerticeDestino = a.idVerticeDestino;
                if ( isDeterminado[ idVerticeDestino ] == true )
                    continue;
                
                if ( distanciaHeuristica[ idVerticeDestino ] == 0 )
                {                    
                    distanciaHeuristica[ idVerticeDestino ] = ( Math.abs( cordenadasX[ idVerticeDestino ] - cordenadasX[ idDestino ] ) + Math.abs( cordenadasY[ idVerticeDestino ] - cordenadasY[ idDestino ] ) );
                }
                
                HeapBinario.HeapNode nodoDestino = rastreador[ idVerticeDestino ];
                long distanciaPrevista = ( a.peso + nodoAtual.getKey() + distanciaHeuristica[ idVerticeDestino ] - distanciaHeuristica[ idNodoAtual ] );
                if ( nodoDestino.getKey() > distanciaPrevista && distanciaPrevista >= 0  )
                {
                    heap.decreaseKey( nodoDestino.getIndiceAtual(), distanciaPrevista );
                    antecessor[ idVerticeDestino ] = idNodoAtual;
                }
            }
            nodoAtual = heap.extractMin();
        }
        
        if ( imprimeRota == true )
            publicaCaminho(antecessor, idOrigem, idDestino);
        
        if ( retornaCusto == true )
            return calculaDistanciaTotal(antecessor, idOrigem, idDestino);
        else
            return -1;
        
    }
    
    private long calculaDistanciaTotal( int []antecessores, int idOrigem, int idDestino )
    {
        if ( idDestino == idOrigem )
            return 0;
        
        Stack<Integer> pilha = new Stack<>();
        int ultimoVisitado = idDestino;
        long custoTotalCaminho = 0;
        
        while ( ultimoVisitado != idOrigem )
        {
            pilha.push( ultimoVisitado );
            ultimoVisitado = antecessores[ ultimoVisitado ];
        }
        
        pilha.push( idOrigem );
        
        int ultimoElemento = pilha.firstElement();
        int elementoOrigem = pilha.pop();
        while ( !pilha.isEmpty() )
        {            
            int elementoDestino;
            if ( elementoOrigem != ultimoElemento )
            {
                elementoDestino = pilha.pop();
            
                for ( Aresta a: verticesGrafo[ elementoOrigem ].arestasAdjacentes )
                {
                    if ( a.getIdVerticeDestino() == elementoDestino )
                    {
                        custoTotalCaminho += a.peso;
                        break;
                    }
                
                }
                elementoOrigem = elementoDestino;
            }
            
        }
        
        return custoTotalCaminho;
    }
    
    public long algoritmoAEstrela( int idOrigem, int idDestino, final boolean imprimeRota, final boolean imprimeCusto )
    {
        HeapBinario heap = new HeapBinario( getNumeroVertices() );
        int []antecessor = new int[ getNumeroVertices() ];
        boolean []isDeterminado = new boolean[ getNumeroVertices() ];
        long []distanciaHeuristica = new long[ getNumeroVertices() ];
        HeapBinario.HeapNode []rastreador = new HeapBinario.HeapNode[ getNumeroVertices() ];
        
        for ( int i = 0; i < getNumeroVertices(); i++ )
        {
            antecessor[ i ] = i;
            isDeterminado[ i ] = false;
            rastreador[ i ] = heap.insertHeap(i, Long.MAX_VALUE );
            distanciaHeuristica[ i ] = 0;
        }
        
        heap.decreaseKey(idOrigem, 0 );
        distanciaHeuristica[ idOrigem ] = 0;
        
        HeapBinario.HeapNode nodoAtual = heap.extractMin();
        
        while ( nodoAtual.getIdVertice() != idDestino )
        {
            
            int idNodoAtual = nodoAtual.getIdVertice();
            isDeterminado[ idNodoAtual ] = true;
            
            for ( Aresta a: verticesGrafo[ idNodoAtual ].getArestasAdjacentes() )
            {
                int idVerticeDestino = a.idVerticeDestino;
                if ( isDeterminado[ idVerticeDestino ] == true )
                    continue;
                
                if ( distanciaHeuristica[ idVerticeDestino ] == 0 )
                {                    
                    distanciaHeuristica[ idVerticeDestino ] = Math.round( Math.sqrt( Math.pow( cordenadasX[ idVerticeDestino ] - cordenadasX[ idDestino ], 2 ) + Math.pow( cordenadasY[ idVerticeDestino ] - cordenadasY[ idDestino ] , 2 ) ) );
                }
                
                HeapBinario.HeapNode nodoDestino = rastreador[ idVerticeDestino ];
                long distanciaPrevista = ( a.peso + nodoAtual.getKey() + distanciaHeuristica[ idVerticeDestino ] - distanciaHeuristica[ idNodoAtual ] );
                if ( nodoDestino.getKey() > distanciaPrevista && distanciaPrevista >= 0  )
                {
                    heap.decreaseKey( nodoDestino.getIndiceAtual(), distanciaPrevista );
                    antecessor[ idVerticeDestino ] = idNodoAtual;
                }
            }
            nodoAtual = heap.extractMin();
        }
        
        if ( imprimeRota == true )
            publicaCaminho(antecessor, idOrigem, idDestino);
        if ( imprimeCusto == true )
            return calculaDistanciaTotal(antecessor, idOrigem, idDestino);
        
        return -1;
        //calculaDistanciaTotal(antecessor, idOrigem, idDestino);
        
    }
    
    public int contaNumeroDeVerticesAbertosDijkstraAdptado( int idOrigem, int idObjetivo )
    {
        HeapBinario heap = new HeapBinario( getNumeroVertices() );
        int []antecessor = new int[ getNumeroVertices() ];
        boolean []isDeterminado = new boolean[ getNumeroVertices() ];
        long []distancias = new long[ getNumeroVertices() ];
        int verticesASeremVisitados = getNumeroVertices();
        int numeroAbertos = 1;
        EstadosVertice []estados = new EstadosVertice[verticesASeremVisitados];
        HeapBinario.HeapNode []rastreador = new HeapBinario.HeapNode[verticesASeremVisitados];
        
        
        for ( int i = 0; i < getNumeroVertices(); i++ )
        {
            antecessor[ i ] = i;
            isDeterminado[ i ] = false;
            rastreador[ i ] = heap.insertHeap(i, Long.MAX_VALUE );
            distancias[ i ]= Integer.MAX_VALUE;
            estados[ i ] = EstadosVertice.NEUTRO;
        }
        
        heap.decreaseKey(idOrigem, 0);
        distancias[ idOrigem ] = 0;
        
        HeapBinario.HeapNode nodoAtual = heap.extractMin();
        while ( nodoAtual.getIdVertice() != idObjetivo )
        {
            
            int idAtual = nodoAtual.getIdVertice();
            isDeterminado[ idAtual ] = true;
            estados[ idAtual ] = EstadosVertice.FECHADO;
            
            for ( Aresta a: verticesGrafo[ idAtual ].arestasAdjacentes )
            {
                int idDestino = a.idVerticeDestino;
                if ( isDeterminado[ idDestino ] )
                    continue;
                
                HeapBinario.HeapNode nodoDestino = rastreador[ idDestino ];
                if ( nodoDestino.getKey() > ( a.peso + nodoAtual.getKey() ) )
                {
                    if ( ( a.peso + nodoAtual.getKey() ) >= 0 )
                    {
                        heap.decreaseKey( nodoDestino.getIndiceAtual(), ( a.peso + nodoAtual.getKey() ) );
                        antecessor[ idDestino ] = idAtual;
                        distancias[ idDestino ] = a.peso + nodoAtual.getKey();
                        if ( estados[ idDestino ] == EstadosVertice.NEUTRO )
                        {
                            estados[ idDestino ] = EstadosVertice.ABERTO;
                            numeroAbertos += 1;
                        }
                    }
                }
            }
            nodoAtual = heap.extractMin();
            
            //verticesASeremVisitados--;
            //if ( isDeterminado[ idAtual ] )
                
            
        }
        
        return numeroAbertos;
        
    }
    
    public void dijkstraHeapBinarioAdptado( int idOrigem, int idObjetivo, boolean imprimeRota )
    {
        HeapBinario heap = new HeapBinario( getNumeroVertices() );
        int []antecessor = new int[ getNumeroVertices() ];
        boolean []isDeterminado = new boolean[ getNumeroVertices() ];
        long []distancias = new long[ getNumeroVertices() ];
        int verticesASeremVisitados = getNumeroVertices();
        HeapBinario.HeapNode []rastreador = new HeapBinario.HeapNode[verticesASeremVisitados];
        
        
        for ( int i = 0; i < getNumeroVertices(); i++ )
        {
            antecessor[ i ] = i;
            isDeterminado[ i ] = false;
            rastreador[ i ] = heap.insertHeap(i, Long.MAX_VALUE );
            distancias[ i ]= Integer.MAX_VALUE;
        }
        
        heap.decreaseKey(idOrigem, 0);
        distancias[ idOrigem ] = 0;
        
        HeapBinario.HeapNode nodoAtual = heap.extractMin();
        while ( nodoAtual.getIdVertice() != idObjetivo )
        {
            
            int idAtual = nodoAtual.getIdVertice();
            isDeterminado[ idAtual ] = true;
            
            for ( Aresta a: verticesGrafo[ idAtual ].arestasAdjacentes )
            {
                int idDestino = a.idVerticeDestino;
                if ( isDeterminado[ idDestino ] )
                    continue;
                
                HeapBinario.HeapNode nodoDestino = rastreador[ idDestino ];
                if ( nodoDestino.getKey() > ( a.peso + nodoAtual.getKey() ) )
                {
                    if ( ( a.peso + nodoAtual.getKey() ) >= 0 )
                    {
                        heap.decreaseKey( nodoDestino.getIndiceAtual(), ( a.peso + nodoAtual.getKey() ) );
                        antecessor[ idDestino ] = idAtual;
                        distancias[ idDestino ] = a.peso + nodoAtual.getKey();
                    }
                }
            }
            nodoAtual = heap.extractMin();
            
            //verticesASeremVisitados--;
            //if ( isDeterminado[ idAtual ] )
                
            
        }
        
        //imprimeDistanciaEAntecessor(antecessor, distancias, "/home/administrador/Área de Trabalho/Testes/ResultadoDijkstraHeapBinarioNovo.txt");
        
        if ( imprimeRota == true )
        {
            System.out.println("Mostrando caminho para Dijkstra Adptado");
            publicaCaminho(antecessor, idOrigem, idObjetivo );
            System.out.println("Custo total para o vértice " + idObjetivo + ": " + distancias[ idObjetivo ] );
            System.out.printf("Fim Dijkstra%n%n");            
        }
        
        
        
        /*System.out.println("Imprimindo menores distanciaHeuristica Dijkstra Heap Binário");
        for ( int i = 0; i < getNumeroVertices(); i++ )
        {
            System.out.printf("Distância( %d ): %d%n", i, distanciaHeuristica[ i ] );
            //System.out.printf("Antecessor( %d ): %d%n", i, antecessor[ i ] );
        }*/
        
    }
            
    private void repassaInconsistentesParaAberto( List<HeapBinario.HeapNode> listaInconsitentes, HeapBinario openHeap, HeapBinario.HeapNode []rastreadorOpen, EstadosVertice []estadosVertices )
    {
        int idVertice;
        HeapBinario.HeapNode nodo;
        for ( int i = 0; i < listaInconsitentes.size(); i++ )
        {
            nodo = listaInconsitentes.remove( i );
            idVertice = nodo.getIdVertice();
            estadosVertices[ idVertice ] = EstadosVertice.ABERTO;
            rastreadorOpen[ idVertice ] = openHeap.insertHeap( idVertice, nodo.getKey() );
        }
    }
    
    private void limpaListaFechado( List<HeapBinario.HeapNode> listaFechado, EstadosVertice []estadosVertices )
    {
        int idVertice;
        HeapBinario.HeapNode nodo;
        for ( int i = 0; i < listaFechado.size(); i++ )
        {
            nodo = listaFechado.remove(i);
            idVertice = nodo.getIdVertice();
            estadosVertices[ idVertice ] = EstadosVertice.LIMBO;
        }
    }
    
    private void atualizaOpen( HeapBinario openHeap, long []distanciaReal, long []distanciaHeuristica, double epsilon)
    {
        HeapBinario.HeapNode novoNodo;
        long novaDistancia;
        int indice;
        for ( int i = 0; i <= openHeap.getHeapSize(); i++ )
        {
            novoNodo = openHeap.getElementoPosicao(i);
            indice = novoNodo.getIdVertice();
            novaDistancia = distanciaReal[ indice ] + Math.round(distanciaHeuristica[ indice ] * epsilon);
            openHeap.decreaseKey( novoNodo.getIndiceAtual(), novaDistancia);
        }       
    }
    
    private void computePathAnytimeSearch( int idDestino, int []antecessores, HeapBinario openHeap, HeapBinario.HeapNode []rastreadorOpen, HeapBinario.HeapNode []rastreadorClosed, EstadosVertice []estadosVertice, long []distanciaReal, long []distanciaHeuristica, List<HeapBinario.HeapNode> listaInconsistentes, List<HeapBinario.HeapNode> listaFechado, double epsilon )
    {                
        while( distanciaReal[ idDestino ] > openHeap.getMin().getKey() )
        {
            HeapBinario.HeapNode nodoAtual = openHeap.extractMin();            
            // 4
            int idNodoAtual = nodoAtual.getIdVertice();
            estadosVertice[ idNodoAtual ] = EstadosVertice.FECHADO;
            rastreadorClosed[ idNodoAtual ] = nodoAtual;
            listaFechado.add(nodoAtual);
            rastreadorOpen[ idNodoAtual ] = null;
            
            for ( Aresta a: verticesGrafo[ idNodoAtual ].getArestasAdjacentes() )
            {
                int idNodoAdjacente = a.idVerticeDestino;
                
                if ( estadosVertice[ idNodoAdjacente ] == EstadosVertice.NEUTRO )
                {
                    distanciaReal[ idNodoAdjacente ] = Long.MAX_VALUE;
                    distanciaHeuristica[ idNodoAdjacente ] = Math.round( Math.sqrt( Math.pow( cordenadasX[ idNodoAdjacente ] - cordenadasX[ idDestino ], 2 ) + Math.pow( cordenadasY[ idNodoAdjacente ] - cordenadasY[ idDestino ], 2 ) ) );
                }
                
                long distanciaPrevista = distanciaReal[ idNodoAtual ] + a.peso;
                if ( distanciaReal[ idNodoAdjacente ] > distanciaPrevista && distanciaPrevista >= 0  )
                {
                    distanciaReal[ idNodoAdjacente ] = distanciaPrevista;
                    long distanciaChave = distanciaPrevista + Math.round(epsilon * distanciaHeuristica[ idNodoAdjacente ] );
                    antecessores[ idNodoAdjacente ] = idNodoAtual;
                    if ( estadosVertice[ idNodoAdjacente ] != EstadosVertice.FECHADO )
                    {
                        estadosVertice[ idNodoAdjacente ] = EstadosVertice.ABERTO;
                    
                        if ( rastreadorOpen[ idNodoAdjacente ] == null )                        
                            rastreadorOpen[ idNodoAdjacente ] = openHeap.insertHeap( idNodoAdjacente, distanciaChave );
                        else
                            openHeap.decreaseKey( rastreadorOpen[ idNodoAdjacente ].getIndiceAtual(), distanciaChave);
                    }
                    else
                    {
                        estadosVertice[ idNodoAdjacente ] = EstadosVertice.INCOSISTENTE;
                        rastreadorClosed[ idNodoAdjacente ].setKey(distanciaChave);
                        listaInconsistentes.add( rastreadorClosed[ idNodoAdjacente ] );
                    }
                }
            }            
        }
        if ( openHeap.getMin().getIdVertice() == idDestino )
        {
            openHeap.extractMin();
            rastreadorOpen[ idDestino ] = null;
            estadosVertice[ idDestino ] = EstadosVertice.LIMBO;
        }
    }
    
    private long computeKeyAD( int idVertice, long []distanciaReal, long []v, long []distanciaHeuristica, double episolon )
    {
        if ( v[ idVertice ] >= distanciaReal[ idVertice ] )
        {
            return Math.max(distanciaReal[ idVertice ] + Math.round(episolon * distanciaHeuristica[ idVertice ] ), distanciaReal[ idVertice ] );
        }
        else
        {
            return Math.max( v[ idVertice ] + distanciaHeuristica[ idVertice ], v[ idVertice ] );
        }
    }
    
    private long calculaDistanciaHeuristicaEuclidiana( int idVerticeOrigem, int idVerticeDestino )
    {
        return Math.round( Math.sqrt( Math.pow( cordenadasX[ idVerticeOrigem ] - cordenadasX[ idVerticeDestino ], 2 ) + Math.pow( cordenadasY[ idVerticeOrigem ] - cordenadasY[ idVerticeDestino ], 2 ) ) );
    }
    
    private void updateSetMembership( int idVertice, HeapBinario openHeap, HeapBinario.HeapNode []rastreadorOpen, HeapBinario.HeapNode []rastreadorClosed, long []distanciaReal, long []v, long []distanciaHeuristica, EstadosVertice []estadosVertices, Map<Integer, HeapBinario.HeapNode> listaInconsistentes, double epsilon )
    {
        if ( v[ idVertice ] != distanciaReal[ idVertice ] )
        {
            if ( estadosVertices[ idVertice ] != EstadosVertice.FECHADO )
            {
                estadosVertices[ idVertice ] = EstadosVertice.ABERTO;
                if ( estadosVertices[ idVertice ] == EstadosVertice.NEUTRO )
                    rastreadorOpen[ idVertice ] = openHeap.insertHeap(idVertice, computeKeyAD(idVertice, distanciaReal, v, distanciaHeuristica, epsilon) );
                else
                    openHeap.decreaseKey( rastreadorOpen[ idVertice ].getIndiceAtual(), computeKeyAD(idVertice, distanciaReal, v, distanciaHeuristica, epsilon));
            }
            else if ( estadosVertices[ idVertice ] != EstadosVertice.INCOSISTENTE )
            {
                estadosVertices[ idVertice ] = EstadosVertice.INCOSISTENTE;
                rastreadorClosed[ idVertice ].setKey( computeKeyAD(idVertice, distanciaReal, v, distanciaHeuristica, epsilon) );
                listaInconsistentes.put(idVertice, rastreadorClosed[ idVertice ] );
                rastreadorClosed[ idVertice ] = null;
            }
        }
        else
            {
                if ( estadosVertices[ idVertice ] == EstadosVertice.ABERTO )
                {
                    rastreadorOpen[ idVertice ] = openHeap.removeElemento( rastreadorOpen[ idVertice ].getIndiceAtual() );
                    estadosVertices[ idVertice ] = EstadosVertice.LIMBO;
                }
                else if ( estadosVertices[ idVertice ] == EstadosVertice.INCOSISTENTE )
                {
                    listaInconsistentes.remove( idVertice );
                    estadosVertices[ idVertice ] = EstadosVertice.LIMBO;
                }
                
            }
    }
    
    private void computePathAD( int idDestino, HeapBinario openHeap, HeapBinario.HeapNode []rastreadorOpen, HeapBinario.HeapNode []rastreadorClosed, long []distanciaReal, long []v, long []distanciaHeuristica, EstadosVertice []estadosVertices, List<HeapBinario.HeapNode> listaFechado, Map<Integer, HeapBinario.HeapNode> listaInconsistentes, HeapBinario.HeapNode []bp, double epsilon )
    {
        while ( computeKeyAD(idDestino, distanciaReal, v, distanciaHeuristica, epsilon) > openHeap.getMin().getKey() || v[ idDestino ] < distanciaReal[ idDestino ] )
        {
            HeapBinario.HeapNode nodoAtual = openHeap.extractMin();
            int idNodoAtual = nodoAtual.getIdVertice();
            if ( v[ idNodoAtual ] > distanciaReal[ idNodoAtual ] )
            {
                v[ idNodoAtual ] = distanciaReal[ idNodoAtual ];
                estadosVertices[ idNodoAtual ] = EstadosVertice.FECHADO;
                listaFechado.add( nodoAtual );
                
                for ( Aresta a: verticesGrafo[ idNodoAtual ].arestasAdjacentes )
                {
                    int idAdjacente = a.idVerticeDestino;
                    // 14
                    if ( estadosVertices[ idAdjacente ] == EstadosVertice.NEUTRO )
                    {
                        distanciaReal[ idAdjacente ] = v[ idAdjacente ] = Long.MAX_VALUE;
                        distanciaHeuristica[ idAdjacente ] = calculaDistanciaHeuristicaEuclidiana(idAdjacente, idDestino );
                        bp[ idAdjacente ] = null;
                    }
                    
                    long distanciaCalculada = distanciaReal[ idNodoAtual ] + a.peso;
                    if ( distanciaReal[ idAdjacente ] > distanciaCalculada && distanciaCalculada >= 0 )
                    {
                        bp[ idAdjacente ] = nodoAtual;
                        distanciaReal[ idAdjacente ] = distanciaReal[ bp[ idAdjacente ].getIdVertice() ] + a.peso;
                        updateSetMembership( idAdjacente, openHeap, rastreadorOpen, rastreadorClosed, distanciaReal, v, distanciaHeuristica, estadosVertices, listaInconsistentes, epsilon );                        
                    }
                }
                
            }
            else
            {
                v[ idNodoAtual ] = Long.MAX_VALUE;
                updateSetMembership(idDestino, openHeap, rastreadorOpen, rastreadorClosed, distanciaReal, v, distanciaHeuristica, estadosVertices, listaInconsistentes, epsilon );
                for ( Aresta a: verticesGrafo[ idNodoAtual ].arestasAdjacentes )
                {
                    int idAdjacente = a.idVerticeDestino;
                    // 22
                    if ( estadosVertices[ idAdjacente ] == EstadosVertice.NEUTRO )
                    {
                        distanciaReal[ idAdjacente ] = v[ idAdjacente ] = Long.MAX_VALUE;
                        distanciaHeuristica[ idAdjacente ] = calculaDistanciaHeuristicaEuclidiana(idAdjacente, idDestino );
                        bp[ idAdjacente ] = null;
                    }
                    
                    if ( bp[ idAdjacente ] == rastreadorOpen[ idNodoAtual ] )
                    {
                        // PAREI AQUI
                    }
                }
            }
            
        }
    }
    
    public void dynamicSearchAEstrela( int idOrigem, int idDestino, double episolon, double fatorDeCorte )
    {
        // Definições de varíaveis
        int []antecessores = new int[ getNumeroVertices() ];
        long []distanciaReal = new long[ getNumeroVertices() ]; // valor correspondente ao g(s)
        long []v = new long[ getNumeroVertices() ]; // valor corresponde ao v(s)
        HeapBinario.HeapNode []bp = new HeapBinario.HeapNode[ getNumeroVertices() ];
        long []distanciaHeuristica = new long[ getNumeroVertices() ];
        HeapBinario openHeap = new HeapBinario( getNumeroVertices() );
        HeapBinario.HeapNode []rastreadorOpen = new HeapBinario.HeapNode[ getNumeroVertices() ];
        HeapBinario.HeapNode []rastreadorClosed = new HeapBinario.HeapNode[ getNumeroVertices() ];
        List<HeapBinario.HeapNode> listaFechado = new LinkedList<>();
        Map<Integer, HeapBinario.HeapNode> listaInconsistentes = new HashMap<>();
        //List<HeapBinario.HeapNode> listaInconsistentes = new LinkedList<>();
        EstadosVertice []estadosVertices = new EstadosVertice[ getNumeroVertices() ];
        
        // 7
        distanciaReal[ idDestino ] = v[ idDestino ] = Long.MAX_VALUE;
        v[ idOrigem ] = Long.MAX_VALUE;
        bp[ idDestino ] = bp[ idOrigem ] = null;
        
        // 8
        distanciaReal[ idOrigem ] = 0;
        distanciaHeuristica[ idOrigem ] = calculaDistanciaHeuristicaEuclidiana( idOrigem, idDestino );
        
        // 9
        rastreadorOpen[ idOrigem ] = openHeap.insertHeap(idOrigem, computeKeyAD(idDestino, distanciaReal, v, distanciaHeuristica, episolon) );
        estadosVertices[ idOrigem ] = EstadosVertice.ABERTO;
    }
    
    public void anyTimeSearchAEstrela( int idOrigem, int idDestino, double episolon, double fatorDeCorte )
    {
        int []antecessores = new int[ getNumeroVertices() ];
        long []distanciaReal = new long[ getNumeroVertices() ];  // Valor correspondete ao g(s)
        long []distanciaHeuristica = new long[ getNumeroVertices() ];
        HeapBinario openHeap = new HeapBinario( getNumeroVertices() );
        HeapBinario.HeapNode []rastreadorOpen = new HeapBinario.HeapNode[ getNumeroVertices() ];
        HeapBinario.HeapNode []rastreadorClosed = new HeapBinario.HeapNode[ getNumeroVertices() ];
        List<HeapBinario.HeapNode> listaInconsistentes = new LinkedList<>();
        List<HeapBinario.HeapNode> listaFechado = new LinkedList<>();
        EstadosVertice []estadosVertice = new EstadosVertice[ getNumeroVertices() ];
        
        for ( int i = 0; i < getNumeroVertices(); i++ )
        {
            antecessores[ i ] = i;
            estadosVertice[ i ] = EstadosVertice.NEUTRO;
        }
        
        // Inicializações 4, 5
        distanciaReal[ idDestino ] = Long.MAX_VALUE;
        distanciaReal[ idOrigem ] = 0;
        
        // 6
        distanciaHeuristica[ idOrigem ] = Math.round( Math.sqrt( Math.pow( cordenadasX[ idOrigem ] - cordenadasX[ idDestino ], 2 ) + Math.pow( cordenadasY[ idOrigem ] - cordenadasY[ idDestino ], 2 ) ) );
        long distanciaCalculada = Math.round(distanciaHeuristica[ idOrigem ] * episolon);
        estadosVertice[ idOrigem ] = EstadosVertice.ABERTO;
        rastreadorOpen[ idOrigem ] = openHeap.insertHeap(idOrigem, distanciaCalculada );
        
        // 7
        computePathAnytimeSearch(idDestino, antecessores, openHeap, rastreadorOpen, rastreadorClosed, estadosVertice, distanciaReal, distanciaHeuristica, listaInconsistentes, listaFechado, episolon);
        System.out.println("Publicando caminho para eps = " + episolon);
        publicaCaminho(antecessores, idOrigem, idDestino);
        System.out.println("Custo total para o vértice " + idDestino + ": " + calculaDistanciaTotal(antecessores, idOrigem, idDestino) );
        System.out.println("");
        while ( episolon > 1 )
        {
            episolon -= fatorDeCorte;
            limpaListaFechado(listaFechado, estadosVertice);
            repassaInconsistentesParaAberto(listaInconsistentes, openHeap, rastreadorOpen, estadosVertice);
            atualizaOpen(openHeap, distanciaReal, distanciaHeuristica, episolon);
            computePathAnytimeSearch(idDestino, antecessores, openHeap, rastreadorOpen, rastreadorClosed, estadosVertice, distanciaReal, distanciaHeuristica, listaInconsistentes, listaFechado, episolon);
            System.out.println("Publicando caminho para eps = " + episolon);
            publicaCaminho(antecessores, idOrigem, idDestino);
            System.out.println("Custo total para o vértice " + idDestino + ": " + calculaDistanciaTotal(antecessores, idOrigem, idDestino) );
            System.out.println("");
        }
        
    }
    
    public void dijkstraHeapBinario( int idOrigem, int idObjetivo )
    {
        HeapBinario heap = new HeapBinario( getNumeroVertices() );
        int []antecessor = new int[ getNumeroVertices() ];
        boolean []isDeterminado = new boolean[ getNumeroVertices() ];
        long []distancias = new long[ getNumeroVertices() ];
        int verticesASeremVisitados = getNumeroVertices();
        HeapBinario.HeapNode []rastreador = new HeapBinario.HeapNode[verticesASeremVisitados];
        
        
        for ( int i = 0; i < getNumeroVertices(); i++ )
        {
            antecessor[ i ] = i;
            isDeterminado[ i ] = false;
            rastreador[ i ] = heap.insertHeap(i, Long.MAX_VALUE );
            distancias[ i ]= Integer.MAX_VALUE;
        }
        
        heap.decreaseKey(idOrigem, 0);
        distancias[ idOrigem ] = 0;
        while ( verticesASeremVisitados > 0 )
        {
            HeapBinario.HeapNode nodoAtual = heap.extractMin();
            int idAtual = nodoAtual.getIdVertice();
            isDeterminado[ idAtual ] = true;
            
            for ( Aresta a: verticesGrafo[ idAtual ].arestasAdjacentes )
            {
                int idDestino = a.idVerticeDestino;
                if ( isDeterminado[ idDestino ] )
                    continue;
                
                HeapBinario.HeapNode nodoDestino = rastreador[ idDestino ];
                if ( nodoDestino.getKey() > ( a.peso + nodoAtual.getKey() ) )
                {
                    if ( ( a.peso + nodoAtual.getKey() ) >= 0 )
                    {
                        heap.decreaseKey( nodoDestino.getIndiceAtual(), ( a.peso + nodoAtual.getKey() ) );
                        antecessor[ idDestino ] = idAtual;
                        distancias[ idDestino ] = a.peso + nodoAtual.getKey();
                    }
                }
            }
            
            verticesASeremVisitados--;
            //if ( isDeterminado[ idAtual ] )
                
            
        }
        
        //imprimeDistanciaEAntecessor(antecessor, distancias, "/home/administrador/Área de Trabalho/Testes/ResultadoDijkstraHeapBinarioNovo.txt");
        
        if ( idObjetivo >= 0 )
        {
            System.out.println("Mostrando para Dijkstra");
            publicaCaminho(antecessor, idOrigem, idObjetivo );
            System.out.println("Custo total para o vértice " + idObjetivo + ": " + distancias[ idObjetivo ] );
            System.out.printf("Fim Dijkstra%n%n");            
        }
        
        
        
        /*System.out.println("Imprimindo menores distanciaHeuristica Dijkstra Heap Binário");
        for ( int i = 0; i < getNumeroVertices(); i++ )
        {
            System.out.printf("Distância( %d ): %d%n", i, distanciaHeuristica[ i ] );
            //System.out.printf("Antecessor( %d ): %d%n", i, antecessor[ i ] );
        }*/
        
    }
     
    
    public void dijkstraCanonico( int idOrigem )
    {
        long []distancias = new long[ getNumeroVertices() ];
        int []antecessor = new int[ getNumeroVertices() ];
        boolean []isDeterminado = new boolean[ getNumeroVertices() ];
        int verticesASeremVisitados = getNumeroVertices();
        
        for ( int i = 0; i < getNumeroVertices(); i++ )
        {
            distancias[ i ] = Integer.MAX_VALUE;
            antecessor[ i ] = i;
            isDeterminado[ i ] = false;
        }
        
        distancias[ idOrigem ] = 0;
        int atualVertice = idOrigem;
        
        while ( verticesASeremVisitados > 0 )
        {
            isDeterminado[ atualVertice ] = true;
            verticesASeremVisitados--;
            
            for ( Aresta a: getVerticesGrafo()[ atualVertice ].getArestasAdjacentes() )
            {
                int verticeDestino = a.getIdVerticeDestino();
                if ( isDeterminado[ verticeDestino ] )
                    continue;
                
                if ( distancias[ verticeDestino ] > distancias[ atualVertice ] + a.getPeso()  )
                {
                    if (  (distancias[ atualVertice ] + a.getPeso()) >= 0 )
                    {
                        distancias[ verticeDestino ] = distancias[ atualVertice ] + a.getPeso();
                        antecessor[ verticeDestino ] = atualVertice;
                    }
                }               
                
            }
            long minimaDistancia = Integer.MAX_VALUE;
            boolean jaSelecionado = false;
            for ( int i = 0; i < getNumeroVertices(); i++ )
            {
                if ( isDeterminado[ i ] )
                    continue;
                if ( jaSelecionado == false )
                {
                    minimaDistancia = distancias[ i ];
                    atualVertice = i;
                    jaSelecionado = true;
                }                
                else if ( distancias[ i ] <  minimaDistancia )
                {
                    atualVertice = i;
                    minimaDistancia = distancias[ i ];
                }
            }
            
        }
        
        //imprimeDistanciaEAntecessor(antecessor, distancias, "/home/administrador/Área de Trabalho/Testes/ResultadoDijkstraCanonicoNovo.txt");
        
    }
    
    
    
    private static void abreArquivo( String nomeArquivo )
    {
        try
        {
            input = new Scanner(Paths.get(nomeArquivo));            
        }
        catch ( IOException e )
        {
            System.err.println("Erro ao abrir arquico de entrada \"" + nomeArquivo + "\". Certifique-se de que ele existe ou o seu endereço foi digitado corretamente.%nEncerrando programa" );
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private void leDados(String nomeArquivo)
    {
        System.out.print("Iniciando leitura de dados...");
        try
        {
            boolean isPadrao = false;
            String avaliador = input.next();
            if ( Character.isLetter( avaliador.charAt(0) ) )
                isPadrao = true;
            else if ( Character.isDigit( avaliador.charAt(0) ) )
                isPadrao = false;           
            else
            {
                System.err.println("O arquivo de entrada não se encontra no padrão esperado de leitura, verifique se o arquivo de entrada está corretamente codificado.%nEncerrando o programa");
                System.exit( 1 );
            }
            
            fechaArquivo();
            abreArquivo(nomeArquivo);
            
            
            if ( isPadrao )
            {
                while ( input.hasNext() )
                {
                    String entradaDecodificadora = input.next();
                
                    if ( entradaDecodificadora.equals( "a" ) )
                    {
                        int id1 = input.nextInt() - 1;
                        int id2 = input.nextInt() - 1;
                        int peso = input.nextInt();
                        Aresta novaAresta = new Aresta( id1, id2, peso );
                        if ( getVerticesGrafo()[ id1 ] == null )
                            verticesGrafo[ id1 ] = new Vertice( id1 );
                        
                        getVerticesGrafo()[ id1 ].getArestasAdjacentes().add(novaAresta);
                        
                    }
                    else if ( entradaDecodificadora.equals( "c" ) )
                    {
                        input.nextLine();
                    }
                    else if ( entradaDecodificadora.equals("p") )
                    {
                        input.next();
                        numeroVertices = input.nextInt();
                        numeroArestas = input.nextInt();
                        
                        verticesGrafo = new Vertice[getNumeroVertices()];
                    }
                    else
                    {
                        System.err.println("Não devia cair aqui");
                        System.exit( 0 );
                    }
                }
                
            }
            else
            {
                numeroVertices = input.nextInt();
                numeroArestas = input.nextInt();
                
                verticesGrafo = new Vertice[getNumeroVertices()];
                
                while ( input.hasNext() )
                {
                    int id1 = input.nextInt();
                    int id2 = input.nextInt();
                    int peso = input.nextInt();
                    Aresta novaAresta = new Aresta(id1, id2, peso);
                    
                    if ( getVerticesGrafo()[ id1 ] == null )
                        verticesGrafo[ id1 ] = new Vertice( id1 );
                    
                    getVerticesGrafo()[ id1 ].getArestasAdjacentes().add(novaAresta);
                }
            }
            
        } catch ( NoSuchElementException e )
        {
            System.err.println("Exeção durante a leitura:%nO arquivo de entrada não se encontra no padrão esperado de leitura, verifique se o arquivo de entrada está corretamente codificado.%nEncerrando o programa");
            e.printStackTrace();
            System.exit( 1 );
        }
        catch ( IllegalStateException e )
        {
            System.err.println("Erro ao ler o arquivo de entrada");
            e.printStackTrace();
            System.exit( 1 );
        }
        
        
        System.out.println(" Feito.");
    }
    
    private static void fechaArquivo()
    {
        if ( input != null )
            input.close();
    }
    
    public void leCordenadas( String nomeArquivoCordenads )
    {
        System.out.print("Iniciando leitura do arquivo de cordenadas... ");
        cordenadasX = new long[numeroVertices];
        cordenadasY = new long[numeroVertices];
        while ( input.hasNext() )
        {
            String caracter = input.next();
            if ( caracter.equals( "v" ) )
            {
                int vertice = input.nextInt() - 1;
                cordenadasX[ vertice ] = input.nextLong();
                cordenadasY[ vertice ] = input.nextLong();
            }
            else if ( caracter.equals( "p" ) )
            {
                input.next();
                input.next();
                input.next();
                int vertificadordeQuantidade = input.nextInt();
                if ( numeroVertices != vertificadordeQuantidade )
                {
                    System.err.println("[ERROR]: O número de vértices do grafo é diferente do número de vertices do arquivo de cordenadas \"" + nomeArquivoCordenads + "\". Encerrando o programa.");
                    System.exit( 1 );
                }
            }
            else
                input.nextLine();
        }
        
        System.out.println("Feito");
    }
    
    public void leArquivoDeCordenadas( String nomeArquivoCordenadas )
    {
        abreArquivo(nomeArquivoCordenadas);
        this.leCordenadas(nomeArquivoCordenadas);
        fechaArquivo();
        
    }
    
    
    public void leArquivoEntrada( String nomeArquivo )
    {
        abreArquivo(nomeArquivo);
        this.leDados(nomeArquivo);
        fechaArquivo();        
    }
   
    public void imprimeGrafo()
    {
        System.out.println("Imprimido Grafo:");
        for (Vertice verticesGrafo1 : getVerticesGrafo()) 
        {
            for (Aresta a : verticesGrafo1.getArestasAdjacentes()) {
                System.out.printf("%d ---> %d w: %d%n", a.getIdVeticeOrigem(), a.getIdVerticeDestino(), a.getPeso());
            }
        }
    }
    
    
    
    
    
    
    // DEFINIÇÕES DAS CLASSES INTERNAS
    
    public class Vertice
    {
        private int idVertice;
        private List<Aresta> arestasAdjacentes = new LinkedList<>();
        
        public Vertice( int idVertice )
        {
            this.idVertice = idVertice;
        }

        /**
         * @return the arestasAdjacentes
         */
        public List<Aresta> getArestasAdjacentes() {
            return arestasAdjacentes;
        }
    }
    
    
    public class Aresta
    {
        private final int idVeticeOrigem;
        private final int idVerticeDestino;
        private final int peso;
        
        
        public Aresta( int origem, int destino, int peso )
        {
            this.idVerticeDestino = destino;
            this.idVeticeOrigem = origem;
            this.peso = peso;
        }

        /**
         * @return the idVeticeOrigem
         */
        public int getIdVeticeOrigem() {
            return idVeticeOrigem;
        }

        /**
         * @return the idVerticeDestino
         */
        public int getIdVerticeDestino() {
            return idVerticeDestino;
        }

        /**
         * @return the peso
         */
        public int getPeso() {
            return peso;
        }
    }

    /**
     * @return the verticesGrafo
     */
    public Vertice[] getVerticesGrafo() {
        return verticesGrafo;
    }

    /**
     * @return the numeroVertices
     */
    public int getNumeroVertices() {
        return numeroVertices;
    }

    /**
     * @return the numeroArestas
     */
    public int getNumeroArestas() {
        return numeroArestas;
    }
}

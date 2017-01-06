/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.pg.grafo;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;
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
    private static Scanner input;
    
 
    public Grafo()
    {
        numeroArestas = 0;
        numeroVertices = 0;
        verticesGrafo = null;
    }
    
    public void buscaEmLargutaPorVertice( int idVertice )
    {
        List<Integer> verticesChegaveis = new LinkedList<>();
        Queue<Integer> fila = new LinkedList<>();
        Cores []cor = new Cores[ numeroVertices ];
        for ( int i = 0; i < numeroVertices; i++ )
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
        Cores []cor = new Cores[ numeroVertices ];
        List<Integer> verticesChegaveis = new LinkedList<>();
        int []pi = new int[ numeroVertices ];
        int time;
        
        for ( int i = 0; i < numeroVertices; i++ )
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
    
    /*public void buscaEmProfundidade()
    {
        Cores []cor = new Cores[ numeroVertices ];
        int []pi = new int[ numeroVertices ];
        int time;
        
        for ( int i = 0; i < numeroVertices; i++ )
        {
            cor[ i ] = Cores.WHITE;
            pi[ i ] = -1;            
        }
        
        time = 0;
        for ( int i = 0; i < verticesGrafo.length; i++ )
        {
            if ( cor[ i ] == Cores.WHITE )
                dsfVisit(i, cor, pi, time);
        }
        
    }*/
    
    public void dijkstraHeapFibonacci( int idOrigem )
    {
        int verticesVisitados = numeroVertices;
        int []antecessor = new int[ numeroVertices ];
        boolean []isDeterminado = new boolean[ numeroVertices ];
        FibonacciHeap.FibNode []rastreador = new FibonacciHeap.FibNode[ numeroVertices ];
        
        
        FibonacciHeap<Integer> heap = new FibonacciHeap<>();
        
        for ( int i = 0; i < numeroVertices; i++ )
        {
            FibonacciHeap.FibNode novoNodo = heap.criaNovoNodo(i, Integer.MAX_VALUE );
            isDeterminado[ i ] = false;
            antecessor[ i ] = i;
            rastreador[ i ] = novoNodo;
            heap.insert(novoNodo);
        }
        
        FibonacciHeap.FibNode nodoAtual = rastreador[ idOrigem ];
        heap.decrease_key(nodoAtual, 0 );
        isDeterminado[ idOrigem ] = true;
        
        while ( verticesVisitados > 0 )
        {
            nodoAtual = heap.extractMin();
            if ( nodoAtual == null )
            {
                verticesVisitados--;
                break;               
            }
            int idNodoAtual = nodoAtual.getIdVertice();
            isDeterminado[ idNodoAtual ] = true;
            
            for ( Aresta a: verticesGrafo[ idNodoAtual ].getArestasAdjacentes() )
            {
                if ( isDeterminado[ a.idVerticeDestino ] )
                    continue;
                
                FibonacciHeap.FibNode nodoDestino = rastreador[ a.idVerticeDestino ];
                
                if ( nodoDestino.getPeso().compareTo(((int)nodoAtual.getPeso()) + a.peso ) > 0 )
                {
                    if ( (((int)nodoAtual.getPeso()) + a.peso ) >= 0 )
                    {
                        heap.decrease_key(nodoDestino, (((int)nodoAtual.getPeso()) + a.peso ) );
                        antecessor[ a.idVerticeDestino ] = a.idVeticeOrigem;
                    }
                }
            }
            
            verticesVisitados--;
        }
        
        System.out.println("Iniciando impressão de rota");
        for ( int i = 0; i < verticesGrafo.length; i++ )
        {
            System.out.printf("Antecessor( %d ): %d%n", i, antecessor[ i ] );
        }
        
    }
    
    public void dijkstraHeapBinario( int idOrigem )
    {
        int []antecessor = new int[ numeroVertices ];
        boolean []isDeterminado = new boolean[ numeroVertices ];
        int []rastreador = new int[ numeroVertices ];
        int verticesASeremVisitados = numeroVertices;
        HeapBinario heap = new HeapBinario( numeroVertices );
        
        for ( int i = 0; i < numeroVertices; i++ )
        {
            heap.insert( i, Integer.MAX_VALUE );
            isDeterminado[ i ] = false;
            antecessor[ i ] = i;
            rastreador[ i ] = i;
        }
        
        heap.setRastreador(rastreador);
        heap.decreaseKey(idOrigem, 0);
        int atualVertice = idOrigem;
        
        while ( verticesASeremVisitados > 0 )
        {
            //atualVertice
            isDeterminado[ atualVertice ] = true;
            verticesASeremVisitados--;
            
            for ( Aresta a: getVerticesGrafo()[ atualVertice ].getArestasAdjacentes() )
            {
                int verticeDestino = heap.getPosicaoRastreador(a.getIdVerticeDestino());
                if ( isDeterminado[ a.getIdVerticeDestino() ] )
                    continue;
                
                if ( heap.getDistanciaNodo( verticeDestino ) > heap.getDistanciaNodo(heap.getPosicaoRastreador(atualVertice)) + a.getPeso() )
                {
                    if ( heap.getDistanciaNodo(heap.getPosicaoRastreador(atualVertice)) + a.getPeso() >= 0  )
                    {
                        heap.decreaseKey( verticeDestino ,  heap.getDistanciaNodo(heap.getPosicaoRastreador(atualVertice)) + a.getPeso() );
                        antecessor[ a.getIdVerticeDestino() ] = atualVertice;
                    }
                }
            }
            atualVertice = heap.extractMin();
        }
        
        System.out.println("Imprimindo menores distancias Heap Binário");
        for ( int i = 0; i < numeroVertices; i++ )
        {
            System.out.printf("%d = %d%n", i, antecessor[ i ] );
        }
        
    }
    
    public void dijkstraCanonico( int idOrigem )
    {
        int []distancias = new int[ numeroVertices ];
        int []antecessor = new int[ numeroVertices ];
        boolean []isDeterminado = new boolean[ numeroVertices ];
        int verticesASeremVisitados = numeroVertices;
        
        for ( int i = 0; i < numeroVertices; i++ )
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
            int minimaDistancia = Integer.MAX_VALUE;
            boolean jaSelecionado = false;
            for ( int i = 0; i < numeroVertices; i++ )
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
        
        System.out.println("Imprimindo menores distancias Dijkstra Canônico");
        for ( int i = 0; i < numeroVertices; i++ )
        {
            System.out.printf("Antecessor( %d ): %d%n", i, antecessor[ i ] );
        }
        
        
        
        
        
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
                        
                        verticesGrafo = new Vertice[numeroVertices];
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
                
                verticesGrafo = new Vertice[numeroVertices];
                
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
}

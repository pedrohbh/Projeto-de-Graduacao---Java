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
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;
import pedro.pg.beta.AEstrela;
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
    
    
    
    public void dijkstraHeapBinario( int idOrigem )
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
        
        imprimeDistanciaEAntecessor(antecessor, distancias, "/home/administrador/Área de Trabalho/Testes/ResultadoDijkstraHeapBinarioNovo.txt");
        
        System.out.println("Mostrando para Dijkstra");
        AEstrela.publicaCaminho(antecessor, this, 0, 180 );
        System.out.println("Custo total para o vértice 180: " + distancias[ 180 ] );
        System.out.printf("Fim Dijkstra%n%n");
        
        
        /*System.out.println("Imprimindo menores distancias Dijkstra Heap Binário");
        for ( int i = 0; i < getNumeroVertices(); i++ )
        {
            System.out.printf("Distância( %d ): %d%n", i, distancias[ i ] );
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
        
        imprimeDistanciaEAntecessor(antecessor, distancias, "/home/administrador/Área de Trabalho/Testes/ResultadoDijkstraCanonicoNovo.txt");
        
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

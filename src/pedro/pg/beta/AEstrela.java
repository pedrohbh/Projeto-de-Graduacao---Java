/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.pg.beta;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import pedro.pg.estruturadedados.HeapBinario;
import pedro.pg.grafo.Grafo;

/**
 *
 * @author administrador
 */
public class AEstrela
{
    private static long xCordenadas[];
    private static long yCordenadas[];
    private static Scanner input;
    private static Grafo grafo = new Grafo();
    
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
    
    private static void leDados( String nome )
    {
        System.out.print("Iniciando a leitura de cordenadas... ");
        try
        {
            while ( input.hasNext() )
            {
                String avaliador = input.next();
                if ( avaliador.equals( "v" ) )
                {
                    int indice = input.nextInt() - 1;
                    xCordenadas[ indice ] = input.nextLong();
                    yCordenadas[ indice ] = input.nextLong();
                }
                else
                    avaliador = input.nextLine();
            }
            System.out.println("Feito.");
        }
        catch ( Exception e )
        {
            System.err.println("Erro ao ler os dados de entrada do arquivos de cordenadas. Encerrando o programa");
            e.printStackTrace();
            System.exit( 1 );
        }
        
    }
    
    private static void fechaArquivo()
    {
        if ( input != null )
            input.close();
    }
    
    
    public static void leArquivoDeCordenadas( String nome )
    {
        xCordenadas = new long[ grafo.getNumeroVertices() ];
        yCordenadas = new long[ grafo.getNumeroVertices() ];
        
        abreArquivo(nome);
        leDados(nome);
        fechaArquivo();
        
    }
    
    public static int []algoritmoAEstrela( int indiceOrigem, int indiceDestino )
    {
        HeapBinario heap = new HeapBinario( grafo.getNumeroVertices());
        int []antecessor = new int[ grafo.getNumeroVertices() ];
        boolean []isDeterminado = new boolean[ grafo.getNumeroVertices() ];
        HeapBinario.HeapNode []rastreador = new HeapBinario.HeapNode[ grafo.getNumeroVertices() ];
        
        for ( int i = 0; i < grafo.getNumeroVertices(); i++ )
        {
            antecessor[ i ] = i;
            isDeterminado[ i ] = false;
            rastreador[ i ] = heap.insertHeap(i, Long.MAX_VALUE );
        }
        
        heap.decreaseKey(indiceOrigem, 0);
        while ( isDeterminado[ rastreador[ indiceDestino ].getIdVertice() ] != true )
        {
            HeapBinario.HeapNode nodoAtual = heap.extractMin();
            int idAtual = nodoAtual.getIdVertice();
            isDeterminado[ idAtual ] = true;
            
            for ( Grafo.Aresta a: grafo.getVerticesGrafo()[ idAtual ].getArestasAdjacentes() )
            {
                int idDestino = a.getIdVerticeDestino();
                if ( isDeterminado[ idDestino ] )
                    continue;
                
                HeapBinario.HeapNode nodoDestino = rastreador[ idDestino ];
                
                long distanciaHeuristica = (long) Math.sqrt(Math.pow( (xCordenadas[ idAtual ] - xCordenadas[ idDestino ] ), 2) + Math.pow( (yCordenadas[ idAtual ] - yCordenadas[ idDestino ] ), 2));
                if ( nodoDestino.getKey() > ( a.getPeso() + nodoAtual.getKey() + distanciaHeuristica ) && ( a.getPeso() + nodoAtual.getKey() + distanciaHeuristica ) >= 0 )
                {
                    heap.decreaseKey( nodoDestino.getIndiceAtual(), ( a.getPeso() + nodoAtual.getKey() + distanciaHeuristica ));
                    antecessor[ idDestino ] = idAtual;
                }
            }
            
        }
        
        return antecessor;
    }
    
    public static void publicaCaminho( int []antecessores, int origem, int destino )
    {
        List<Integer> listaDeAntecessores = new Stack<>();
        //listaDeAntecessores.add(destino);
        int ultimo = destino;
        //listaDeAntecessores.add(ultimo);
        
        while ( true )
        {            
            listaDeAntecessores.add( ultimo );
            
            
            if ( ultimo == origem )
                break;
            
            ultimo = antecessores[ ultimo ];
        }
        
        System.out.println("Caminho partindo de " + origem + " até " + destino );
        while ( !listaDeAntecessores.isEmpty() )
        {
            int caminho = listaDeAntecessores.remove( listaDeAntecessores.size() - 1 );
            System.out.print( caminho + " -> ");
        }
        System.out.println("");
    }
    
    public static void main(String[] args) 
    {
        String caminho = "/home/administrador/Documentos/Trabalhos/Projeto de Graduação/PG-Codigo/Testes/";
        String nomeEntrada = "USA-road-d.NY.gr";
        String nomeCordenadas = caminho + "USA-road-d.NY.co";
        caminho = caminho + nomeEntrada;
        
        grafo.leArquivoEntrada( caminho );
        leArquivoDeCordenadas( nomeCordenadas );
        int []antecessores = algoritmoAEstrela(0, 180);
        publicaCaminho(antecessores, 0, 180);
        
        
        System.out.println("OK");
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.pg.principal;

import pedro.pg.grafo.Grafo;
import pedro.pg.utilitario.GraphViz;

/**
 *
 * @author administrador
 */
public class Principal 
{
    public static void main(String[] args) 
    {
        String caminho = "/home/administrador/Documentos/Trabalhos/Projeto de Graduação/PG-Codigo/Testes/";
        String nomeEntrada = "teste2.txt";
        caminho = caminho + nomeEntrada;
        
        
        
        Grafo novoGrafo = new Grafo();
        novoGrafo.leArquivoEntrada(caminho);
        novoGrafo.dijkstraCanonico(2);
        novoGrafo.dijkstraHeapFibonacci( 2 );
        
        //novoGrafo.dijkstraHeapFibonacciPrototipo(2 );
        //GraphViz.desenhaGrafo(caminho, novoGrafo);
                
        System.out.println("SUCESS");
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.pg.principal;

import pedro.pg.grafo.Grafo;

/**
 *
 * @author administrador
 */
public class Principal 
{
    public static void main(String[] args) 
    {
        String caminho = "/home/administrador/Documentos/Trabalhos/Projeto de Graduação/PG-Codigo/Testes/";
        String nomeEntrada = "USA-road-d.NY.gr";
        String nomeCordenadas = caminho + "Cordenadas/" + "USA-road-d.NY.co";
        caminho = caminho + nomeEntrada;
               
        Grafo novoGrafo = new Grafo();
        novoGrafo.leArquivoEntrada(caminho);
        novoGrafo.leArquivoDeCordenadas(nomeCordenadas);
        novoGrafo.dijkstraHeapBinario( 0, 180 );
        novoGrafo.anytimeSearchAEstrela(0, 180, 2, 0.5 );
        novoGrafo.dynamicSearchAEstrela(0, 180, 2, 0.5, 5, true );
        //novoGrafo.anyTimeSearchAEstrela(0, 2, 2, 1 );
        //novoGrafo.dynamicSearchAEstrela(0, 2, 2, 0.5 );
                        
        System.out.println("SUCESS");
    }
    
}

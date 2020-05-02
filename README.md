# Simulador de Filas

## Informações Gerais

- [Modelo](./modelo.pdf)
- [Arquivo de entrada do modelo](./src/main/resources/probabilidades-de-roteamento.json)
- [Resultados](./resultados/)
- Rotina de inicialização do simulador: com.github.rafaritter44.simulador.Main#main(String[])
- Rotinas de tratamento de eventos:
  - com.github.rafaritter44.simulador.evento.Chegada#executar()
  - com.github.rafaritter44.simulador.evento.Saida#executar()
  - com.github.rafaritter44.simulador.evento.Passagem#executar()

## Arquivo de entrada

- Cada fila no programa possui um identificador, que é uma das propriedades do objeto fila, e que também é a chave do mapa de objetos "filas". Esse identificador é utilizado para uma fila fazer referência a outra no array "roteamentos" (especificamente, na propriedade "destino").
- Segue o template do arquivo de entrada do simulador:

```json
[
  {
    "filas": {
      "1": {
        "id": 1,
        "roteamentos": [
          {
            "destino": 1,
            "probabilidade": 1.0
          }
        ],
        "servidores": 1,
        "capacidade": 1,
        "capacidadeInfinita": false,
        "comChegadasDaRua": false,
        "tempoChegadaInicial": 1.0,
        "tempoMinimoChegada": 1.0,
        "tempoMaximoChegada": 1.0,
        "tempoMinimoSaida": 1.0,
        "tempoMaximoSaida": 1.0
      }
    },
    "simulacao": {
      "execucoes": 1,
      "maximoAleatoriosConsumidos": 1
    }
  }
]
```

## Comando para rodar o simulador:

`$ ./gradlew run --args="nome_do_arquivo"`

Obs. 1: Passar o nome_do_arquivo sem a extensão (.json)

Obs. 2: O arquivo deve estar localizado no diretório [src/main/resources/](./src/main/resources/)

## Comando para rodar os testes:

`$ ./gradlew clean test`

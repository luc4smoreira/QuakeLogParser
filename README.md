# QuakeLogParser 

Autor: Lucas Moreira Carneiro de Miranda

Agosto 2023

## Estrutura
Os logs são processados linha por linha, agrupando-os pelo mesmo time.
Caso um erro ocorra, é tratado de forma que o processamento continue caso possível.
Quando um log não tem um tipo é feito um flush nos logs do mesmo time, enviando-os para processamento.

### Processamento em lote
Como os logs são processados agrupados por time, foi definida uma prioridade para cada tipo de log dentro do mesmo time, de forma que não seja processado na ordem das linhas sequencialmente. Isso foi feito considerando que em alguns casos pode ocorrer um registro tardio da linha no log após algum evento.
A enum LogEventTypeEnum define os tipos de log e suas prioridadades, sendo as prioridades mais baixas executadas primeiro

### Dados ignorados
Alguns tipos de logs são ignorados são eles: Item, Exit
No evento Kill, são considerados os id do evento e ignorados a texto descritivo do Kill. Na linha de log abaixo, de exemplo
> 0:05 Kill: 6 7 3: Zeh killed Mal by MOD_MACHINEGUN

O trecho 
>: Zeh killed Mal by MOD_MACHINEGUN 
é ignorado

## Means of Death
Foi criada uma Enum para Means of Death associando um id para cada tipo.
Como o código de referência indica que existem "meansOfDeath" exclusivos somente se "mission pack" estiver habilibilitado e isso afeta o número associado com o MOD_GRAPPLE. Após analisar o log foi obervado que nenhum meansOfDeath do tipo "mission pack" estava registrado e nem mesmo o MOD_GRAPPLE, então os means of death com id maior que 22 não foram incluídos na enum

## Lista de decisões tomadas

### 1. Log corrompido.

Tem um trecho do arquivo de log que parece estar corrompido, na linha 97 onde deveria ter um horário encontra-se o número 26 seguido de um horário 00:00

  > 26 0:00 ------------------------------------------------------------

Considerando esse caso,  foi adicionado um tratamento para falhas e uma mensagem de aviso.

### 2. Jogador se desconecta

Não ficou claro se o jogador que se desconecta da partida antes dela acabar precisa ser considerado no relatório. 

**Tomei a decisão de considerar todos os jogadores, mesmo os que se desconectaram**

### 3. Reconexão do jogador

 O jogador pode se desconectar da partida e um novo jogador se reconectar com o mesmo id.  Não ficou claro se é para considerar essa nova conexão como um novo jogador e entra a questão "Jogador se desconecta"  anterior

**Considerei que o jogador que se desconecta e entra na partida novamente utilizando o mesmo nome é o mesmo jogador, a contagem de kills continuará**


### 4. Player Ranking
Em Report não ficou claro se o ranking dos jogadores seria global de todas as partidas ou para cada partida

  >3.2. Report
  >
  >Create a script that prints a report (grouped information) for each match and a player ranking.

**Foi criado um ranking global de todas as partidas, assumindo como identificador o nome do jogador utilizado**


### 5. Execução e saída

Não ficou claro como deverá ser executado o programa e gerada a saída. 
**Foi definido que o programa será executado pelo console e gerará a saída também no console**

#### Observação: 
Dentro do que foi especificado e observando o log, se o jogador se mata durante a partida conta como um kill e ele ficará melhor classificado no ranking se matando.
 (sem considerar no caso em que WORLD é quem mata)

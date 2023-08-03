# QuakeLogParser 

Autor: Lucas Moreira Carneiro de Miranda

Agosto 2023



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

**Considerei que o jogador que se desconecta e entra na partida novamente utilizando o mesmo id é o mesmo jogador, a contagem dele de kills continuará**


### 4. Player Ranking
Em Report não ficou claro se o ranking dos jogadores seria global de todas as partidas ou para cada partida

  >3.2. Report
  >
  >Create a script that prints a report (grouped information) for each match and a player ranking.

**Foi criado um ranking global de todas as partidas, assumindo como identificador o nome do jogador utilizado**


### 5. Execução e saída

Não ficou claro como deverá ser executado o programa e gerada a saída. 
**Define que o programa será executado pelo console e gerará a saída também no console**

#### Observação: 
Dentro do que foi especificado e observando o log, se o jogador se mata durante a partida conta como um kill e ele ficará melhor classificado no ranking se matando.
 (sem considerar no caso em que WORLD é quem mata)

# Challenge ONE | Java | Back End | Conversor de Moeda

## Conversor de moeda e sistema númerico

O Conversor de moeda e sistema númerico é um sistema que foi desenvolvido para o 5° Challenge do programa ONE (Oracle Next Education). O desafio repassado foi: Fazer um conversor de moeda.
O sistema desenvolvido possui 2 funcionalidades principais: Conversor de moedas e sistemas númericos.

## Funcionalidades do Conversor de moedas

Há ao total __162__ moedas disponíveis para conversão, algumas delas são:

1. Dólar fijiano (FJD)
2. Peso mexicano (MXN)
3. Rupia seichelense (SCR)
4. Franco congolês (CDF)
5. Dólar barbadense (BBD)
6. Quetzal guatemalteco (GTQ)
7. Peso chileno (CLP)
8. Lempira hondurenha (HNL)
9. Xelim ugandense (UGX)
10. Rand sul-africano (ZAR)
11. Dinar tunisiano (TND)
12. Dobra de São Tomé e Príncipe (STN)
13. Dólar bahamense (BSD)
14. Leone de Serra Leoa (SLL)
15. Libra sudanesa (SDG)
16. Dinar iraquiano (IQD)
17. Peso cubano (CUP)
18. Dalasi gambiano (GMD)
21. Novo dólar taiwanês (TWD)
22. Dinar sérvio (RSD)

![Intrerface de conversão de moeda](https://media.discordapp.net/attachments/1049092226585726979/1066912712136855662/image.png)

Basta que o usuário digite um valor __válido__ (um número positivo) que a conversão vai ser realizada automaticamente.

Há a disposição do usuário alguns botões como o de limpar o conteúdo das caixas de texto, o de sair e o de menu que fará com que o usuário temha acesso ao menu principal.

Caso o usuário tente converter de uma moeda X para X será informado a ele que essa ação não pode ser realizada.

![Pop-up conversão inválida](https://media.discordapp.net/attachments/1049092226585726979/1066913287507292160/image.png)

Além disso o sistema também mostra a valorização da moeda X em relação a moeda Y nos últimos 30 dias.

![Gráfico de valorização do real em relação ao dolár](https://media.discordapp.net/attachments/1049092226585726979/1066912471706767400/image.png)

Funcionalidades do Conversor de sistemas númericas

Há ao total 4 sistemas númericos disponíveis no sistema, sendo:

1. Hexadecimal
2. Decimal
3. Octal
4. Binário

O sistema converte de um sistema númerico X para o Y. Mas somente se o número X for um numero natural.

![Interface do conversor de sistema númerico](https://media.discordapp.net/attachments/1049092226585726979/1066917081712435290/image.png)

Há presente na interface algumas funcionalidades como limpar as caixas de texto e copiar o conteúdo que se encontra traduzido.

O usuário ao informar o número que deseja traduzir deve apertar o botão "traduzir" para o conteúdo ser traduzido. E também os botões de sair e menu que foram citados anteriormente.

Caso o usuário esqueça de informar o número, aparecerá um pop-up intruindo ele.

![Entrada inválida: usuário esqueceu de informar o número](https://media.discordapp.net/attachments/1049092226585726979/1066921022235885599/image.png)

Caso ele informe um número inválido, também será informado.

![Entrada inválida: usuário informou um número inválido](https://media.discordapp.net/attachments/1049092226585726979/1066921448955973713/image.png)

## Menu principal

O menu consiste de uma interface amigável com uma mensagem de boas vindas de acordo com o horário da sua região que vai mudando de forma dinâmicamente ao decorrer de certos intervalos de tempo. Para acessar as funcionalidades do sistema, basta que o usuário clique no _dropdown_ que se encontra mais abaixo, onde há as funcionalidades até o momento. 

![Menu principal do sistema](https://media.discordapp.net/attachments/1049092226585726979/1066919755262468146/image.png)

## Packages utilizados

Foi utilizado, ao total, 3 packages:

1. [JFreeChart](https://www.jfree.org/jfreechart/): biblioteca utilizada para fazer gráficos em Java.
2. [Jcommom](https://www.jfree.org/jcommon/): biblioteca utilizada principalmente pelo JFreeChart.
3. [org.JSON](https://mvnrepository.com/artifact/org.json/json): usada para manipular [JSON](https://pt.wikipedia.org/wiki/JSON).

Contudo, também foi utilizado o WindowBuilder para fazer interfaces gráficas (GUI) que utiliza o AWT e o SWING.

## API

Foi utilizado, ao total, 2 APIS:

1. [ExchangeRate](https://www.exchangerate-api.com/): para pegar a cotação das moedas e as respectivas moedas citadas anteriormente.
2. [Forbes](https://www.forbes.com/advisor/money-transfer/currency-converter/usd-brl/?amount=1): para coletar os dados de valores anteriores de cada moeda em um período de 30 dias.




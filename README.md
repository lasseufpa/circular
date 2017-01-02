# Circular UFPA (SmartBus)

Mais da metade da população mundial está concentrada em centros urbanos e o número de pessoas morando em cidades tende a crescer rapidamente nos próximos anos. Com o acelerado crescimento das cidades, muitos problemas surgem e com eles a necessidade de fazer planejamentos cada vez mais inteligentes para os centros urbanos.

Existe uma forte tendência para que as cidades atuais se tornem smartcities em um futuro próximo. Uma smartcity ou cidade inteligente Segundo o Ágoralab (2014) pode ser definida como uma cidade que promove o bem estar de todos os seus cidadãos e de forma sustentável é capaz de se transformar num lugar cada vez melhor para morar, trabalhar, estudar e se divertir. É essencial para o desenvolvimento dessas smartcities o desenvolvimento de diversas ferramentas tecnológicas que tornaram a vida de seus moradores mais confortável, redes de sensores, internet das coisas e big data são alguns exemplos de conceitos tecnológicos que serão indispensáveis para a infraestrutura básica de uma cidade inteligente.

Nesse contexto surge dentro do Programa Especial de Treinamento inclusivo (PETi) o projeto "Circular UFPA (SmartBus)", o principal objetivo do projeto é desenvolver um sistema que seja capaz de monitorar os ônibus circulares da cidade universitária e disponibilizar para a comunidade acadêmica informações que facilitem o dia a dia dos usuários dos ônibus. A principal informação que será disponibilizada pelo aplicativo será a localização dos ônibus e o tempo estimado para uma viagem, porem outras informações também estarão disponíveis, como por exemplo, a quantidade de pessoas nos ônibus. 

Além de informações relacionadas aos ônibus também pretende se fazer com que os ônibus circulares também façam monitoramento ambiental da cidade universitária, coletando informações, como temperatura, umidade, qualidade do ar e etc.

# Estrutura do Projeto

![Mind Map](https://github.com/lasseufpa/circular/blob/master/Mind%20Map.png)

 O projeto pode ser dividido em três partes principais:

## Embarcado

O sistema embarcado que ficará nos ônibus será composto por um Arduino com um chip SIM808 que disponibiliza as funções de GSM e GPS, além de diversos outros sensores que farão o monitoramento do ônibus e o monitoramento ambiental.

## Aplicações

### Aplicativo *móbile*
O sistema contará com um aplicativo android que mostrará aos usuários as localizações dos ônibus circulares em tempo real, localização das paradas, tempo até que o próximo ônibus passe em determinada parada, lotação dos ônibus e etc.

### Página *web*
O sistema também contará com uma pagina web que mostrará a localização dos ônibus circulares em tempo real e a localização dos pontos de parada.

### Bots
Os usuários também poderão interagir com o sistema e obter informações sobre os ônibus por meio de chatbots em várias plataformas como Telegram e Twitter.

# Servidor
No servidor funcionará um broker MQTT que será a base para a comunicação entre todas as partes do sistema, além de scripts que farão o tratamento dos dados.

# Como participar

Adote uma tarefa [aqui](https://github.com/lasseufpa/circular/issues) ou entre em contato através do [forum](https://www.quicktopic.com/52/H/SaqZMyqNWQ66K).

Temos reuniões semanais com duração de 1h todas as quartas no LASSE (no prédio do LEEC).

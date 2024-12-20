![Static Badge](https://img.shields.io/badge/java-21)  [![Continuous integration com GitHub](https://github.com/joseeliomar/banco-digital/actions/workflows/docker-publish.yml/badge.svg)](https://github.com/joseeliomar/banco-digital/actions/workflows/docker-publish.yml)

# JBank (Back-End de um Banco Digital)
Esse sistema de banco digital (back-end) e é focado em funcionalidades reais do setor financeiro, como abertura de contas, depósitos, saques, geração de extratos com as movimentações bancárias realizadas pelos clientes e múltiplas modalidades de transferências.

# Nesse projeto utilizei:
- Java;
- Spring Boot;
- Arquitetura de microsserviços;
- APIs REST;
- TDD (Desenvolvimento Orientado a Testes);
- Testes unitários;
- Testes de integração;
- Docker;
- Mensageria com Kafka;
- Bancos de dados relacionais (MySQL e PostgreSQL);
- Banco de dados não relacional (MongoDB);
- Swagger (documentação de APIs);
- Integração de sistemas com Apache Camel;
- IA Generativa para o assistente virtual do sistema.

# Artigo do LinkedIn em que apresento o sistema funcionando:
**Link do artigo (no vídeo mostro o sistema funcionando):** bit.ly/artigo-linkedin-meu-sistema-banco

# Requisitos Funcionais
## 1. Criação da conta digital do cliente
- O sistema deverá criar cada conta digital para pessoa física com os seguintes dados obrigatórios: CPF, senha da conta, número de telefone, e-mail, nome completo, data de nascimento, país em que nasceu, nome completo da mãe e endereço formado pela rua, pelo número, bairro, município, unidade federativa e CEP. Nesse caso o CPF será o código da conta digital.
- O sistema deverá criar cada conta digital para pessoa jurídica com os seguintes dados obrigatórios: CNPJ, senha da conta, número de telefone, e-mail, nome social e endereço formado pela rua, pelo número, bairro, município, unidade federativa e CEP. Nesse caso o CNPJ será o código da conta digital.
- O sistema deverá gerar automaticamente o número da agência e da conta no momento da criação da conta digital tanto para pessoa física, quanto para pessoa jurídica.
- O sistema deverá permitir o cadastro da senha, mas ela deverá ser informada duas vezes e as duas senhas informadas deverão ser comparadas para saber se são iguais. A senha deverá ter no mínimo 8 números.

## 2. Alteração da conta digital do cliente
- O sistema deverá permitir alterar os seguintes dados da conta digital para pessoa física: senha da conta, número de telefone, e-mail, nome completo, data de nascimento, país em que nasceu, nome completo da mãe e endereço formado pela rua, pelo número, bairro, município, unidade federativa e CEP.
- O sistema deverá permitir alterar os seguintes dados da conta digital para pessoa jurídica: senha da conta, número de telefone, e-mail, nome social e endereço formado pela rua, pelo número, bairro, município, unidade federativa e CEP.
- O sistema deverá permitir a alteração da senha informando a senha atual e a nova senha. A senha deverá ter no mínimo 8 números.
  
## 3. Exclusão da conta digital do cliente
- O sistema deverá permitir a remoção da conta digital para pessoa física.
- O sistema deverá permitir a remoção da conta digital para pessoa jurídica.

## 4. Busca da conta digital do cliente
- O sistema deverá permitir a busca da conta digital pelo CFP.
- O sistema deverá permitir a busca da conta digital pelo CNPJ.
- O sistema deverá permitir a busca da conta digital pelo número da agência e da conta.
  
## 5. Criação da conta corrente e da conta poupança
- O sistema deverá criar automaticamente a conta corrente no momento da criação da conta digital.
- O sistema deverá criar automaticamente a conta poupança no momento da criação da conta digital.
  
## 6. Busca da conta corrente e da conta poupança
- O sistema deverá permitir a busca da conta corrente pelo código da conta digital do cliente.
- O sistema deverá permitir a busca da conta poupança pelo código da conta digital do cliente.
  
## 7. Relacionados com o depósito
- O sistema deverá permitir que sejam feitos depósitos por boleto.
- O sistema deverá permitir que sejam feitos depósitos por Pix.
  
## 8. Relacionados com o saque
- O sistema deverá permitir que sejam feitos saques.
  
## 9. Transferência para contas de outras instituições financeiras
- O sistema deverá permitir que sejam feitas transferências para contas de outras instituições financeiras.
  
## 10. Transferência entre contas do próprio banco
- O sistema deverá permitir que sejam feitas transferências da conta corrente para a conta poupança.
- O sistema deverá permitir que sejam feitas transferências da conta poupança para a conta corrente.
  
## 11. Extrato da conta corrente
- O sistema deverá registrar todas as transferências feitas e todos os saques e depósitos feitos.
- O sistema deverá permitir que seja gerado o extra da conta corrente.
- O sistema deverá permitir que seja gerado o extra da conta poupança.

## 12. Assistente virtual
- O sistema deverá ter um assistente virtual alimentado por inteligência artificial (IA) que realizará de forma autonôma a consulta de saldo e transações bancárias (todas as modalidades disponíveis) solicitas pelo cliente via Telegram (App de mensagens semelhante ao WhatsApp).

## 13. Serviço de notificação
- O sistema deverá notificar o cliente através de SMS sempre que alguma transação bancária for realizada pelo sistema. A notificação deverá ter os detalhes da transação efetuada.

# Visão geral do sistema
Aqui é mostrada uma visão geral do sistema com os microsserviços, porém sem detalhar a comunicação de entre os microsserviços.

<img src="/imagens-para-o-readme/Visão geral do sistema.png">

## Explicação:
- API Gateway: Microsserviço responsável por fazer o roteamentos das requisições HTTP feitas para o sistema. No caso da imagem acima, a API Gatway está fazendo o roteamento para os microsserviços que estão envolvidos pelo retângulo roxo;
- Service Discovery (eureka-server): Microsserviço responsável pela localização dos microsserviços. No caso da imagem acima, todos os microsserviços que envolvidos pelo retângulo verde se conectam ao Service Discovery e informam para ele os seus dados e assim, o Service Discovery fica sabendo qual endereço IP e porta cada um desses microsserviços está usando, essas informações são utilizadas para fazer o balanceamento de carga;
- conta-digital-cliente-ms: Microsserviço responsável pelo gerenciamento das contas digitais do banco que podem ser físicas e jurídicas;
- conta-corrente-poupanca-ms: Microsserviço responsável pelo gerenciamento da conta corrente e da conta poupança;
- deposito-ms: Microsserviço responsável pelos depósitos bancários;
- saque-ms: Microsserviço responsável pelos saques bancários;
- transferencia-ms: Microsserviço responsável pelas operações de transferência bancária;
- extrato-bancario-ms: Microsserviço responsável pela geração dos extratos bancários;
- endereco-ms: Microsserviço responsável pelo gerenciamento dos endereços;
- gerador-numero-conta-afins-ms: Microsserviço responsável pela geração de números de contas com o digito verificador e operações parecidas;
- notifica-ms: Microsserviço responsável pelo envio de mensagens para os usuários utilizando serviços externos como o Twilio;
- bots-ms: Microsserviço responsável pela comunicação dos clientes com o assistente virtual.

## Diagramas de classes
Aqui são encontrados os diagramas de classes dos microsserviços.

## Diagrama de classes do microsserviço conta-digital-cliente-ms
<img src="/imagens-para-o-readme/Diagrama de classes do microsserviço conta-digital-cliente-ms.png">

## Diagrama de classes do microsserviço endereco-ms
<img src="/imagens-para-o-readme/Diagrama de classes do microsserviço endereco-ms.png">

## Diagrama de classes do microsserviço conta-corrente-poupanca-ms
<img src="/imagens-para-o-readme/Diagrama de classes do microsserviço conta-corrente-poupanca-ms.png">

## Diagrama de classes do microsserviço extrato-bancario-ms
<img src="/imagens-para-o-readme/Diagrama de classes do microsserviço extrato-bancario-ms.png">


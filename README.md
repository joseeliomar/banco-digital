# Banco-Digital (ainda em processo de desenvolvimento)


# Requisitos funcionais

## Requisitos funcionais relacionados a criação da conta digital do cliente.

  O sistema deverá criar cada conta digital para pessoa física com os seguintes dados obrigatórios: CPF, senha da conta, número de telefone, e-mail, nome completo, data de nascimento, país em que nasceu, nome completo da mãe e endereço formado pela rua, pelo número, bairro, município, unidade federativa e CEP. Nesse caso o CPF será o código da conta digital.
 
  O sistema deverá criar cada conta digital para pessoa jurídica com os seguintes dados obrigatórios: CNPJ, senha da conta, número de telefone, e-mail, nome social e endereço formado pela rua, pelo número, bairro, município, unidade federativa e CEP. Nesse caso o CNPJ será o código da conta digital.
  
  O sistema deverá gerar automaticamente o número da agência e da conta no momento da criação da conta digital tanto para pessoa física, quanto para pessoa jurídica.
  
  O sistema deverá permitir o cadastro da senha, mas ela deverá ser informada duas vezes e as duas senhas informadas deverão ser comparadas para saber se são iguais. A senha deverá ter no mínimo 8 números.
## Requisitos funcionais relacionados com a alteração da conta digital do cliente.

  O sistema deverá permitir alterar os seguintes dados da conta digital para pessoa física: senha da conta, número de telefone, e-mail, nome completo, data de nascimento, país em que nasceu, nome completo da mãe e endereço formado pela rua, pelo número, bairro, município, unidade federativa e CEP.
  O sistema deverá permitir alterar os seguintes dados da conta digital para pessoa jurídica: senha da conta, número de telefone, e-mail, nome social e endereço formado pela rua, pelo número, bairro, município, unidade federativa e CEP.
  
  O sistema deverá permitir a alteração da senha informando a senha atual e a nova senha. A senha deverá ter no mínimo 8 números.
  
## Requisitos funcionais relacionados com a exclusão da conta digital do cliente.
  O sistema deverá permitir a remoção da conta digital para pessoa física.
  
  O sistema deverá permitir a remoção da conta digital para pessoa jurídica.
## Requisitos funcionais relacionados com a busca da conta digital do cliente.
  O sistema deverá permitir a busca da conta digital pelo CFP.
  
  O sistema deverá permitir a busca da conta digital pelo CNPJ.
  
  O sistema deverá permitir a busca da conta digital pelo número da agência e da conta.
  
## Requisitos funcionais relacionados com a criação da conta corrente e da conta poupança.

  O sistema deverá criar automaticamente a conta corrente no momento da criação da conta digital.
  
  O sistema deverá criar automaticamente a conta poupança no momento da criação da conta digital.
  
## Requisitos funcionais relacionados com a busca da conta corrente e da conta poupança.

  O sistema deverá permitir a busca da conta corrente pelo código da conta digital do cliente.
  
  O sistema deverá permitir a busca da conta poupança pelo código da conta digital do cliente.
  
## Requisitos funcionais relacionados com o depósito.

  O sistema deverá permitir que sejam feitos depósitos por boleto.
  
  O sistema deverá permitir que sejam feitos depósitos por Pix.
  
## Requisitos funcionais relacionados com o saque

  O sistema deverá permitir que sejam feitos saques.
  
## Requisitos funcionais relacionados com a transferência para contas de outras instituições financeiras

  O sistema deverá permitir que sejam feitas transferências para contas de outras instituições financeiras.
  
## Requisitos funcionais relacionados com a transferência entre contas do próprio banco

  O sistema deverá permitir que sejam feitas transferências da conta corrente para a conta poupança.
  
  O sistema deverá permitir que sejam feitas transferências da conta poupança para a conta corrente.
  
## Requisitos funcionais relacionados com o extrato da conta corrente.

  O sistema deverá registrar todas as transferências feitas e todos os saques e depósitos feitos.
  
  O sistema deverá permitir que seja gerado o extra da conta corrente.
  
  O sistema deverá permitir que seja gerado o extra da conta poupança.
  
## Requisitos funcionais relacionados ao login no sistema.

  O sistema deverá permitir que seja feito login via token.

# Visão geral do sistema
Aqui é mostrada uma visão geral do sistema com os microsserviços e os bancos de dados deles.

<img src="/imagens-para-o-readme/Visão geral do sistema.png">

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


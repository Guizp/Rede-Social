# 📱 Rede Social

* Projeto da disciplina ARQDMO2 – Dispositivos Móveis II
* Professor: Henrique Galati
* Instituição: IFSP - Campus Araraquara
* Curso: Análise e Desenvolvimento de Sistemas

---

## 💡 Sobre o Projeto

A **Rede Social** é um aplicativo Android desenvolvido em Kotlin que permite o cadastro e autenticação de usuários, criação de perfil personalizado e publicação de conteúdos em um feed.

O app simula uma micro rede social, onde usuários podem compartilhar posts com imagem e descrição, armazenados na nuvem utilizando Firebase.

O foco do projeto é aplicar conceitos de autenticação, banco NoSQL, manipulação de imagens e organização de código com boas práticas de arquitetura.

---

## 🚀 Funcionalidades

🔐 **Cadastro de Usuário (SignUp)**
Criação de conta com email e senha usando Firebase Authentication.

🔑 **Login de Usuário**
Autenticação segura com persistência de sessão.

👤 **Perfil do Usuário (Profile)**

* Foto de perfil via galeria
* Username editável
* Nome completo fixo após cadastro

🖼️ **Upload de Imagem de Perfil**
Imagem convertida para Base64 e salva no Firestore.

---

### 📸 **Posts (Publicações)**

📝 **Criação de Post**
Usuário pode criar posts com:

* Descrição
* Imagem
* Cidade
* Data automática

🗄️ **Armazenamento no Firestore**
Posts são salvos como documentos na coleção `posts`.

---

### 📰 **Feed de Posts**

📋 **Exibição de Feed**
Lista de posts exibidos para o usuário.

🕒 **Ordenação por Data**
Posts mais recentes aparecem primeiro.

🖼️ **Exibição de Imagens**
Imagens convertidas de Base64 para Bitmap no app.

---

### 🏠 **Home do Usuário**

Exibe:

* Foto de perfil
* Username
* Nome completo

---

### 🔄 **Sessão Persistente**

Usuário permanece logado automaticamente.

🚪 **Logout**
Encerramento da sessão com retorno ao login.

---

## 🧠 Arquitetura do Projeto

O projeto foi estruturado com separação de responsabilidades:

📦 **Model**

* `User`
* `Post`

🗄️ **DAO**

* `UserDAO`
* `PostDAO`

🔐 **Auth**

* `UserAuth`

🖥️ **UI (Activities)**

* LoginActivity
* SignUpActivity
* ProfileActivity
* HomeActivity
* (Activity de criação de posts / feed)

👉 As Activities não acessam diretamente o Firebase.

---

## 🧩 Tecnologias e Recursos Utilizados

* Linguagem: Kotlin
* IDE: Android Studio

### 🔥 Firebase

* Firebase Authentication
* Cloud Firestore

### 📱 Android

* ViewBinding
* Intents
* Activity Result API (galeria)

### 🗄️ Banco de Dados

* NoSQL (Firestore)
* Estrutura com coleções:

  * `usuarios`
  * `posts`

---

## 🖼️ Capturas de Tela


### 🔹 Login

<img width="396" height="823" alt="Login" src="https://github.com/user-attachments/assets/207c8dc7-c229-45ff-91d7-8ee30ad0e391" />



### 🔹 Criar Conta

<img width="398" height="886" alt="CriarConta" src="https://github.com/user-attachments/assets/20df4dc3-757c-4bd3-9899-a8fd924e1335" />



### 🔹 Perfil

<img width="394" height="883" alt="Perfil" src="https://github.com/user-attachments/assets/2d7682d7-7477-48bc-9a9e-1200f75dae6e" />



### 🔹 Feed

<img width="396" height="883" alt="Feed" src="https://github.com/user-attachments/assets/010aacd6-238b-4985-a66d-7c8b8694a41f" />



### 🔹 Post-In

<img width="396" height="886" alt="Post1" src="https://github.com/user-attachments/assets/fd5a1338-e650-4ae2-8b58-e7ffdf43436c" />



### 🔹 Post-Out

<img width="394" height="890" alt="Post2" src="https://github.com/user-attachments/assets/106c6d56-49a8-4a00-93c7-d0808989da7a" />

---

## 📹 Vídeos de Demonstração

### 🔹 Demonstração Geral


https://github.com/user-attachments/assets/17a8b739-e01e-4514-9dbe-5dc51fdff9b5


### 🔹 Criação de Post e Feed


https://github.com/user-attachments/assets/ca510a1c-ca3b-4a26-8054-026d5a95d0c3


---

## 📷 Vídeo de Explicação do Código



---

## 🎯 Objetivo Acadêmico

Aplicar na prática:

* Autenticação de usuários
* Persistência em banco NoSQL
* Manipulação de imagens
* Arquitetura com separação de camadas
* Desenvolvimento de app Android completo


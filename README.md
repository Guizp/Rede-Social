# 📱 Rede Social

* Projeto da disciplina ARQDMO1 – Dispositivos Móveis I
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



---

## 📹 Vídeos de Demonstração

### 🔹 Demonstração Geral

https://github.com/user-attachments/assets/879174a1-da3c-4cf7-a0e3-1a8c7c408413


### 🔹 Criação de Post e Feed


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


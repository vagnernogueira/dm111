# [INATEL – Pós-graduação](https://inatel.br/) – [Desenvolvimento Mobile e Cloud Computing](https://inatel.br/pos/desenvolvimento-mobile-e-cloud-computing)
## Disciplina DM111 - Desenvolvimento de Web Services com segurança em Java no Google App Engine
## Prof. Edilson Justiniano
### Aluno [Vagner Nogueira](https://github.com/vagnernogueira)
### Avaliação da disciplina<br>


---
## Projeto

Sistema em arquitetura de micro serviços composto pelos seguintes módulos em spring boot

- vale food auth
- vale food promotion management
- vale food restaurant management
- vale food user management

O módulo de promoções é o cerne da avaliação proposta. Para fins didáticos foi utilizado persistência in memory.

Cada módulo é um projeto independente, porém optou-se pela implementação em repositório único com possibilidade de build unificado via pom.xml referenciando os módulos, na raiz do repositório.


---
## Technology stack

- **[Java](https://www.java.com/pt-BR/) version 17**
- **[Maven](https://maven.apache.org/) version 3.X**
- **[Spring Boot](https://spring.io/projects/spring-boot) version 3.5.4**

### IDE
- **[Ecplise](https://www.eclipse.org/) version 2025-06**

---
### Download and Build

```bash

cd ~

git clone https://github.com/vagnernogueira/dm111.git

cd dm111

mvn package -DskipTests

```

---

### 2 - Execution

```bash

java -jar .\vale-food-auth\target\vale-food-auth-0.0.1-SNAPSHOT.jar


```<br>

```bash

java -jar .\vale-food-promotion-management\target\vale-food-promotion-management-0.0.1-SNAPSHOT.jar


```<br>

```bash

java -jar .\vale-food-restaurant-management\target\vale-food-restaurant-management-0.0.1-SNAPSHOT.jar


```<br>

```bash

java -jar .\vale-food-user-management\target\vale-food-user-management-0.0.1-SNAPSHOT.jar


```<br>

---

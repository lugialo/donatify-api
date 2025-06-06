# Estágio 1: Build da aplicação com Maven
# Usamos uma imagem base que já tem o Maven e o JDK 21
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o pom.xml para o contêiner para baixar as dependências
COPY pom.xml .

# Baixa todas as dependências do projeto
RUN mvn dependency:go-offline

# Copia o resto do código fonte da aplicação
COPY src ./src

# Executa o build do projeto, gerando o arquivo JAR. O -DskipTests pula a execução dos testes.
RUN mvn package -DskipTests

# Estágio 2: Execução da aplicação
# Usamos uma imagem base mais leve, apenas com o Java 21, para rodar a aplicação
FROM eclipse-temurin:21-jre-alpine

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo JAR gerado no estágio de build para o nosso contêiner final
COPY --from=build /app/target/donatify-0.0.1-SNAPSHOT.jar ./app.jar

# Expõe a porta 8080, que é a porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação quando o contêiner for executado
ENTRYPOINT ["java", "-jar", "app.jar"]
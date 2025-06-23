# Usa una imagen base con JDK 17
FROM openjdk:17-jdk-slim

# Instala wget, unzip y Tomcat 9
RUN apt-get update && apt-get install -y wget unzip && \
    wget https://downloads.apache.org/tomcat/tomcat-9/v9.0.85/bin/apache-tomcat-9.0.85.zip && \
    unzip apache-tomcat-9.0.85.zip && \
    mv apache-tomcat-9.0.85 /opt/tomcat && \
    rm apache-tomcat-9.0.85.zip

# Crea directorio de despliegue para tu proyecto
RUN mkdir -p /opt/tomcat/webapps/ROOT

# Copia el WAR ya compilado al directorio de despliegue
COPY target/*.war /opt/tomcat/webapps/ROOT.war

# Expone el puerto 8080 (Render lo detectará)
EXPOSE 8080

# Inicia Tomcat
CMD ["/opt/tomcat/bin/catalina.sh", "run"]
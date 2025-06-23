# Imagen base de Tomcat (compatible con Java 8)
FROM tomcat:9.0-jdk8

# Elimina las apps por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia el archivo WAR generado por Maven (ajusta el nombre si cambia)
COPY target/CALCULADORAVLSMWEB-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Render usa esta variable para determinar el puerto expuesto
EXPOSE 8080
ENV PORT 8080

# Comando para iniciar Tomcat
CMD ["catalina.sh", "run"]
